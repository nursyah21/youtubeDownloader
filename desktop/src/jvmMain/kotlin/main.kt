import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import screen.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "youtube downloader",
        state = rememberWindowState(
            width = 360.dp, height = 580.dp,
            position = WindowPosition(Alignment.CenterEnd)
        ),
        alwaysOnTop = true
    ) {
        App()
    }
}