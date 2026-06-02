package com.iamkaf.mochila.item.backpack;

import com.iamkaf.amber.api.event.v1.events.common.PlayerEvents;
import com.iamkaf.amber.api.functions.v1.PlayerFunctions;
import com.iamkaf.amber.api.functions.v1.WorldFunctions;
import com.iamkaf.mochila.MochilaConfig;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.registry.DataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuickStash {
    static {
        PlayerEvents.ENTITY_INTERACT.register((player, level, hand, entity) -> {
            if (!level.isClientSide() && player.getItemInHand(hand)
                    .getItem() instanceof BackpackItem backpackItem && player.isShiftKeyDown() && entity instanceof
                    ItemFrame frame) {
                Direction direction = frame.getDirection().getOpposite();
                BlockPos blockPosBehindFrame = frame.getPos().relative(direction);
                BlockState blockState = level.getBlockState(blockPosBehindFrame);

                Result result = quickStash(blockState,
                        player,
                        level,
                        blockPosBehindFrame,
                        hand,
                        direction.getOpposite(),
                        backpackItem.size,
                        getMode(player.getItemInHand(hand))
                );

                if (result.handled()) {
                    return InteractionResult.CONSUME;
                }
            }

            return InteractionResult.PASS;
        });
    }

    public static Mode getMode(ItemStack stack) {
        return stack.getOrDefault(DataComponents.QUICKSTASH_MODE.get(), 0) == 0 ? Mode.DUMP : Mode.STORE;
    }

    public static void sendParticles(Direction direction, Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            BlockPos particlesPosition = pos.relative(direction);
            serverLevel.sendParticles(
                    ParticleTypes.CLOUD,
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

    public static Result quickStash(BlockState state, Player player, Level level, BlockPos pos, InteractionHand hand,
            Direction direction, BackpackContainer.BackpackSize size, Mode mode) {
        if (!MochilaConfig.quickstashEnabled()) {
            return Result.disabled();
        }

        if (!isConfiguredTarget(state)) {
            return Result.unsupported();
        }

        BackpackContainer backpack = new BackpackContainer(player.getItemInHand(hand), size);
        Container container = HopperBlockEntity.getContainerAt(level, pos);

        if (container != null) {
            boolean inserted = false;
            int movedStacks = 0;
            int movedItems = 0;
            boolean attemptedNonEmpty = false;
            boolean skippedByStoreMode = false;
            for (var i = 0; i < backpack.getContainerSize(); i++) {
                if (mode.equals(Mode.STORE) && !contains(backpack.getItem(i), container)) {
                    if (!backpack.getItem(i).isEmpty()) {
                        skippedByStoreMode = true;
                    }
                    continue;
                }
                ItemStack toInsert = ContainerHelper.takeItem(backpack.getItems(), i);
                int inCount = toInsert.getCount();
                boolean insertingEmpty = toInsert.isEmpty();
                attemptedNonEmpty |= !insertingEmpty;
                ItemStack remainder = HopperBlockEntity.addItem(backpack, container, toInsert, null);
                backpack.setItem(i, remainder);
                if (!insertingEmpty && (remainder.isEmpty() || inCount > remainder.getCount())) {
                    inserted = true;
                    movedStacks++;
                    movedItems += inCount - remainder.getCount();
                }
            }
            if (inserted) {
                if (MochilaConfig.quickstashSound() && player instanceof ServerPlayer serverPlayer) {
                    WorldFunctions.playSoundAt(
                            serverPlayer.level(),
                            serverPlayer.blockPosition(),
                            SoundEvents.ARMOR_EQUIP_NETHERITE,
                            SoundSource.PLAYERS,
                            0.5f,
                            1f
                    );
                }
                if (MochilaConfig.quickstashParticles()) {
                    sendParticles(direction, level, pos);
                }
            }
            backpack.setChanged();
            Result result = new Result(
                    true,
                    inserted,
                    movedStacks,
                    movedItems,
                    attemptedNonEmpty,
                    skippedByStoreMode,
                    state.getBlock().getName(),
                    mode
            );
            sendFeedback(player, result);
            return result;
        }
        return Result.unsupported();
    }

    public static boolean contains(ItemStack stack, Container container) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (ItemStack.isSameItem(stack, container.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isConfiguredTarget(BlockState state) {
        return configuredTargets().stream().anyMatch(state::is);
    }

    private static Set<Block> configuredTargets() {
        return MochilaConfig.quickstashTargets()
                .stream()
                .map(Identifier::tryParse)
                .flatMap(id -> id == null ? Stream.<Block>empty() : BuiltInRegistries.BLOCK.getOptional(id).stream())
                .collect(Collectors.toSet());
    }

    public enum Mode {
        DUMP,
        STORE
    }

    private static void sendFeedback(Player player, Result result) {
        if (!MochilaConfig.quickstashFeedback()) {
            return;
        }
        if (!result.moved() && !MochilaConfig.quickstashFeedbackNoop()) {
            return;
        }
        PlayerFunctions.sendActionBar(player, result.message());
    }

    public record Result(
            boolean handled,
            boolean moved,
            int movedStacks,
            int movedItems,
            boolean attemptedNonEmpty,
            boolean skippedByStoreMode,
            Component targetName,
            Mode mode
    ) {
        static Result unsupported() {
            return new Result(false, false, 0, 0, false, false, Component.empty(), Mode.DUMP);
        }

        static Result disabled() {
            return new Result(false, false, 0, 0, false, false, Component.empty(), Mode.DUMP);
        }

        public Component message() {
            if (moved) {
                String key = mode == Mode.STORE
                        ? "mochila.quickstash.feedback.stored_matching"
                        : "mochila.quickstash.feedback.stored";
                return Component.translatable(key, movedItems, targetName);
            }
            if (mode == Mode.STORE && skippedByStoreMode) {
                return Component.translatable("mochila.quickstash.feedback.no_matching");
            }
            if (attemptedNonEmpty) {
                return Component.translatable("mochila.quickstash.feedback.full", targetName);
            }
            return Component.translatable("mochila.quickstash.feedback.empty");
        }
    }
}
