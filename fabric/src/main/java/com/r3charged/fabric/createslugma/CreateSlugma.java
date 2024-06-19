package com.r3charged.fabric.createslugma;

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
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CreateSlugma implements ModInitializer {

    public static final String ID = "createslugma";
    public static final Logger LOGGER = LoggerFactory.getLogger("CreateSlugma");
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);

    public static BlockEntry<SlugmaBurnerBlock> SLUGMA_BURNER_BLOCK;
    public static BlockEntityEntry<SlugmaBurnerBlockEntity> BURNER;

    @Override
    public void onInitialize() {
        LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", "CobblemonSlugma", Create.VERSION);

        AllGameRules.init();
        //CommandRegistrationCallback.EVENT.register(ExampleCommandRegistry::registerCommands);

        SLUGMA_BURNER_BLOCK = REGISTRATE.block("slugma_burner", SlugmaBurnerBlock::new)
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
        BURNER = REGISTRATE
                .blockEntity("slugma_heater", SlugmaBurnerBlockEntity::new)
                .validBlocks(SLUGMA_BURNER_BLOCK)
                .renderer(() -> SlugmaBurnerRenderer::new)
                .register();
        REGISTRATE.register();
    }






}
