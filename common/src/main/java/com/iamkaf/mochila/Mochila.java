package com.iamkaf.mochila;

import com.iamkaf.mochila.item.BackpackItem;
import com.iamkaf.mochila.registry.CreativeModeTabs;
import com.iamkaf.mochila.registry.Items;
import com.iamkaf.mochila.registry.Keybinds;
import com.iamkaf.mochila.registry.RecipeSerializers;
import com.mojang.logging.LogUtils;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public final class Mochila {
    public static final String MOD_ID = "mochila";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation OPEN_BACKPACK_PACKET_ID = resource("open_backpack");

    public static void init() {
        LOGGER.info("Bag 'em up!");

        // Registries
        Items.init();
        CreativeModeTabs.init();
        RecipeSerializers.init();

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, OpenBackpackPayload.TYPE, OpenBackpackPayload.STREAM_CODEC, ((packet, context) -> {
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            BackpackItem.onBackpackKeybindPressed(player);
        }));
    }

    /**
     * Creates resource location in the mod namespace with the given path.
     */
    public static ResourceLocation resource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public record OpenBackpackPayload(int button) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<OpenBackpackPayload> TYPE =
                new CustomPacketPayload.Type<>(OPEN_BACKPACK_PACKET_ID);

        public static final StreamCodec<ByteBuf, OpenBackpackPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                OpenBackpackPayload::button,
                OpenBackpackPayload::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
