package com.nursyah.utils

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MyData {
  var myData =  "null"

  fun dataFlow():Flow<String> = flow{
    for(i in 0..10){
      println(i)
      emit(i.toString())
      delay(1000L)
    }
  }

  suspend fun myData(){
    myData = "first run"
    println(myData)
    delay(2000L)
    myData = "2 second has passed"
    println(myData)
    delay(2000L)
    myData = "4 second has passed"
    println(myData)
    delay(2000L)
    myData = "finish"
    println(myData)
  }
}