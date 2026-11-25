import com.iamkaf.multiloader.support.MultiloaderProjectContext

plugins {
    id("com.iamkaf.multiloader.forge")
}

apply(from = rootProject.file("gradle/jei.gradle.kts"))

val multiloader = MultiloaderProjectContext.of(project)

dependencies {
    // JEI at dev runtime on 1.21.1, so its recipe categories can be checked there.
    if (multiloader.minecraftVersion() == "1.21.1") {
        runtimeOnly(multiloader.catalogFor().findLibrary("jei-forge").get())
    }
}
