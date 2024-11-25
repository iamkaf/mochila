package com.iamkaf.mochila.item;

import com.iamkaf.amber.api.sound.SoundHelper;
import com.iamkaf.mochila.Mochila;
import com.iamkaf.mochila.registry.Items;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class BackpackItem extends Item {
    public static final Holder<SoundEvent> BACKPACK_EQUIP_SOUND = SoundEvents.ARMOR_EQUIP_LEATHER;
    public static final List<Block> CONTAINER_WHITELIST =
            List.of(Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.BARREL);

    static {
        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            Level level = player.level();

            if (!level.isClientSide && player.getItemInHand(hand)
                    .getItem() instanceof BackpackItem backpackItem && player.isShiftKeyDown() && entity instanceof ItemFrame frame) {
                Direction direction = frame.getDirection().getOpposite();
                BlockPos blockPosBehindFrame = frame.getPos().relative(direction);
                BlockState blockState = level.getBlockState(blockPosBehindFrame);

                boolean inserted = backpackItem.unloadIntoContainer(blockState,
                        player,
                        level,
                        blockPosBehindFrame,
                        hand,
                        direction.getOpposite()
                );

                if (inserted) {
                    return EventResult.interruptTrue();
                }
            }

            return EventResult.pass();
        });
    }

    public final BackpackContainer.BackpackSize size;

    public BackpackItem(BackpackContainer.BackpackSize size, Properties properties) {
        super(properties);
        this.size = size;
    }

    public static void onBackpackKeybindPressed(ServerPlayer player) {
        // TODO: test if this works for backpacks in the armor slots and backpacks in a
        //  trinket/curio/accessory slot.
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() instanceof BackpackItem backpack) {
                backpack.openForPlayer(player, stack);
                return;
            }
        }
    }

    /**
     * Gets the equivalent backpack.
     *
     * @param backpack The backpack.
     * @param color    The desired color.
     * @return The backpack of the same tier and desired color.
     */
    public static Item getBackpackByColor(ItemStack backpack, DyeColor color) {
        Tier tier = determineTier(backpack);

        return getBackpackByTierAndColor(tier, color);
    }

    public static @Nullable Tier getNextTier(Tier tier) {
        return switch (tier) {
            case LEATHER -> Tier.IRON;
            case IRON -> Tier.GOLD;
            case GOLD -> Tier.DIAMOND;
            case DIAMOND -> Tier.NETHERITE;
            case NETHERITE -> null;
        };
    }

    public static Item getBackpackByTierAndColor(Tier tier, @Nullable DyeColor color) {
        return switch (tier) {
            case LEATHER -> switch (color) {
                case WHITE -> Items.WHITE_LEATHER_BACKPACK.get();
                case LIGHT_GRAY -> Items.LIGHT_GRAY_LEATHER_BACKPACK.get();
                case GRAY -> Items.GRAY_LEATHER_BACKPACK.get();
                case BLACK -> Items.BLACK_LEATHER_BACKPACK.get();
                case BROWN -> Items.BROWN_LEATHER_BACKPACK.get();
                case RED -> Items.RED_LEATHER_BACKPACK.get();
                case YELLOW -> Items.YELLOW_LEATHER_BACKPACK.get();
                case ORANGE -> Items.ORANGE_LEATHER_BACKPACK.get();
                case LIME -> Items.LIME_LEATHER_BACKPACK.get();
                case GREEN -> Items.GREEN_LEATHER_BACKPACK.get();
                case CYAN -> Items.CYAN_LEATHER_BACKPACK.get();
                case LIGHT_BLUE -> Items.LIGHT_BLUE_LEATHER_BACKPACK.get();
                case BLUE -> Items.BLUE_LEATHER_BACKPACK.get();
                case PURPLE -> Items.PURPLE_LEATHER_BACKPACK.get();
                case MAGENTA -> Items.MAGENTA_LEATHER_BACKPACK.get();
                case PINK -> Items.PINK_LEATHER_BACKPACK.get();
                case null -> Items.LEATHER_BACKPACK.get();
            };
            case IRON -> switch (color) {
                case WHITE -> Items.WHITE_IRON_BACKPACK.get();
                case LIGHT_GRAY -> Items.LIGHT_GRAY_IRON_BACKPACK.get();
                case GRAY -> Items.GRAY_IRON_BACKPACK.get();
                case BLACK -> Items.BLACK_IRON_BACKPACK.get();
                case BROWN -> Items.BROWN_IRON_BACKPACK.get();
                case RED -> Items.RED_IRON_BACKPACK.get();
                case YELLOW -> Items.YELLOW_IRON_BACKPACK.get();
                case ORANGE -> Items.ORANGE_IRON_BACKPACK.get();
                case LIME -> Items.LIME_IRON_BACKPACK.get();
                case GREEN -> Items.GREEN_IRON_BACKPACK.get();
                case CYAN -> Items.CYAN_IRON_BACKPACK.get();
                case LIGHT_BLUE -> Items.LIGHT_BLUE_IRON_BACKPACK.get();
                case BLUE -> Items.BLUE_IRON_BACKPACK.get();
                case PURPLE -> Items.PURPLE_IRON_BACKPACK.get();
                case MAGENTA -> Items.MAGENTA_IRON_BACKPACK.get();
                case PINK -> Items.PINK_IRON_BACKPACK.get();
                case null -> Items.IRON_BACKPACK.get();
            };
            case GOLD -> switch (color) {
                case WHITE -> Items.WHITE_GOLD_BACKPACK.get();
                case LIGHT_GRAY -> Items.LIGHT_GRAY_GOLD_BACKPACK.get();
                case GRAY -> Items.GRAY_GOLD_BACKPACK.get();
                case BLACK -> Items.BLACK_GOLD_BACKPACK.get();
                case BROWN -> Items.BROWN_GOLD_BACKPACK.get();
                case RED -> Items.RED_GOLD_BACKPACK.get();
                case YELLOW -> Items.YELLOW_GOLD_BACKPACK.get();
                case ORANGE -> Items.ORANGE_GOLD_BACKPACK.get();
                case LIME -> Items.LIME_GOLD_BACKPACK.get();
                case GREEN -> Items.GREEN_GOLD_BACKPACK.get();
                case CYAN -> Items.CYAN_GOLD_BACKPACK.get();
                case LIGHT_BLUE -> Items.LIGHT_BLUE_GOLD_BACKPACK.get();
                case BLUE -> Items.BLUE_GOLD_BACKPACK.get();
                case PURPLE -> Items.PURPLE_GOLD_BACKPACK.get();
                case MAGENTA -> Items.MAGENTA_GOLD_BACKPACK.get();
                case PINK -> Items.PINK_GOLD_BACKPACK.get();
                case null -> Items.GOLD_BACKPACK.get();
            };
            case DIAMOND -> switch (color) {
                case WHITE -> Items.WHITE_DIAMOND_BACKPACK.get();
                case LIGHT_GRAY -> Items.LIGHT_GRAY_DIAMOND_BACKPACK.get();
                case GRAY -> Items.GRAY_DIAMOND_BACKPACK.get();
                case BLACK -> Items.BLACK_DIAMOND_BACKPACK.get();
                case BROWN -> Items.BROWN_DIAMOND_BACKPACK.get();
                case RED -> Items.RED_DIAMOND_BACKPACK.get();
                case YELLOW -> Items.YELLOW_DIAMOND_BACKPACK.get();
                case ORANGE -> Items.ORANGE_DIAMOND_BACKPACK.get();
                case LIME -> Items.LIME_DIAMOND_BACKPACK.get();
                case GREEN -> Items.GREEN_DIAMOND_BACKPACK.get();
                case CYAN -> Items.CYAN_DIAMOND_BACKPACK.get();
                case LIGHT_BLUE -> Items.LIGHT_BLUE_DIAMOND_BACKPACK.get();
                case BLUE -> Items.BLUE_DIAMOND_BACKPACK.get();
                case PURPLE -> Items.PURPLE_DIAMOND_BACKPACK.get();
                case MAGENTA -> Items.MAGENTA_DIAMOND_BACKPACK.get();
                case PINK -> Items.PINK_DIAMOND_BACKPACK.get();
                case null -> Items.DIAMOND_BACKPACK.get();
            };
            case NETHERITE -> switch (color) {
                case WHITE -> Items.WHITE_NETHERITE_BACKPACK.get();
                case LIGHT_GRAY -> Items.LIGHT_GRAY_NETHERITE_BACKPACK.get();
                case GRAY -> Items.GRAY_NETHERITE_BACKPACK.get();
                case BLACK -> Items.BLACK_NETHERITE_BACKPACK.get();
                case BROWN -> Items.BROWN_NETHERITE_BACKPACK.get();
                case RED -> Items.RED_NETHERITE_BACKPACK.get();
                case YELLOW -> Items.YELLOW_NETHERITE_BACKPACK.get();
                case ORANGE -> Items.ORANGE_NETHERITE_BACKPACK.get();
                case LIME -> Items.LIME_NETHERITE_BACKPACK.get();
                case GREEN -> Items.GREEN_NETHERITE_BACKPACK.get();
                case CYAN -> Items.CYAN_NETHERITE_BACKPACK.get();
                case LIGHT_BLUE -> Items.LIGHT_BLUE_NETHERITE_BACKPACK.get();
                case BLUE -> Items.BLUE_NETHERITE_BACKPACK.get();
                case PURPLE -> Items.PURPLE_NETHERITE_BACKPACK.get();
                case MAGENTA -> Items.MAGENTA_NETHERITE_BACKPACK.get();
                case PINK -> Items.PINK_NETHERITE_BACKPACK.get();
                case null -> Items.NETHERITE_BACKPACK.get();
            };
        };
    }

    public static Tier determineTier(ItemStack backpack) {
        String path = Objects.requireNonNull(backpack.getItem().arch$registryName()).getPath();
        if (path.contains("leather")) {
            return Tier.LEATHER;
        }
        if (path.contains("iron")) {
            return Tier.IRON;
        }
        if (path.contains("gold")) {
            return Tier.GOLD;
        }
        if (path.contains("diamond")) {
            return Tier.DIAMOND;
        }
        if (path.contains("netherite")) {
            return Tier.NETHERITE;
        }
        throw new IllegalStateException("Invalid backpack type");
    }

    public static @Nullable DyeColor determineDyeColor(ItemStack backpack) {
        String path = Objects.requireNonNull(backpack.getItem().arch$registryName()).getPath();
        if (path.contains("white")) {
            return DyeColor.WHITE;
        }
        if (path.contains("light_gray")) {
            return DyeColor.LIGHT_GRAY;
        }
        if (path.startsWith("gray")) {
            return DyeColor.GRAY;
        }
        if (path.contains("black")) {
            return DyeColor.BLACK;
        }
        if (path.contains("brown")) {
            return DyeColor.BROWN;
        }
        if (path.contains("red")) {
            return DyeColor.RED;
        }
        if (path.contains("yellow")) {
            return DyeColor.YELLOW;
        }
        if (path.contains("orange")) {
            return DyeColor.ORANGE;
        }
        if (path.contains("lime")) {
            return DyeColor.LIME;
        }
        if (path.contains("green")) {
            return DyeColor.GREEN;
        }
        if (path.contains("cyan")) {
            return DyeColor.CYAN;
        }
        if (path.contains("light_blue")) {
            return DyeColor.LIGHT_BLUE;
        }
        if (path.startsWith("blue")) {
            return DyeColor.BLUE;
        }
        if (path.contains("purple")) {
            return DyeColor.PURPLE;
        }
        if (path.contains("magenta")) {
            return DyeColor.MAGENTA;
        }
        if (path.contains("pink")) {
            return DyeColor.PINK;
        }
        // undyed backpack
        return null;
    }

    private static void sendParticles(Direction direction, Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos particlesPosition = pos.relative(direction);
            serverLevel.sendParticles(ParticleTypes.CLOUD,
                    particlesPosition.getX() + 0.5d,
                    particlesPosition.getY() + 0.5d,
                    particlesPosition.getZ() + 0.5d,
                    8,
                    0.01d,
                    -0.02d,
                    0.01d,
                    0.01d
            );
        }
    }

    public void openForPlayer(Player player, ItemStack stack) {
        var container = new BackpackContainer(stack, size);
        SoundHelper.sendClientSound(player, BACKPACK_EQUIP_SOUND.value());
        player.openMenu(new SimpleMenuProvider((i, inventory, playerx) -> new ChestMenu(container.getMenuType(),
                i,
                inventory,
                container,
                container.rows()
        ) {
            @Override
            public void clicked(int slotId, int button, ClickType clickType, Player player) {
                // this prevents dupes and backpackception
                if (slotId > -1 && (getSlot(slotId).getItem().getItem() instanceof BackpackItem || getSlot(
                        slotId).getItem().is(net.minecraft.world.item.Items.SHULKER_BOX))) {
                    Mochila.LOGGER.debug("Tried to move illegal item: {} {} {} {}",
                            getSlot(slotId).getItem(),
                            slotId,
                            button,
                            clickType
                    );
                    return;
                }

                super.clicked(slotId, button, clickType, player);
            }
        }, stack.getDisplayName()));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player,
            InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        openForPlayer(player, stack);
        return InteractionResultHolder.consume(stack);
    }

    // TODO: implements Modes
    // modes: Store, only store items that are already in the chest
    //        Dump, store everything (this is the current implementation)
    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        Direction direction = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (player != null && player.isShiftKeyDown()) {
            boolean inserted = unloadIntoContainer(state, player, level, pos, hand, direction);
            if (inserted) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }

            // check for signs
            if (state.getBlock() instanceof WallSignBlock) {
                Direction facing = state.getValue(WallSignBlock.FACING);
                BlockPos blockPosBehindSign = pos.relative(facing.getOpposite());
                BlockState blockState = level.getBlockState(blockPosBehindSign);
                boolean insertedx =
                        unloadIntoContainer(blockState, player, level, blockPosBehindSign, hand, facing);
                if (insertedx) {
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }

        return super.useOn(context);
    }

    private boolean unloadIntoContainer(BlockState state, Player player, Level level, BlockPos pos,
            InteractionHand hand, Direction direction) {
        if (CONTAINER_WHITELIST.stream().anyMatch(state::is)) {
            if (player instanceof ServerPlayer serverPlayer) {
                SoundHelper.sendClientSound(player, SoundEvents.ARMOR_EQUIP_NETHERITE.value(), SoundSource.PLAYERS, 0.5f, 1f);
            }

            BackpackContainer backpack = new BackpackContainer(player.getItemInHand(hand), size);
            Container container = HopperBlockEntity.getContainerAt(level, pos);

            if (container != null) {
                boolean inserted = false;
                for (var i = 0; i < backpack.getContainerSize(); i++) {
                    ItemStack toInsert = ContainerHelper.takeItem(backpack.getItems(), i);
                    int inCount = toInsert.getCount();
                    boolean insertingEmpty = toInsert.isEmpty();
                    ItemStack remainder = HopperBlockEntity.addItem(backpack, container, toInsert, null);
                    backpack.setItem(i, remainder);
                    if (!insertingEmpty && (remainder.isEmpty() || inCount > remainder.getCount())) {
                        inserted = true;
                    }
                }
                if (inserted) {
                    sendParticles(direction, level, pos);
                }
                backpack.setChanged();
                return true;
            }
        }
        return false;
    }

    public enum Tier {
        LEATHER,
        IRON,
        GOLD,
        DIAMOND,
        NETHERITE
    }
}
