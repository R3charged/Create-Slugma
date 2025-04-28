package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.AllPartialModels;
import com.r3charged.common.createslugma.AllSpriteShifts;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateSlugmaClient {

    public static void onInitializeClient() {
        AllSpriteShifts.init();
        AllPartialModels.init();
    }

}
