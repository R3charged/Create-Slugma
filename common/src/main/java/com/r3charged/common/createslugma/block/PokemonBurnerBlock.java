package com.r3charged.common.createslugma.block;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.List;

public class PokemonBurnerBlock extends BlazeBurnerBlock {
    public PokemonBurnerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pBuilder) {
        List<ItemStack> drops = super.getDrops(pState, pBuilder);

        return drops;
    }
}
