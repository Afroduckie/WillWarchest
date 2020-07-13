package com.andrielgaming.agwarchest.init;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.enchantments.AttunedEnchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantInit 
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
			ForgeRegistries.ENCHANTMENTS, WarchestMaster.MOD_ID);

	public static final RegistryObject<Enchantment> ATTUNED = ENCHANTMENTS.register("attuned",
			() -> new AttunedEnchant(Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values()));
}
//EnchantingTableBlock