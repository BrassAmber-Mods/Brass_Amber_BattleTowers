package com.brass_amber.ba_bt.client.renderer;

import com.brass_amber.ba_bt.BABattleTowers;
import com.brass_amber.ba_bt.client.model.hostile.FragmentOfObthuurynModel;
import com.brass_amber.ba_bt.entity.hostile.FragmentOfObthuuryn;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FragmentOfObthuurynRenderer extends MobRenderer<FragmentOfObthuuryn, FragmentOfObthuurynModel<FragmentOfObthuuryn>> {
   private static final ResourceLocation DEFAULT_TEXTURE_LOCATION = BABattleTowers.locate("textures/entity/fragment_of_obthuuryn.png");
   public static final ModelLayerLocation TEXTURE = new ModelLayerLocation(DEFAULT_TEXTURE_LOCATION, "main");

   public FragmentOfObthuurynRenderer(EntityRendererProvider.Context p_174370_) {
      super(p_174370_, new FragmentOfObthuurynModel<>(p_174370_.bakeLayer(TEXTURE)), 0.0F);
   }

   public Vec3 getRenderOffset(FragmentOfObthuuryn p_115904_, float p_115905_) {
      return p_115904_.getRenderPosition(p_115905_).orElse(super.getRenderOffset(p_115904_, p_115905_));
   }

   public boolean shouldRender(FragmentOfObthuuryn p_115913_, Frustum p_115914_, double p_115915_, double p_115916_, double p_115917_) {
      return super.shouldRender(p_115913_, p_115914_, p_115915_, p_115916_, p_115917_) ? true : p_115913_.getRenderPosition(0.0F).filter((p_174374_) -> {
         EntityType<?> entitytype = p_115913_.getType();
         float f = entitytype.getHeight() / 2.0F;
         float f1 = entitytype.getWidth() / 2.0F;
         Vec3 vec3 = Vec3.atBottomCenterOf(p_115913_.blockPosition());
         return p_115914_.isVisible((new AABB(p_174374_.x, p_174374_.y + (double)f, p_174374_.z, vec3.x, vec3.y + (double)f, vec3.z)).inflate((double)f1, (double)f, (double)f1));
      }).isPresent();
   }

   public ResourceLocation getTextureLocation(FragmentOfObthuuryn p_115902_) {
      return DEFAULT_TEXTURE_LOCATION;
   }

   protected void setupRotations(FragmentOfObthuuryn p_115907_, PoseStack p_115908_, float p_115909_, float p_115910_, float p_115911_) {
      super.setupRotations(p_115907_, p_115908_, p_115909_, p_115910_ + 180.0F, p_115911_);
      p_115908_.translate(0.0D, 0.5D, 0.0D);
      p_115908_.mulPose(p_115907_.getAttachFace().getOpposite().getRotation());
      p_115908_.translate(0.0D, -0.5D, 0.0D);
   }
}