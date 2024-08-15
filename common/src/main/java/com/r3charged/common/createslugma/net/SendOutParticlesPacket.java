package com.r3charged.common.createslugma.net;

import com.r3charged.common.createslugma.CobblemonUtils;
import com.r3charged.common.createslugma.CreateSlugmaImplementation;
import com.r3charged.common.createslugma.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class SendOutParticlesPacket implements Packet<SendOutParticlesPacket> {

    public final Vec3 sendOutPosition;
    public final Vec3 sendOutOffset;
    public final String ballType;
    public static final ResourceLocation PACKED_ID = new ResourceLocation(CreateSlugmaImplementation.MOD_ID, "send_out_particles");

    public ResourceLocation getId() {
        return PACKED_ID;
    }

    public SendOutParticlesPacket(Vec3 sendOutPosition, Vec3 sendOutOffset, String ballType) {
        this.sendOutPosition = sendOutPosition;
        this.sendOutOffset = sendOutOffset;
        this.ballType = ballType;
    }


    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(sendOutPosition.x);
        buf.writeDouble(sendOutPosition.y);
        buf.writeDouble(sendOutPosition.z);
        buf.writeDouble(sendOutOffset.x);
        buf.writeDouble(sendOutOffset.y);
        buf.writeDouble(sendOutOffset.z);
        buf.writeUtf(ballType);
    }

    public static SendOutParticlesPacket decode(FriendlyByteBuf buf) {
        Vec3 sendOutPosition = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        Vec3 sendOutOffset = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        String ballType = buf.readUtf();
        return new SendOutParticlesPacket(sendOutPosition, sendOutOffset, ballType);
    }

}
