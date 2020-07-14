package com.andrielgaming.agwarchest.enchantments;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AttunedMending extends Enchantment 
{
	   public AttunedMending(Enchantment.Rarity rarityIn, EquipmentSlotType... slots) 
	   {
	      super(rarityIn, EnchantmentType.BREAKABLE, slots);
	   }

	   public int getMinEnchantability(int enchantmentLevel) 
	   {
	      return enchantmentLevel * 25;
	   }

	   public int getMaxEnchantability(int enchantmentLevel) 
	   {
	      return this.getMinEnchantability(enchantmentLevel) + 50;
	   }

	   public boolean isTreasureEnchantment() 
	   {
	      return true;
	   }
//CrossbowItem
	   public int getMaxLevel() 
	   {
	      return 1;
	   }
	   
	   @Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
	   public static class AttunedMendingEquipped
	   {
		    @SubscribeEvent
			public static void doStuff(PickupXp event) 
			{
				PlayerEntity player = event.getPlayer();
				
				//Armor first
				Iterable<ItemStack> it = player.getArmorInventoryList();
				for(ItemStack i : it)
				{
					if(i.isEnchanted() && (EnchantmentHelper.getEnchantmentLevel(EnchantInit.ATTUNED_MENDING.get(), i) > 0) || EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, i) > 0)
					{
						i.setDamage(i.getDamage()-2);
					}
				}
				
				//Check mainhand
				ItemStack t = player.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
				if(t.isEnchanted() && (EnchantmentHelper.getEnchantmentLevel(EnchantInit.ATTUNED_MENDING.get(), t) > 0) || EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, t) > 0)
				{
					t.setDamage(t.getDamage()-2);
				}
				
				//Check offhand
				t = player.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
				if(t.isEnchanted() && (EnchantmentHelper.getEnchantmentLevel(EnchantInit.ATTUNED_MENDING.get(), t) > 0) || EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, t) > 0)
				{
					t.setDamage(t.getDamage()-2);
				}
			}
	   }
}