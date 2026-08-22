pluginManagement {
    repositories {
        mavenLocal()
        maven("https://maven.kaf.sh") { name = "Kaf Maven" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("com.iamkaf.multiloader.settings") version providers.gradleProperty("project.plugins").get()
}

dependencyResolutionManagement {
    versionCatalogs.named("libsMc262") {
        version("neoforge", "26.2.0.26-beta")
    }
}
