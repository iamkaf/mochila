import org.gradle.api.GradleException
import org.gradle.api.artifacts.VersionCatalogsExtension

// The JEI plugin lives in common and compiles into every loader against JEI's loader-neutral API.
// A Minecraft line without a JEI release pins "null" in its catalog, and has no plugin sources.
val catalog = extensions.getByType<VersionCatalogsExtension>()
    .named("libsMc${project.name.replace(".", "").replace("-", "")}")

if (catalog.findVersion("jei-api").map { it.requiredVersion != "null" }.orElse(false)) {
    val api = catalog.findLibrary("jei-common-api")
        .orElseThrow { GradleException("Missing library alias: jei-common-api") }
    // Obfuscated Fabric lines remap mod dependencies.
    val configuration = if (configurations.names.contains("modCompileOnly")) "modCompileOnly" else "compileOnly"
    dependencies.add(configuration, api)
}
