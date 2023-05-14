rootProject.name = "voicechatapp"

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(
    ":common",
    ":common:const",
    ":common:protocol",

    // Client
    ":client",
    "client-compose",
    ":client-compose:android",
    ":client-compose:desktop",
    ":client-compose:web",
    ":client-compose:ui",

    // Server
    ":server",
    ":server:api",
    ":server:launcher",
    ":server:core",


    )

