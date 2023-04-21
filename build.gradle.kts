import org.jetbrains.dokka.gradle.DokkaCollectorTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.internal.KaptTask

plugins {
    //kotlin plugins
    kotlin("jvm") version "1.8.20" apply false
    kotlin(("kapt")) version "1.8.20" apply false
    kotlin("multiplatform") version "1.8.20" apply false

    //versions plugin
    id("com.github.ben-manes.versions") version "0.46.0"

    //spotless plugin
    id("com.diffplug.spotless") version "6.18.0" apply false

    //shadow
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false

    //dokka
    id("org.jetbrains.dokka") version "1.8.10"
}



allprojects {
    apply(plugin = "idea")
    //apply(plugin = "org.jetbrains.dokka")


    group = "de.phyrone"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}


/*
dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}
*/

tasks {
    wrapper {
        this.gradleVersion = "8.1"
        this.distributionType = Wrapper.DistributionType.ALL
    }
}