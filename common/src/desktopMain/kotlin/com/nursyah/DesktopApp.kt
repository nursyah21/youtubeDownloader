package com.nursyah

import java.awt.Desktop
import java.net.URI

actual fun getPlatformName(): String = "Desktop"

actual fun openLink(s: String) {
  Desktop.getDesktop().browse(URI.create(s))
}