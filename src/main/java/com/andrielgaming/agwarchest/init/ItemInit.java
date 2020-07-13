package com.andrielgaming.agwarchest.init;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.items.Detonator;
import com.andrielgaming.agwarchest.items.MCSpawnEgg;
import com.andrielgaming.agwarchest.items.TotemAttuner;

import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid=WarchestMaster.MOD_ID, bus=Bus.MOD)
public class ItemInit 
{
	//Create DeferredRegister for items
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WarchestMaster.MOD_ID);
	//public static final DeferredRegister<ToolItem> TOOL_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WarchestMaster.MOD_ID);
		
	//Register blockitems
	public static final RegistryObject<BlockItem> C4_ITEM = ITEMS.register("c4_block", () -> new BlockItem(BlockInit.C4_BLOCK.get(), new Item.Properties().maxStackSize(64).group(ItemGroup.REDSTONE)));
	
	//Register C4 Detonator item
	public static final RegistryObject<Detonator> DETONATOR = ITEMS.register("detonator", () -> new Detonator(new Item.Properties().group(ItemGroup.REDSTONE).maxStackSize(1)));
	//Molten Creeper Spawn Egg
	public static final RegistryObject<MCSpawnEgg> MOLTEN_CREEPER_SPAWN_EGG = ITEMS.register("molten_creeper_spawn_egg", () -> new MCSpawnEgg(ModEntityTypes.MOLTEN_CREEPER, 0xF0A5A2, 0xA9672B, new Item.Properties().group(ItemGroup.MISC)));
	//Totem Attuner item
	public static final RegistryObject<TotemAttuner> TOTEM_ATTUNER = ITEMS.register("totem_attuner", () -> new TotemAttuner(new Item.Properties().group(ItemGroup.MISC).maxStackSize(1).maxDamage(2).defaultMaxDamage(2)));
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) 
	{
		MCSpawnEgg.initUnaddedEggs();
	}
}
