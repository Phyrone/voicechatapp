package de.phyrone.voicechatapp.server.api

import org.atteo.classindex.ClassIndex
import org.atteo.classindex.IndexAnnotated
import org.atteo.classindex.IndexSubclasses

import java.util.ServiceLoader

import kotlin.reflect.KClass

/**
 * Autoloader allows to load classes indexed at compile time The retuned classes are singletons and
 * reused if already instantiated Autoloader uses [ClassIndex] to find classes at runtime using
 * generated resouces at compiletime. There is no classpath scanning involved. This means that the
 * classes must be indexed at compile time using [IndexAnnotated] or [IndexSubclasses].
 *
 * @see ClassIndex
 */
interface Autoloader {
    /**
     * Gets a list of all instances annotated with the given [annotation] and are children of the
     * given [clazz]
     *
     * @param annotation for which indexed classes should be returned it must be annotated with
     *   [IndexAnnotated]
     * @param clazz the class which the returned classes must be children of classes that are not
     *   children of [clazz] will be ignored but instanced anyway
     * @return a list of instances of the given [clazz] and [annotation]
     */
    suspend fun <T : Any> getAnnotated(annotation: Class<out Annotation>, clazz: Class<T>): List<T>

    /**
     * Gets a list of all instances annotated with the given [annotation] and are children of the
     * given [clazz]
     *
     * @param annotation for which indexed classes should be returned it must be annotated with
     *   [IndexAnnotated]
     * @param clazz the class which the returned classes must be children of classes that are not
     *   children of [clazz] will be ignored but instanced anyway
     * @return a list of instances of the given [clazz] and [annotation]
     */
    suspend fun <T : Any> getAnnotated(annotation: KClass<out Annotation>, clazz: KClass<T>): List<T>

    /**
     * Gets a list of all instances annotated with the given [annotation]
     *
     * @param annotation for which indexed classes should be returned it must be annotated with
     *   [IndexAnnotated]
     * @return a list of instances of the given [annotation]
     */
    suspend fun getAnnotated(annotation: Class<out Annotation>): List<Any>

    /**
     * Gets a list of all instances annotated with the given [annotation]
     *
     * @param annotation for which indexed classes should be returned it must be annotated with
     *   [IndexAnnotated]
     * @return a list of instances of the given [annotation]
     */
    suspend fun getAnnotated(annotation: KClass<out Annotation>): List<Any>

    /**
     * get all subclasses of the given [clazz] that are indexed the [clazz] must be annotated with
     * [IndexSubclasses]. It behaves similar to the [ServiceLoader] but only instantiates the classes
     * once. So f.e. AutoService annotated classes are also work
     *
     * @param clazz the class which the returned classes must be children of classes that are not
     *   children of [clazz] will be ignored but instanced anyway
     * @return a list of instances of the given [clazz]
     * @see ServiceLoader
     */
    suspend fun <T : Any> getSubclasses(clazz: Class<T>): List<T>

    /**
     * get all subclasses of the given [clazz] that are indexed the [clazz] must be annotated with
     * [IndexSubclasses]. It behaves similar to the [ServiceLoader] but only instantiates the classes
     * once. So f.e. AutoService annotated classes are also work
     *
     * @param clazz the class which the returned classes must be children of classes that are not
     *   children of [clazz] will be ignored but instanced anyway
     * @return a list of instances of the given [clazz]
     * @see ServiceLoader
     */
    suspend fun <T : Any> getSubclasses(clazz: KClass<T>): List<T>
}
