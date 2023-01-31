package com.nursyah.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nursyah.utils.download
import com.nursyah.utils.downloadData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

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
                TempScreen()
            }
        }
    }
}



var search by mutableStateOf("")
@Composable
private fun TempScreen(){
    SearchBar(
        search,
        onDone = {
            download = !download
        }
    ) {search = it}

    StatusDownload()
}

@Composable
private fun StatusDownload(){
    var status by remember { mutableStateOf("") }

    Text(
        status,
        color = Color.White.copy(alpha = .7f),
        fontSize = 12.sp
    )

    LaunchedEffect(download){
        if(search.isNotEmpty()) downloadData(search).collect{status = it}
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(
    search: String,
    onDone: ()->Unit = {},
    onValueChange: (String) -> Unit,
){
    OutlinedTextField(
        value = search,
        onValueChange = onValueChange,
        keyboardActions = KeyboardActions(
            onDone = { println(search) }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        modifier = Modifier.onKeyEvent{
            if(it.type == KeyEventType.KeyDown && it.key.keyCode == Key.Enter.keyCode)onDone.invoke()
            false
        },
        label = {
            Text("link youtube", color = Color.White.copy(alpha = .7f))
        }
    )
}


fun mytest() {
    val i = """g.M.call(this,a)};
Tla=function(a){var b=a.split(""),c=[-382806166,2056665129,-888575707,1764770305,98308992,-1483961372,function(d,e,f,h,l){return e(f,h,l)},
function(d,e){for(e=(e%d.length+d.length)%d.length;e--;)d.unshift(d.pop())},
-825128129,null,function(d,e,f,h,l,m,n,p,q,r){return e(l,m,n,p,q,r)},
512565425,-1659495181,-103794430,null,1797616044,-1650451862,924590065,function(d,e){d=(d%e.length+e.length)%e.length;e.splice(0,1,e.splice(d,1,e[0])[0])},
b,function(d){for(var e=d.length;e;)d.push(d.splice(--e,1)[0])},
-940564641,1536822439,1727548152,function(){for(var d=64,e=[];++d-e.length-32;){switch(d){case 58:d-=14;case 91:case 92:case 93:continue;case 123:d=47;case 94:case 95:case 96:continue;case 46:d=95}e.push(String.fromCharCode(d))}return e},
-103794430,-1027639546,/]'(',()])(;)/,b,function(d){for(var e=d.length;e;)d.push(d.splice(--e,1)[0])},
function(d){d.reverse()},
-452294242,-1932680662,function(d,e,f){var h=e.length;d.forEach(function(l,m,n){this.push(n[m]=e[(e.indexOf(l)-e.indexOf(this[m])+m+h--)%e.length])},f.split(""))},
-329103904,1376361579,function(d,e){d=(d%e.length+e.length)%e.length;e.splice(d,1)},
1855391894,299750877,function(d,e,f,h,l,m){return e(h,l,m)},
-1911259912,1657740765,-1653317071,-1066231885,"JRlYmp",-1045722836,149786639,1716215834,function(d,e,f,h,l,m,n,p){return d(m,n,p)},
function(){for(var d=64,e=[];++d-e.length-32;){switch(d){case 91:d=44;continue;case 123:d=65;break;case 65:d-=18;continue;case 58:d=96;continue;case 46:d=95}e.push(String.fromCharCode(d))}return e},
-2100264169,-210853790,function(d,e){d=(d%e.length+e.length)%e.length;var f=e[0];e[0]=e[d];e[d]=f},
function(d,e,f,h,l,m,n,p,q,r,w,x){return f(h,l,m,n,p,q,r,w,x)},
"\"',,)",1861497500,function(){for(var d=64,e=[];++d-e.length-32;)switch(d){case 58:d=96;continue;case 91:d=44;break;case 65:d=47;continue;case 46:d=153;case 123:d-=58;default:e.push(String.fromCharCode(d))}return e},
-689992620,-655881835,-1077893745,209625078,-850696961,b,1813324137,41289392,function(d,e){e.splice(e.length,0,d)},
482975614,function(d,e){d=(d%e.length+e.length)%e.length;e.splice(-d).reverse().forEach(function(f){e.unshift(f)})},
null];c[9]=c;c[14]=c;c[68]=c;try{try{-8<=c[15]&&(2<=c[66]?(0,c[new Date("31 December 1969 19:00:18 EST")/1E3])(c[55],c[68]):(0,c[33])(c[62],(0,c[24])(),c[0])),c[45]<new Date("1969-12-31T17:14:54.000-06:45")/1E3&&(0,c[10])(((0,c[30])(c[9]),c[62])((0,c[62])((0,c[16])(c[28],c[54]),c[50],c[56],c[Math.pow(3,3)+6351-6324]),c[61],c[49],c[18])<<(0,c[61])(c[6],c[64]),c[29],(0,c[16])(c[46],c[6]),(0,c[39])(c[59]),(0,c[Math.pow(1,1)+76+-59])(c[3],c[0]),c[65],(0,c[67])(c[Math.pow(3,2)+-1550- -1542],c[62])>(0,c[18])(c[26],
c[16]),c[47],c[14])<<(0,c[52])(c[13],c[62]),-1>=c[43]&&(0,c[53])((((0,c[20])(c[62]),c[18])(c[57],c[28]),(0,c[36])(c[35],c[28])),(0,c[39])((0,c[163+Math.pow(4,5)+-1180])(c[28],c[0]),c[67],(0,c[new Date("01 January 1970 11:45:18 +1145")/1E3])(c[41],c[62]),c[21],c[9])%(0,c[25])((0,c[26])(c[35],c[63]),c[60],c[67],c[44]),c[50],c[38],(0,c[20])(c[61],c[18]),(0,c[54])(c[39],c[21]),(0,c[69])(c[34],c[16]),(0,c[27])(c[new Date("1969-12-31T18:00:48.000-06:00")/1E3],c[new Date("Wednesday 31 December 1969 17:00:22 PDT")/
1E3]),c[1],c[39])}catch(d){(0,c[17])(c[25],c[Math.pow(3,4)-65- -13]),(0,c[26])((0,c[17])(c[8],c[12]),c[53],c[39],(0,c[6])(),c[4])}try{4!==c[52]&&(9==c[45]||((0,c[56])(c[18],c[12]),0))&&(0,c[56])(c[58],c[34]),(9==c[-365+426%Math.pow(7,5)]||((0,c[26])((0,c[26])((0,c[53])(c[12],(0,c[69])(),c[4]),c[2],c[65],c[39]),c[new Date("01/01/1970 00:00:02 UTC")/1E3],c[35],c[36]),(0,c[2])(c[16],c[36]),0))&&(((0,c[26])((0,c[50])(c[48]),c[53],c[Math.pow(1,4)+-12804+12851],(0,c[0])(),c[4]),c[56])(c[51],c[new Date("31 December 1969 20:00:48 EDT")/
1E3]),c[53])(c[12],(0,c[0])(),c[4])}catch(d){(0,c[27])(c[12],c[44]),(0,c[17])(c[62],c[12])}finally{-2<c[19]&&(-3>=c[66]||((0,c[38])(c[43],c[34]),0))&&(0,c[38])(c[63],c[34]),(c[45]!==-25*Math.pow(3,5)+6085||((((0,c[56])(c[37],c[12]),c[27])(c[29],c[67]),c[50])(c[36]),0))&&(0,c[59])((0,c[17])(c[54],c[393%Math.pow(5,2)+16]),c[25],(0,c[Math.pow(8,1)%148+-5])(c[12],c[56]),c[56]),8!=c[7]&&(0,c[46])(c[40],c[22]),-2!==c[67]&&(-9<c[39]?((0,c[14])(c[3]),c[46])(c[5],c[8]):(0,c[0])((0,c[24])(c[10]),c[23],c[67],
c[13]))}}catch(d){return"enhanced_except_n5cB4Oj-_w8_"+a}return b.join("")};
g.Pp=function(a){this.name=a};
Qp=function(a){g.M.call(this,a)};"""
    val c = "Tla"
    val b = "${c}=\\s*function([\\S\\s]*?\\}\\s*return [\\w${'$'}]+?\\.join\\(\"\"\\)\\s*\\};)".toRegex()


    //.get("n"))&&(b=dta[0]
    /*val b = """.get\("n"\)\)&&\([a-zA-Z0-9_$]=([a-zA-Z0-9${'$'}_]+)\[(\d+)]""".toRegex()
    val c = b.find(i)?.groups
    var name = c?.get(1)?.value
    val j = """asdqwc var dta=[Tla];g.k=g.tD.prototype;g.k.bI=function(a){this.segments.push(a)};"""*/
    try {
        println(b.find(i)?.groups?.get(1)?.value)
        /*val re = """var ${name}\s*=\[(.+?)];""".toRegex()*/

    }catch (e:Exception){
        println(e)
    }

}
