package com.r3charged.common.createslugma;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface Packet<T> {

    ResourceLocation getId();

    void encode(FriendlyByteBuf buf);
}
