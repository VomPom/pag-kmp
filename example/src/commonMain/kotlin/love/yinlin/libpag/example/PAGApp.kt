package love.yinlin.libpag.example

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import libpag_compose.example.generated.resources.Res
import love.yinlin.libpag.PAGAnimation
import love.yinlin.libpag.PAGConfig
import love.yinlin.libpag.rememberPAGPainter

@Stable
class PAGAnimationState {
    var data: ByteArray? by mutableStateOf(null)
    var isPlaying: Boolean by mutableStateOf(false)
    var isCompleted: Boolean by mutableStateOf(false)
    var progress: Double by mutableStateOf(0.0)
}

val PAG_FILE_LIST = listOf("simple", "video")

@Composable
fun PAGApp() {
    var pagName by remember { mutableStateOf<String>(PAG_FILE_LIST[1]) }
    val pagState = remember { PAGAnimationState() }
    var usePainter by remember { mutableStateOf(false) }
    val listener = PAGConfig.rememberAnimationListener(
        onStart = {
            pagState.isCompleted = false
            println("PAG 动画开始播放")
        },
        onEnd = {
            pagState.isCompleted = true
            println("PAG 动画播放完毕")
        },
        onRepeat = { println("PAG 动画重新开始播放") },
        onUpdate = { _, progress ->
            if (!usePainter) {
                pagState.progress = progress
            }
        }
    )

    LaunchedEffect(pagName) {
        pagState.data = Res.readBytes("files/$pagName.pag")
        pagState.isPlaying = true

        while (true) {
            delay(1000 / 60)
            if (usePainter && pagState.isPlaying) {
                var progress = pagState.progress
                progress = (progress + 1 / 174.0)
                if (progress >= 1.0) progress = 0.0
                pagState.progress = progress
            }
        }
    }

    Box(
        Modifier.fillMaxSize().background(Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        if (!usePainter) {
            PAGAnimation(
                data = pagState.data,
                modifier = Modifier.fillMaxSize(),
                isPlaying = pagState.isPlaying,
                progress = pagState.progress,
                listener = listener
            )
        } else {
            Image(
                painter = rememberPAGPainter(
                    data = pagState.data,
                    progress = pagState.progress
                ),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = remember(pagState.progress) { "PAG play progress: ${pagState.progress}" })
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    pagState.isPlaying = !pagState.isPlaying
                }) {
                    Text(text = if (pagState.isPlaying) "Pause" else "Play")
                }

                Button(onClick = {
                    usePainter = !usePainter
                }) {
                    Text(text = "Switch Render")
                }
            }
        }
        PAGFileSelectBox(onPAGSelected = {
            pagName = it
            println("pag selected: $it")
        })
    }
}

@Composable
fun BoxScope.PAGFileSelectBox(onPAGSelected: (String) -> Unit = {}) {
    val radioOptions = PAG_FILE_LIST
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(
        modifier =
            Modifier.selectableGroup().align(Alignment.TopEnd)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "PAG Files"
        )
        radioOptions.forEach { pagName ->
            Row(
                Modifier.selectable(
                    selected = (pagName == selectedOption),
                    onClick = {
                        onOptionSelected(pagName)
                        onPAGSelected(pagName)
                    },
                    role = Role.RadioButton
                )
                    .height(30.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (pagName == selectedOption),
                    onClick = null
                )
                Text(
                    text = pagName,
                    style = MaterialTheme.typography.button,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
        }
    }
}