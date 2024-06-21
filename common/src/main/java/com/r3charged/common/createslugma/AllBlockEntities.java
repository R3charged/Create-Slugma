package com.r3charged.common.createslugma;

import com.r3charged.common.createslugma.block.entity.SlugmaBurnerBlockEntity;
import com.r3charged.common.createslugma.block.entity.SlugmaBurnerRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import static com.r3charged.common.createslugma.CreateSlugmaImplementation.REGISTRATE;

public class AllBlockEntities {

    public static BlockEntityEntry<SlugmaBurnerBlockEntity> BURNER = REGISTRATE
            .blockEntity("slugma_heater", SlugmaBurnerBlockEntity::new)
            .validBlocks(AllBlocks.SLUGMA_BURNER_BLOCK)
            .renderer(() -> SlugmaBurnerRenderer::new)
            .register();


    public static void register() {

    }
}
