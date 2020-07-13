package com.andrielgaming.agwarchest.enchantments;

import com.andrielgaming.agwarchest.WarchestMaster;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AttunedEnchant extends Enchantment
{
	public AttunedEnchant(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) 
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
	
	@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
	public static class Attuned 
	{

		@SubscribeEvent
		public static void doStuff(PlayerTickEvent event) 
		{
			
		}
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
}
