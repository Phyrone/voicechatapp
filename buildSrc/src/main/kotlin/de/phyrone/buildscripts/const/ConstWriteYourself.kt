package de.phyrone.buildscripts.const

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

object ConstWriteYourselfTasks {

    open class WriteBuildInfoTask : DefaultTask() {

        @get:Input
        
        @TaskAction
        fun run() {

        }
    }
}