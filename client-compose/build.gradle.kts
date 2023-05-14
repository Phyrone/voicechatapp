import de.phyrone.buildscripts.PROJ_CLIENT_ANDROID_COMPILE_SDK
import de.phyrone.buildscripts.PROJ_CLIENT_JAVA_VERSION
import de.phyrone.buildscripts.PROJ_CLIENT_KOTLIN_JVM_TARGET

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

kotlin {
    jvmToolchain(PROJ_CLIENT_KOTLIN_JVM_TARGET)
    jvm("desktop")
    android("android")

    //TODO browser support
    /*
    js(IR) {
        browser()
        binaries.library()
    }*/

    sourceSets {

        @Suppress("UNUSED_VARIABLE")
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))

                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                //implementation(project("ui"))
                implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val androidMain by getting {
            dependencies {
                api("androidx.appcompat:appcompat:1.5.1")
                api("androidx.core:core-ktx:1.10.1")
            }
        }
        @Suppress("UNUSED_VARIABLE")
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
            }
        }
    }
}

compose {
    kotlinCompilerPlugin.set("androidx.compose.compiler:compiler:1.4.7")
}
android {
    namespace = "de.phyrone.voicechatapp.client.common"
    compileSdk = PROJ_CLIENT_ANDROID_COMPILE_SDK

    compileOptions {
        sourceCompatibility = PROJ_CLIENT_JAVA_VERSION
        targetCompatibility = PROJ_CLIENT_JAVA_VERSION
    }


}