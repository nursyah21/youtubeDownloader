package utils

data class DataUrl(
  val url: String,
  val type: String,
  val size: String
)

data class DataDownloader(
  val title: String,
  val listUrl: List<DataUrl>
)