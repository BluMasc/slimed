package de.blumasc.slimed.block;

import de.blumasc.slimed.Slimed;
import de.blumasc.slimed.block.custom.CageBlock;
import de.blumasc.slimed.block.custom.SlimeLayerBlock;
import de.blumasc.slimed.item.ModCreativeModeTab;
import de.blumasc.slimed.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Slimed.MODID);


    public static final RegistryObject<Block> SLIME_CAGE = BLOCKS.register("slime_cage",
            () -> new CageBlock(BlockBehaviour.Properties.of(Material.METAL).instabreak()));
    public static final RegistryObject<Block> SLIME_LAYER = registerBlock("slime_layer",
            () -> new SlimeLayerBlock(BlockBehaviour.Properties.of(Material.FROGSPAWN).speedFactor(0.75f).sound(SoundType.SLIME_BLOCK)), ModCreativeModeTab.SLIMED_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab)
    {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name,toReturn,tab);

        return toReturn;
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab)
    {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }
    public static void register(IEventBus eventBus)
    {
        BLOCKS.register(eventBus);
    }
}
