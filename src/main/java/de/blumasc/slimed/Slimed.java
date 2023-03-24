package de.blumasc.slimed;

import com.mojang.logging.LogUtils;
import de.blumasc.slimed.block.ModBlocks;
import de.blumasc.slimed.config.SlimedCommonConfigs;
import de.blumasc.slimed.entity.ModEntityTypes;
import de.blumasc.slimed.entity.client.peacefulslime.PeacefulSlimeRenderer;
import de.blumasc.slimed.entity.client.lovelyslime.LovelySlimeRenderer;
import de.blumasc.slimed.item.ModItems;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Slimed.MODID)
public class Slimed
{
    public static final String MODID = "slimed";
    private static final Logger LOGGER = LogUtils.getLogger();
    public Slimed()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SlimedCommonConfigs.SPEC, "slimed.toml");

        ModEntityTypes.register(modEventBus);

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntityTypes.PEACEFUL_SLIME.get(), PeacefulSlimeRenderer::new);
            EntityRenderers.register(ModEntityTypes.LOVELY_SLIME.get(), LovelySlimeRenderer::new);

        }
    }
}
