package de.blumasc.slimed.item.custom;

import de.blumasc.slimed.entity.BaseSlime;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;

public class TreatItem extends Item {
    private final EntityType mobF,mobT;
    public TreatItem(Properties properties, EntityType mob1, EntityType mob2) {
        super(properties);
        this.mobF = mob1;
        this.mobT = mob2;
    }
    @Override
    public InteractionResult interactLivingEntity(ItemStack item, Player player, LivingEntity entity, InteractionHand hand) {
        if (!feed(item, entity)) return InteractionResult.FAIL;
        player.swing(hand);
        item.shrink(1);
        return InteractionResult.CONSUME;
    }
    public boolean feed(ItemStack stack, LivingEntity target) {
        if (target.getCommandSenderWorld().isClientSide) return false;
        if (target instanceof Player || !target.canChangeDimensions() || !target.isAlive()) return false;
        if (target.getType() == this.mobF) {
            Level world = target.level;
            CompoundTag nbt = new CompoundTag();
            target.saveWithoutId(nbt);
            target.remove(Entity.RemovalReason.KILLED);
            Entity entity = mobT.create(world);
            entity.load(nbt);
            world.addFreshEntity(entity);
            ((BaseSlime)entity).setWorkTime(64);
            return true;
        }
        else
        {
            if (target instanceof BaseSlime)
            {
                ((BaseSlime)target).setWorkTime(64);
                return true;
            }
            return false;
        }

    }
}
