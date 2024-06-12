package com.r3charged.fabric.createslugma;

import javax.annotation.Nullable;

import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;

import com.simibubi.create.foundation.block.render.SpriteShifter;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

import static com.r3charged.fabric.createslugma.AllPartialModels.*;

public class SlugmaBurnerRenderer extends SafeBlockEntityRenderer<SlugmaBurnerBlockEntity> {

    public static final SpriteShiftEntry SUPER_SLUGMA_BURNER_FLAME = get("block/blaze_burner_flame", "block/slugma_burner_flame_superheated_scroll"),
            SHINY_SLUGMA_BURNER_FLAME = get("block/blaze_burner_flame", "block/slugma_burner_flame_superheated_shiny_scroll");


    public static void registerPartialModels() {}
    public SlugmaBurnerRenderer(BlockEntityRendererProvider.Context context) {
    }
    @Override
    protected void renderSafe(SlugmaBurnerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource,
                              int light, int overlay) {
        HeatLevel heatLevel = be.getHeatLevelFromBlock();
        if (heatLevel == HeatLevel.NONE)
            return;
        Level level = be.getLevel();
        BlockState blockState = be.getBlockState();
        float animation = be.getHeadAnimation().getValue(partialTicks) * .175f;
        float horizontalAngle = AngleHelper.rad(be.getHeadAngle().getValue(partialTicks));
        boolean canDrawFlame = heatLevel.isAtLeast(HeatLevel.FADING);
        boolean drawGoggles = false;
        int hashCode = be.hashCode();


        renderShared(ms, null, bufferSource,
                level, blockState, heatLevel, animation, horizontalAngle,
                canDrawFlame, hashCode, be.pokemonState, be.getPokemon(), partialTicks, be.bodyAngle.getValue(partialTicks));
    }

    public static void renderInContraption(MovementContext context, VirtualRenderWorld renderWorld,
                                           ContraptionMatrices matrices, MultiBufferSource bufferSource, LerpedFloat headAngle, boolean conductor) {
        BlockState state = context.state;
        HeatLevel heatLevel = BlazeBurnerBlock.getHeatLevelOf(state);
        if (heatLevel == HeatLevel.NONE)
            return;

        if (!heatLevel.isAtLeast(HeatLevel.FADING)) {
            heatLevel = HeatLevel.FADING;
        }

        Level level = context.world;
        float horizontalAngle = AngleHelper.rad(headAngle.getValue(AnimationTickHolder.getPartialTicks(level)));
        boolean drawGoggles = context.blockEntityData.contains("Goggles");
        boolean drawHat = conductor || context.blockEntityData.contains("TrainHat");
        int hashCode = context.hashCode();

        renderShared(matrices.getViewProjection(), matrices.getModel(), bufferSource,
                level, state, heatLevel, 0, horizontalAngle,
                false, hashCode, null, NBTHelper.getDefaultSlugma(), 0, horizontalAngle);
    }

    private static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource,
                                     Level level, BlockState blockState, HeatLevel heatLevel, float animation, float horizontalAngle,
                                     boolean canDrawFlame, int hashCode, PoseableEntityState<PokemonEntity> state, Pokemon pokemon, float partialTicks, float bodyAngle) {

        boolean blockAbove = animation > 0.125f;
        float time = AnimationTickHolder.getRenderTime(level);

        float headPitch = 0;
        String pose = "standing";
        String aspect = "burner";
        if (heatLevel.isAtLeast(HeatLevel.SEETHING)) {
            aspect = "super_heated";
            if (blockAbove) {
                pose = "active";
            }
        } else if (heatLevel.isAtLeast(HeatLevel.FADING)) {
            if (blockAbove) {
                pose = "active";
            }
        } else {
            pose = "sleep";
            headPitch = 45;
        }
        Set<String> aspects = new HashSet<>();
        aspects.addAll(pokemon.getAspects());
        aspects.add(aspect);
        boolean isShiny = aspects.contains("shiny");
        Vector3f light1 = new Vector3f(0.2F, 1.0F, -1.0F);
        Vector3f light2 = new Vector3f(0.1F, 0.0F, 8.0F);
        RenderSystem.setShaderLights(light1, light2);
        CobblemonUtils.Companion.drawPokemon(pokemon.getSpecies(), aspects, pose, ms, state,
                AngleHelper.deg(horizontalAngle),headPitch, bodyAngle,
                0,-.1f, .2f,
                partialTicks, LightTexture.FULL_BRIGHT);





        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());

        ms.pushPose();

        //SEETHING FLAMES RENDER STEP---------------------

        if (canDrawFlame && blockAbove) {
            SpriteShiftEntry spriteShift =
                    heatLevel == HeatLevel.SEETHING ? SUPER_SLUGMA_BURNER_FLAME : AllSpriteShifts.BURNER_FLAME;
            if (isShiny) {
                spriteShift =
                        heatLevel == HeatLevel.SEETHING ? AllSpriteShifts.SUPER_BURNER_FLAME : SHINY_SLUGMA_BURNER_FLAME;
            }


            float spriteWidth = spriteShift.getTarget()
                    .getU1()
                    - spriteShift.getTarget()
                    .getU0();

            float spriteHeight = spriteShift.getTarget()
                    .getV1()
                    - spriteShift.getTarget()
                    .getV0();

            float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

            double vScroll = speed * time;
            vScroll = vScroll - Math.floor(vScroll);
            vScroll = vScroll * spriteHeight / 2;

            double uScroll = speed * time / 2;
            uScroll = uScroll - Math.floor(uScroll);
            uScroll = uScroll * spriteWidth / 2;

            SuperByteBuffer flameBuffer = CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, blockState);
            if (modelTransform != null)
                flameBuffer.transform(modelTransform);
            flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);
            draw(flameBuffer, horizontalAngle, ms, cutout);
        }

        // -------------------------------

        // RENDER SLUGMA -------------------------------------------
        PartialModel blazeModel;
        if (isShiny) {
            blazeModel = heatLevel == HeatLevel.SEETHING ? BASE_SUPERHEATED_SHINY : BASE_SHINY ;
        } else {
            blazeModel = heatLevel == HeatLevel.SEETHING ? BASE_SUPERHEATED : BASE;
        }

        SuperByteBuffer blazeBuffer = CachedBufferer.partial(blazeModel, blockState);
        if (modelTransform != null)
            blazeBuffer.transform(modelTransform);
        blazeBuffer.translate(0, .251, 0);
        draw(blazeBuffer, 0, ms, solid);
        // RENDER SLUGMA -------------------------------------------


        ms.popPose();
    }

    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(Direction.UP, horizontalAngle)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc);
    }

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(Create.asResource(originalLocation), asResource(targetLocation));
    }
    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(CreateSlugma.ID, path);
    }
}
