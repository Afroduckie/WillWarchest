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
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
		    return this.getDefaultTranslationKey();
		}

		@Override
		public ITextComponent getDisplayName(int level) 
		{
		      IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent("Attuned");
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