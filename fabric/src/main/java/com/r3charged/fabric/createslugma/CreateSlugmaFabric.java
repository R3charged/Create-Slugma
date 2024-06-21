package com.r3charged.fabric.createslugma;

import com.r3charged.common.createslugma.*;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.burner.BlazeBurnerInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CreateSlugmaFabric extends CreateSlugmaImplementation implements ModInitializer{




    public CreateSlugmaFabric() {
        CreateSlugmaImplementation.instance = this;
    }
    @Override
    public void onInitialize() {

        LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", "CobblemonSlugma", Create.VERSION);

        AllGameRules.init();
        //CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);

        REGISTRATE.register();
    }


    @Override
    public GameRules.Key registerGameRule(String name, GameRules.Category category, GameRules.Type type) {
        return GameRuleRegistry.register(name, category, type);
    }
}
