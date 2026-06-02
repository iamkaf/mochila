plugins {
    id("dev.kikugie.stonecutter")
    id("com.iamkaf.multiloader.root")
    id("fabric-loom") version "1.15.5" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
}

stonecutter active "26.1.2"

stonecutter handlers {
    inherit("json5", "json")
}

stonecutter parameters {
    replacements.string(eval(current.version, ">=1.21.11")) {
        replace("ResourceLocation", "Identifier")
    }
}

tasks.register<Exec>("teakitRunScenario") {
    group = "verification"
    description = "Runs a TeaKit scenario through the checked-in teakitw runner."

    doFirst {
        val node = providers.gradleProperty("teakit.node").orNull
            ?: throw GradleException("Missing required property: -Pteakit.node=<version-loader>")
        val scenario = providers.gradleProperty("teakit.scenario").orNull
            ?: throw GradleException("Missing required property: -Pteakit.scenario=<path>")

        val args = mutableListOf(
            "./teakitw",
            "run",
            "--node", node,
            "--scenario", scenario,
            "--readiness", providers.gradleProperty("teakit.readiness").orElse("world").get(),
            "--timeout", providers.gradleProperty("teakit.timeout").orElse("180").get(),
        )

        providers.gradleProperty("teakit.outputDir").orNull?.let {
            args.addAll(listOf("--output-dir", it))
        }
        providers.gradleProperty("teakit.gradle").orNull?.let {
            args.addAll(listOf("--gradle", it))
        }
        providers.gradleProperty("teakit.projectKey").orNull?.let {
            args.addAll(listOf("--project-key", it))
        }

        commandLine(args)
    }
}
