package com.r3charged.common.createslugma;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class AllArmInteractions {
    private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
        T type = factory.apply(CreateSlugmaImplementation.asResource(id));
        ArmInteractionPointType.register(type);
        return type;
    }


    public static ArmInteractPointFactory SLUGMA_BURNER_POINT;


    public static final SlugmaBurnerType LIQUID_BLAZE_BURNER = register("slugma_burner", SlugmaBurnerType::new);

    public static class SlugmaBurnerType extends ArmInteractionPointType {
        public SlugmaBurnerType(ResourceLocation id) {
            super(id);
        }

        @Override
        public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
            return AllBlocks.SLUGMA_BURNER_BLOCK.has(state);
        }

        @Override
        public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
            return SLUGMA_BURNER_POINT.create(this, level, pos, state);
        }
    }


    @FunctionalInterface
    public interface ArmInteractPointFactory {
        ArmInteractionPoint create(ArmInteractionPointType pointType, Level level, BlockPos pos, BlockState state);
    }

    public static void register() {}
}
