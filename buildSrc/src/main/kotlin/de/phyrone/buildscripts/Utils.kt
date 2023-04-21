package de.phyrone.buildscripts

import org.gradle.api.artifacts.ResolvedDependency
import java.io.File


fun Collection<ResolvedDependency>.withSubDependencies(): List<ResolvedDependency> =
    this.flatMap { it.resolveToAllIncludeSubDependencies() }.distinct()

fun Collection<ResolvedDependency>.jarFiles(): List<File> =
    this.map { it.moduleArtifacts.filter { it.file.extension == "jar" } }
        .flatten().map { it.file }

fun ResolvedDependency.resolveToAllIncludeSubDependencies(): List<ResolvedDependency> {
    val result = mutableListOf<ResolvedDependency>()
    val stack = mutableListOf<ResolvedDependency>()

    stack.add(this)

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        result.add(current)

        current.children.forEach {
            if (!result.contains(it) && !stack.contains(it)) {
                stack.add(it)
            }
        }
    }

    return result
}