package com.r3charged.common.createslugma;

import com.r3charged.common.createslugma.block.SlugmaBurnerBlock;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;

import static com.r3charged.common.createslugma.CreateSlugmaImplementation.REGISTRATE;
import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class AllBlocks {

    public static BlockEntry<SlugmaBurnerBlock> SLUGMA_BURNER_BLOCK = REGISTRATE.block("slugma_burner", SlugmaBurnerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY))
            .properties(p -> p.lightLevel(SlugmaBurnerBlock::getLight))
            .transform(pickaxeOnly())
            .addLayer(() -> RenderType::cutoutMipped)
            .tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag, AllTags.AllBlockTags.PASSIVE_BOILER_HEATERS.tag)
            .loot((lt, block) -> lt.add(block, SlugmaBurnerBlock.buildLootTable()))
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .onRegister(movementBehaviour(new BlazeBurnerMovementBehaviour()))
            .onRegister(interactionBehaviour(new BlazeBurnerInteractionBehaviour()))
            .lang("Slugma Burner")
            .item()
            .model(AssetLookup.customBlockItemModel("slugma_burner", "block_with_slugma"))
            .build()
            .register();

    public static void register() {

    }
}
