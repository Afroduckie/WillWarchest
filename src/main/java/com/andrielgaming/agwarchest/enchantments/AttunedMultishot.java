package com.andrielgaming.agwarchest.enchantments;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AttunedMultishot extends Enchantment 
{
	   public AttunedMultishot(Enchantment.Rarity p_i50017_1_, EquipmentSlotType... p_i50017_2_) 
	   {
	      super(p_i50017_1_, EnchantmentType.CROSSBOW, p_i50017_2_);
	   }

	   public int getMinEnchantability(int enchantmentLevel) 
	   {
	      return 20;
	   }

	   public int getMaxEnchantability(int enchantmentLevel) 
	   {
	      return 50;
	   }

	   public int getMaxLevel() 
	   {
	      return 1;
	   }

	   public boolean canApplyTogether(Enchantment ench) 
	   {
	      return super.canApplyTogether(ench) && ench != Enchantments.PIERCING;
	   }
	   
	   @Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
	   public static class Attuned 
		{
			@SubscribeEvent
			public static void doStuff(ArrowLooseEvent event) 
			{
				//Grab reference to player, world, and the bow used
				PlayerEntity player = event.getPlayer();
				World world = player.world;
				ItemStack bow = event.getBow();
				
				//Check if its a crossbow specifically and has the attuned multishot enchant
				if(bow.getItem() == Items.CROSSBOW && bow.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(EnchantInit.ATTUNED_MULTISHOT.get(), bow) > 0)
				{
					
				}
			}
		}
}

//SilkTouchEnchantment