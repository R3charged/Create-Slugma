package com.r3charged.fabric.createslugma;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class AllGameRules {

    public static void init() {}

    public static final GameRules.Key<GameRules.BooleanValue> allowWildSlugmaCaging =
            GameRuleRegistry.register("allowWildSlugmaCaging", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
}
