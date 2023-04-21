package de.phyrone.voicechatapp.server.plugins

import java.io.File
import org.atteo.classindex.ClassIndex

class PluginLoader(
    private val classloader: JarClassloader,
) {

  fun loadPlugins(folder: File = File("plugins")) {
    require(folder.isDirectory || !folder.exists()) {
      "${folder.absolutePath} is a file but needs to be a folder!"
    }
    folder.mkdirs()
    folder
        .listFiles { file -> file.extension.equals("jar", true) }
        ?.forEach { file -> classloader.addFile(file.absolutePath) }
  }

  fun loadSubclassesOf(clazz: Class<*>) = ClassIndex.getSubclasses(clazz, classloader)

  fun loadAnnotatedClasses(clazz: Class<out Annotation>) =
      ClassIndex.getAnnotated(clazz, classloader)
}
