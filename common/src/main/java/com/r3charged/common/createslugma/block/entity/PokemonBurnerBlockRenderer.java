package com.r3charged.common.createslugma.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;


public class PokemonBurnerBlockRenderer extends SafeBlockEntityRenderer<PokemonBurnerBlockEntity> {

    public PokemonBurnerBlockRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    protected void renderSafe(PokemonBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

    }
}
