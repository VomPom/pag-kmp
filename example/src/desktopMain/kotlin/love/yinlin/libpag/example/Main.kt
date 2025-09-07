package love.yinlin.libpag.example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import libpag_compose.example.generated.resources.Res
import org.libpag.PAGFile
import org.libpag.PAGLayer

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "libpag-compose example",
    ) {
        var pagFile by remember { mutableStateOf<PAGFile?>(null) }
        Box {
            PAGApp()
            if (pagFile == null) {
                Text("PAG file is null.")
            } else {
                PAGFileInfo(Modifier, pagFile!!)
            }
        }

        LaunchedEffect(Unit) {
            pagFile = PAGFile.Load(Res.readBytes("files/video.pag"))
        }
    }
}

@Composable
fun PAGFileInfo(modifier: Modifier, pagFile: PAGFile) {
    val size = 1
    val width = size * 100
    LazyVerticalGrid(
        columns = GridCells.Fixed(size),
        modifier = modifier.width(width.dp),
        contentPadding = PaddingValues(10.dp)
    ) {
        item { InfoItem("Duration", usToS(pagFile.duration()), "s") }
        item { InfoItem("FrameRate", pagFile.frameRate(), "FPS") }
        item { InfoItem("Width", pagFile.width()) }
        item { InfoItem("Height", pagFile.height()) }

        item { InfoItem("Images", pagFile.numImages()) }
        item { InfoItem("Layers", pagFile.numChildren()) }
        item { InfoItem("VideoTrack ", videoTrackTags(pagFile)) }
        item { InfoItem("Version", tagCodeToVersion(pagFile.tagLevel()), pagFile.tagLevel().toString()) }
    }
}


@Composable
private fun InfoItem(desc: String, value: Any, unit: String = "") {
    Column(
        modifier = Modifier
            .background(Color(0xFF2d2d36))
            .border(width = 1.dp, color = Color(0xff141417))
            .padding(top = 5.dp, start = 10.dp, bottom = 5.dp)
    ) {
        Text(
            modifier = Modifier,
            text = desc,
            style = TextStyle.Default.copy(fontSize = 10.sp, color = Color(0xFF9b9b9b))
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row {
            Text(
                modifier = Modifier,
                text = value.toString(),
                style = TextStyle.Default.copy(fontSize = 14.sp, color = Color.White)
            )
            Spacer(modifier = Modifier.width(3.dp))
            if (!unit.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.Bottom),
                    text = unit,
                    style = TextStyle.Default.copy(fontSize = 10.sp, color = Color.Gray)
                )
            }
        }
    }
}