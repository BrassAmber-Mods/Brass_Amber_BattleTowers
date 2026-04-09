package com.brass_amber.ba_bt.client.model.hostile;

import com.brass_amber.ba_bt.entity.hostile.FragmentOfObthuuryn;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Shulker;



public class FragmentOfObthuurynModel<T extends FragmentOfObthuuryn> extends ListModel<T> {
   private static final String LID = "lid";
   private static final String BASE = "base";
   private final ModelPart base;
   private final ModelPart lid;
   private final ModelPart head;

   public FragmentOfObthuurynModel(ModelPart p_170922_) {
      super(RenderType::entityCutoutNoCullZOffset);
      this.lid = p_170922_.getChild("lid");
      this.base = p_170922_.getChild("base");
      this.head = p_170922_.getChild("head");
   }

   public static LayerDefinition createBodyLayer() {
      MeshDefinition meshdefinition = new MeshDefinition();
      PartDefinition partdefinition = meshdefinition.getRoot();
      partdefinition.addOrReplaceChild("lid", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 12.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
      partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 8.0F, 16.0F), PartPose.offset(0.0F, 24.0F, 0.0F));
      partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 52).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.offset(0.0F, 12.0F, 0.0F));
      return LayerDefinition.create(meshdefinition, 64, 64);
   }

   public void setupAnim(T p_103735_, float p_103736_, float p_103737_, float p_103738_, float p_103739_, float p_103740_) {
   }

   public Iterable<ModelPart> parts() {
      return ImmutableList.of(this.base, this.lid);
   }

   public ModelPart getLid() {
      return this.lid;
   }

   public ModelPart getHead() {
      return this.head;
   }
}