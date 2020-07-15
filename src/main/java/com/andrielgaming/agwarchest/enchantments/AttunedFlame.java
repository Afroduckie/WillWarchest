package com.andrielgaming.agwarchest.enchantments;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AttunedFlame extends Enchantment
{
	public AttunedFlame(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots)
	{ super(rarityIn, EnchantmentType.BREAKABLE, slots); }

	@Override
	public int getMaxLevel()
	{ return 1; }

	@Override
	public int getMinLevel()
	{ return 1; }

	@Override
	protected boolean canApplyTogether(Enchantment ench)
	{ return true; }

	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{ return 0; }

	@Override
	public String getName()
	{ return "Enhanced Flame"; }

	@Override
	public ITextComponent getDisplayName(int level)
	{
		IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent("Flame II");
		iformattabletextcomponent.func_240699_a_(TextFormatting.DARK_PURPLE);
		return iformattabletextcomponent;
	}
	//AbstractFireBlock
	@Override
	public boolean isTreasureEnchantment()
	{ return false; }

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{ return false; }

	@Override
	public boolean canApply(ItemStack stack)
	{ return true; }

	@Override
	public boolean isAllowedOnBooks()
	{ return false; }

	@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
	public static class AttunedFlameEquipped
	{
		private static PlayerEntity player;
		private static ItemStack bow;
		private static LivingEntity hurt;
			
		// Ideally Flame II will set the enemy on fire with soul fire instead of regular fire
		@SubscribeEvent
		public static void doStuff(LivingHurtEvent event)
		{
			if(event.getSource().isProjectile() && event.getSource().getTrueSource() instanceof PlayerEntity)
			{
				player = (PlayerEntity)event.getSource().getTrueSource();
				bow = player.getHeldItemMainhand();
				if(EnchantmentHelper.getEnchantments(bow).containsKey(EnchantInit.ATTUNED_FLAME.get()))
				{
					hurt = (LivingEntity)event.getEntity();
					BlockPos pos = new BlockPos(hurt.getPosX(), hurt.getPosY(), hurt.getPosZ());
					World world = hurt.world;
					hurt.func_241209_g_(340);
				}
			}
		}
	}
}
