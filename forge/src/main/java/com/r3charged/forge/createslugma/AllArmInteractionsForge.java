package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.AllArmInteractions;
import com.r3charged.common.createslugma.block.SlugmaBurnerBlock;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class AllArmInteractionsForge {
    public static void register() {
        AllArmInteractions.register();
        AllArmInteractions.SLUGMA_BURNER_POINT = SlugmaBurnerPoint::new;
    }

    public static class SlugmaBurnerPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {
        public SlugmaBurnerPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
            super(type, level, pos, state);
        }

        @Override
        public ItemStack insert(ItemStack stack, boolean simulate) {
            ItemStack input = stack.copy();
            InteractionResultHolder<ItemStack> res =
                    SlugmaBurnerBlock.tryInsert(cachedState, level, pos, input, false, false, simulate);
            ItemStack remainder = res.getObject();
            if (input.isEmpty()) {
                return remainder;
            } else {
                if (!simulate)
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remainder);
                return input;
            }
        }
    }
}
