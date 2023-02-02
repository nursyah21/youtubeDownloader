package com.nursyah.utils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.common.base.Charsets
import com.google.common.io.Resources
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import kotlinx.coroutines.flow.channelFlow
import org.jsoup.Jsoup
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

var download by mutableStateOf(false)

/**
 * download data with status progress
 *
 * */
fun downloadData(url: String, status: String = "download data") = channelFlow<String>{
    try{
        send(status)
        val response = HttpClient().get(url){
            onDownload{ bytesSentTotal, contentLength ->
                val tempData = if(contentLength > 0) "Received $bytesSentTotal bytes from $contentLength"
                else "Received $bytesSentTotal bytes"
                send(tempData)
            }
        }
        send("success $status")
        val responseBody = response.body<String>()
        println(responseBody)
    }catch (e:Exception) {
        println(e)
        send("error $status")
    }
}

data class DataDownload(
    val url: String,
    val size: String,
    val format: String,
    val detailFormat: String = "",
)
data class YTDownload(
    val urlImg: String,
    val title: String,
    val desc: String,
    val duration: String,
    val download: List<DataDownload>
)

fun extractData(data: String = yttext) {
    try{
        val html = Jsoup.parse(data)
        var js = ""
        html.select("link").forEach {
            if(it.attr("href").contains(".*player_ias*".toRegex())){
                js = "https://www.youtube.com%s".format(it.attr("href"))
            }
        }

        html.getElementsByTag("script").forEach {
            if(it.data().startsWith("var ytInitialPlayerResponse")){
                val i = it.data().drop(30).dropLast(170)
                val json = com.google.gson.JsonParser.parseString(i)
                val videoDetails = json.asJsonObject["videoDetails"]
                val title = videoDetails.asJsonObject["title"].asString
                val duration = durationFormat(videoDetails.asJsonObject["lengthSeconds"].asString)
                val description = videoDetails.asJsonObject["shortDescription"].asString
                val thumbnail = "https://i.ytimg.com/vi/%s/hqdefault.jpg".format(videoDetails.asJsonObject["videoId"].asString)
                val videoDownload = json.asJsonObject["streamingData"].asJsonObject["adaptiveFormats"].asJsonArray

                videoDownload.forEach {it2->
                    DataDownload(
                        url = it2.asJsonObject["signatureCipher"].asString,
                        size = sizeFormat(it2.asJsonObject["contentLength"].asString),
                        format = it2.asJsonObject["mimeType"].asString.split("/")[0],
                        detailFormat = it2.asJsonObject["quality"].asString
                    )
                }

            }
        }
    }catch (e: Exception){
        println(e)
    }
}

private val tempUrl = "s=e8O8Oq0QJ8wRQIgCAWfK8SCqVRgEbleQqOs5eNB5VjuCECq7PfFOT2OcuA%3DIQCMzOqmWyMMvVwHApMz3ivU-mvfNVsE95Pd6QCoNHr8yw%3DCw%3DC&sp=sig&url=https://rr8---sn-2uuxa3vh-n0ce.googlevideo.com/videoplayback%3Fexpire%3D1675337893%26ei%3DRUzbY8OGCOa9vcAPn82TgAE%26ip%3D110.139.40.120%26id%3Do-AK_wkoXdokck2O1DOPoP2OYSAqBq1yoAl1k22iVL-sRN%26itag%3D137%26aitags%3D133%252C134%252C135%252C136%252C137%252C160%252C242%252C243%252C244%252C247%252C248%252C278%252C394%252C395%252C396%252C397%252C398%252C399%26source%3Dyoutube%26requiressl%3Dyes%26mh%3DEv%26mm%3D31%252C29%26mn%3Dsn-2uuxa3vh-n0ce%252Csn-npoe7nds%26ms%3Dau%252Crdu%26mv%3Dm%26mvi%3D8%26pcm2cms%3Dyes%26pl%3D22%26initcwndbps%3D541250%26vprv%3D1%26mime%3Dvideo%252Fmp4%26ns%3DS9tbswdk00HIJM9FNtSeMqsL%26gir%3Dyes%26clen%3D23390562%26dur%3D244.416%26lmt%3D1643240796114619%26mt%3D1675316001%26fvip%3D1%26keepalive%3Dyes%26fexp%3D24007246%26c%3DWEB%26txp%3D5535434%26n%3DOpN_dTvxj-i0Novr_XjODC%26sparams%3Dexpire%252Cei%252Cip%252Cid%252Caitags%252Csource%252Crequiressl%252Cvprv%252Cmime%252Cns%252Cgir%252Cclen%252Cdur%252Clmt%26lsparams%3Dmh%252Cmm%252Cmn%252Cms%252Cmv%252Cmvi%252Cpcm2cms%252Cpl%252Cinitcwndbps%26lsig%3DAG3C_xAwRQIhAJ6Zrzaq0dcs5lzB8eRbzbQ-u--qevWVXVJHLECtrCXGAiAnVPBiS_kejciNCnQOgxcQxQ3F4uUpzbBwOLLm7ea0Mw%253D%253D"

fun decipherUrl(url: String = tempUrl){
    try {
        val text = Resources.toString(Resources.getResource("raw/asd.txt"), Charsets.UTF_8)

        println(text)
    }catch (e:Exception){
        println(e)
    }
}
private fun sizeFormat(size: String): String{
    return try {
        "%dkb".format(size.toInt())
    }catch (e:Exception){
        println(e)
        ""
    }
}
private fun durationFormat(time: String): String{
    return try{
        val i = time.toInt()
        "%d:%02d".format(i/60, i%60)
    }catch (e:Exception){
        println(e)
        ""
    }
}
