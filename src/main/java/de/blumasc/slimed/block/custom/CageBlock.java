package de.blumasc.slimed.block.custom;

import de.blumasc.slimed.config.SlimedCommonConfigs;
import de.blumasc.slimed.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import de.blumasc.slimed.item.ModItems;

import java.util.ArrayList;
import java.util.List;

public class CageBlock extends Block {
    public CageBlock(Properties properties) {
        super(properties);
    }
    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity)
    {
        if( entity instanceof LivingEntity livingEntity)
        {
            List<String> mobs = SlimedCommonConfigs.SLIMES.get();
            if (mobs.contains(EntityType.getKey(entity.getType()).toString())) {
                ItemStack mobcage = new ItemStack(ModItems.SLIME_CAGE.get());
                CompoundTag nbt = new CompoundTag();
                nbt.putString("entity", EntityType.getKey(entity.getType()).toString());
                entity.saveWithoutId(nbt);
                mobcage.setTag(nbt);
                entity.spawnAtLocation(mobcage, 0.0F);
                entity.playSound(SoundEvents.IRON_TRAPDOOR_CLOSE, 1.0F, 1.0F);
                entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
                level.destroyBlock(pos, false);
            }
        }
    }
}
