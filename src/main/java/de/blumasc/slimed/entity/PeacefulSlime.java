package de.blumasc.slimed.entity;

import de.blumasc.slimed.block.ModBlocks;
import de.blumasc.slimed.block.custom.SlimeLayerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class PeacefulSlime extends BaseSlime {

    protected PeacefulSlime(EntityType<? extends PeacefulSlime> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new PeacefulSlime.SlimeMoveControl(this);
    }

    public void push(Entity p_33636_) {
        super.push(p_33636_);
        if (p_33636_ instanceof Chicken) {
            this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.playSound(SoundEvents.PLAYER_BURP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            p_33636_.playSound(SoundEvents.CHICKEN_DEATH, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            p_33636_.remove(RemovalReason.KILLED);
            int i=8;
            for(int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * (float)i * 0.5F * f1;
                float f3 = Mth.cos(f) * (float)i * 0.5F * f1;
                this.level.addParticle(ParticleTypes.SMOKE, p_33636_.getX() + (double)f2, p_33636_.getY(), p_33636_.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            this.workTimes=32;
        }

    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BaseSlime.SlimeFloatGoal(this));
        this.goalSelector.addGoal(2, new BaseSlime.SlimeAttackGoal(this));
        this.goalSelector.addGoal(3, new BaseSlime.SlimeRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new BaseSlime.SlimeKeepOnJumpingGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Chicken.class, 10, true, false, (p_33641_) -> {
            return Math.abs(p_33641_.getY() - this.getY()) <= 4.0D;
        }));
    }



    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            int i = Mth.floor(this.getX());
            int j = Mth.floor(this.getY());
            int k = Mth.floor(this.getZ());
            BlockPos blockpos = new BlockPos(i, j, k);
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                return;
            }
            if(workTimes > 0) {

                BlockState blockstate = ModBlocks.SLIME_LAYER.get().defaultBlockState();

                for (int l = 0; l < 4; ++l) {
                    i = Mth.floor(this.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
                    j = Mth.floor(this.getY());
                    k = Mth.floor(this.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
                    BlockPos blockpos1 = new BlockPos(i, j, k);
                    if (this.level.isEmptyBlock(blockpos1) && blockstate.canSurvive(this.level, blockpos1)) {
                        this.level.setBlockAndUpdate(blockpos1, blockstate);
                        this.level.gameEvent(GameEvent.BLOCK_PLACE, blockpos1, GameEvent.Context.of(this, blockstate));
                        this.workTimes--;
                    } else if (this.level.getBlockState(blockpos1).is(blockstate.getBlock())) {

                        if (this.jumping) {
                            int size = this.level.getBlockState(blockpos1).getValue(SlimeLayerBlock.LAYERS);
                            int newsize = size + (Math.round(random.nextInt(0, 18) / (10 + size)));
                            if (newsize>size) {
                                BlockState higherslime = this.level.getBlockState(blockpos1).setValue(SlimeLayerBlock.LAYERS, Math.min(newsize, 8));
                                this.level.setBlockAndUpdate(blockpos1, higherslime);
                                this.level.gameEvent(GameEvent.BLOCK_PLACE, blockpos1, GameEvent.Context.of(this, higherslime));
                                this.workTimes--;
                            }
                        }
                    }
                }
            }
        }
    }
}
