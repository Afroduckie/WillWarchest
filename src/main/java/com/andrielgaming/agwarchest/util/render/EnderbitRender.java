package com.andrielgaming.agwarchest.util.render;

import java.util.Random;
import com.andrielgaming.agwarchest.entities.mobs.Enderbit;
import com.andrielgaming.agwarchest.util.models.EnderbitEyesLayer;
import com.andrielgaming.agwarchest.util.models.EnderbitModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderbitRender extends MobRenderer<Enderbit, EnderbitModel<Enderbit>>
{
	private static final ResourceLocation ENDERBIT_TEXTURES = new ResourceLocation("textures/entities/enderbit/enderbit.png");
	private final Random rnd = new Random();

	public EnderbitRender(EntityRendererManager renderManagerIn)
	{
		super(renderManagerIn, new EnderbitModel<>(0.0F), 0.5F);
		this.addLayer(new EnderbitEyesLayer<>(this));
		this.addLayer(new EnderbitHeldBlockLayer(this));
	}

	public void render(Enderbit entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		BlockState blockstate = entityIn.getHeldBlockState();
		EnderbitModel<Enderbit> endermanmodel = this.getEntityModel();
		endermanmodel.isCarrying = blockstate != null;
		endermanmodel.isAttacking = entityIn.isScreaming();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	public Vector3d getRenderOffset(Enderbit entityIn, float partialTicks)
	{
		if(entityIn.isScreaming())
		{
			double d0 = 0.02D;
			return new Vector3d(this.rnd.nextGaussian() * 0.02D, 0.0D, this.rnd.nextGaussian() * 0.02D);
		}
		else
		{
			return super.getRenderOffset(entityIn, partialTicks);
		}
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	public ResourceLocation getEntityTexture(Enderbit entity)
	{ return ENDERBIT_TEXTURES; }
}
