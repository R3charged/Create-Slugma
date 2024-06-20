package com.r3charged.fabric.createslugma;

import com.r3charged.common.createslugma.AllSpriteShifts;
import net.fabricmc.api.ClientModInitializer;

public class CreateSlugmaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AllPartialModels.init();
        AllSpriteShifts.init();
    }

}
