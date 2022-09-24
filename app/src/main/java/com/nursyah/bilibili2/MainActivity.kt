package com.nursyah.bilibili2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nursyah.bilibili2.ui.theme.Bilibili2Theme
import kotlinx.coroutines.launch
import okhttp3.*
import okio.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          Bilibili2Theme {
              Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                  App()
              }
          }
        }
    }
}

@Composable
fun App(){
    var url by remember {mutableStateOf("")}
    var status by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(40.dp))
        Text("bilibili")
        OutlinedTextField(value = url, onValueChange = {url = it}, placeholder = {Text("https://www.bilibili.com/video/BV1wr4y1y7sV")})
        Button(onClick = {scope.launch { status = downloadBiliBili(url) }}){Text("submit")}

        Spacer(modifier = Modifier.height(10.dp))
        SelectionContainer {
            Text(text = status,
                Modifier
                    .padding(horizontal = 8.dp)
                    .height(IntrinsicSize.Max)
                    .verticalScroll(scroll))
        }
    }
}

suspend fun realDownloadBilibili(url:String):String = suspendCoroutine {continuation->
    val client = OkHttpClient()
    val request = Request.Builder().url(URL(url)).build()

    client.newCall(request).enqueue(object :Callback{
        override fun onFailure(call: Call, e: IOException) {
            continuation.resumeWithException(e)
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use { continuation.resume(response.body.string()) }
        }
    })
}

suspend fun downloadBiliBili(url:String):String{
    Log.i("test",url)
    if(!"https://.*".toRegex().matches(url)) return "fail"

    val result = realDownloadBilibili(url)
    Log.i("test",result)

    return result
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    Bilibili2Theme {
        Surface {
            App()
        }
    }
}