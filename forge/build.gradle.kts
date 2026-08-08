plugins {
    id("com.iamkaf.multiloader.forge")
}

val minecraftVersion = project.name
val useTeaKit = providers.systemProperty("mochila.withTeaKit")
    .orElse(providers.gradleProperty("mochila.withTeaKit"))
    .map { it.toBoolean() }
    .orElse(false)
    .get()

dependencies {
    if (useTeaKit) {
        runtimeOnly("com.iamkaf.teakit:teakit-forge:0.13.2+$minecraftVersion")
    }
}
