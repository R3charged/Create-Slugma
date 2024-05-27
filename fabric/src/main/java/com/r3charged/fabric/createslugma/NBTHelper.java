package com.r3charged.fabric.createslugma;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import static com.cobblemon.mod.common.api.scheduling.SchedulingFunctionsKt.afterOnClient;

public class NBTHelper {

    public static void savePokemonWithBlockEntity(CompoundTag tag, Pokemon pokemon) {

        CompoundTag blockEntity = new CompoundTag();

        savePokemon(blockEntity, pokemon);
        tag.put("BlockEntityTag", blockEntity);
    }

    public static void savePokemon(CompoundTag tag, Pokemon pokemon) {
        if (pokemon == null) {
            return;
        }
        tag.put("pokemon", pokemon.saveToNBT(new CompoundTag()));
    }


    public static ItemStack getSlugmaBurnerItem(Pokemon pokemon) {
        ItemStack filled = CreateSlugma.SLUGMA_BURNER_BLOCK.asStack();
        savePokemonWithBlockEntity(filled.getOrCreateTag(), pokemon);

        return filled;
    }

    public static Pokemon getDefaultSlugma() {
        PokemonProperties pokemon = new PokemonProperties();
        pokemon.setSpecies("Slugma");
        return pokemon.create();
    }

    public static boolean isSlugma(Pokemon pokemon) {
        return pokemon.getSpecies().getName().equals("Slugma");
    }



}
