package com.r3charged.forge.createslugma;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class NeoForgeDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        var gen = event.getGenerator();
        var lookup = event.getLookupProvider();
        var helper = event.getExistingFileHelper();

    }
}
