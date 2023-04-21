import de.phyrone.buildscripts.KOTLIN_JVM_TARGET
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    kotlin("jvm")
    kotlin("kapt")

    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
}

dependencies {

    api(kotlin("stdlib"))
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))

    //kotlin coroutines
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.0-Beta")

    //classindex
    implementation("org.atteo.classindex:classindex:3.13")
    kapt("org.atteo.classindex:classindex:3.13")

    //google fluent logger
    api("com.google.flogger:flogger:0.7.4")

    //apache commons
    api("org.apache.commons:commons-lang3:3.12.0")

    //google guava
    api("com.google.guava:guava:31.1-jre")

    //koin
    api("io.insert-koin:koin-core:3.4.0")

}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11
kotlin {
    jvmToolchain(KOTLIN_JVM_TARGET)
}

spotless {
    kotlin {
        licenseHeader(
            """
            {{ project }}
            Copyright (C) {{ year }}  {{ organization }}

            This program is free software: you can redistribute it and/or modify
            it under the terms of the GNU General Public License as published by
            the Free Software Foundation, either version 3 of the License, or
            (at your option) any later version.

            This program is distributed in the hope that it will be useful,
            but WITHOUT ANY WARRANTY; without even the implied warranty of
            MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
            GNU General Public License for more details.

            You should have received a copy of the GNU General Public License
            along with this program.  If not, see <http://www.gnu.org/licenses/>.
        """.trimIndent()
        )
        ktlint()
        ktfmt()
        diktat().configFile(rootProject.projectDir.resolve("diktat-analysis.yml"))
    }
}

tasks {
    withType<DokkaTask>() {
        dependsOn("kaptKotlin")
    }
    withType<DokkaTaskPartial>() {
        dependsOn("kaptKotlin")
    }
}