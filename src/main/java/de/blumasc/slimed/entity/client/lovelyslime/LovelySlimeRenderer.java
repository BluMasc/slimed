package de.blumasc.slimed.entity.client.lovelyslime;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import de.blumasc.slimed.Slimed;
import de.blumasc.slimed.entity.LovelySlime;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class LovelySlimeRenderer extends MobRenderer<LovelySlime, LovelySlimeModel<LovelySlime>> {
    public static final ModelLayerLocation LOVELY_SLIME_INNER = new ModelLayerLocation(new ResourceLocation(Slimed.MODID, "lovely_slime"), "main");
    public static final ModelLayerLocation LOVELY_SLIME_OUTER = new ModelLayerLocation(new ResourceLocation(Slimed.MODID, "lovely_slime"), "outer");
    private static final ResourceLocation SLIME_LOCATION = new ResourceLocation(Slimed.MODID, "textures/entity/slime/lovely_slime.png");

    public LovelySlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new LovelySlimeModel<>(context.bakeLayer(LOVELY_SLIME_INNER)), 0.25F);
        this.addLayer(new LovelySlimeOuterLayer<>(this, context.getModelSet()));
    }

    public void render(LovelySlime p_115976_, float p_115977_, float p_115978_, PoseStack p_115979_, MultiBufferSource p_115980_, int p_115981_) {
        this.shadowRadius = 0.25F * (float)p_115976_.getSize();
        super.render(p_115976_, p_115977_, p_115978_, p_115979_, p_115980_, p_115981_);
    }

    protected void scale(LovelySlime p_115983_, PoseStack p_115984_, float p_115985_) {
        float f = 0.999F;
        p_115984_.scale(0.999F, 0.999F, 0.999F);
        p_115984_.translate(0.0D, (double)0.001F, 0.0D);
        float f1 = (float)p_115983_.getSize();
        float f2 = Mth.lerp(p_115985_, p_115983_.oSquish, p_115983_.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        p_115984_.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    public ResourceLocation getTextureLocation(LovelySlime p_115974_) {
        return SLIME_LOCATION;
    }
    //registry list

}
