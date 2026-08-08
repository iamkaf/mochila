import com.iamkaf.multiloader.fabric.MultiloaderFabricExtension
import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.iamkaf.multiloader.fabric")
}

val minecraftVersion = project.name
val isModernLine = !minecraftVersion.startsWith("1.")
val hasRuntimeJei = !minecraftVersion.startsWith("26.2")
val catalog = mcCatalog(minecraftVersion)
val useTeaKit = providers.systemProperty("mochila.withTeaKit")
    .orElse(providers.gradleProperty("mochila.withTeaKit"))
    .map { it.toBoolean() }
    .orElse(false)
    .get()

fun mcCatalog(minecraftVersion: String): VersionCatalog {
    val catalogs = extensions.getByType<VersionCatalogsExtension>()
    val name = "libsMc${minecraftVersion.replace(".", "").replace("-", "")}"
    return catalogs.named(name)
}

fun VersionCatalog.requiredDependency(alias: String) =
    findLibrary(alias).orElseThrow { GradleException("Missing library alias: $alias") }

extensions.configure<MultiloaderFabricExtension>("multiloaderFabric") {
    commonDatagen.set(true)
}

dependencies {
    if (isModernLine) {
        compileOnly(catalog.requiredDependency("jei-common-api"))
        compileOnly(catalog.requiredDependency("jei-fabric-api"))
        if (hasRuntimeJei) {
            runtimeOnly(catalog.requiredDependency("jei-fabric"))
        }
        if (useTeaKit) {
            runtimeOnly("com.iamkaf.teakit:teakit-fabric:0.13.2+$minecraftVersion")
        }
    } else {
        "modCompileOnly"(catalog.requiredDependency("jei-common-api"))
        "modCompileOnly"(catalog.requiredDependency("jei-fabric-api"))
        if (useTeaKit) {
            "modLocalRuntime"("com.iamkaf.teakit:teakit-fabric:0.13.2+$minecraftVersion")
        }
    }
}
