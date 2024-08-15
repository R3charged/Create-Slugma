package com.r3charged.common.createslugma.net;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.r3charged.common.createslugma.CobblemonUtils;
import net.minecraft.client.Minecraft;

public class SendOutParticlesHandler{

    public static void handle(SendOutParticlesPacket packet) {
        CobblemonUtils.Companion.spawnSendOutParticles(
                packet.sendOutPosition,
                packet.sendOutOffset,
                packet.ballType
        );
    }
}
