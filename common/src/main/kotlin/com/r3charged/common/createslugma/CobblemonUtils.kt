package com.r3charged.common.createslugma

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.scheduling.afterOnClient
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
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3

class CobblemonUtils {
    companion object {


        fun spawnSendOutParticles(sendOutPosition: Vec3, sendOutOffset: Vec3, pokemon: Pokemon) {
            val client = Minecraft.getInstance()
            val soundPos = sendOutPosition.add(sendOutOffset)
            if (client.soundManager.availableSounds.contains(CobblemonSounds.POKE_BALL_SEND_OUT.location)) {
                client.level?.playSound(client.player, soundPos.x, soundPos.y, soundPos.z, CobblemonSounds.POKE_BALL_SEND_OUT, SoundSource.BLOCKS, 0.6f, 1f)
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

        fun drawPokemon(
            species: Species,
            aspects: Set<String>,
            pose: String,
            matrixStack: PoseStack,
            state: PoseableEntityState<PokemonEntity>? = null,
            headYaw: Float,
            headPitch: Float,
            bodyYaw: Float,
            xOffset: Float,
            yOffset: Float,
            zOffset: Float,
            partialTicks: Float,
            packedLight: Int
        ) {
            if (state != null) {
                state.updatePartialTicks(partialTicks)
            }
            val k = Mth.wrapDegrees(bodyYaw - headYaw)

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

            if (state == null) {
                model.setupAnimStateless(setOf(PoseType.SLEEP, PoseType.SLEEP), headPitch = headPitch, headYaw = headYaw)
            } else {
                val originalPose = state.currentPose

                model.getPose(pose)?.let { state.setPose(it.poseName) }
                state.timeEnteredPose = 0F

                model.setupAnimStateful(
                    entity = null,
                    state = state,
                    headYaw = k,
                    headPitch = headPitch,
                    limbSwing = 0F,
                    limbSwingAmount = 0F,
                    ageInTicks = state.animationSeconds * 20
                )
                originalPose?.let { state.setPose(it) }
            }

            matrixStack.pushPose()

            matrixStack.scale(.7f, .7f, .7f)
            matrixStack.translate(.75, 1.5, .75)
            matrixStack.mulPose(Axis.XP.rotationDegrees(180.0f));
            matrixStack.mulPose(Axis.YP.rotationDegrees(180.0f - bodyYaw))
            matrixStack.translate(xOffset, yOffset, zOffset)




            val immediate = Minecraft.getInstance().renderBuffers().bufferSource()
            val buffer = immediate.getBuffer(renderType)
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