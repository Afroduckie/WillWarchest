package com.andrielgaming.agwarchest.util.render;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import com.andrielgaming.agwarchest.init.BlockInit;
import com.andrielgaming.agwarchest.util.models.PrimedC4Model;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class C4EntityRender extends EntityRenderer<PrimedC4>
{
	private static final Logger LOGGER = LogManager.getLogger();
	private static final ResourceLocation PRIMED_C4_MODEL = new ResourceLocation(WarchestMaster.MOD_ID, "textures/entities/primed_c4/c4_dummy_entity");
	private final ModelRenderer bone;

	   public C4EntityRender(EntityRendererManager renderManagerIn) 
	   {
	      super(renderManagerIn);
	      AtlasTexture c4t = new AtlasTexture(PRIMED_C4_MODEL);
	      this.shadowSize = 0.5F;
	      bone = new ModelRenderer(new PrimedC4Model());
	      LOGGER.info("C4 Entity Renderer initialized success! ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	   }

	   @OnlyIn(Dist.CLIENT)
	   public void render(PrimedC4 entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) 
	   {
	      matrixStackIn.push();
	      matrixStackIn.translate(0.0D, 0.5D, 0.0D);
	      if ((float)entityIn.getFuse() - partialTicks + 1.0F < 10.0F) 
	      {
	         float f = 1.0F - ((float)entityIn.getFuse() - partialTicks + 1.0F) / 10.0F;
	         f = MathHelper.clamp(f, 0.0F, 1.0F);
	         f = f * f;
	         f = f * f;
	         float f1 = 1.0F + f * 0.3F;
	         matrixStackIn.scale(f1, f1, f1);
	      }
	      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
	      matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
	      matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
	      renderFlash(BlockInit.C4_BLOCK.get().getDefaultState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getFuse() / 5 % 2 == 0);
	      matrixStackIn.pop();
	      super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	   }
	   
	   public static void renderFlash(BlockState blockStateIn, MatrixStack matrixStackIn, IRenderTypeBuffer renderTypeBuffer, int combinedLight, boolean doFullBright) 
	   {
		      int i;
		      if (doFullBright) 
		      {
		         i = OverlayTexture.getPackedUV(OverlayTexture.getU(2.0F), 20);
		      } else {
		         i = OverlayTexture.NO_OVERLAY;
		      }

		      Minecraft.getInstance().getBlockRendererDispatcher().renderBlock(blockStateIn, matrixStackIn, renderTypeBuffer, combinedLight, i);
		   }
	   
	   public static class RenderFactory implements IRenderFactory<PrimedC4>
	   {
			@Override
			public EntityRenderer<? super PrimedC4> createRenderFor(EntityRendererManager manager)
			{
				return new C4EntityRender(manager);
			}
	   }
	   
	   @Nullable
	   @Override
	   public ResourceLocation getEntityTexture(PrimedC4 entity) 
	   {
		  //LOGGER.info("C4 Entity Model fetched ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	      return PRIMED_C4_MODEL;
	   }

	    //@Override
		public EntityRenderer<? super PrimedC4> createRenderFor(EntityRendererManager manager)
		{
			return new C4EntityRender(manager);
		}
}