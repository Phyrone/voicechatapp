import de.phyrone.buildscripts.PROJ_CLIENT_KOTLIN_JVM_TARGET
import de.phyrone.buildscripts.SemanticVersion
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

dependencies {
    implementation(project(":client-compose"))
    implementation(compose.desktop.currentOs)
}

kotlin {
    jvmToolchain(11)
}
compose {
    kotlinCompilerPlugin.set("androidx.compose.compiler:compiler:1.4.7")
    desktop {
        application {
            mainClass = "de.phyrone.voicechatapp.client.Main"

            nativeDistributions {
                targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                packageName = "voicechatapp"
                packageVersion = (project.version as? SemanticVersion)?.toComposeCompatibleVersion()
                macOS {

                }
            }
        }
    }
}