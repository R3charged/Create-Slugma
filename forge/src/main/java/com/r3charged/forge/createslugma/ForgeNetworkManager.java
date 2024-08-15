package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.CreateSlugmaImplementation;
import com.r3charged.common.createslugma.NetworkManager;
import com.r3charged.common.createslugma.Packet;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ForgeNetworkManager implements NetworkManager {


    private String PROTOCOL_VERSION = "1";
    private int packetId = 0;
    private final SimpleChannel channel = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CreateSlugmaImplementation.MOD_ID, "main_channel"),
            () -> PROTOCOL_VERSION,
            s -> true,
            s -> true
    );

    // Register a client-bound packet
    public <T extends Packet<T>> void registerClientBoundPacket(ResourceLocation id, Class<T> klass, Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        channel.messageBuilder(klass, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(decoder)
                .encoder(Packet::encode)
                .consumerMainThread((packet, contextSupplier) -> {
                    NetworkEvent.Context context = contextSupplier.get();
                    context.enqueueWork(() -> handler.accept(packet));
                    context.setPacketHandled(true);
                })
                .add();

    }

    // Send a packet to players tracking a specific position
    @Override
    public <T extends Packet<T>> void sendToTracking(Level level, BlockPos position, T packet) {
        channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(position)), packet);
    }

}
