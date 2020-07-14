package com.andrielgaming.agwarchest.enchantments;

import java.util.Random;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class AttunedMultishot extends Enchantment 
{
	private static final Random rand = new Random();
	
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
	   
	   //Uses the event subscriber bus to attempt to grab the crossbow item being used on
	   // occurrence of a RightClickItem event, meaning when the player tries to shoot.
	   @Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
	   public static class AttunedMultishotEquipped
	   {
			@SubscribeEvent
			@SuppressWarnings("static-access")
			public static void doStuff(RightClickItem event) 
			{
				//Grab reference to player, world, and the bow used
				PlayerEntity player = event.getPlayer();
				World world = player.world;
				ItemStack bow = event.getItemStack();
			
				//Checks if bow loaded, adds up to 10 additional arrows
				if(bow.getItem() == Items.CROSSBOW && bow.isEnchanted() && EnchantmentHelper.getEnchantmentLevel(EnchantInit.ATTUNED_MULTISHOT.get(), bow) > 0)
				{
					 CrossbowItem temp = (CrossbowItem)bow.getItem();
					 if(temp.isCharged(bow))
					 {
						 CompoundNBT compoundnbt = bow.getOrCreateTag();
					     ListNBT listnbt;
					     if(compoundnbt.contains("ChargedProjectiles", 9)) 
					     {
					        listnbt = compoundnbt.getList("ChargedProjectiles", 10);
					     } 
					     else 
					     {
					        listnbt = new ListNBT();
					     }
					     for(int x = 0;x<8;x++)
					     {
					    	 if(rand.nextInt(9)>0)
					    	 {
					    		 CompoundNBT compoundnbt1 = new CompoundNBT();
							     bow.write(compoundnbt1);
					    		 listnbt.add(compoundnbt1); 
					    	 } 
					     }
					     compoundnbt.put("ChargedProjectiles", listnbt);
					 }
				}
			}
		}
}

//SilkTouchEnchantment