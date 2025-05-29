package com.r3charged.common.createslugma;

import com.r3charged.common.createslugma.block.entity.PokemonBurnerBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import com.r3charged.common.createslugma.block.entity.PokemonBurnerBlockRenderer;
import static com.r3charged.common.createslugma.CreateSlugmaImplementation.REGISTRATE;

public class AllBlockEntities {


    public static BlockEntityEntry<PokemonBurnerBlockEntity> BURNER = REGISTRATE
            .blockEntity("slugma_heater", PokemonBurnerBlockEntity::new)
            .validBlocks(AllBlocks.SLUGMA_BURNER_BLOCK)
            .renderer(() -> PokemonBurnerBlockRenderer::new)
            .register();


    public static void register() {

    }
}
