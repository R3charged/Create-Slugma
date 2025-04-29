package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.AllPartialModels;
import com.r3charged.common.createslugma.AllSpriteShifts;

public class CreateSlugmaClient {

    public static void onInitializeClient() {
        AllSpriteShifts.init();
        AllPartialModels.init();
    }

}
