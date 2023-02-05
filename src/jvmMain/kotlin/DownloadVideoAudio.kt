import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.*
import org.bytedeco.javacpp.Loader
import java.io.File

private val dirDownload = File(System.getProperty("user.home"), "Downloads").path
var safeNameVideo = ""
var safeNameAudio = ""

suspend fun downloadVideoAudio() {
    val videoExist = dataYt?.videoFormats?.isNotEmpty() == true
    val audioExist = dataYt?.audioFormats?.isNotEmpty() == true
    safeNameVideo = videoDownload?.name?.replace("/","-") ?: ""
    safeNameAudio = audioDownload?.name?.replace("/","-") ?: ""

    runBlocking {
        if (videoExist) {
            if (!download("Download video", videoDownload?.url, safeNameVideo)) return@runBlocking
        }
        if (audioExist){
            if(!download("Download audio", audioDownload?.url, safeNameAudio)) return@runBlocking
        }
        if (videoExist && audioExist) mergeAudioVideo()
        download = false
    }
}

private fun convertSize(size: Long) = "%.2fmb".format(size.toFloat().div(1048576))

private var client: HttpClient? = null

/**
 * download video or audio and save to folder download
 * */
private suspend fun download(state: String, url: String?, name: String): Boolean{
    download = true
    status = name
    statusProgressDownload = state

    return try {
        val file = File(dirDownload, name)

        client = HttpClient {
            install(HttpTimeout){
                requestTimeoutMillis = Long.MAX_VALUE
            }
        }

        val httpResponse = client?.get(url!!){
            onDownload { bytesSentTotal, contentLength ->
                val i = convertSize(bytesSentTotal)
                val j = convertSize(contentLength)
                statusProgressDownload = "$state $i / $j"
            }
        }

        if(file.exists()) file.delete()
        val responseBody: ByteReadChannel = httpResponse!!.body()
        statusProgressDownload = "waiting write file ..."
        withContext(Dispatchers.IO) {
            file.createNewFile()
        }
        while(!responseBody.isClosedForRead){
            val packet = responseBody.readRemaining(DEFAULT_BUFFER_SIZE.toLong())
            while (packet.isNotEmpty){
                val bytes = packet.readBytes()
                file.appendBytes(bytes)
                val i = convertSize(file.length())
                val j = convertSize(responseBody.totalBytesRead)
                statusProgressDownload = "write file $i / $j"
            }
        }
        statusProgressDownload = ""
        val res = file.exists() && file.length() != 0L
        if(!res) {
            if(file.exists())file.delete()
            download = false
            status = "fail write file"
        }
        res
    }
    catch(e: Exception){
        download = false
        status = "fail download $name"
        Logger.error(e.message)
        false
    }
}

fun cancelDownloadVideoAudio() {
    runBlocking {
        download = false
        client?.cancel()
        delay(1000)
        status = ""
        statusProgressDownload = ""
    }
}

suspend fun mergeAudioVideo(){
    download = true
    statusProgressDownload = "merge audio and video"
    try {
        val video = File(dirDownload, safeNameVideo).path
        val audio = File(dirDownload, safeNameAudio).path
        val outputAudioArr = safeNameAudio.split('.')
        val outputVideoArr = safeNameVideo.split('.')

        val nameOutput = outputVideoArr[0]
        val fmtAudio = outputAudioArr[outputAudioArr.size -2]
        val fmtVideo = outputVideoArr[outputVideoArr.size -2]
        val extAudio = outputAudioArr[outputAudioArr.size -1]
        val extVideo = outputVideoArr[outputVideoArr.size -1]
        val ext =
            if(extAudio == extVideo) extVideo
            else if(extAudio == "webm" && extVideo == "mp4") "mkv"
            else extVideo

        val outputFile = File(dirDownload, "$nameOutput.$fmtVideo-$fmtAudio.$ext")

        val ffmpeg = Loader.load(org.bytedeco.ffmpeg.ffmpeg::class.java)
        statusProgressDownload = "please wait ..."
        withContext(Dispatchers.IO) {
            ProcessBuilder(ffmpeg, "-i", video, "-i", audio, "-c:v", "copy","-c:a", "copy", outputFile.path, "-y").inheritIO()
                .start()
                .waitFor()
        }
        statusProgressDownload = "success merge audio and video"
        statusProgressDownload = "remove unnecessary file"
        try {
            if(outputFile.exists()){
                File(dirDownload, safeNameVideo).delete()
                File(dirDownload, safeNameAudio).delete()
            }
        }catch (e: Exception){
            Logger.error(e.message)
            statusProgressDownload = "fail remove unnecessary file"
        }
        statusProgressDownload = ""
        status = outputFile.path
    }catch (e:Exception){
        status = "error merge audio and video"
        Logger.error(e.message)
    }
    download = false
}
