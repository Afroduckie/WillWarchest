package com.andrielgaming.agwarchest.enchantments;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AttunedFlame extends Enchantment
{
	public AttunedFlame(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots)
	{
		super(rarityIn, EnchantmentType.BREAKABLE, slots);
	}
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	
	@Override
	public int getMinLevel()
	{
		return 1;
	}
	
	@Override
	protected boolean canApplyTogether(Enchantment ench)
	{
		return true;
	}
	
	@Override
	public int getMinEnchantability(int enchantmentLevel)
	{
		return 0;
	}
	
	@Override
	public String getName()
	{
		return "Enhanced Flame";
	}
	
	@Override
	public ITextComponent getDisplayName(int level)
	{
		IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent("Flame II");
		iformattabletextcomponent.func_240699_a_(TextFormatting.DARK_PURPLE);
		return iformattabletextcomponent;
	}
	
	@Override
	public boolean isTreasureEnchantment()
	{
		return false;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
	{
		return false;
	}
	
	@Override
	public boolean canApply(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean isAllowedOnBooks()
	{
		return false;
	}
	//BowItem
	@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
	public static class AttunedFlameEquipped
	{
		//Flame II sets the enemy on fire with Soul Fire instead of regular fire
		private static PlayerEntity player;
		private static BowItem bow;
		private static ItemStack bowstack;
		
		//I'm not sure what event to use, so I'm putting a few functions down here 
		
		@SubscribeEvent
		public static void doStuff(ArrowLooseEvent event)
		{
			player = (PlayerEntity)event.getEntityLiving();
			bow = (BowItem)event.getBow().getItem();
			bowstack = event.getBow();
		}
		
		@SubscribeEvent
		public static void doStuff(LivingHurtEvent event)
		{
			if(event.getSource().isProjectile() && event.getSource().getTrueSource() == player)
			{
				ItemStack bow2 = player.getActiveItemStack();
				if(bow2.equals(bowstack))
				{
					if(bowstack.isEnchanted() && EnchantmentHelper.getEnchantments(bowstack).containsKey(EnchantInit.ATTUNED_FLAME.get()))
					{
						LivingEntity shot = (LivingEntity)event.getEntity();
						World world = shot.world;
						world.setBlockState(new BlockPos(shot.getPosX(), shot.getPosY(), shot.getPosZ()), Blocks.field_235335_bO_.getDefaultState());
						//world.setBlockState(new BlockPos(shot.getPosX(), shot.getPosY(), shot.getPosZ()), Blocks.AIR.getDefaultState());
					}
				}
			}
		}
		
	}
}
