package com.r3charged.fabric.createslugma

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.scheduling.afterOnClient
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay.Companion.PORTRAIT_DIAMETER
import com.cobblemon.mod.common.client.particle.BedrockParticleEffectRepository
import com.cobblemon.mod.common.client.particle.ParticleStorm
import com.cobblemon.mod.common.client.render.MatrixWrapper
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState
import com.cobblemon.mod.common.client.render.models.blockbench.repository.PokemonModelRepository
import com.cobblemon.mod.common.client.render.models.blockbench.repository.RenderContext
import com.cobblemon.mod.common.entity.PoseType
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import com.cobblemon.mod.common.util.cobblemonResource
import com.mojang.blaze3d.platform.Lighting
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.sounds.SoundSource
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

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

        fun drawPortraitPokemon(
            species: Species,
            aspects: Set<String>,
            matrixStack: PoseStack,
            state: PoseableEntityState<PokemonEntity>? = null,
            partialTicks: Float
        ) {
            var scale = 13F
            var reversed = false
            val model = PokemonModelRepository.getPoser(species.resourceIdentifier, aspects)
            val texture = PokemonModelRepository.getTexture(species.resourceIdentifier, aspects, state?.animationSeconds ?: 0F)

            val context = RenderContext()
            PokemonModelRepository.getTextureNoSubstitute(species.resourceIdentifier, aspects, 0f).let { it -> context.put(
                RenderContext.TEXTURE, it) }
            context.put(RenderContext.SCALE, species.getForm(aspects).baseScale)
            context.put(RenderContext.SPECIES, species.resourceIdentifier)
            context.put(RenderContext.ASPECTS, aspects)

            val renderType = model.renderType(texture)

            RenderSystem.applyModelViewMatrix()
            val quaternion1 = Axis.YP.rotationDegrees(-32.0f * (if (reversed) -1.0f else 1.0f))
            val quaternion2 = Axis.XP.rotationDegrees(5.0f)

            if (state == null) {
                model.setupAnimStateless(setOf(PoseType.PORTRAIT, PoseType.PROFILE))
            } else {
                val originalPose = state.currentPose
                model.getPose(PoseType.SLEEP)?.let { state.setPose(it.poseName) }
                state.timeEnteredPose = 0F
                state.updatePartialTicks(partialTicks)
                model.setupAnimStateful(null, state, 0F, 0F, 0F, 0F, 0F)
                originalPose?.let { state.setPose(it) }
            }

            matrixStack.pushPose()
            matrixStack.translate(0.0, 0.0, 0.0)
            matrixStack.scale(.75f, .75f, .75f)
            matrixStack.translate(0.5, -0.5, 0.5)
            //matrixStack.translate(model.portraitTranslation.x * if (reversed) -1F else 1F, model.portraitTranslation.y, model.portraitTranslation.z - 4)
            //matrixStack.scale(model.portraitScale, model.portraitScale, 1 / model.portraitScale)
            //matrixStack.mulPose(quaternion1)
            //matrixStack.mulPose(quaternion2)

            val light1 = Vector3f(0.2F, 1.0F, -1.0F)
            val light2 = Vector3f(0.1F, 0.0F, 8.0F)
            RenderSystem.setShaderLights(light1, light2)
            quaternion1.conjugate()

            val immediate = Minecraft.getInstance().renderBuffers().bufferSource()
            val buffer = immediate.getBuffer(renderType)
            val packedLight = LightTexture.pack(11, 7)

            model.withLayerContext(immediate, state, PokemonModelRepository.getLayers(species.resourceIdentifier, aspects)) {
                model.render(context, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F)
                immediate.endBatch()
            }

            matrixStack.popPose()
            model.setDefault()

            Lighting.setupFor3DItems()
        }
    }
}