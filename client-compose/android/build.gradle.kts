import de.phyrone.buildscripts.PROJ_CLIENT_ANDROID_COMPILE_SDK

plugins {
    kotlin("android")
    id("com.android.application")
}

dependencies {
    implementation(project(":client-compose"))

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation(platform("androidx.compose:compose-bom:2023.05.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
android {
    namespace = "de.phyrone.voicechatapp.client"
    compileSdk = PROJ_CLIENT_ANDROID_COMPILE_SDK
    this.androidResources
    defaultConfig {
        applicationId = "de.phyrone.voicechatapp"
        minSdk = 21
        targetSdk = 33
        versionCode = 1

        vectorDrawables.useSupportLibrary = true
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }


}
kotlin {
    jvmToolchain(8)
}