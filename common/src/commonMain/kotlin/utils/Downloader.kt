package utils

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking

object Utils{
  fun downloadUrl(){
    val client = HttpClient()

    runBlocking {
      val httpResponse = client.get("https://ktor.io/")
      if(httpResponse.status.value in 200..299) println("success") else println("error")
    }
  }
}

