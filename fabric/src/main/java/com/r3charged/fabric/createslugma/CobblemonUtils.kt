package com.r3charged.fabric.createslugma

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.scheduling.afterOnClient
import com.cobblemon.mod.common.client.particle.BedrockParticleEffectRepository
import com.cobblemon.mod.common.client.particle.ParticleStorm
import com.cobblemon.mod.common.client.render.MatrixWrapper
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.cobblemonResource
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.phys.Vec3

class CobblemonUtils {
    companion object {


        fun spawnSendOutParticles(sendOutPosition: Vec3, sendOutOffset: Vec3, pokemon: Pokemon) {
            val client = Minecraft.getInstance()
            val soundPos = sendOutPosition.add(sendOutOffset)
            if (client.soundManager.availableSounds.contains(CobblemonSounds.POKE_BALL_SEND_OUT.location)) {
                client.level?.playSound(client.player, soundPos.x, soundPos.y, soundPos.z, CobblemonSounds.POKE_BALL_SEND_OUT, SoundSource.PLAYERS, 0.6f, 1f)
            }
            sendOutPosition?.let {
                val newPos = it.add(sendOutOffset)
                val ballType = pokemon.caughtBall.name.path.lowercase().replace("_", "")
                val mode = "casual"
                val sendflash =
                    BedrockParticleEffectRepository.getEffect(cobblemonResource("${ballType}/${mode}/sendflash"))
                sendflash?.let { effect ->
                    val wrapper = MatrixWrapper()
                    val matrix = PoseStack();
                    matrix.translate(newPos.x, newPos.y, newPos.z)
                    val var10001 = matrix.last().pose()
                    wrapper.updateMatrix(var10001)
                    val world = Minecraft.getInstance().level ?: return@let
                    ParticleStorm(effect, wrapper, world).spawn()
                    val ballsparks =
                        BedrockParticleEffectRepository.getEffect(cobblemonResource("${ballType}/${mode}/ballsparks"))
                    val ballsendsparkle =
                        BedrockParticleEffectRepository.getEffect(cobblemonResource("${ballType}/${mode}/ballsendsparkle"))
                    afterOnClient(seconds = 0.01667f) {
                        ballsparks?.let { effect ->
                            ParticleStorm(effect, wrapper, world).spawn()
                        }
                        ballsendsparkle?.let { effect ->
                            ParticleStorm(effect, wrapper, world).spawn()
                        }
                        afterOnClient(seconds = 0.4f){
                            val ballsparkle =
                                BedrockParticleEffectRepository.getEffect(cobblemonResource("${ballType}/ballsparkle"))
                            ballsparkle?.let { effect ->
                                ParticleStorm(effect, wrapper, world).spawn()
                            }
                        }
                        /*
                        currentEntity.after(seconds = 0.4f) {
                            val ballsparkle =
                                BedrockParticleEffectRepository.getEffect(cobblemonResource("${ballType}/ballsparkle"))
                            ballsparkle?.let { effect ->
                                ParticleStorm(effect, wrapper, world).spawn()
                            }
                        }*/
                    }
                }
            }
        }
    }
}