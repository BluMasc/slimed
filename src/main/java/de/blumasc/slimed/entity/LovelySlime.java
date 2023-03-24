package de.blumasc.slimed.entity;


import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class LovelySlime extends BaseSlime {


    protected LovelySlime(EntityType<? extends LovelySlime> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new LovelySlime.SlimeMoveControl(this);
    }

    protected ParticleOptions getParticleType() {
        if(workTimes > 0){return ParticleTypes.HEART;}
        return ParticleTypes.ITEM_SNOWBALL;
    }
}