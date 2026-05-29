# Mochila TeaKit Scenarios

TeaKit scenarios are intentionally hierarchical. Leaf scenarios test one behavior; group scenarios run leaves with `run_scenario`; `all.json` is the full suite.

## Hierarchy

- `smoke.json` runs the cheapest checks: registry, core recipes, one backpack menu, one ender backpack menu.
- `all.json` runs every group and is the suite to use before release.
- `fixtures/` scenarios create repeatable safe world state above terrain.
- `content/` verifies all registered item ids, core tags, and generated data visibility.
- `recipes/` verifies shaped recipes, custom coloring/upgrading recipes, and smithing transforms.
- `backpack/` verifies backpack opening, tier menu sizes, keybind behavior, and container-component persistence smoke.
- `ender/` verifies the ender backpack menu and keybind path.
- `quickstash/` sets up controlled containers and exercises the interaction path. Exact moved-item assertions need a Mochila debug command or a TeaKit crouch-aware block-use action because client key-state does not reliably set server crouching for `useOn`.
- `visual/` captures representative UI/item screenshots for resource and layout regressions.

Some state-heavy checks use vanilla commands. If those become too brittle across Minecraft versions, the next step is to add Mochila debug commands for exact backpack component, quickstash, and container assertions.
