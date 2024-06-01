package com.r3charged.fabric.createslugma

import com.cobblemon.mod.common.api.scheduling.SchedulingTracker
import com.cobblemon.mod.common.client.render.models.blockbench.PoseableEntityState
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity

class BlockPokemonState(startAge: Int = -1, startPartialTicks: Float = 0F) : PoseableEntityState<PokemonEntity>() {

    var totalPartialTicks = 0F
    init {
        // generate phase offset if new
        age = if (startAge >= 0) startAge else (200F * Math.random()).toInt()
        currentPartialTicks = if(startAge > 0f) startPartialTicks else 0F
    }
    override fun getEntity() = null

    fun peekAge() : Int { // Need to find a way to do this with getAge that's not protected
        return this.age
    }

    override fun updatePartialTicks(partialTicks: Float) {
        currentPartialTicks += partialTicks / 2
        totalPartialTicks += partialTicks / 2
    }

    override val schedulingTracker = SchedulingTracker()


}