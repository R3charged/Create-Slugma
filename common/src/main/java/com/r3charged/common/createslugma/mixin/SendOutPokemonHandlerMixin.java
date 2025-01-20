package com.r3charged.common.createslugma.mixin;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.net.messages.server.SendOutPokemonPacket;
import com.cobblemon.mod.common.net.serverhandling.storage.SendOutPokemonHandler;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.cobblemon.mod.common.util.TraceResult;
import com.r3charged.common.createslugma.CobblemonUtils;
import com.r3charged.common.createslugma.CreateSlugmaImplementation;
import com.r3charged.common.createslugma.net.SendOutParticlesPacket;
import com.r3charged.common.createslugma.util.NBTHelper;
import com.r3charged.common.createslugma.block.entity.SlugmaBurnerBlockEntity;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SendOutPokemonHandler.class, remap = true)
public abstract class SendOutPokemonHandlerMixin {

    @Inject(method = "handle(Lcom/cobblemon/mod/common/net/messages/server/SendOutPokemonPacket;Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerPlayer;)V",
            at = @At(value = "HEAD"), cancellable = true, remap = true)
    private void onHandle(SendOutPokemonPacket packet, MinecraftServer server, ServerPlayer player, CallbackInfo ci) {
        TraceResult result = PlayerExtensionsKt.traceBlockCollision(player, 10f, .05f, (it) -> it.isSolid());

        // The API from PlayerExtensionsKt isn't null-safe and returns a null when a HIT can't be found.
        // Fixes: Issue #16
        if (result == null) {
          return;
        }

        BlockState state = player.level().getBlockState(result.getBlockPos());
        if (player.level().getBlockEntity(result.getBlockPos()) instanceof SlugmaBurnerBlockEntity slugmaBurnerBlockEntity) {
            Pokemon pokemon = slugmaBurnerBlockEntity.getPokemon();
            if(player.getUUID().toString().equals(pokemon.getOriginalTrainer())) { // getOwner is not instantiated
                Cobblemon.INSTANCE.getStorage().getParty(player).add(pokemon);
                player.level().setBlock(result.getBlockPos(), AllBlocks.BLAZE_BURNER.getDefaultState(), 3);
                String balltype = pokemon.getCaughtBall().getName().getPath().toLowerCase().replace("_", "");
                CreateSlugmaImplementation.instance.networkManager.sendToTracking(player.level(), result.getBlockPos(), new SendOutParticlesPacket(
                        new Vec3(result.getBlockPos().getX(),result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3(.5,.1,.5), balltype));
                ci.cancel();
            }

        } else if (state.getBlock() instanceof BlazeBurnerBlock) {
            if (state.getValue(BlazeBurnerBlock.HEAT_LEVEL).equals(BlazeBurnerBlock.HeatLevel.NONE)) {
                Pokemon pokemon = Cobblemon.INSTANCE.getStorage().getParty(player).get(packet.getSlot());
                if (NBTHelper.isSlugma(pokemon)) {
                    BlockState newState = com.r3charged.common.createslugma.AllBlocks.SLUGMA_BURNER_BLOCK.get().getStateForPlacement(player);
                    player.level().setBlock(result.getBlockPos(), newState, 3);


                    SlugmaBurnerBlockEntity blazeBurnerBlockEntity = (SlugmaBurnerBlockEntity) player.level().getBlockEntity(result.getBlockPos());
                    CompoundTag tag = new CompoundTag();
                    NBTHelper.savePokemon(tag, pokemon);
                    blazeBurnerBlockEntity.read(tag, false);
                    Cobblemon.INSTANCE.getStorage().getParty(player).remove(pokemon);
                    String balltype = pokemon.getCaughtBall().getName().getPath().toLowerCase().replace("_", "");
                    CreateSlugmaImplementation.instance.networkManager.sendToTracking(player.level(), result.getBlockPos(), new SendOutParticlesPacket(
                            new Vec3(result.getBlockPos().getX(),result.getBlockPos().getY(), result.getBlockPos().getZ()), new Vec3(.5,.1,.5), balltype));
                    ci.cancel();
                }
            }

        }
    }

}
