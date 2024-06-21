package com.r3charged.fabric.createslugma;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class CreateSlugmaDataFabric implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {

        Path railwaysResources = Paths.get(System.getProperty(ExistingFileHelper.EXISTING_RESOURCES));
        // fixme re-enable the existing file helper when porting lib's ResourcePackLoader.createPackForMod is fixed
        ExistingFileHelper helper = new ExistingFileHelper(
                Set.of(railwaysResources), Set.of("create"), false, null, null
        );
        FabricDataGenerator.Pack pack = gen.createPack();
        CreateSlugmaFabric.REGISTRATE.setupDatagen(pack, helper);
    }
}
