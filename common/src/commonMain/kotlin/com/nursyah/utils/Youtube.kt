package com.nursyah.utils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

class Youtube{
    private val _data = MutableStateFlow("")
    val data = _data.asStateFlow()

    suspend fun download(){
        (1..100).forEach{
            _data.value = it.toString()
            delay(1000)
        }

    }
}

var statusDownload by mutableStateOf("")
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

/**
 * download data with status progress
 *
 * */
fun downloadData1(
    url: String
) = flow<String> {
    try{
        val response = HttpClient().get(url){
            onDownload{ bytesSentTotal, contentLength ->
                statusDownload = if(contentLength > 0) "Received $bytesSentTotal bytes from $contentLength"
                else "Received $bytesSentTotal bytes"
                emit(statusDownload)
                println(statusDownload)
            }
        }
        val responseBody = response.body<String>()
        println(responseBody)
    }catch (e:Exception){
        emit("Error download data")
        println(e)
    }

}