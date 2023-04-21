import de.phyrone.buildscripts.KOTLIN_JVM_TARGET
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    kotlin("jvm")
    kotlin("kapt")

    id("com.diffplug.spotless")
    id("org.jetbrains.dokka")

    //shadow
    id("com.github.johnrengelman.shadow")
}

repositories{
    maven("https://repo.phyrone.de/repository/maven/")
}
dependencies {
    compileOnly(project(":server:api"))

    //classindex
    implementation("org.atteo.classindex:classindex:3.13")
    kapt("org.atteo.classindex:classindex:3.13")

    //exposed
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")

    //hikari
    implementation("com.zaxxer:HikariCP:5.0.1")

    //h2
    implementation("com.h2database:h2:2.1.214")



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
    jar {
        enabled = false
        finalizedBy(shadowJar)
    }
    shadowJar {
        dependsOn(jar)

    }
    create<Copy>("copy-core-to-dev") {
        dependsOn(shadowJar)
        destinationDir = rootDir.resolve("debug-workdir/server/")
        from(shadowJar.get().outputs.files.singleFile) {
            into("plugins")
        }

    }
}