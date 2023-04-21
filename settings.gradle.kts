rootProject.name = "voicechatapp"

include(
    ":common",
    ":common:const",
    ":common:protocol",

    // Client
    ":client",

    // Server
    ":server",
    ":server:api",
    ":server:launcher",
    ":server:core",


    )