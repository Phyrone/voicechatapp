package de.phyrone.voicechatapp.server.plugins

import java.net.URL
import java.net.URLClassLoader

open class JarClassloader(
    parent: ClassLoader? = null,
) : URLClassLoader(arrayOf(), parent) {

  fun addFile(file: String) {
    addURL(java.io.File(file).toURI().toURL())
  }

  public override fun addURL(url: URL?) {
    super.addURL(url)
  }
}
