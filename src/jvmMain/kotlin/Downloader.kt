import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.google.common.base.Charsets
import com.google.common.io.Resources
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.awt.Desktop
import java.io.ByteArrayInputStream
import java.net.URL

var link by mutableStateOf("")
var download by mutableStateOf(false)
var status by mutableStateOf("")
var dataYt:DataYt? by mutableStateOf(null)

var videoDownload:Download? by mutableStateOf(null)
var audioDownload:Download? by mutableStateOf(null)
var progressDownload by mutableStateOf(0f)
var statusProgressDownload by mutableStateOf("")

data class Download(
    val url: String?,
    val name: String?
)
data class FormatYt(
    val id: String?,
    val ext: String?,
    val url: String?,
    val size: Int?
)
data class DataYt(
    val title: String? ,
    val uploader: String?,
    val thumbnail: String?,
    val audioFormats: List<FormatYt>?,
    val videoFormats: List<FormatYt>?
)


private val env = Resources.toString(Resources.getResource(".env"), Charsets.UTF_8)
private val keyApi = env.split("\n")[0].split("=")[1]
private val webApi = env.split("\n")[1].split("=")[1]

/**
 * download metadata youtube from https://ytdl-7wabejrcqq-uc.a.run.app/youtube
 * and store metadata to dataYt
 * */
suspend fun downloadYt(){
    download = true
    dataYt = null
    videoDownload = null
    audioDownload = null

    try {
        Logger.debug("download data")
        status = "download data"
        val client = HttpClient(CIO){
            /*install(Logging){
                level = LogLevel.INFO
            }*/
        }
        val response = client.get("$webApi?key=$keyApi&q=$link"){
            onDownload{ bytes, length ->
                status = if(length > 0) "Received $bytes bytes from $length"
                else "Received $bytes"
            }
        }

        status = "download data complete"
        Logger.debug(response)
        val text = response.bodyAsText()
        Logger.debug(response.bodyAsText())
        Logger.debug(text)

        status = if (text.contains("Error")) "Error extract data" else ""
        try {
            val json = JsonParser.parseString(text).asJsonObject
            val audioFormats = mutableListOf<FormatYt>()
            val videoFormats = mutableListOf<FormatYt>()
            json["formats"].asJsonArray.map { it.asJsonObject }.forEach {
                val data = FormatYt(
                    id = it["id"].asString,
                    ext = it["ext"].asString,
                    url = it["url"].asString,
                    size = if (it["size"].isJsonNull) 0 else it["size"].asInt,
                )
                if(it["id"].asString.contains("audio"))audioFormats.add(data)
                else if(!it["size"].isJsonNull) videoFormats.add(data)
            }
            dataYt = DataYt(
                json["title"].asString,
                json["uploader"].asString,
                json["thumbnail"].asString,
                audioFormats,
                videoFormats
            )
        }catch (e: Exception){
            status = "Error extract data"
            Logger.error(e.message)
        }
    }catch (e:Exception){
        Logger.error(e.message)
        status = "Error download data"
    }
    download = false
}

suspend fun loadImage(url: String?): ImageBitmap? =
    try {
        urlStream(url ?: "").use { org.jetbrains.skia.Image.makeFromEncoded(it.readAllBytes()).toComposeImageBitmap() }
    }
    catch (e: Exception){
        status = "Error download thumbnail"
        Logger.error(e.message)
        null
    }

private suspend fun urlStream(url: String) = HttpClient().use {
    status = ""
    ByteArrayInputStream(
        it.get(url).readBytes()
    )
}

/**
 * Open link in browser
 * */
fun openLink(url: String) = Desktop.getDesktop().browse(URL(url).toURI())

fun getNameDownload(id: String?, ext: String?) = "${dataYt?.title}.f${id!!.split("-")[0].trim()}.$ext"

fun getNameSize(yt: FormatYt?) = "%s - %.2fmb".format(yt?.id, yt?.size?.toFloat()?.div(1048576))

fun getDataYt(yt: FormatYt?):Download? =
    try {
        Download(yt?.url, getNameDownload(yt?.id, yt?.ext))
    }catch (e: Exception){
        Logger.error(e.message)
        null
    }