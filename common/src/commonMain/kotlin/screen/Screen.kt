package screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import utils.Utils
import java.awt.TextArea

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
    var link by remember { mutableStateOf("https://www.youtube.com/watch?v=puZ4gdj1OD0") }
    var load by remember { mutableStateOf(false) }
    var data by remember { mutableStateOf("") }

    Text("input link youtube")

    TextField(
        value = link,
        onValueChange = {link = it}
    )

    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch{
            data = ""
            load = true
            data = Utils.downloadData(link)
            load = false
        }
    }) {
        Text("download")
    }

    if(load && data.isBlank()) LinearProgressIndicator()

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.height(400.dp).verticalScroll(scrollState)
    ) {
        TextField(
            value = data,
            onValueChange = {data = it},
            readOnly = true
        )

//        SelectionContainer {
//            Text(data)
//        }
    }
}

