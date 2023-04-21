package de.phyrone.voicechatapp.server.api

import kotlin.reflect.KClass

interface Autoloader {

    suspend fun <T : Any> getAnnotated(annotation: Class<out Annotation>, clazz: Class<T>): List<T>
    suspend fun <T : Any> getAnnotated(annotation: KClass<out Annotation>, clazz: KClass<T>): List<T>

    suspend fun getAnnotated(annotation: Class<out Annotation>): List<Any>
    suspend fun getAnnotated(annotation: KClass<out Annotation>): List<Any>

    suspend fun <T : Any> getSubclasses(clazz: Class<T>): List<T>
    suspend fun <T : Any> getSubclasses(clazz: KClass<T>): List<T>

}