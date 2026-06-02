import {
  beforeEach,
  Capability,
  describe,
  expect,
  test,
  type BlockId,
  type BlockPos,
  type ItemId,
  type TeaKitTestContext,
} from "@teakit/test";

const ROOM_Y = 200;
const ORIGIN: BlockPos = { x: 0, y: ROOM_Y, z: 0 };
const ROOM_MIN: BlockPos = { x: -10, y: ROOM_Y - 1, z: -10 };
const ROOM_MAX: BlockPos = { x: 10, y: ROOM_Y + 7, z: 10 };
const FLOOR_MIN: BlockPos = { x: -10, y: ROOM_Y - 1, z: -10 };
const FLOOR_MAX: BlockPos = { x: 10, y: ROOM_Y - 1, z: 10 };

const TIERS = ["leather", "iron", "gold", "diamond", "netherite"] as const;
const COLOR_TIERS = ["leather", "iron", "gold", "diamond", "netherite"] as const;
const COLORS = ["white", "red", "blue", "black", "pink"] as const;
const BACKPACK_MENUS = [
  ["leather", "Leather Backpack"],
  ["iron", "Iron Backpack"],
  ["gold", "Gold Backpack"],
  ["diamond", "Diamond Backpack"],
  ["netherite", "Netherite Backpack"],
] as const;

type Ctx = TeaKitTestContext;

describe.configure({
  timeout: "12m",
  readiness: ["client-ready", "integrated-server-ready", "player-spawned"],
  capabilities: [
    Capability.RuntimeCapabilities,
    Capability.RuntimeTiming,
    Capability.PlayerInventory,
    Capability.PlayerUseItem,
    Capability.ClientScreen,
    Capability.ClientInput,
    Capability.ClientScreenshot,
    Capability.WorldBlock,
    Capability.WorldFill,
    Capability.WorldSetBlock,
    Capability.WorldRecipes,
  ],
});

beforeEach(async (ctx) => {
  await cleanRoom(ctx);
});

function pos(x: number, z = 0, y = ROOM_Y): BlockPos {
  return { x, y, z };
}

async function cleanRoom({ commands, player, world }: Ctx) {
  await commands.run("/gamemode creative");
  await commands.run("/difficulty peaceful");
  await world.setWeather("clear");
  await world.setTime({ dayTime: 1000 });
  await commands.run("/effect clear @s");
  await commands.run("/clear @s");
  await commands.run("/kill @e[type=minecraft:item,distance=..32]");
  await player.teleport({ x: ORIGIN.x + 0.5, y: ORIGIN.y, z: ORIGIN.z + 0.5 });
  await world.clear(ROOM_MIN, ROOM_MAX);
  await world.fill(FLOOR_MIN, FLOOR_MAX, "minecraft:glass");
  await expect(() => world.block({ x: 0, y: ROOM_Y - 1, z: 0 })).toEventuallyEqual(
    expect.objectContaining({ id: "minecraft:glass" }),
    { timeout: "3s" },
  );
}

async function buildContainerLine(ctx: Ctx) {
  await ctx.world.setBlock(pos(2), { id: "minecraft:chest", properties: { facing: "north" } });
  await ctx.world.setBlock(pos(4), { id: "minecraft:barrel", properties: { facing: "up" } });
  await ctx.world.setBlock(pos(6), { id: "minecraft:trapped_chest", properties: { facing: "north" } });
  await expect(() => ctx.world.block(pos(2))).toEventuallyEqual(expect.objectContaining({ id: "minecraft:chest" }));
  await expect(() => ctx.world.block(pos(4))).toEventuallyEqual(expect.objectContaining({ id: "minecraft:barrel" }));
  await expect(() => ctx.world.block(pos(6))).toEventuallyEqual(expect.objectContaining({ id: "minecraft:trapped_chest" }));
}

async function assertInventoryContains(ctx: Ctx, item: ItemId) {
  await expect(ctx.player.inventory()).toContainItem(item);
}

async function equipMainHand(ctx: Ctx, item: ItemId | string) {
  await ctx.commands.assert(`/item replace entity @s weapon.mainhand with ${item}`);
}

async function useMainHand(ctx: Ctx) {
  await ctx.player.useItem();
}

async function waitForTitle(ctx: Ctx, title: string) {
  await ctx.client.waitForScreen(title, { timeoutMs: 5_000 });
}

async function closeMenu(ctx: Ctx) {
  await ctx.client.closeMenus({ timeoutMs: 3_000 });
}

async function keyTap(ctx: Ctx, key: number) {
  await ctx.client.keyState(key, true);
  await ctx.client.keyState(key, false);
}

async function openInventory(ctx: Ctx) {
  await ctx.client.openInventory();
}

async function assertRecipe(ctx: Ctx, width: number, height: number, items: ItemId[], resultItemId: ItemId) {
  await ctx.recipes.assertCrafting(width, height, items, resultItemId);
}

async function assertSmithing(ctx: Ctx, baseItemId: ItemId, resultItemId: ItemId) {
  await ctx.recipes.assertSmithingTransform(baseItemId, resultItemId, {
    templateItemId: "minecraft:netherite_upgrade_smithing_template",
    additionItemId: "minecraft:netherite_ingot",
  });
}

async function assertCommandOutput(ctx: Ctx, command: string, expectOutputContains: string[]) {
  await ctx.commands.assert(command, {
    expectOutputContains,
  });
}

async function commandOutput(ctx: Ctx, command: string, expectOutputContains: string[]) {
  await ctx.commands.run(command, {
    expectOutputContains,
  });
}

async function screenshot(ctx: Ctx, name: string) {
  await ctx.client.screenshot(name, { timeoutMs: 10_000 });
}

async function waitMs(ctx: Ctx, durationMs: number) {
  await ctx.runtime.wait(durationMs, { timeoutMs: durationMs + 1_000 });
}

test("content exposes core backpack items", async (ctx) => {
  for (const tier of TIERS) {
    await ctx.commands.assert(`/give @s mochila:${tier}_backpack`);
  }
  await ctx.commands.assert("/give @s mochila:ender_backpack");

  for (const tier of TIERS) {
    await assertInventoryContains(ctx, `mochila:${tier}_backpack`);
  }
  await assertInventoryContains(ctx, "mochila:ender_backpack");
});

test("content exposes color backpack items", async ({ commands }) => {
  for (const color of COLORS) {
    for (const tier of COLOR_TIERS) {
      await commands.assert(`/give @s mochila:${color}_${tier}_backpack`);
    }
  }
});

test("content item tags classify backpacks", async (ctx) => {
  await equipMainHand(ctx, "mochila:leather_backpack");
  await ctx.commands.assert("/execute if items entity @s weapon.mainhand #mochila:backpacks");
  await ctx.commands.assert("/execute if items entity @s weapon.mainhand #mochila:leather_backpacks");

  await equipMainHand(ctx, "mochila:iron_backpack");
  await ctx.commands.assert("/execute if items entity @s weapon.mainhand #mochila:backpacks");
  await ctx.commands.assert("/execute if items entity @s weapon.mainhand #mochila:iron_backpacks");

  await equipMainHand(ctx, "mochila:ender_backpack");
  await ctx.commands.assert("/execute if items entity @s weapon.mainhand #mochila:backpacks");
});

test("recipes include base and ender backpack shapes", async (ctx) => {
  await assertRecipe(ctx, 3, 3, [
    "minecraft:leather",
    "minecraft:air",
    "minecraft:leather",
    "minecraft:leather",
    "minecraft:chest",
    "minecraft:leather",
    "minecraft:air",
    "minecraft:leather",
    "minecraft:air",
  ], "mochila:leather_backpack");

  await assertRecipe(ctx, 3, 3, [
    "minecraft:leather",
    "minecraft:iron_ingot",
    "minecraft:leather",
    "minecraft:leather",
    "minecraft:ender_chest",
    "minecraft:string",
    "minecraft:leather",
    "minecraft:white_wool",
    "minecraft:iron_ingot",
  ], "mochila:ender_backpack");
});

test("recipes include coloring transforms", async (ctx) => {
  await assertRecipe(ctx, 2, 1, ["mochila:leather_backpack", "minecraft:red_dye"], "mochila:red_leather_backpack");
  await assertRecipe(ctx, 2, 1, ["mochila:iron_backpack", "minecraft:blue_dye"], "mochila:blue_iron_backpack");
  await assertRecipe(ctx, 2, 1, ["mochila:diamond_backpack", "minecraft:black_dye"], "mochila:black_diamond_backpack");
});

test("recipes include tier upgrades", async (ctx) => {
  await assertRecipe(ctx, 3, 3, [
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "mochila:leather_backpack",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
  ], "mochila:iron_backpack");
  await assertRecipe(ctx, 3, 3, [
    "minecraft:gold_ingot",
    "minecraft:gold_ingot",
    "minecraft:gold_ingot",
    "minecraft:gold_ingot",
    "mochila:iron_backpack",
    "minecraft:gold_ingot",
    "minecraft:gold_ingot",
    "minecraft:gold_ingot",
    "minecraft:gold_ingot",
  ], "mochila:gold_backpack");
  await assertRecipe(ctx, 3, 3, [
    "minecraft:diamond",
    "minecraft:diamond",
    "minecraft:diamond",
    "minecraft:diamond",
    "mochila:gold_backpack",
    "minecraft:diamond",
    "minecraft:diamond",
    "minecraft:diamond",
    "minecraft:diamond",
  ], "mochila:diamond_backpack");
  await assertRecipe(ctx, 3, 3, [
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "mochila:red_leather_backpack",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
    "minecraft:iron_ingot",
  ], "mochila:red_iron_backpack");
});

test("recipes include smithing transforms", async (ctx) => {
  await assertSmithing(ctx, "mochila:diamond_backpack", "mochila:netherite_backpack");
  await assertSmithing(ctx, "mochila:red_diamond_backpack", "mochila:red_netherite_backpack");
});

test("recipes preserve backpack components", async ({ commands }) => {
  await commands.assert("/mochila debug sample mochila:leather_backpack");
  await commands.assert("/mochila debug recipe color minecraft:red_dye");
  await commands.assert("/mochila debug assert-sample mochila:red_leather_backpack");
  await commands.assert("/mochila debug sample mochila:red_leather_backpack");
  await commands.assert("/mochila debug recipe upgrade minecraft:iron_ingot");
  await commands.assert("/mochila debug assert-sample mochila:red_iron_backpack");
  await commands.assert("/mochila debug recipe upgrade minecraft:gold_ingot");
  await commands.assert("/mochila debug assert-sample mochila:red_gold_backpack");
  await commands.assert("/mochila debug recipe upgrade minecraft:diamond");
  await commands.assert("/mochila debug assert-sample mochila:red_diamond_backpack");
  await commands.assert("/mochila debug sample mochila:red_diamond_backpack");
  await commands.assert("/mochila debug recipe smith-netherite");
  await commands.assert("/mochila debug assert-sample mochila:red_netherite_backpack");
  await commands.assert("/mochila debug sample mochila:leather_backpack");
  await commands.assert("/mochila debug recipe assert-invalid-upgrade minecraft:gold_ingot");
  await commands.assert("/mochila debug sample mochila:netherite_backpack");
  await commands.assert("/mochila debug recipe assert-invalid-upgrade minecraft:diamond");
});

test("backpack opens by item use", async (ctx) => {
  await equipMainHand(ctx, "mochila:leather_backpack");
  await useMainHand(ctx);
  await waitForTitle(ctx, "Leather Backpack");
  await screenshot(ctx, "mochila-leather-backpack-menu");
  await closeMenu(ctx);
});

test("backpack tiers open their expected menus", async (ctx) => {
  for (const [tier, title] of BACKPACK_MENUS) {
    await equipMainHand(ctx, `mochila:${tier}_backpack`);
    await useMainHand(ctx);
    await waitForTitle(ctx, title);
    await closeMenu(ctx);
  }
});

test("backpack keybind opens and quickstash mode component persists", async (ctx) => {
  await equipMainHand(ctx, "mochila:leather_backpack");
  await keyTap(ctx, 66);
  await waitForTitle(ctx, "Leather Backpack");
  await closeMenu(ctx);

  await equipMainHand(ctx, "mochila:leather_backpack[mochila:quickstash_mode=1]");
  await ctx.commands.assert("/execute if items entity @s weapon.mainhand mochila:leather_backpack[mochila:quickstash_mode=1]");
});

test("backpack keeps container component contents", async (ctx) => {
  await equipMainHand(
    ctx,
    'mochila:leather_backpack[minecraft:container=[{slot:0,item:{id:"minecraft:cobblestone",count:16}},{slot:1,item:{id:"minecraft:oak_log",count:8}}]]',
  );
  await useMainHand(ctx);
  await waitForTitle(ctx, "Leather Backpack");
  await closeMenu(ctx);
  await assertInventoryContains(ctx, "mochila:leather_backpack");
});

test("ender backpack opens by item use", async (ctx) => {
  await equipMainHand(ctx, "mochila:ender_backpack");
  await useMainHand(ctx);
  await waitForTitle(ctx, "Ender Chest");
  await screenshot(ctx, "mochila-ender-backpack-menu");
  await closeMenu(ctx);
});

test("ender backpack keybind opens menu", async (ctx) => {
  await equipMainHand(ctx, "mochila:ender_backpack");
  await keyTap(ctx, 86);
  await waitForTitle(ctx, "Ender Chest");
  await closeMenu(ctx);
});

test("quickstash dumps backpack contents into a chest", async (ctx) => {
  await buildContainerLine(ctx);
  await equipMainHand(
    ctx,
    'mochila:leather_backpack[minecraft:container=[{slot:0,item:{id:"minecraft:cobblestone",count:16}},{slot:1,item:{id:"minecraft:oak_log",count:8}}]]',
  );
  await assertCommandOutput(ctx, "/mochila debug quickstash 2 200 0 north", ["Stored 24 items in Chest"]);
  await waitMs(ctx, 250);
  await commandOutput(ctx, "/data get block 2 200 0 Items", ["minecraft:cobblestone", "minecraft:oak_log"]);
});

test("quickstash dumps backpack contents into a barrel", async (ctx) => {
  await buildContainerLine(ctx);
  await equipMainHand(ctx, 'mochila:iron_backpack[minecraft:container=[{slot:0,item:{id:"minecraft:dirt",count:32}}]]');
  await assertCommandOutput(ctx, "/mochila debug quickstash 4 200 0 up", ["Stored 32 items in Barrel"]);
  await waitMs(ctx, 250);
  await commandOutput(ctx, "/data get block 4 200 0 Items", ["minecraft:dirt"]);
});

test("quickstash store mode only stores matching items", async (ctx) => {
  await buildContainerLine(ctx);
  await ctx.commands.assert("/item replace block 2 200 0 container.0 with minecraft:cobblestone 1");
  await equipMainHand(
    ctx,
    'mochila:leather_backpack[mochila:quickstash_mode=1,minecraft:container=[{slot:0,item:{id:"minecraft:cobblestone",count:16}},{slot:1,item:{id:"minecraft:oak_log",count:8}}]]',
  );
  await assertCommandOutput(ctx, "/mochila debug quickstash 2 200 0 north", ["Stored 16 matching items in Chest"]);
  await waitMs(ctx, 250);
  await commandOutput(ctx, "/data get block 2 200 0 Items", ["minecraft:cobblestone"]);
});

test("visual item lineup renders in inventory", async (ctx) => {
  const lineup: ItemId[] = [
    "mochila:leather_backpack",
    "mochila:iron_backpack",
    "mochila:gold_backpack",
    "mochila:diamond_backpack",
    "mochila:netherite_backpack",
    "mochila:red_leather_backpack",
    "mochila:blue_diamond_backpack",
    "mochila:ender_backpack",
  ];
  for (const [slot, item] of lineup.entries()) {
    await ctx.commands.run(`/item replace entity @s hotbar.${slot} with ${item}`);
  }

  await openInventory(ctx);
  await ctx.client.waitForScreen("net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen", {
    timeoutMs: 5_000,
  });
  await screenshot(ctx, "mochila-item-lineup-inventory");
  await closeMenu(ctx);
});

test("visual backpack menus render", async (ctx) => {
  await equipMainHand(ctx, "mochila:netherite_backpack");
  await useMainHand(ctx);
  await waitForTitle(ctx, "Netherite Backpack");
  await screenshot(ctx, "mochila-netherite-backpack-menu");
  await closeMenu(ctx);

  await equipMainHand(ctx, "mochila:ender_backpack");
  await useMainHand(ctx);
  await waitForTitle(ctx, "Ender Chest");
  await screenshot(ctx, "mochila-ender-backpack-menu");
  await closeMenu(ctx);
});
