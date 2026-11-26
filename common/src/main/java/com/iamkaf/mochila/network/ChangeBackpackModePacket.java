package com.iamkaf.mochila.network;

import com.iamkaf.amber.api.functions.v1.PlayerFunctions;
import com.iamkaf.amber.api.networking.v1.Packet;
import com.iamkaf.amber.api.networking.v1.PacketContext;
import com.iamkaf.mochila.compat.BackpackData;
import com.iamkaf.mochila.compat.Texts;
import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.item.backpack.QuickStash;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ChangeBackpackModePacket implements Packet<ChangeBackpackModePacket> {
    private final int button;

    public ChangeBackpackModePacket(int button) {
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    public static void encode(ChangeBackpackModePacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarInt(packet.button);
    }

    public static ChangeBackpackModePacket decode(FriendlyByteBuf buffer) {
        int button = buffer.readVarInt();
        return new ChangeBackpackModePacket(button);
    }

    public static void handle(ChangeBackpackModePacket packet, PacketContext context) {
        context.execute(() -> {
            ServerPlayer player = context.getServerPlayer();
            ItemStack stack = player.getMainHandItem();

            if (stack.getItem() instanceof BackpackItem) {
                int newMode = BackpackData.quickstashMode(stack) == 0 ? 1 : 0;
                BackpackData.setQuickstashMode(stack, newMode);
                PlayerFunctions.sendActionBar(
                        player,
                        Texts.translatable("mochila.keybind_mode_changed", "§e" + QuickStash.getMode(stack))
                                .withStyle(ChatFormatting.BLUE)
                );
            }
        });
    }
}
