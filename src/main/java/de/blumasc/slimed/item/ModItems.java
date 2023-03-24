package de.blumasc.slimed.item;

import de.blumasc.slimed.Slimed;
import de.blumasc.slimed.entity.ModEntityTypes;
import de.blumasc.slimed.item.custom.TreatItem;
import de.blumasc.slimed.item.custom.CageItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Slimed.MODID);
    public static final RegistryObject<Item> SLIMETREAT = ITEMS.register("slime_treat",
            () -> new TreatItem(new Item.Properties().tab(ModCreativeModeTab.SLIMED_TAB), EntityType.SLIME, ModEntityTypes.PEACEFUL_SLIME.get()));

    public static final RegistryObject<Item> PEACEFUL_SLIME_SPAWN_EGG = ITEMS.register("peaceful_slime_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PEACEFUL_SLIME, 0x29913e, 0x82bf8e,
                    new Item.Properties().tab(ModCreativeModeTab.SLIMED_TAB)));
    public static final RegistryObject<Item> LOVELY_SLIME_SPAWN_EGG = ITEMS.register("lovely_slime_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.LOVELY_SLIME, 0xdedad9, 0x630500,
                    new Item.Properties().tab(ModCreativeModeTab.SLIMED_TAB)));
    public static final RegistryObject<Item> SLIME_CAGE = ITEMS.register("slime_cage",
            () -> new CageItem(new Item.Properties().tab(ModCreativeModeTab.SLIMED_TAB)));
    public static void register(IEventBus eventbus){
        ITEMS.register(eventbus);
    }
}
