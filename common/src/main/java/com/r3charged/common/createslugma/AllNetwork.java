package com.r3charged.common.createslugma;

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.r3charged.common.createslugma.net.SendOutParticlesHandler;
import com.r3charged.common.createslugma.net.SendOutParticlesPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AllNetwork {


    public static void registerClientBound() {
        registerClientBoundPacket(SendOutParticlesPacket.PACKED_ID, SendOutParticlesPacket.class, SendOutParticlesPacket::decode, SendOutParticlesHandler::handle);
    }

    private static <T extends Packet<T>> void registerClientBoundPacket(ResourceLocation loc, Class<T> klass, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        CreateSlugmaImplementation.instance.networkManager.registerClientBoundPacket(loc, klass, decoder, handler);
    }


}
