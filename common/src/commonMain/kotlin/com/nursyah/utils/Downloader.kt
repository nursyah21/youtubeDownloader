package com.nursyah.utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.schabi.newpipe.extractor.utils.Parser.getLinksFromString
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import java.util.zip.GZIPInputStream
import kotlin.text.Charsets.UTF_8


object Utils{
  private var videoId = ""
  private var needSig = false

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

    return emptySet()
    return try {
      var res: String = downloadHtml(client, url)


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

      if(needSig){
        try {
          dechiperUrlVideo(url)
        }catch (e:Exception){
          println(e)
        }
      }

      listData
    }catch (e:Exception){
      emptySet()
    }
  }

  private suspend fun downloadHtml(client: HttpClient = HttpClient(), url: String): String {
    var res: String
    val httpResponse = client.get(url) {
      headers {
        append(HttpHeaders.AcceptEncoding, "gzip")
        append(HttpHeaders.UserAgent, "Mozilla")
      }
    }
    val data: InputStream = httpResponse.body()
    println("download data")
    return try{
      withContext(Dispatchers.IO) {
        res = GZIPInputStream(data).bufferedReader(UTF_8).use { it.readText() }
      }
      res
    }catch (_:Exception){
      String(withContext(Dispatchers.IO) {
        data.readAllBytes()
      }, StandardCharsets.UTF_8)
    }
  }

  private fun extractUrl(url: String): DataUrl {
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

    return DataUrl(url,type,size)
  }

  private suspend fun dechiperUrlVideo(str: String): String {
    return try {
      val id = str.split("?")[1].split("=")[1]
      var urlembed = "https://www.youtube.com/embed/${id}"
      val data = downloadHtml(url = urlembed)
      val html = Jsoup.parse(data)
      val js = html.select("script").attr("name", "player_ias/base")
      var urlJs = ""
      js.forEach {
        if(it.attr("src").contains("base.js")){
          urlJs = "https://www.youtube.com${it.attr("src")}"
        }
      }

      val dataJs = downloadHtml(url = urlJs)

      // get funcName
      val patternFuncName = """.get\("n"\)\)&&\([a-zA-Z\d_$]=([a-zA-Z\d${'$'}_]+)\[(\d+)]"""
        .toRegex().find(dataJs)?.groups
      var funcName = patternFuncName?.get(1)?.value

      try {
        val numFuncName = patternFuncName?.get(2)?.value?.toInt() ?: 0
        val listFuncName = """var ${funcName}\s*=\[(.+?)];""".toRegex().find(dataJs)?.groups
        funcName = listFuncName?.get(numFuncName+1)?.value
      }catch (_:Exception){}

      // get funcCode
      val patternFuncCode =
        "${funcName}=\\s*function([\\S\\s]*?\\}\\s*return [\\w${'$'}]+?\\.join\\(\"\"\\)\\s*\\};)".toRegex()
      val funcCode = patternFuncCode.find(dataJs)?.groups?.get(1)?.value
      println(funcCode)

      data
    }catch (e:Exception){
      println(e)
      ""
    }
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
    needSig = !"&sig=".toRegex().containsMatchIn(temp)

    return temp
  }
}

