package de.blumasc.slimed.entity;

import com.google.common.annotations.VisibleForTesting;
import de.blumasc.slimed.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BaseSlime extends Mob {
    private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(BaseSlime.class, EntityDataSerializers.INT);
    public static final int MIN_SIZE = 1;
    public static final int MAX_SIZE = 127;

    protected int workTimes=0;
    public float targetSquish;
    public float squish;
    public float oSquish;
    private boolean wasOnGround;

    protected BaseSlime(EntityType<? extends BaseSlime> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new BaseSlime.SlimeMoveControl(this);
    }

    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    public void setWorkTime(int time)
    {
        this.workTimes = time;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BaseSlime.SlimeFloatGoal(this));
        this.goalSelector.addGoal(3, new BaseSlime.SlimeRandomDirectionGoal(this));
        this.goalSelector.addGoal(5, new BaseSlime.SlimeKeepOnJumpingGoal(this));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_SIZE, 1);
    }


    @VisibleForTesting
    public void setSize(int p_33594_, boolean p_33595_) {
        int i = Mth.clamp(p_33594_, 1, 127);
        this.entityData.set(ID_SIZE, i);
        this.reapplyPosition();
        this.refreshDimensions();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((double)(i * i));
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)(0.2F + 0.1F * (float)i));
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((double)i);
        if (p_33595_) {
            this.setHealth(this.getMaxHealth());
        }

        this.xpReward = i;
    }
    public static AttributeSupplier registerAttributes() {
        return Monster.createMonsterAttributes().build();
    }

    public int getSize() {
        return this.entityData.get(ID_SIZE);
    }

    public void addAdditionalSaveData(CompoundTag p_33619_) {
        super.addAdditionalSaveData(p_33619_);
        p_33619_.putInt("Size", this.getSize() - 1);
        p_33619_.putBoolean("wasOnGround", this.wasOnGround);
        p_33619_.putInt("WorkTime", this.workTimes);

    }

    public void readAdditionalSaveData(CompoundTag p_33607_) {
        this.setSize(p_33607_.getInt("Size") + 1, false);
        super.readAdditionalSaveData(p_33607_);
        this.wasOnGround = p_33607_.getBoolean("wasOnGround");
        this.workTimes = p_33607_.getInt("WorkTime");
    }

    public boolean isTiny() {
        return this.getSize() <= 1;
    }

    protected ParticleOptions getParticleType() {
        return ParticleTypes.ITEM_SLIME;
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        if (this.onGround && !this.wasOnGround) {
            int i = this.getSize();

            if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
            for(int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * (float)i * 0.5F * f1;
                float f3 = Mth.cos(f) * (float)i * 0.5F * f1;
                this.level.addParticle(this.getParticleType(), this.getX() + (double)f2, this.getY(), this.getZ() + (double)f3, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetSquish = -0.5F;
        } else if (!this.onGround && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }

        this.wasOnGround = this.onGround;
        this.decreaseSquish();
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> p_33609_) {
        if (ID_SIZE.equals(p_33609_)) {
            this.refreshDimensions();
            this.setYRot(this.yHeadRot);
            this.yBodyRot = this.yHeadRot;
            if (this.isInWater() && this.random.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }

        super.onSyncedDataUpdated(p_33609_);
    }

    public EntityType<? extends BaseSlime> getType() {
        return (EntityType<? extends BaseSlime>)super.getType();
    }

    public void remove(RemovalReason p_149847_) {
        int i = this.getSize();
        if (!this.level.isClientSide && i > 1 && this.isDeadOrDying()) {
            Component component = this.getCustomName();
            boolean flag = this.isNoAi();
            float f = (float)i / 4.0F;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);

            for(int l = 0; l < k; ++l) {
                float f1 = ((float)(l % 2) - 0.5F) * f;
                float f2 = ((float)(l / 2) - 0.5F) * f;
                BaseSlime slime = this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    slime.setPersistenceRequired();
                }

                slime.setCustomName(component);
                slime.setNoAi(flag);
                slime.setInvulnerable(this.isInvulnerable());
                slime.setSize(j, true);
                slime.moveTo(this.getX() + (double)f1, this.getY() + 0.5D, this.getZ() + (double)f2, this.random.nextFloat() * 360.0F, 0.0F);
                this.level.addFreshEntity(slime);
            }
        }

        super.remove(p_149847_);
    }

    protected float getStandingEyeHeight(Pose p_33614_, EntityDimensions p_33615_) {
        return 0.625F * p_33615_.height;
    }

    protected SoundEvent getHurtSound(DamageSource p_33631_) {
        return this.isTiny() ? SoundEvents.SLIME_HURT_SMALL : SoundEvents.SLIME_HURT;
    }

    protected SoundEvent getDeathSound() {
        return this.isTiny() ? SoundEvents.SLIME_DEATH_SMALL : SoundEvents.SLIME_DEATH;
    }

    protected SoundEvent getSquishSound() {
        return this.isTiny() ? SoundEvents.SLIME_SQUISH_SMALL : SoundEvents.SLIME_SQUISH;
    }

    protected float getSoundVolume() {
        return 0.4F * (float)this.getSize();
    }

    public int getMaxHeadXRot() {
        return 0;
    }

    protected boolean doPlayJumpSound() {
        return this.getSize() > 0;
    }

    protected void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, (double)this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_33601_, DifficultyInstance p_33602_, MobSpawnType p_33603_, @Nullable SpawnGroupData p_33604_, @Nullable CompoundTag p_33605_) {
        RandomSource randomsource = p_33601_.getRandom();
        int i = randomsource.nextInt(3);
        if (i < 2 && randomsource.nextFloat() < 0.5F * p_33602_.getSpecialMultiplier()) {
            ++i;
        }

        int j = 1 << i;
        this.setSize(j, true);
        return super.finalizeSpawn(p_33601_, p_33602_, p_33603_, p_33604_, p_33605_);
    }

    float getSoundPitch() {
        float f = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    protected SoundEvent getJumpSound() {
        return this.isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
    }

    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale(0.255F * (float)this.getSize());
    }

    /**
     * Called when the slime spawns particles on landing, see onUpdate.
     * Return true to prevent the spawning of the default particles.
     */
    protected boolean spawnCustomParticles() { return false; }

    static class SlimeFloatGoal extends Goal {
        private final BaseSlime slime;

        public SlimeFloatGoal(BaseSlime p_33655_) {
            this.slime = p_33655_;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            p_33655_.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof BaseSlime.SlimeMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            ((BaseSlime.SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.2D);
        }
    }

    static class SlimeKeepOnJumpingGoal extends Goal {
        private final BaseSlime slime;

        public SlimeKeepOnJumpingGoal(BaseSlime p_33660_) {
            this.slime = p_33660_;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        public void tick() {
            ((BaseSlime.SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.0D);
        }
    }

    static class SlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final BaseSlime slime;
        private boolean isAggressive;

        public SlimeMoveControl(BaseSlime p_33668_) {
            super(p_33668_);
            this.slime = p_33668_;
            this.yRot = 180.0F * p_33668_.getYRot() / (float)Math.PI;
        }

        public void setDirection(float p_33673_, boolean p_33674_) {
            this.yRot = p_33673_;
            this.isAggressive = p_33674_;
        }

        public void setWantedMovement(double p_33671_) {
            this.speedModifier = p_33671_;
            this.operation = Operation.MOVE_TO;
        }

        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.mob.isOnGround()) {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpControl().jump();
                        if (this.slime.doPlayJumpSound()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
                        }
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }

            }
        }
    }
    static class SlimeAttackGoal extends Goal {
        private final BaseSlime slime;
        private int growTiredTimer;

        public SlimeAttackGoal(BaseSlime p_33648_) {
            this.slime = p_33648_;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else {
                return !this.slime.canAttack(livingentity) ? false : this.slime.getMoveControl() instanceof BaseSlime.SlimeMoveControl;
            }
        }

        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        public boolean canContinueToUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!this.slime.canAttack(livingentity)) {
                return false;
            } else {
                return --this.growTiredTimer > 0;
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity != null) {
                this.slime.lookAt(livingentity, 10.0F, 10.0F);
            }

            ((BaseSlime.SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
        }
    }

    public boolean isDealsDamage() {
        return !this.isTiny() && this.isEffectiveAi();
    }

    static class SlimeRandomDirectionGoal extends Goal {
        private final BaseSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public SlimeRandomDirectionGoal(BaseSlime p_33679_) {
            this.slime = p_33679_;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return this.slime.getTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof BaseSlime.SlimeMoveControl;
        }

        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
            }

            ((BaseSlime.SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
        }
    }
}
