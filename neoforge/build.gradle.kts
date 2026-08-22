import com.iamkaf.multiloader.support.MultiloaderProjectContext
import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.iamkaf.multiloader.neoforge")
}

val minecraftVersion = project.name
val isModernLine = !minecraftVersion.startsWith("1.")
val multiloader = MultiloaderProjectContext.of(project)
val hasRuntimeJei = minecraftVersion !in setOf("26.1", "26.1.1", "26.2")
val catalog = mcCatalog(minecraftVersion)
val useTeaKit = providers.systemProperty("mochila.withTeaKit")
    .orElse(providers.gradleProperty("mochila.withTeaKit"))
    .map { it.toBoolean() }
    .orElse(false)
    .get()
val useCurios = providers.systemProperty("mochila.withCurios")
    .orElse(providers.gradleProperty("mochila.withCurios"))
    .map { it.toBoolean() }
    .orElse(true)
    .get()
val curiosVersion = multiloader.requiredProperty("dependencies.curios")
val useTrinkets = providers.systemProperty("mochila.withTrinkets")
    .orElse(providers.gradleProperty("mochila.withTrinkets"))
    .map { it.toBoolean() }
    .orElse(true)
    .get()
val trinketsVersion = multiloader.requiredProperty("dependencies.trinkets-neoforge")

fun mcCatalog(minecraftVersion: String): VersionCatalog {
    val catalogs = extensions.getByType<VersionCatalogsExtension>()
    val name = "libsMc${minecraftVersion.replace(".", "").replace("-", "")}"
    return catalogs.named(name)
}

fun VersionCatalog.requiredDependency(alias: String) =
    findLibrary(alias).orElseThrow { GradleException("Missing library alias: $alias") }

repositories {
    maven("https://maven.theillusivec4.top/") { name = "Curios" }
    maven("https://api.modrinth.com/maven") { name = "Modrinth" }
}

dependencies {
    compileOnly(catalog.requiredDependency("jei-common-api"))
    compileOnly(catalog.requiredDependency("jei-neoforge-api"))
    if (hasRuntimeJei) {
        runtimeOnly(catalog.requiredDependency("jei-neoforge"))
    }
    if (useTeaKit) {
        runtimeOnly("com.iamkaf.teakit:teakit-neoforge:0.13.2+$minecraftVersion")
    }
    compileOnly("top.theillusivec4.curios:curios-neoforge:$curiosVersion:api")
    if (useCurios) {
        runtimeOnly("top.theillusivec4.curios:curios-neoforge:$curiosVersion")
    }
    if (isModernLine) {
        compileOnly("maven.modrinth:trinkets-updated:$trinketsVersion")
        if (useTrinkets) {
            runtimeOnly("maven.modrinth:trinkets-updated:$trinketsVersion")
        }
    }
}
