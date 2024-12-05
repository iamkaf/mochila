package com.iamkaf.mochila.item;

import com.iamkaf.amber.api.sound.SoundHelper;
import com.iamkaf.mochila.Mochila;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.List;

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
    public @NotNull InteractionResult use(Level level, Player player,
            InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        openForPlayer(player, stack);
        return InteractionResult.CONSUME;
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
                return InteractionResult.SUCCESS;
            }

            // check for signs
            if (state.getBlock() instanceof WallSignBlock) {
                Direction facing = state.getValue(WallSignBlock.FACING);
                BlockPos blockPosBehindSign = pos.relative(facing.getOpposite());
                BlockState blockState = level.getBlockState(blockPosBehindSign);
                boolean insertedx =
                        unloadIntoContainer(blockState, player, level, blockPosBehindSign, hand, facing);
                if (insertedx) {
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.useOn(context);
    }

    private boolean unloadIntoContainer(BlockState state, Player player, Level level, BlockPos pos,
            InteractionHand hand, Direction direction) {
        if (CONTAINER_WHITELIST.stream().anyMatch(state::is)) {
            if (player instanceof ServerPlayer serverPlayer) {
                SoundHelper.sendClientSound(
                        player,
                        SoundEvents.ARMOR_EQUIP_NETHERITE.value(),
                        SoundSource.PLAYERS,
                        0.5f,
                        1f
                );
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
