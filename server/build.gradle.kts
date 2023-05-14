import de.phyrone.buildscripts.PROJ_SERVER_JAVA_VERSION
import de.phyrone.buildscripts.PROJ_SERVER_KOTLIN_JVM_TARGET

/*
import de.phyrone.buildscripts.jarFiles
import de.phyrone.buildscripts.withSubDependencies
import proguard.gradle.ProGuardTask

buildscript {
    dependencies {
        classpath("com.guardsquare:proguard-gradle:7.3.2")
    }
}*/
plugins {
    //application
    kotlin("jvm")
    //kotlin("kapt")

    id("com.diffplug.spotless")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.dokka")
}


dependencies {
    implementation(project(":server:launcher"))
    implementation(project(":server:core"))

    /*
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    //kotlin coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-Beta")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.0-Beta")

    //koin
    implementation("io.insert-koin:koin-core:3.4.0")
    implementation("io.insert-koin:koin-logger-slf4j:3.4.0")


    //logback
    implementation("ch.qos.logback:logback-classic:1.4.6")
    //google fluent logger
    implementation("com.google.flogger:flogger:0.7.4")
    implementation("com.google.flogger:flogger-slf4j-backend:0.7.4")

    //picocli
    implementation("info.picocli:picocli:4.7.2")
    kapt("info.picocli:picocli-codegen:4.7.2")

    //jansi
    implementation("org.fusesource.jansi:jansi:2.4.0")

    //apache commons
    implementation("org.apache.commons:commons-lang3:3.12.0")

    //classindex
    implementation("org.atteo.classindex:classindex:3.13")
    kapt("org.atteo.classindex:classindex:3.13")

    //caffeine
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.6")

    //auto
    implementation("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")

     */
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
    jar {
        enabled = false
        finalizedBy(shadowJar)
    }

    val relocationTask = create("relocate-shadow-jar-packages") {

    }
    shadowJar {
        dependsOn(relocationTask)
        enabled = true
        this.entryCompression = ZipEntryCompression.DEFLATED

        //archiveClassifier.set("unoptimized")
        archiveClassifier.set("")
        manifest {
            attributes["Main-Class"] = "de.phyrone.voicechatapp.server.main.ServerMain"
        }
    }
    compileKotlin {
        kotlinOptions.jvmTarget = PROJ_SERVER_KOTLIN_JVM_TARGET.toString()
    }

    /*
    val rtLib = buildDir.resolve("jrte/jr.jar")
    val extractTask = create("extract-jrte") {
        inputs.property("java.home", System.getProperty("java.home"))
        outputs.file(rtLib)
        doLast {
            JRTExtractor.extract(rtLib)
        }
    }*/
    /*
    create<ProGuardTask>("obfuscate") {
        group = "build"
        dependsOn(shadowJar, extractTask)
        inputs.file(shadowJar.get().outputs.files.singleFile)
        inputs.files(configurations.compileClasspath.get())
        val outFile = buildDir.resolve("libs/${project.name}-${project.version}.jar")
        outputs.files(outFile)

        injars(shadowJar.get().outputs.files.singleFile)
        outjars(outFile)
        dontwarn()
        allowaccessmodification()
        optimizationpasses(5)
        dontobfuscate()
        //lattenpackagehierarchy("de.phyrone.voicechatapp.server")
        //printmapping("obfuscation/mapping.txt")

        //classobfuscationdictionary("obfuscation/dictionary.txt")
        //packageobfuscationdictionary("obfuscation/dictionary.txt")


        /*
        val keepArgsDenyAll = mapOf("allowoptimization" to false, "allowshrinking" to false, "allowobfuscation" to false)
        keep(
            keepArgsDenyAll, """
            class de.phyrone.voicechatapp.server.main.ServerMain {
            	public void main(java.lang.String[]);
            }
        """.trimIndent()
        )*/
        keep(
            mapOf("allowobfuscation" to true), """
             class de.phyrone.voicechatapp.server.main.ServerMain {
               public static void main(java.lang.String[]);
             }
        """.trimIndent()
        )
        keepclassmembernames(
            mapOf("allowobfuscation" to false),
            """
            class de.phyrone.voicechatapp.server.main.ServerMain {
              public static void main(java.lang.String[]);
            }
            """.trimIndent()
        )

        keep("class kotlin.Metadata")

        keepattributes("*Annotation*")



        adaptclassstrings()
        adaptresourcefilecontents()
        adaptresourcefilenames()

        val dictionaryFile = project.rootProject.projectDir.resolve("misc-files/dictionary.txt")
        packageobfuscationdictionary(dictionaryFile)
        classobfuscationdictionary(dictionaryFile)


        doFirst {
            libraryjars(
                configurations.compileClasspath.get().resolvedConfiguration
                    .firstLevelModuleDependencies
                    .withSubDependencies()
                    .jarFiles() + rtLib
                // val files = configurations.compileClasspath.get()
            )
        }
    }*/


}
java.sourceCompatibility = PROJ_SERVER_JAVA_VERSION
java.targetCompatibility = PROJ_SERVER_JAVA_VERSION
kotlin {
    jvmToolchain(PROJ_SERVER_KOTLIN_JVM_TARGET)
}

/*
subprojects {
    apply(plugin = "org.jetbrains.dokka")
    tasks {
        withType<DokkaTask>() {
            dependsOn("kaptKotlin")
        }
        withType<DokkaTaskPartial>() {
            dependsOn("kaptKotlin")
        }
    }

}*/
//application.mainClass.set("de.phyrone.voicechatapp.server.main.ServerMain")