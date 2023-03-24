package de.blumasc.slimed.entity;

import de.blumasc.slimed.Slimed;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES=
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Slimed.MODID);

    public static final RegistryObject<EntityType<PeacefulSlime>> PEACEFUL_SLIME =
            ENTITY_TYPES.register("peaceful_slime",
                    () -> EntityType.Builder.of(PeacefulSlime::new, MobCategory.MISC).sized(2f,2f).build(new ResourceLocation(Slimed.MODID, "peaceful_slime").toString()));
    public static final RegistryObject<EntityType<LovelySlime>> LOVELY_SLIME =
            ENTITY_TYPES.register("lovely_slime",
                    () -> EntityType.Builder.of(LovelySlime::new, MobCategory.MISC).sized(2f,2f).build(new ResourceLocation(Slimed.MODID, "lovely_slime").toString()));
    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
