package de.blumasc.slimed.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlimedCommonConfigs {

    public static final List<String> defaultList = Arrays.asList("minecraft:slime", "minecraft:magma_cube","slimed:peaceful_slime","slimed:lovely_slime");;

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<String>> SLIMES;


    static {
        BUILDER.push("Config for Slimed");

        SLIMES = BUILDER.comment("List of supported slime entities").define("Slimes", defaultList);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
