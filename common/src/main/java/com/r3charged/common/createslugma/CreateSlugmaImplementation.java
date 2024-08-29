package com.r3charged.common.createslugma;

import com.r3charged.common.createslugma.block.SlugmaBurnerBlock;
import com.simibubi.create.content.fluids.tank.BoilerHeaters;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CreateSlugmaImplementation {

    public static CreateSlugmaImplementation instance;
    public static final String MOD_ID = "createslugma";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    public NetworkManager networkManager;

    public static final Logger LOGGER = LogManager.getLogger("CreateSlugma");



    public void initialize() {
        AllBlocks.register();
        AllBlockEntities.register();
        AllGameRules.init();

    }

    public void setup() {
        BoilerHeaters.registerHeater(AllBlocks.SLUGMA_BURNER_BLOCK.get(), (level, pos, state) -> {
            BlazeBurnerBlock.HeatLevel value = state.getValue(SlugmaBurnerBlock.HEAT_LEVEL);
            if (value == BlazeBurnerBlock.HeatLevel.NONE) return -1;
            if (value == BlazeBurnerBlock.HeatLevel.SEETHING) return 2;
            if (value.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) return 1;
            return 0;
        });
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
