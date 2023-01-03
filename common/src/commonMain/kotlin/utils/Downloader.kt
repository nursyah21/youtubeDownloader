package utils

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.StreamingService
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.linkhandler.LinkHandler
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.utils.ExtractorHelper
import org.schabi.newpipe.extractor.services.youtube.extractors.*

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
      val data = httpResponse.body<String>()
//      val regex = listOf(
//        Regex("/s/player/(?P<id>[a-zA-Z0-9_-]{8,})/player")
//      )
      data
      //        _PLAYER_INFO_RE = (
//        r'/s/player/(?P<id>[a-zA-Z0-9_-]{8,})/player',
//        r'/(?P<id>[a-zA-Z0-9_-]{8,})/player(?:_ias\.vflset(?:/[a-zA-Z]{2,3}_[a-zA-Z]{2,3})?|-plasma-ias-(?:phone|tablet)-[a-z]{2}_[A-Z]{2}\.vflset)/base\.js$',
//        r'\b(?P<id>vfl[a-zA-Z0-9_-]+)\b.*?\.js$',
//    )
    } catch (e:Exception){
      e.printStackTrace()
      e.toString()
    }
  }
}

