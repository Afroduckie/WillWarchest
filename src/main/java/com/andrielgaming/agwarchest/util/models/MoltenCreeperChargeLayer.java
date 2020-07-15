package com.andrielgaming.agwarchest.util.models;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.mobs.MoltenCreeperEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.EnergyLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MoltenCreeperChargeLayer extends EnergyLayer<MoltenCreeperEntity, MoltenCreeperModel<MoltenCreeperEntity>>
{
	private static final ResourceLocation LAVA_TEXTURE = new ResourceLocation(WarchestMaster.MOD_ID, "textures/entities/molten_creeper/molten_creeper_armor.png");
	private final MoltenCreeperModel<MoltenCreeperEntity> creeperModelMolten = new MoltenCreeperModel<>(2.0F);

	public MoltenCreeperChargeLayer(IEntityRenderer<MoltenCreeperEntity, MoltenCreeperModel<MoltenCreeperEntity>> p_i50947_1_)
	{ super(p_i50947_1_); }

	protected float func_225634_a_(float p_225634_1_)
	{ return p_225634_1_ * 0.02F; }

	protected ResourceLocation func_225633_a_()
	{ return LAVA_TEXTURE; }

	protected EntityModel<MoltenCreeperEntity> func_225635_b_()
	{ return this.creeperModelMolten; }
}
