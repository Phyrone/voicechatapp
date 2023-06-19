import de.phyrone.buildscripts.SemanticVersion
import org.jetbrains.dokka.gradle.DokkaCollectorTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleTask
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import org.jetbrains.kotlin.gradle.internal.KaptTask

plugins {
    //kotlin plugins
    kotlin("jvm") version "1.8.21" apply false
    kotlin(("kapt")) version "1.8.21" apply false
    kotlin("multiplatform") version "1.8.21" apply false
    kotlin("android") version "1.8.21" apply false

    //versions plugin
    id("com.github.ben-manes.versions") version "0.46.0"

    //spotless plugin
    id("com.diffplug.spotless") version "6.18.0" apply false

    //shadow
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false

    //dokka
    id("org.jetbrains.dokka") version "1.8.10"

    //android
    id("com.android.application") version "7.4.2" apply false
    id("com.android.library") version "7.4.1" apply false

    id("org.jetbrains.compose") version "1.4.0" apply false
}


val projectVersion = SemanticVersion(1, 0, 0, SemanticVersion.PreReleaseType.DEV)
allprojects {
    apply(plugin = "idea")
    //apply(plugin = "org.jetbrains.dokka")


    group = "de.phyrone"
    version = projectVersion

    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}

tasks {

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
    named("dokkaHtmlMultiModule") {
        dependsOn(
            ":server:dokkaHtmlMultiModule",

        )
    }

    wrapper {
        this.gradleVersion = "8.1.1"
        this.distributionType = Wrapper.DistributionType.ALL
    }
}