package com.andrielgaming.agwarchest.util.models;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PrimedC4Model extends EntityModel<PrimedC4>
{
	// Model for primed C4 block, pretty much just a box that links to the C4 texture file
	public ModelRenderer body;
	private static final ResourceLocation PRIMED_C4_MODEL = new ResourceLocation(WarchestMaster.MOD_ID, "textures/entities/primed_c4/primed_c4");

	public PrimedC4Model()
	{
		textureWidth = 64;
		textureHeight = 32;
		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, 24.0F, 0.0F);
		body.addBox(0F, 0F, -8.0F, -16.0F, -8.0F, 16, 16, 16, 0.0F);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder iVertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
	{ body.render(matrixStack, iVertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha); }

	@Override
	public void setRotationAngles(PrimedC4 entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch)
	{
		// TODO Auto-generated method stub

	}
}
