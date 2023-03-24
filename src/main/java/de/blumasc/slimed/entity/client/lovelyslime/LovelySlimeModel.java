package de.blumasc.slimed.entity.client.lovelyslime;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.blumasc.slimed.Slimed;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class LovelySlimeModel<T extends Entity> extends EntityModel<T> {

    private final ModelPart root;

    public LovelySlimeModel(ModelPart p_170955_) {
        this.root = p_170955_.getChild("cube");
    }

    public static LayerDefinition createOuterBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public static LayerDefinition createInnerBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition cube = partdefinition.addOrReplaceChild("cube", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        cube.addOrReplaceChild("eye0", CubeListBuilder.create().texOffs(32, 0).addBox(-3.3F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

        cube.addOrReplaceChild("eye1", CubeListBuilder.create().texOffs(32, 4).addBox(1.3F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

        cube.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

        cube.addOrReplaceChild("heart", CubeListBuilder.create().texOffs(0, 24).addBox(-1.0F, 19.0F, 1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 21.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 20.0F, 0.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 18.0F, 1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 20.0F, -2.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 18.0F, -3.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 18.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 19.0F, -3.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 19.0F, -1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 17.0F, 1.0F, 2.0F, 1.0F, 2.0F)
                .texOffs(0, 24).addBox(-1.0F, 17.0F, -3.0F, 2.0F, 1.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }


    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }


    public ModelPart root() {
        return root;
    }


}
