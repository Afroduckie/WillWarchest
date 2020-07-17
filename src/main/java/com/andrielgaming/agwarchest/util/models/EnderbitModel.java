package com.andrielgaming.agwarchest.util.models;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderbitModel<T extends LivingEntity> extends BipedModel<T>
{
	public boolean isCarrying;
	public boolean isAttacking;
	private final ModelRenderer secondRightArm;
	private final ModelRenderer secondLeftArm;

	public EnderbitModel(float scale)
	{
		super(0.0F, -14.0F, 64, 32);
		float f = -14.0F;
		this.bipedBody = new ModelRenderer(this);
		this.bipedBody.setRotationPoint(0.0F, -14.0F, 0.0F);
		this.bipedBody.setTextureOffset(36, 19).addBox(-4.0F, 12.0F, -3.0F, 8.0F, 7.0F, 6.0F, scale);

		this.bipedHead = new ModelRenderer(this);
		this.bipedHead.setRotationPoint(0.0F, -14.0F, 0.0F);
		this.bipedHead.setTextureOffset(0, 0).addBox(-4.0F, 4.5F, -4.0F, 8.0F, 8.0F, 8.0F, (scale - 0.75F));

		this.bipedHeadwear = new ModelRenderer(this);
		this.bipedHeadwear.setRotationPoint(0.0F, -14.0F, 0.0F);
		this.bipedHeadwear.setTextureOffset(0, 16).addBox(-4.0F, 4.5F, -4.0F, 4.0F, 4.0F, 4.0F, scale + 0.95F);

		this.bipedRightArm = new ModelRenderer(this);
		this.bipedRightArm.setRotationPoint(-1.0F, 2.0F, 0.0F);
		this.bipedRightArm.setTextureOffset(56, 0).addBox(-1.0F, 10.0F, -1.0F, 2.0F, 9.0F, 2.0F, scale);
		
		this.secondRightArm = new ModelRenderer(this);
		this.secondRightArm.setRotationPoint(-1.0F, 1.0F, 0.0F);
		this.secondRightArm.setTextureOffset(56, 0).addBox(-1.0F, 8.0F, -1.0F, 2.0F, 9.0F, 2.0F, scale);

		this.bipedLeftArm = new ModelRenderer(this);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedLeftArm.setTextureOffset(56, 0).addBox(-1.0F, 10.0F, -1.0F, 2.0F, 9.0F, 2.0F, scale);
		
		this.secondLeftArm = new ModelRenderer(this);
		this.secondLeftArm.setRotationPoint(5.0F, 1.0F, 0.0F);
		this.secondLeftArm.setTextureOffset(56, 0).addBox(-1.0F, 8.0F, -1.0F, 2.0F, 9.0F, 2.0F, scale);

		this.bipedRightLeg = new ModelRenderer(this);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedRightLeg.setTextureOffset(56, 0).addBox(-2.0F, 7.0F, -1.0F, 2.0F, 4.0F, 2.0F, scale);

		this.bipedLeftLeg = new ModelRenderer(this);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg.setTextureOffset(56, 0).addBox(0.0F, 7.0F, -1.0F, 2.0F, 4.0F, 2.0F, scale);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
	{
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
