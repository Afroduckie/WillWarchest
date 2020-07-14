package com.andrielgaming.agwarchest.init;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.enchantments.AttunedChanneling;
import com.andrielgaming.agwarchest.enchantments.AttunedEnchant;
import com.andrielgaming.agwarchest.enchantments.AttunedFlame;
import com.andrielgaming.agwarchest.enchantments.AttunedInfinity;
import com.andrielgaming.agwarchest.enchantments.AttunedMending;
import com.andrielgaming.agwarchest.enchantments.AttunedMultishot;
import com.andrielgaming.agwarchest.enchantments.AttunedSilk;

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
	
	public static final RegistryObject<Enchantment> ATTUNED_MULTISHOT = ENCHANTMENTS.register("attuned_multishot",
			() -> new AttunedMultishot(Rarity.RARE, EquipmentSlotType.values()));
	
	public static final RegistryObject<Enchantment> ATTUNED_MENDING = ENCHANTMENTS.register("attuned_mending",
			() -> new AttunedMending(Rarity.RARE, EquipmentSlotType.values()));
	
	public static final RegistryObject<Enchantment> ATTUNED_CHANNELING = ENCHANTMENTS.register("attuned_channeling",
			() -> new AttunedChanneling(Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values()));
	
	public static final RegistryObject<Enchantment> ATTUNED_FLAME = ENCHANTMENTS.register("attuned_flame",
			() -> new AttunedFlame(Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values()));
	
	public static final RegistryObject<Enchantment> ATTUNED_INFINITY = ENCHANTMENTS.register("attuned_infinity",
			() -> new AttunedInfinity(Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values()));
	
	public static final RegistryObject<Enchantment> ATTUNED_SILKTOUCH = ENCHANTMENTS.register("attuned_silktouch",
			() -> new AttunedSilk(Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.values()));
}
