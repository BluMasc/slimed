package de.blumasc.slimed.item.custom;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import de.blumasc.slimed.block.ModBlocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CageItem extends BlockItem {
    public CageItem(Properties p) {
        super(ModBlocks.SLIME_CAGE.get(), p.stacksTo(1));
    }
    public InteractionResult place(BlockPlaceContext context) {
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        Direction facing = context.getClickedFace();
        Level worldIn = context.getLevel();
        ItemStack stack = context.getItemInHand();
        release(player,pos,facing,worldIn,stack);
        return super.place(context);
    }
    public boolean release(Player player, BlockPos pos, Direction facing, Level worldIn, ItemStack stack) {
        if (player.getCommandSenderWorld().isClientSide) return false;
        if (!containsEntity(stack)) return false;
        LivingEntity entity = (LivingEntity) getEntityFromStack(stack, worldIn, true);
        BlockPos blockPos = pos.relative(facing);
        entity.absMoveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, 0, 0);
        List<BlockPos> blocks = new ArrayList<>();
        for (double y = 1; y < 5; ++y) {
            for (double x = -5; x < 5; ++x) {
                for (double z = -5; z < 5; ++z) {
                    if(x!=0 && z!=0) {
                        blocks.add(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                    }
                }
            }
        }
        Vec3 random = blockPosToVec3d(blocks.get(worldIn.random.nextInt(blocks.size())));
        int tries = 20;
        while (tries > 0 && !canEntitySpawn(worldIn, entity)) {
            random = blockPosToVec3d(blocks.get(worldIn.random.nextInt(blocks.size())));
            random = random.add(0.5, 0, 0.5);
            entity.moveTo(random.x, random.y, random.z, worldIn.random.nextFloat() * 360F, 0);
            --tries;
        }
        stack.setTag(null);
        worldIn.addFreshEntity(entity);
        return true;
    }
    private boolean canEntitySpawn(Level worldIn, LivingEntity living) {
        return worldIn.isUnobstructed(living) && (!worldIn.containsAnyLiquid(living.getBoundingBox()));
    }
    private Vec3 blockPosToVec3d(BlockPos blockPos) {
        return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if(containsEntity(stack)) {
            components.add(Component.literal("Mob: " + getID(stack)).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
            components.add(Component.literal("Mob: " + getSize(stack)).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
        else {
            components.add(Component.literal("Empty").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, level, components, flag);
    }
    public boolean containsEntity(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains("entity");
    }
    public String getID(ItemStack stack) {
        return stack.getTag().getString("entity");
    }
    public int getSize(ItemStack stack) {
        return stack.getTag().getInt("size")+1;
    }
    public Entity getEntityFromStack(ItemStack stack, Level world, boolean withInfo) {
        if (stack.hasTag()) {
            EntityType type = EntityType.byString(stack.getTag().getString("entity")).orElse(null);
            if (type != null ) {
                Entity entity = type.create(world);
                if (withInfo) {
                    entity.load(stack.getTag());
                } else if (!type.canSummon()) {
                    return null;
                }
                return entity;
            }
        }
        return null;
    }
}
