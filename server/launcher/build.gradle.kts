import de.phyrone.buildscripts.PROJ_JAVA_VERSION
import de.phyrone.buildscripts.PROJ_KOTLIN_JVM_TARGET
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins{
    kotlin("jvm")
    kotlin("kapt")

    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")
}

dependencies {

    implementation(project(":common:const"))
    implementation(project(":server:api"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    //kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.0-RC")

    //koin
    implementation("io.insert-koin:koin-core:3.4.0")
    //implementation("io.insert-koin:koin-logger-slf4j:3.4.0")


    //logback
    implementation("ch.qos.logback:logback-classic:1.4.7")
    //google fluent logger
    implementation("com.google.flogger:flogger:0.7.4")
    implementation("com.google.flogger:flogger-slf4j-backend:0.7.4")

    //picocli
    implementation("info.picocli:picocli:4.7.3")
    kapt("info.picocli:picocli-codegen:4.7.3")

    //jansi
    implementation("org.fusesource.jansi:jansi:2.4.0")

    //apache commons
    implementation("org.apache.commons:commons-lang3:3.12.0")

    //google guava
    implementation("com.google.guava:guava:31.1-jre")

    //classindex
    implementation("org.atteo.classindex:classindex:3.13")
    kapt("org.atteo.classindex:classindex:3.13")

    //caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")

    //auto
    implementation("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")

    //jfiglet
    implementation("com.github.lalyos:jfiglet:0.0.9")

    //oshi
    implementation("com.github.oshi:oshi-core:6.4.2")

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
        //diktat()
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
java.sourceCompatibility = PROJ_JAVA_VERSION
java.targetCompatibility = PROJ_JAVA_VERSION
kotlin {
    jvmToolchain(PROJ_KOTLIN_JVM_TARGET)
}