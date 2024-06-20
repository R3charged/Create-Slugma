package com.r3charged.common.createslugma;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;
import net.minecraft.resources.ResourceLocation;

public class AllSpriteShifts {

    public static final SpriteShiftEntry SUPER_SLUGMA_BURNER_FLAME = get("block/blaze_burner_flame", "block/slugma_burner_flame_superheated_scroll"),
            SHINY_SLUGMA_BURNER_FLAME = get("block/blaze_burner_flame", "block/slugma_burner_flame_superheated_shiny_scroll");

    public static void init() {

    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Create.asResource(originalLocation), asResource(targetLocation));
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation("createslugma", path);
    } //TODO use const
}
