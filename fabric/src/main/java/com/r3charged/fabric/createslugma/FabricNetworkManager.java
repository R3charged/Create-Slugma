package com.r3charged.fabric.createslugma;

import com.r3charged.common.createslugma.NetworkManager;
import com.r3charged.common.createslugma.Packet;
import io.github.fabricators_of_create.porting_lib.util.ServerLifecycleHooks;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FabricNetworkManager implements NetworkManager {

    public <T extends Packet<T>> void registerClientBoundPacket(ResourceLocation id, Class<T> klass, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handlerIn, buf, responseSender) -> {
            T decodedPacket = decoder.apply(buf);
            client.execute(() -> handler.accept(decodedPacket));
        });
    }

    @Override
    public <T extends Packet<T>> void sendToTracking(Level level, BlockPos position, T packet) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(buf);
        for (ServerPlayer player : PlayerLookup.tracking((ServerLevel) level, position)) {
            ServerPlayNetworking.send(player, packet.getId(), buf);
        }
    }


}
