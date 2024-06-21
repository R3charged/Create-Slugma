package com.r3charged.common.createslugma.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BlazeBurnerBlockItem.class)
public interface BlazeBurnerBlockItemAccessor {

    @Invoker("spawnCaptureEffects")
    void invokeSpawnCaptureEffects(Level world, Vec3 vec);

}
