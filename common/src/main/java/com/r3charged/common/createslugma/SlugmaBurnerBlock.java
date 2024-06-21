package com.r3charged.common.createslugma;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;

import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

public class SlugmaBurnerBlock extends BlazeBurnerBlock {
    public SlugmaBurnerBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(HEAT_LEVEL, HeatLevel.SMOULDERING));
    }
    @Override
    public BlockEntityType<? extends BlazeBurnerBlockEntity> getBlockEntityType() {
        return AllBlockEntities.BURNER.get();
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStateForPlacement(context.getPlayer());
    }

    public BlockState getStateForPlacement(Player player) {
        BlockState defaultState = defaultBlockState();
        HeatLevel initialHeat = HeatLevel.SMOULDERING;
        return defaultState.setValue(HEAT_LEVEL, initialHeat)
                .setValue(FACING, player == null ? Direction.NORTH : player.getDirection()
                        .getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return super.newBlockEntity(pos, state);
    }

    public static LootTable.@NotNull Builder buildLootTable() {
        LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
        SlugmaBurnerBlock block = AllBlocks.SLUGMA_BURNER_BLOCK.get();
        LootTable.Builder builder = LootTable.lootTable();
        LootPool.Builder poolBuilder = LootPool.lootPool();
        for (HeatLevel level : HeatLevel.values()) {
            ItemLike drop = level == HeatLevel.NONE ? AllItems.EMPTY_BLAZE_BURNER.get() : AllBlocks.SLUGMA_BURNER_BLOCK.get();
            poolBuilder.add(LootItem.lootTableItem(drop)
                    .when(survivesExplosion)
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(HEAT_LEVEL, level)))
                    .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                            .copy("pokemon", "BlockEntityTag.pokemon", CopyNbtFunction.MergeStrategy.REPLACE)));
        }

        builder.withPool(poolBuilder.setRolls(ConstantValue.exactly(1)));
        return builder;
    }


}

