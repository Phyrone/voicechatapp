import de.phyrone.buildscripts.PROJ_CLIENT_ANDROID_COMPILE_SDK
import de.phyrone.buildscripts.PROJ_CLIENT_JAVA_VERSION
import de.phyrone.buildscripts.PROJ_COMMON_KOTLIN_JVM_TARGET
import de.phyrone.buildscripts.PROJ_COMMON_KOTLIN_JVM_TARGET_STRING
import de.phyrone.buildscripts.WRITE_YOURSELF_TASK_GROUP
import de.phyrone.buildscripts.const.ConstWriteYourself
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.time.Instant

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

kotlin {
    jvmToolchain(PROJ_COMMON_KOTLIN_JVM_TARGET)
    jvm()
    js(IR) {
        browser()
        binaries.library()
    }

    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies{
                implementation(project("const"))
            }
        }
    }
}