import de.phyrone.buildscripts.PROJ_SERVER_JAVA_VERSION
import de.phyrone.buildscripts.PROJ_SERVER_KOTLIN_JVM_TARGET
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

repositories {
    maven("https://repo.phyrone.de/repository/maven/")
}
dependencies {
    implementation("io.ktor:ktor-server-partial-content-jvm:2.3.0")
    compileOnly(project(":common:const"))
    implementation("io.ktor:ktor-server-default-headers-jvm:2.3.0")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:2.3.0")
    implementation("io.ktor:ktor-server-compression-jvm:2.3.0")
    implementation("io.ktor:ktor-server-conditional-headers-jvm:2.3.0")
    implementation("io.ktor:ktor-server-core-jvm:2.3.0")
    implementation("io.ktor:ktor-server-websockets-jvm:2.3.0")
    compileOnly(project(":server:api"))

    //classindex
    implementation("org.atteo.classindex:classindex:3.13")
    kapt("org.atteo.classindex:classindex:3.13")

    //exposed
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")

    //nats
    implementation("io.nats:jnats:2.16.11")

    //hikari
    implementation("com.zaxxer:HikariCP:5.0.1")

    //database drivers (h2, postgresql)
    implementation("com.h2database:h2:2.1.214")
    implementation("org.postgresql:postgresql:42.6.0")

    //ktor
    implementation("io.ktor:ktor-server-core:2.3.0")
    implementation("io.ktor:ktor-server-netty:2.3.0")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.0")
    implementation("io.ktor:ktor-server-websockets:2.3.0")

    //jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")



    testImplementation(kotlin("test"))

}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11
kotlin {
    jvmToolchain(PROJ_SERVER_KOTLIN_JVM_TARGET)
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
    test {
        useJUnitPlatform()
    }
}
java.sourceCompatibility = PROJ_SERVER_JAVA_VERSION
java.targetCompatibility = PROJ_SERVER_JAVA_VERSION
kotlin {
    jvmToolchain(PROJ_SERVER_KOTLIN_JVM_TARGET)
}