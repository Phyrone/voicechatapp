plugins{
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}
dependencies{
    implementation("com.squareup:kotlinpoet:1.13.0")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
}

kotlin{
    jvmToolchain(11)
}