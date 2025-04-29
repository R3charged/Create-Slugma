package com.r3charged.common.createslugma;

import com.simibubi.create.Create;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;
import net.minecraft.resources.ResourceLocation;

import static com.r3charged.common.createslugma.CreateSlugmaImplementation.asResource;

public class AllSpriteShifts {



    public static final SpriteShiftEntry SUPER_SLUGMA_BURNER_FLAME = get("block/blaze_burner_flame", "block/slugma_burner_flame_superheated_scroll"),
            SHINY_SLUGMA_BURNER_FLAME = get("block/blaze_burner_flame", "block/slugma_burner_flame_superheated_shiny_scroll");

    public static void init() {

    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Create.asResource(originalLocation), asResource(targetLocation));
    }

}
