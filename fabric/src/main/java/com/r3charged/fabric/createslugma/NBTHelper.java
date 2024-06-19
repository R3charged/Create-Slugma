package com.r3charged.fabric.createslugma;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
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

    public static ItemStack popPokemonEntityToItemStack(ItemStack itemstack, PokemonEntity entity) {
        Pokemon pokemon = entity.getPokemon();
        entity.discard();
        ServerPlayer player = pokemon.getOwnerPlayer();
        PlayerPartyStore party = Cobblemon.INSTANCE.getStorage().getParty(player);
        if (!party.remove(pokemon)) {
            try {
                Cobblemon.INSTANCE.getStorage().getPC(player.getUUID()).remove(pokemon);
            } catch (NoPokemonStoreException e) {
            }
        }
        savePokemonWithBlockEntity(itemstack.getOrCreateTag(), pokemon);

        return itemstack;
    }

    public static void popPartyPokemonToBlockEntity(ServerPlayer player, Pokemon pokemon, PokemonEntity entity) {}



}
