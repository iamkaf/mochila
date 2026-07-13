# Mochila TeaKit Scenarios

TeaKit scenarios live in `mochila.scenario.ts`. The file registers one TypeScript test per behavioral leaf and keeps reusable setup, cleanup, and recipe helpers in the same module.

## Coverage

- Content tests verify registered item ids, core tags, and generated data visibility.
- Recipe tests verify shaped recipes, custom coloring/upgrading recipes, smithing transforms, and preservation behavior through Mochila debug commands.
- Backpack tests verify backpack opening, tier menu titles, keybind behavior, and container-component persistence smoke.
- Ender tests verify the ender backpack menu and keybind path.
- Quickstash tests set up controlled containers and exercise the debug quickstash interaction path. Exact moved-item assertions still depend on Mochila debug commands or a TeaKit crouch-aware block-use action because client key-state does not reliably set server crouching for `useOn`.
- Visual tests capture representative UI/item screenshots for resource and layout regressions.

Fixtures build explicit safe world state at Y=200:

- `cleanRoom` resets the player, weather, time, nearby dropped items, and a glass platform above terrain.
- `containerLine` extends `cleanRoom` with a chest, barrel, and trapped chest.

Run the suite with the current TypeScript runner:

```sh
./gradlew teakitCheck -Pteakit.node=26.1.2-fabric -Pteakit.scenario=test/scenarios/mochila/mochila.scenario.ts -Pteakit.timeout=240
```

The checked-in `teakitw` wrapper remains available as a direct Runner fallback.

Some state-heavy checks use vanilla commands. If those become too brittle across Minecraft versions, add Mochila debug commands for exact backpack component, quickstash, and container assertions.
