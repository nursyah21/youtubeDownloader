import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.google.common.base.Charsets
import com.google.common.io.Resources
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

val Logger: Logger = LogManager.getLogger()

fun main() = application {
    val icon = painterResource("images/icon.png")
    val env = Resources.toString(Resources.getResource(".env"), Charsets.UTF_8)
    val keyApi = env.split("\n")[0].split("=")[1].trim()
    val webApi = env.split("\n")[1].split("=")[1].trim()

    Logger.debug(webApi)
    Logger.debug(keyApi)

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 360.dp, height = 640.dp),
        title = "youtube downloader - 1.0 Beta",
        icon = icon,
        resizable = false
    ) {
        app()
    }
}
