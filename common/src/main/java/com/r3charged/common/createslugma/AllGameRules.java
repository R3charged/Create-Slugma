package com.r3charged.common.createslugma;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;

import static net.minecraft.world.level.GameRules.BooleanValue.*;

public class AllGameRules {

    public static void init() {}

    public static final GameRules.Key<GameRules.BooleanValue> allowWildSlugmaCaging =
            GameRules.register("allowWildSlugmaCaging", GameRules.Category.MOBS, BooleanValue.create(false));
}
