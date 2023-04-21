package de.phyrone.voicechatapp.server.plugins

import com.github.benmanes.caffeine.cache.Caffeine
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import org.koin.core.Koin
import org.koin.core.component.KoinComponent

class DefaultSingletonInstancer(
    private val koin: Koin,
) : KoinComponent {
  override fun getKoin(): Koin = koin

  private val cache = Caffeine.newBuilder().softValues().build<Class<*>, Any?>(::instanceClass)

  operator fun get(clazz: Class<*>): Any? = clazz.kotlin.objectInstance ?: cache.get(clazz)

  inline operator fun <reified T> get(clazz: KClass<*>): T = get(clazz.java) as T

  inline fun <reified T> get(): T = get(T::class)

  private fun instanceClass(clazz: Class<*>): Any? {
    for (candidate in clazz.kotlin.constructors) {
      val params = candidate.parameters.mapNotNull(::loadForParam).toMap()

      // check if all required params are loaded and if that's the case call the constructor
      if (params.keys.containsAll(params.keys.filterNot { it.isOptional })) {
        return candidate.callBy(params)
      }
    }
    return null
  }

  private fun loadForParam(param: KParameter): Pair<KParameter, Any?>? {
    val typeContainer = param.type
    val type = typeContainer.classifier as? KClass<*> ?: return null
    val resolved = koin.getOrNull<Any>(type)
    return if (resolved != null) {
      param to resolved
    } else if (typeContainer.isMarkedNullable) {
      param to null
    } else if (param.isOptional) {
      return null
    } else {
      null
    }
  }
}
