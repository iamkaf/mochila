import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    id("com.iamkaf.multiloader.neoforge")
}

val minecraftVersion = project.name
val hasRuntimeJei = minecraftVersion !in setOf("26.1", "26.1.1", "26.2")
val catalog = mcCatalog(minecraftVersion)

fun mcCatalog(minecraftVersion: String): VersionCatalog {
    val catalogs = extensions.getByType<VersionCatalogsExtension>()
    val name = "libsMc${minecraftVersion.replace(".", "").replace("-", "")}"
    return catalogs.named(name)
}

fun VersionCatalog.requiredDependency(alias: String) =
    findLibrary(alias).orElseThrow { GradleException("Missing library alias: $alias") }

dependencies {
    compileOnly(catalog.requiredDependency("jei-common-api"))
    compileOnly(catalog.requiredDependency("jei-neoforge-api"))
    if (hasRuntimeJei) {
        runtimeOnly(catalog.requiredDependency("jei-neoforge"))
    }
}
