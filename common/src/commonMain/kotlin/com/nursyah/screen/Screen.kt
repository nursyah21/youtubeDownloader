package com.nursyah.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nursyah.openLink
import com.nursyah.utils.Utils
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun typography() =
    MaterialTheme.typography.copy(body1 = TextStyle.Default.copy(color = Color.White))
@Composable
fun colors() =
    MaterialTheme.colors.copy()

@Composable
fun App() {
    MaterialTheme(
        colors = colors(),
        typography = typography()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.DarkGray
        ){
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Screen()
            }
        }
    }
}

@Composable
private fun Screen() {
    var link by remember { mutableStateOf("https://www.youtube.com/watch?v=86IxCGKUOzY") }
    var load by remember { mutableStateOf(false) }
    var data by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var newData by remember { mutableStateOf(null) }
    var listLink = remember { mutableListOf<String>() }


    Text("input link youtube")

    TextField(
        value = link,
        onValueChange = {link = it}
    )

    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch{
            data = ""
            listLink.clear()
            load = true
            Utils.downloadData(link).forEach {
                data += it.url + "\n\n"
                listLink.add(it.url)
            }
            load = false
        }
    }) {
        Text("download")
    }

    if(load && data.isBlank()) LinearProgressIndicator()

    if(title.isNotBlank()) {
        Text(
            title,
            fontSize = 18.sp
        )
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.height(400.dp).verticalScroll(scrollState)
    ) {
        listLink.forEach {
            Button(
                onClick = { openLink(it) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFAA0000)
                ),
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Download")
            }
        }
    }

    mytest()

}

fun mytest() {
    val i = """cacqw.get("n"))&&(b=dta[0](b),a.set("n",b),dta.length||Tla(""))}};
eta=function(a){nD(a);var b=a.B+(a.B?"://":"//")+a.C+cqwcd"""
    //.get("n"))&&(b=dta[0]
    val b = """.get\("n"\)\)&&\([a-zA-Z0-9_$]=([a-zA-Z0-9${'$'}_]+)\[(\d+)]""".toRegex()
    val c = b.find(i)?.groups
    var name = c?.get(1)?.value
    val j = """asdqwc var dta=[Tla];g.k=g.tD.prototype;g.k.bI=function(a){this.segments.push(a)};"""
    try {
        val re = """var ${name}\s*=\[(.+?)];""".toRegex()

    }catch (e:Exception){
        println(e)
    }


}
