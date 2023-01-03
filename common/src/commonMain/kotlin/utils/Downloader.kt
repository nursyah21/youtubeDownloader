package utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.python.util.PythonInterpreter
import java.io.File
import java.io.InputStream


object Utils{
  fun downloadUrl(){
    val client = HttpClient()

    runBlocking {
      val httpResponse = client.get("https://ktor.io/")
      if(httpResponse.status.value in 200..299) println("success") else println("error")
    }
  }

  suspend fun downloadData(url: String): String{
    val client = HttpClient()

    return try {
      val httpResponse = client.get(url)
      //val data = httpResponse.body<String>()
      val file = File("test.txt")
      //javaClass.getResource()
      println("path: ${file.path}, ${file.exists()}")
      //val scriptPython:InputStream = file.inputStream()

      PythonInterpreter().use {
        it.exec("""
          _PLAYER_INFO_RE = (
            r'/s/player/(?P<id>[a-zA-Z0-9_-]{8,})/player',
            r'/(?P<id>[a-zA-Z0-9_-]{8,})/player(?:_ias\.vflset(?:/[a-zA-Z]{2,3}_[a-zA-Z]{2,3})?|-plasma-ias-(?:phone|tablet)-[a-z]{2}_[A-Z]{2}\.vflset)/base\.js${'$'}',
            "\b(?P<id>vfl[a-zA-Z0-9_-]+)\b.*?\.js${'$'}",
          )
          import youtube_dl
          print('hello')
        """.trimIndent()
        )
      }
      "test"
      //data

    } catch (e:Exception){
      e.printStackTrace()
      e.toString()
    }
  }
}

