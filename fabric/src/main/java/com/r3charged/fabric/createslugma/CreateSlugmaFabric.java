package com.r3charged.fabric.createslugma;

import com.r3charged.common.createslugma.*;
import com.simibubi.create.Create;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class CreateSlugmaFabric extends CreateSlugmaImplementation implements ModInitializer{

    public CreateSlugmaFabric() {
        CreateSlugmaImplementation.instance = this;
        super.networkManager = new FabricNetworkManager();
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", "CobblemonSlugma", Create.VERSION);
        initialize();
        //CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);
        AllArmInteractionsFabric.register();
        REGISTRATE.register();
    }

}
