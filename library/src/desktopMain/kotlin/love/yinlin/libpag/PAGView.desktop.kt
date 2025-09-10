package love.yinlin.libpag

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.delay
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.libpag.PAGFile
import org.libpag.PAGPlayer
import org.libpag.PAGSurface
import kotlin.use

@Composable
fun PAGView(
    file: PAGFile?,
    modifier: Modifier,
    isPlaying: Boolean = true,
    repeatCount: Int = PAGConfig.INFINITY,
    listener: PAGConfig.AnimationListener = PAGConfig.rememberAnimationListener(),
) {
    val player = remember { PAGPlayer() }
    var surface by remember { mutableStateOf<PAGSurface?>(null) }
    var painter by remember { mutableStateOf<Painter>(BitmapPainter(ImageBitmap(1, 1))) }
    var imageInfo by remember { mutableStateOf(ImageInfo.makeUnknown(0, 0)) }
    var buffer by remember { mutableStateOf(byteArrayOf()) }
    var fps by remember { mutableStateOf<Float>(30f) }
    var playDurationUs by remember { mutableStateOf<Long>(0) }
    var totalDurationUs by remember { mutableStateOf<Long>(0) }
    var playCount by remember { mutableStateOf<Int>(0) }

    var progress = remember(playDurationUs, totalDurationUs) {
        if (totalDurationUs > 0) {
            playDurationUs.coerceIn(0, totalDurationUs).toDouble() / totalDurationUs.toDouble()
        } else {
            0.0
        }
    }
    LaunchedEffect(file) {
        if (file == null) return@LaunchedEffect
        file.let { pagFile ->
            player.composition = pagFile
            totalDurationUs = pagFile.duration()
            fps = pagFile.frameRate()
            val size = IntSize(pagFile.width(), pagFile.height())
            imageInfo = ImageInfo(size.width, size.height, ColorType.RGBA_8888, ColorAlphaType.PREMUL, ColorSpace.sRGB)
            buffer = ByteArray(size.width * size.height * 4)
            surface?.release()
            surface = PAGSurface.MakeOffscreen(size.width, size.height)
            surface?.let { surface ->
                player.surface = surface
            }
        }
    }

    LaunchedEffect(playCount) {
        while (true) {
            val perFrameUs = (1_000_000 / fps).toLong()
            delay(perFrameUs / 1_000)
            if (isPlaying) {
                playDurationUs += perFrameUs
            }
        }
    }

    LaunchedEffect(progress) {
        player.progress = progress
        listener.onAnimationUpdate(null, progress)
        if (progress == 0.0) {
            listener.onAnimationStart(null)
        }
        if (progress >= 1.0) {
            if (repeatCount == PAGConfig.INFINITY || ++playCount < repeatCount) {
                playDurationUs = 0
                listener.onAnimationRepeat(null)
            }
            listener.onAnimationEnd(null)
        }
        player.flush()
        surface?.let {
            if (it.copyPixelsTo(buffer, imageInfo.width * 4)) {
                val image = Image.makeRaster(imageInfo, buffer, imageInfo.width * 4)
                image.use {
                    painter = BitmapPainter(image.toComposeImageBitmap())
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            surface?.release()
            surface = null
            player.release()
        }
    }

    Image(
        painter = painter,
        contentDescription = "",
        modifier = modifier
    )
}

@Composable
fun PAGView(
    data: ByteArray?,
    modifier: Modifier,
    isPlaying: Boolean = true,
    repeatCount: Int = PAGConfig.INFINITY,
    listener: PAGConfig.AnimationListener = PAGConfig.rememberAnimationListener(),
) {
    data?.let {
        PAGView(PAGFile.Load(it), modifier, isPlaying, repeatCount, listener)
    }
}
