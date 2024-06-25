package com.r3charged.common.createslugma;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.world.level.GameRules;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CreateSlugmaImplementation {

    public static CreateSlugmaImplementation instance;
    public static final String MOD_ID = "createslugma";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static final Logger LOGGER = LogManager.getLogger("CreateSlugma");


    public void initialize() {
        AllBlocks.register();
        AllBlockEntities.register();
        AllGameRules.init();

    }

}
