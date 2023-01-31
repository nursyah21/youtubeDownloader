package com.nursyah.utils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import kotlinx.coroutines.flow.channelFlow

var download by mutableStateOf(false)

/**
 * download data with status progress
 *
 * */
fun downloadData(url: String) = channelFlow<String>{
    try{
        send("download data")
        val response = HttpClient().get(url){
            onDownload{ bytesSentTotal, contentLength ->
                val tempData = if(contentLength > 0) "Received $bytesSentTotal bytes from $contentLength"
                else "Received $bytesSentTotal bytes"
                send(tempData)
            }
        }
        send("success download data")
        val responseBody = response.body<String>()
        println(responseBody)
    }catch (e:Exception) {
        println(e)
        send("error Download data")
    }
}

data class DataDownload(
    val url: String,
    val size: String,
    val format: String
)
data class YTDownload(
    val urlImg: String,
    val title: String,
    val desc: String,
    val duration: String,
    val download: List<DataDownload>
)

fun extractData(data: String = yttext) {
//    link
    println(data)
}
