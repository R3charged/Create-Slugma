package com.r3charged.common.createslugma;

import com.cobblemon.mod.common.api.net.ClientNetworkPacketHandler;
import com.cobblemon.mod.common.api.net.NetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface NetworkManager {

    <T extends Packet<T>> void registerClientBoundPacket(ResourceLocation id, Class<T> klass,
                                                         Function<FriendlyByteBuf, T> decoder,
                                                         Consumer<T> handler) ;


    //<T extends Packet<T>> void sendToPlayer(ServerPlayer player, T packet);

    //<T extends Packet<T>> void sendToAll(T packet);

    //<T extends Packet<T>> void sendToAllInRange(Vec3 position, double range, T packet);
    <T extends Packet<T>> void sendToTracking(Level level, BlockPos position, T packet);

    //<T extends Packet<T>> void sendToServer(T packet);


}
