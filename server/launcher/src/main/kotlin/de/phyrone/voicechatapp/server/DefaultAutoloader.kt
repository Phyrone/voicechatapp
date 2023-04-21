package de.phyrone.voicechatapp.server

import de.phyrone.voicechatapp.server.api.Autoloader
import de.phyrone.voicechatapp.server.plugins.DefaultSingletonInstancer
import kotlin.reflect.KClass
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.atteo.classindex.ClassIndex
import org.koin.core.component.KoinComponent

class DefaultAutoloader(
    val instancer: DefaultSingletonInstancer,
    val classloader: ClassLoader,
) : Autoloader, KoinComponent {

  private suspend fun Iterable<Class<*>>.instanceAll(): List<Any> = coroutineScope {
    map { async { instancer.get(it) } }.awaitAll().filterNotNull()
  }

  private fun <T> List<Any>.typed(clazz: Class<T>): List<T> = filterIsInstance(clazz)
  override suspend fun <T : Any> getAnnotated(
      annotation: Class<out Annotation>,
      clazz: Class<T>,
  ): List<T> = ClassIndex.getAnnotated(annotation, classloader).instanceAll().typed(clazz)

  override suspend fun <T : Any> getAnnotated(
      annotation: KClass<out Annotation>,
      clazz: KClass<T>,
  ) = getAnnotated(annotation.java, clazz.java)

  override suspend fun getAnnotated(annotation: Class<out Annotation>): List<Any> =
      ClassIndex.getAnnotated(annotation, classloader).instanceAll()

  override suspend fun getAnnotated(annotation: KClass<out Annotation>): List<Any> =
      getAnnotated(annotation.java)

  override suspend fun <T : Any> getSubclasses(clazz: Class<T>): List<T> =
      ClassIndex.getSubclasses(clazz, classloader).instanceAll().typed(clazz)

  override suspend fun <T : Any> getSubclasses(clazz: KClass<T>): List<T> =
      getSubclasses(clazz.java)
}
