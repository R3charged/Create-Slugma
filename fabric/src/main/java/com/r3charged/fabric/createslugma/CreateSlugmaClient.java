package com.r3charged.fabric.createslugma;

import net.fabricmc.api.ClientModInitializer;

public class CreateSlugmaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AllPartialModels.init();
    }
}
