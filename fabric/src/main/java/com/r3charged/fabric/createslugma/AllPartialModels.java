package com.r3charged.fabric.createslugma;

import com.jozufozu.flywheel.core.PartialModel;
import net.minecraft.resources.ResourceLocation;

public class AllPartialModels {

    public static void init() {

    }
    public static final PartialModel BASE = new PartialModel(new ResourceLocation(CreateSlugma.ID ,"block/slugma_burner/slugma/slugma_base"));
    public static final PartialModel BASE_SHINY = new PartialModel(new ResourceLocation(CreateSlugma.ID ,"block/slugma_burner/slugma/slugma_base_shiny"));
    public static final PartialModel BASE_SUPERHEATED = new PartialModel(new ResourceLocation(CreateSlugma.ID ,"block/slugma_burner/slugma/slugma_base_superheated"));
    public static final PartialModel BASE_SUPERHEATED_SHINY = new PartialModel(new ResourceLocation(CreateSlugma.ID ,"block/slugma_burner/slugma/slugma_base_superheated_shiny"));

}
