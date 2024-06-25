package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.CreateSlugmaImplementation;
import com.r3charged.common.createslugma.block.SlugmaBurnerBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.burner.BlazeBurnerInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.DistExecutor;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

@Mod(CreateSlugmaImplementation.MOD_ID)
public class CreateSlugmaForge extends CreateSlugmaImplementation {

    public CreateSlugmaForge() {
        instance = this;
        LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", "CobblemonSlugma", Create.VERSION);
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CreateSlugmaClient::onInitializeClient);

        REGISTRATE.registerEventListeners(modEventBus);
        //MinecraftForge.EVENT_BUS.register(this);
        initialize();

    }
}
