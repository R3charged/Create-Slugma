package com.r3charged.common.createslugma;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class AllGameRules {

    public static void init() {}

    public static final GameRules.Key<GameRules.BooleanValue> allowWildSlugmaCaging =
            CreateSlugmaImplementation.instance.registerGameRule("allowWildSlugmaCaging", GameRules.Category.MOBS, GameRuleFactory.createBooleanRule(false));
}
