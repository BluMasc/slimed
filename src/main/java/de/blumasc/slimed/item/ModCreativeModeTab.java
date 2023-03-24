package de.blumasc.slimed.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab SLIMED_TAB = new CreativeModeTab("slimed") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.SLIMETREAT.get());
        }
    };
}
