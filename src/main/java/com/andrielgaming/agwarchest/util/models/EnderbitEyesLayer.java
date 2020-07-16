package com.andrielgaming.agwarchest.util.models;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EndermanModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class EnderbitEyesLayer<T extends LivingEntity> extends AbstractEyesLayer<T, EnderbitModel<T>>
{
	private static final RenderType RENDER_TYPE = RenderType.getEyes(new ResourceLocation("textures/entity/enderbit/enderbit_eyes.png"));

	public EnderbitEyesLayer(IEntityRenderer<T, EnderbitModel<T>> rendererIn)
	{ super(rendererIn); }

	public RenderType getRenderType()
	{ return RENDER_TYPE; }
}
