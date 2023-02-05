import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.google.common.base.Charsets
import com.google.common.io.Resources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun app() {
    //test()
    Column {
        searchBar()
        youtube()
    }
    bottom()
}

@Composable
private fun youtube(){
    val modifier = Modifier.padding(5.dp).verticalScroll(rememberScrollState())
    val focusManager = LocalFocusManager.current

    AnimatedVisibility(
        visible = dataYt != null,
        enter = slideInHorizontally() + fadeIn(),
        exit = slideOutHorizontally() + fadeOut(),
    ){
        Column(
            modifier,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ){
            asyncImage(
                load = { loadImage(dataYt?.thumbnail) },
                painterFor = { remember { BitmapPainter(it!!) } }
            )

            SelectionContainer {
                Column{
                    Text("Title: ${dataYt?.title?: ""}")
                    Text("Channel: ${dataYt?.uploader?: ""}")
                }
            }
//            if(dataYt?.videoFormats?.isNotEmpty()!!) downloadOption(dataYt?.videoFormats, "Video")
//            if(dataYt?.audioFormats?.isNotEmpty()!!) downloadOption(dataYt?.audioFormats, "Audio")
            downloadOption(dataYt?.videoFormats, "Video")
            downloadOption(dataYt?.audioFormats, "Audio")
            var buttonDownload by remember { mutableStateOf(false) }

            buttonDownload = statusProgressDownload.isBlank() ||
                    progressDownload == 1f ||
                    statusProgressDownload == ""

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)){
                Button(
                    onClick = {
                        if(buttonDownload) CoroutineScope(Dispatchers.IO).launch {
                            downloadVideoAudio()
                        }
                        else cancelDownloadVideoAudio()
                        focusManager.clearFocus()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = redYt,
                        contentColor = Color.White
                    )
                ){
                    Text(
                        if(buttonDownload) "Download" else "Cancel"
                    )
                }

                AnimatedVisibility(
                    visible = progressDownload == 1f ||
                            statusProgressDownload == "success merge audio and video" ||
                            !download,
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ){
                    Button(
                        onClick = {
                          dataYt = null
                          progressDownload = 0f
                          statusProgressDownload = ""
                          status = ""
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = redYt,
                            contentColor = Color.White
                        )
                    ){
                        Text("Search")
                    }
                }
            }
        }
    }
}

@Composable
private fun  downloadOption(data: List<FormatYt>?, title: String){
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero)}
    var selectedText by remember { mutableStateOf(getNameSize(data?.get(0))) }
    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown

    println(videoDownload)
    println(audioDownload)
    with(getDataYt(data?.get(0))){
        if(title == "Video" && videoDownload == null) videoDownload = this
        else if(audioDownload == null) audioDownload = this
    }

    Column {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            label = {Text(title)},
            trailingIcon = {
                Icon(icon,null,
                    Modifier.clickable {
                        if(statusProgressDownload.isBlank() || progressDownload == 1f) expanded = true
                    }
                )
            },
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = redYt
            ),
            enabled = statusProgressDownload.isBlank() || progressDownload == 1f
                    || statusProgressDownload == "success merge audio and video"
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textFieldSize.width.toDp()})
        ) {
            data?.forEachIndexed { index, formatYt ->
                DropdownMenuItem(onClick = {
                    selectedText = getNameSize(formatYt)
                    expanded = !expanded

                    with(getDataYt(data[index])){
                        if(title == "Video") videoDownload = this
                        else audioDownload = this
                    }
                }) {
                    Text(text = getNameSize(formatYt))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun searchBar(){
    AnimatedVisibility(
        visible = statusProgressDownload.isBlank() && dataYt == null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ){
        TextField(
            link,
            onValueChange = {link = it},
            modifier = Modifier
                .fillMaxWidth()
                .onKeyEvent {
                    if(it.type == KeyEventType.KeyDown && it.key.keyCode == Key.Enter.keyCode){
                        CoroutineScope(Dispatchers.IO).launch {
                            downloadYt()
                        }
                    }
                    false
                },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor =  redYt,
                textColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = redYt
            ),
            shape = AbsoluteCutCornerShape(0),
            placeholder = { Text("paste link youtube") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                CoroutineScope(Dispatchers.IO).launch {
                    downloadYt()
                }
            }),
        )
    }

    if(download){
        LinearProgressIndicator(
            Modifier.fillMaxWidth(),
            color = redYt,
            backgroundColor = Color.White
        )
    }
}

@Composable
private fun bottom(){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        status()
        Spacer(Modifier.padding(vertical = 2.dp))
        ads()
    }
}

@Composable
private fun status(){
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // progress download
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                statusProgressDownload,
                color = statusText,
                fontSize = 12.sp,
            )
        }

        // text marque
        val scrollState = rememberScrollState()
        var shouldAnimate by remember {
            mutableStateOf(true)
        }
        LaunchedEffect(shouldAnimate){
            scrollState.animateScrollTo(
                scrollState.maxValue,
                animationSpec = tween(3000, 30)
            )
            scrollState.scrollTo(0)
            shouldAnimate = !shouldAnimate
        }

        Text(
            text = status,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            color = statusText,
            modifier = Modifier.padding(4.dp).horizontalScroll(scrollState, false),
            maxLines = 1,
        )
    }
}

@Composable
private fun ads(){
    Card(
        Modifier.fillMaxWidth().clickable {
            openLink("https://i.ibb.co/cv3KQ5C/Whats-App-Image-2023-02-04-at-02-03-52.jpg")
        },
        backgroundColor = greyAds,
    ){
        Text(
            "DONATE WITH GOPAY",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Composable
private fun <T> asyncImage(
    load: suspend () -> T,
    painterFor: @Composable (T) -> Painter
){
    val image:T? by produceState<T?>(null){
        value = withContext(Dispatchers.IO){
            try {
                load()
            }catch (e: IOException){
                Logger.error(e.message)
                null
            }
        }
    }

    if (image != null){
        Image(
            painterFor(image!!),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(5.dp)
        )
    }
}
