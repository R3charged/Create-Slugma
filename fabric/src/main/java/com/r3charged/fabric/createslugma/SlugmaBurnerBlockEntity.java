package com.r3charged.fabric.createslugma;


import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PokemonFloatingState;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;

import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SlugmaBurnerBlockEntity extends BlazeBurnerBlockEntity {

    public PoseableEntityState<PokemonEntity> pokemonState;
    public float oldXBodyRot;
    public float xBodyRot;
    protected LerpedFloat bodyAnimation;
    protected LerpedFloat bodyAngle;

    protected Pokemon pokemon;
    public SlugmaBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        pokemonState = new BlockPokemonState();
        bodyAnimation = LerpedFloat.linear();
        bodyAngle = LerpedFloat.angular();
        bodyAngle.startWithValue((AngleHelper.horizontalAngle(state.getOptionalValue(BlazeBurnerBlock.FACING)
                .orElse(Direction.SOUTH)) + 180) % 360);
    }

    @Override
    public void tick() {
        if (level.isClientSide()) {
            tickBodyAnim();
        }
        super.tick();

    }

    private void tickBodyAnim() {
        boolean active = getHeatLevelFromBlock().isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) && isValidBlockAbove();

        if (!active) {
            float target = 0;
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.isInvisible()) {
                double x;
                double z;
                if (isVirtual()) {
                    x = -4;
                    z = -10;
                } else {
                    x = player.getX();
                    z = player.getZ();
                }
                double dx = x - (getBlockPos().getX() + 0.5);
                double dz = z - (getBlockPos().getZ() + 0.5);
                target = AngleHelper.deg(-Mth.atan2(dz, dx)) - 90;
            }
            target = bodyAngle.getValue() + AngleHelper.getShortestAngleDiff(bodyAngle.getValue(), target);
            bodyAngle.chase(target, .125f, LerpedFloat.Chaser.exp(2.5));
            bodyAngle.tickChaser();
        } else {
            bodyAngle.chase((AngleHelper.horizontalAngle(getBlockState().getOptionalValue(BlazeBurnerBlock.FACING)
                    .orElse(Direction.SOUTH)) + 180) % 360, .0625f, LerpedFloat.Chaser.EXP);
            bodyAngle.tickChaser();
        }

        bodyAnimation.chase(active ? 1 : 0, .125f, LerpedFloat.Chaser.exp(.125f));
        bodyAnimation.tickChaser();
    }

    @Override
    public void read(CompoundTag tag, boolean clientPacket) {
        try {
            pokemon = Pokemon.Companion.loadFromNBT(tag.getCompound("pokemon"));
        } catch (NullPointerException e) {
            pokemon = NBTHelper.getDefaultSlugma();
        }
        super.read(tag, clientPacket);
    }


    @Override
    public void write(CompoundTag compound, boolean clientPacket) { //Save Additional, compound is gotten from BlockEntityTag
        NBTHelper.savePokemon(compound, this.pokemon);
        super.write(compound, clientPacket);
    }


    public Pokemon getPokemon() {
        return pokemon;
    }
    @Override
    public BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock() {
        return BlazeBurnerBlock.getHeatLevelOf(getBlockState());
    }

    protected void spawnParticles(BlazeBurnerBlock.HeatLevel heatLevel, double burstMult) {
        if (level == null)
            return;
        if (heatLevel == BlazeBurnerBlock.HeatLevel.NONE)
            return;

        RandomSource r = level.getRandom();

        Vec3 c = VecHelper.getCenterOf(worldPosition);
        Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
                .multiply(1, 0, 1));

        if (r.nextInt(4) != 0)
            return;

        boolean empty = level.getBlockState(worldPosition.above())
                .getCollisionShape(level, worldPosition.above())
                .isEmpty();
		/*
		if (empty || r.nextInt(8) == 0)
			level.addParticle(ParticleTypes.SMALL_FLAME, v.x, v.y, v.z, 0, 0, 0);
		 */

        double yMotion = empty ? .0625f : r.nextDouble() * .0125f;
        Vec3 v2 = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
                        .multiply(1, .25f, 1)
                        .normalize()
                        .scale((empty ? .25f : .5) + r.nextDouble() * .125f))
                .add(0, .5, 0);

        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING)) {
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, v2.x, v2.y, v2.z, 0, yMotion, 0);
        } else if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            level.addParticle(ParticleTypes.FLAME, v2.x, v2.y, v2.z, 0, yMotion, 0);
        }
    }

    protected LerpedFloat getHeadAnimation() {
        return headAnimation;
    }

    protected LerpedFloat getHeadAngle() {
        return headAngle;
    }




}
