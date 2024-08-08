package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.CreateSlugmaImplementation;
import com.simibubi.create.Create;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.DistExecutor;

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
        AllArmInteractionsForge.register();

    }
}
