package com.nursyah.utils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

var statusDownload1 = MutableStateFlow("")
var statusDownload = ""

suspend fun downloadData1(url: String) {
    (1..10).forEach{
        statusDownload1.value = it.toString()
        delay(1000)
        println(it)
    }
}

fun status(): Flow<String> = flow<String> {
    (1..10).forEach{
//        statusDownload = it.toString()
        emit(it.toString())
        println(it)
        delay(1000)
    }
}
/**
 * download data with status progress
 *
 * */
fun downloadData(
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
        println(e)
    }
}