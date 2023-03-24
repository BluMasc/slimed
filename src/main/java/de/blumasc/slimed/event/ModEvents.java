package de.blumasc.slimed.event;

import de.blumasc.slimed.Slimed;
import de.blumasc.slimed.entity.ModEntityTypes;
import de.blumasc.slimed.entity.PeacefulSlime;
import de.blumasc.slimed.entity.client.lovelyslime.LovelySlimeModel;
import de.blumasc.slimed.entity.client.lovelyslime.LovelySlimeRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = Slimed.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents{
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event){
            event.put(ModEntityTypes.PEACEFUL_SLIME.get(), PeacefulSlime.registerAttributes());
            event.put(ModEntityTypes.LOVELY_SLIME.get(), PeacefulSlime.registerAttributes());
        }
        @SubscribeEvent
        public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(LovelySlimeRenderer.LOVELY_SLIME_INNER, LovelySlimeModel::createInnerBodyLayer);
            event.registerLayerDefinition(LovelySlimeRenderer.LOVELY_SLIME_OUTER,  LovelySlimeModel::createOuterBodyLayer);

        }
    }
}
