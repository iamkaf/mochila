import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.iamkaf.multiloader.neoforge")
}

val minecraftVersion = project.name
// The catalogs pin a NeoForge older than the one JEI requires on 1.21.4 through 1.21.8, so JEI cannot load in dev there.
val hasRuntimeJei = minecraftVersion !in setOf("26.1", "26.1.1", "26.2", "26.3", "1.21.4", "1.21.5", "1.21.6", "1.21.7", "1.21.8")
val catalog = mcCatalog(minecraftVersion)

fun mcCatalog(minecraftVersion: String): VersionCatalog {
    val catalogs = extensions.getByType<VersionCatalogsExtension>()
    val name = "libsMc${minecraftVersion.replace(".", "").replace("-", "")}"
    return catalogs.named(name)
}

fun VersionCatalog.requiredDependency(alias: String) =
    findLibrary(alias).orElseThrow { GradleException("Missing library alias: $alias") }

apply(from = rootProject.file("gradle/jei.gradle.kts"))

dependencies {
    if (hasRuntimeJei) {
        runtimeOnly(catalog.requiredDependency("jei-neoforge"))
    }
}
