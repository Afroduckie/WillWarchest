package com.andrielgaming.agwarchest.util.render;

import java.util.Random;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.mobs.MoltenCreeperEntity;
import com.andrielgaming.agwarchest.util.models.MoltenCreeperChargeLayer;
import com.andrielgaming.agwarchest.util.models.MoltenCreeperModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MCRenderer extends MobRenderer<MoltenCreeperEntity, MoltenCreeperModel<MoltenCreeperEntity>>
{
	private static final ResourceLocation MCCREEPER_TEXTURES = new ResourceLocation(WarchestMaster.MOD_ID, "textures/entities/molten_creeper/molten_creeper.png");
	private final Random rnd = new Random();
	
	   public MCRenderer(EntityRendererManager renderManagerIn) 
	   {
	      super(renderManagerIn, new MoltenCreeperModel<MoltenCreeperEntity>(), 0.5F);
	      this.addLayer(new MoltenCreeperChargeLayer(this));
	   }

	   protected void preRenderCallback(MoltenCreeperEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) 
	   {
	      float f = entitylivingbaseIn.getMCreeperFlashIntensity(partialTickTime);
	      float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
	      f = MathHelper.clamp(f, 0.0F, 1.0F);
	      f = f * f;
	      f = f * f;
	      float f2 = (1.0F + f * 0.4F) * f1;
	      float f3 = (1.0F + f * 0.1F) / f1;
	      matrixStackIn.scale(f2, f3, f2);
	   }

	   @Override
	   public int getBlockLight(MoltenCreeperEntity entityIn, BlockPos partialTicks) 
	   {
		   return 100;
	   }

	   protected float getOverlayProgress(MoltenCreeperEntity livingEntityIn, float partialTicks) 
	   {
	      float f = livingEntityIn.getMCreeperFlashIntensity(partialTicks);
	      return (int)(f * 15.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
	   }
	   
	   public Vector3d getRenderOffset(MoltenCreeperEntity entityIn, float partialTicks) 
	   {
		      if (entityIn.isPrimed()) 
		      {
		         double d0 = 0.035D;
		         return new Vector3d(this.rnd.nextGaussian() * 0.022D, 0.0D, this.rnd.nextGaussian() * 0.022D);
		      } 
		      else 
		      {
		         return super.getRenderOffset(entityIn, partialTicks);
		      }
		}

	   public ResourceLocation getEntityTexture(MoltenCreeperEntity entity) 
	   {
	      return MCCREEPER_TEXTURES;
	   }
	   
	   public static class RenderFactory implements IRenderFactory<MoltenCreeperEntity>
	   {
			@Override
			public EntityRenderer<? super MoltenCreeperEntity> createRenderFor(EntityRendererManager manager)
			{
				return new MCRenderer(manager);
			}
	   }
}
