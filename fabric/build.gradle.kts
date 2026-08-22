import com.iamkaf.multiloader.fabric.MultiloaderFabricExtension
import com.iamkaf.multiloader.support.MultiloaderProjectContext
import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.iamkaf.multiloader.fabric")
}

val minecraftVersion = project.name
val multiloader = MultiloaderProjectContext.of(project)
val isModernLine = !minecraftVersion.startsWith("1.")
val hasRuntimeJei = !minecraftVersion.startsWith("26.2")
val catalog = mcCatalog(minecraftVersion)
val useTeaKit = providers.systemProperty("mochila.withTeaKit")
    .orElse(providers.gradleProperty("mochila.withTeaKit"))
    .map { it.toBoolean() }
    .orElse(false)
    .get()
val useTrinkets = providers.systemProperty("mochila.withTrinkets")
    .orElse(providers.gradleProperty("mochila.withTrinkets"))
    .map { it.toBoolean() }
    .orElse(true)
    .get()
val trinketsVersion = multiloader.requiredProperty("dependencies.trinkets")

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

repositories {
    maven("https://api.modrinth.com/maven") { name = "Modrinth" }
    maven("https://maven.ladysnake.org/releases") { name = "Ladysnake" }
}

dependencies {
    if (isModernLine) {
        compileOnly("maven.modrinth:trinkets-updated:$trinketsVersion")
        if (useTrinkets) {
            runtimeOnly("maven.modrinth:trinkets-updated:$trinketsVersion")
        }
        compileOnly(catalog.requiredDependency("jei-common-api"))
        compileOnly(catalog.requiredDependency("jei-fabric-api"))
        if (hasRuntimeJei) {
            runtimeOnly(catalog.requiredDependency("jei-fabric"))
        }
        if (useTeaKit) {
            runtimeOnly("com.iamkaf.teakit:teakit-fabric:0.13.2+$minecraftVersion")
        }
    } else {
        "modCompileOnly"("maven.modrinth:trinkets-updated:$trinketsVersion")
        "modCompileOnly"("org.ladysnake.cardinal-components-api:cardinal-components-base:7.3.0")
        if (useTrinkets) {
            "modLocalRuntime"("maven.modrinth:trinkets-updated:$trinketsVersion")
            "modLocalRuntime"("org.ladysnake.cardinal-components-api:cardinal-components-base:7.3.0")
            "modLocalRuntime"("org.ladysnake.cardinal-components-api:cardinal-components-entity:7.3.0")
        }
        "modCompileOnly"(catalog.requiredDependency("jei-common-api"))
        "modCompileOnly"(catalog.requiredDependency("jei-fabric-api"))
        if (useTeaKit) {
            "modLocalRuntime"("com.iamkaf.teakit:teakit-fabric:0.13.2+$minecraftVersion")
        }
    }
}
