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

val writeYourselfDIR = buildDir.resolve("write-yourself")

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
            kotlin {
                srcDir(writeYourselfDIR)
            }
        }
    }
}

tasks {

    val buildInfoTask = create("writeBuildInfo") {
        val buildTime =Instant.now()
        val buildInfoProps = mapOf(
            "version" to version.toString(),
            "build_timestamp" to buildTime.toString(),
            "build_timestamp_millis" to buildTime.toEpochMilli(),
        ) + ConstWriteYourself.getGitInfo(rootProject.projectDir)

        group = WRITE_YOURSELF_TASK_GROUP

        buildInfoProps.forEach { (key, value) ->
            inputs.property(key, value)
        }
        outputs.dir(writeYourselfDIR)
        doLast {
            ConstWriteYourself.writeBuildInfo(
                writeYourselfDIR,
                buildInfoProps
            )
        }
    }

    val writeTasks = listOf(buildInfoTask)

    val kotlinCompileTasks = listOf(
        "compileCommonMainKotlinMetadata",
        "compileKotlinJs",
        "compileKotlinJvm"
    )
    withType {
        if (this.name in kotlinCompileTasks)
            dependsOn(*writeTasks.toTypedArray())
    }
    withType<DokkaTask>(){
        dependsOn(*writeTasks.toTypedArray())
    }
    withType<DokkaTaskPartial>(){
        dependsOn(*writeTasks.toTypedArray())
    }

}