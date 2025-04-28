package com.r3charged.forge.createslugma;

import com.r3charged.common.createslugma.AllNetwork;
import com.r3charged.common.createslugma.CreateSlugmaImplementation;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.Create;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.DistExecutor;

@Mod(CreateSlugmaImplementation.MOD_ID)
public class CreateSlugmaForge extends CreateSlugmaImplementation {

    public CreateSlugmaForge() {
        instance = this;

        this.networkManager = new ForgeNetworkManager();
        LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", "CobblemonSlugma", Create.VERSION);
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CreateSlugmaClient::onInitializeClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::fmlsetup);
        REGISTRATE.registerEventListeners(modEventBus);

        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
        //MinecraftForge.EVENT_BUS.register(this);
        initialize();
        AllNetwork.registerClientBound();
        AllArmInteractionsForge.register();

    }

    private void fmlsetup(final FMLCommonSetupEvent event) {
        setup();
    }
}
