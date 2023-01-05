package utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.python.antlr.PythonParser
import org.python.util.PythonInterpreter
import org.schabi.newpipe.extractor.utils.Parser.getLinksFromString
import java.io.InputStream
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import kotlin.text.Charsets.UTF_8


object Utils{
  fun downloadUrl(){
    val client = HttpClient()

    runBlocking {
      val httpResponse = client.get("https://ktor.io/")
      if(httpResponse.status.value in 200..299) println("success") else println("error")
    }
  }

  suspend fun downloadData(url: String): Set<DataUrl>{
    val client = HttpClient()
    val listData = mutableSetOf<DataUrl>()

    return try {
      val httpResponse = client.get(url) {
        headers {
          append(HttpHeaders.AcceptEncoding, "gzip")
          append(HttpHeaders.UserAgent, "Mozilla")
        }
      }
      val data: InputStream = httpResponse.body()
      var res: String
      withContext(Dispatchers.IO) {
        res = GZIPInputStream(data).bufferedReader(UTF_8).use { it.readText() }
      }

      val html = Jsoup.parse(res)
      val title = html.title().dropLast(10)

      val str = getLinksFromString(res)
      val pattern = "googlevideo.com/videoplayback".toRegex()
      var res2 = ""

      str.forEach {
        if(pattern.containsMatchIn(it)) {
          res2 += "${urlDecode(it)}\n\n"
          listData.add(extractUrl(urlDecode(it)))
        }
      }

      println(title)
      listData
    }catch (e:Exception){
      emptySet<DataUrl>()
    }
  }



  private fun extractUrl(url: String): DataUrl{
    var type = ""
    var size = ""
    url.split("&").forEach {
      if(it.startsWith("mime")){
        type = it.split("=")[1]
      }
      if(it.startsWith("itag")){
        type = extractItag(it.split("=")[1])
      }
      if(it.startsWith("clen")){
        size = it.split("=")[1]
        size = "%.02fmb".format(size.toFloat() / 1048576)
      }

    }
    /*if(type.isNotBlank()) {
      println(type)
      println(size)
    }*/

    return DataUrl(url,type,size)
  }

  private fun extractItag(s: String): String {
    val fmt = listOf(hashMapOf("as" to "as"))
    fmt.forEach {
      it[s]
    }
    return ""
  }

  private fun urlDecode(it: String?):String {
    val temp = it?.replace("\\u0026","&")
      ?.replace("%25", "%")
      ?.replace("%2C",",")
      ?.replace("%3D","=")
      ?.replace("%2F","/")
      ?.replace("%26", "&")
      ?.replace("%3F","?") ?: ""

    val needSig = !"&sig=".toRegex().containsMatchIn(temp)

    if(needSig){
      println("need sig")
    }

    return temp
  }
}

