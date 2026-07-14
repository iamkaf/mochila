# Mochila

A colorful, upgradable backpack mod for Minecraft.

![License](https://img.shields.io/badge/license-MIT-blue.svg)

## About

Mochila adds backpack items with multiple storage tiers, dyeable variants, an ender backpack, quick stash actions, and recipe viewer support.

The project is built as a Stonecutter multi-loader mod. Shared code lives in `common/`, loader integrations live in `fabric/`, `forge/`, and `neoforge/`, and Minecraft-version settings live under `versions/`.

## Features

- Backpack tiers: leather, iron, gold, diamond, and netherite.
- Dyeable backpacks using all 16 vanilla dye colors.
- Ender backpack with separate keybind support.
- Backpack opening by right click or keybind.
- Quick stash modes for moving backpack contents into target containers.
- Configurable quick stash target blocks.
- In-game configuration screens through Konfig where supported.
- JEI recipe views for backpack coloring and crafting upgrades on Fabric and NeoForge.
- TeaKit tests for repeatable smoke, recipe, content, visual, and interaction checks.

## Requirements

- [Amber](https://modrinth.com/mod/amber)
- [Konfig](https://modrinth.com/mod/konfig)

## Supported Versions

| Minecraft | Fabric | Forge | NeoForge |
| --- | --- | --- | --- |
| 26.1.2 | Supported | Supported | Supported |

JEI integration is currently enabled for Fabric and NeoForge.

## Project Layout

```text
mochila/
├── common/             # Shared mod code and generated resources
├── fabric/             # Fabric-specific entrypoints and compat
├── forge/              # Forge-specific entrypoints
├── neoforge/           # NeoForge-specific entrypoints and compat
├── test/teakit/        # TeaKit test suite
├── versions/26.1.2/    # Stonecutter version properties
├── justfile            # Common build/test commands
└── teakitw             # Direct TeaKit Runner fallback
```

## Building

Prerequisites:

- Java 25 for the current `26.1.2` target.
- `just`

Useful commands:

```bash
# List available version-loader nodes
just list-nodes

# Build one node
just build 26.1.2-fabric
just build 26.1.2-forge
just build 26.1.2-neoforge

# Build all nodes
just build-all

# Compile all source sets
just compile-all

# Run a client for one node
just run-client 26.1.2-fabric

# Run the TypeScript TeaKit suite through Gradle
./gradlew teakitCheck -Pteakit.node=26.1.2-neoforge -Pteakit.testFile=test/teakit/mochila.test.ts -Pteakit.timeout=240

# Use the direct Runner wrapper when debugging outside Gradle
./teakitw run --node 26.1.2-neoforge --test-file test/teakit/mochila.test.ts --timeout 240
```

Built jars are written under each loader/version build directory, for example `fabric/versions/26.1.2/build/libs/`.

## Testing

The TeaKit tests under `test/teakit/` cover:

- basic launch smoke checks
- item and tag availability
- backpack opening and keybind behavior
- backpack tier sizes and component persistence
- quick stash behavior
- crafting, coloring, smithing, and upgrade preservation
- visual fixture checks

Run the suite with `just teakit-check <node> [test_file]`, or use Gradle tasks such as `:fabric:26.1.2:build`, `:forge:26.1.2:build`, and `:neoforge:26.1.2:build` for loader builds.

## Links

- CurseForge: https://www.curseforge.com/minecraft/mc-mods/mochila
- Modrinth: https://modrinth.com/mod/mochila
- Source: https://github.com/iamkaf/mochila

## License

Mochila is licensed under the MIT License. See [LICENSE](LICENSE).
