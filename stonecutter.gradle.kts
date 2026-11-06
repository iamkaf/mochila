plugins {
    id("dev.kikugie.stonecutter")
    id("fabric-loom") apply false
    id("net.fabricmc.fabric-loom") apply false
    id("com.iamkaf.multiloader.root")
    id("com.iamkaf.teakit") version "0.19.0"
}

teakit {
    runnerVersion.set("0.18.0")
    testDirectories.add("test/teakit")
    timeoutSeconds.set(720)
    failOnRuntimeIncomplete.set(true)
    background.set(true)
}

stonecutter active "26.1.2".let { multiloaderStonecutter.active(it) }

stonecutter parameters {
    // 1.21.11 renamed ResourceLocation to Identifier.
    replacements.string(eval(current.version, ">=1.21.11")) {
        replace("ResourceLocation", "Identifier")
    }
    // 26.1 renamed ClickType to ContainerInput.
    replacements.string(eval(current.version, ">=26.1")) {
        replace("ClickType", "ContainerInput")
    }
}
