package com.andrielgaming.agwarchest.enchantments;

import java.util.Random;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
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
		IFormattableTextComponent iformattabletextcomponent = new TranslationTextComponent("Multishot II");
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
	
	// Tries to grab when player uses the crossbow via RightClickItem event rather
	// than ArrowLoose or nock
	@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
	public static class AttunedMultishotEquipped
	{
		@SubscribeEvent
		@SuppressWarnings("static-access")
		public static void doStuff(RightClickItem event)
		{
			// Grab reference to player, world, and the bow used
			PlayerEntity player = event.getPlayer();
			World world = player.world;
			ItemStack bow = event.getItemStack();
			
			if (bow.getItem() == Items.CROSSBOW && bow.isEnchanted()
					&& EnchantmentHelper.getEnchantmentLevel(EnchantInit.ATTUNED_MULTISHOT.get(), bow) > 0)
			{
				CrossbowItem temp = (CrossbowItem) bow.getItem();
				if (!temp.isCharged(bow))
				{
					chargeShots(bow, 3);
				}
				// public static void fireProjectiles(World worldIn, LivingEntity shooter, Hand
				// handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
				if (temp.isCharged(bow) && temp.canContinueUsing(bow, event.getItemStack()))
				{
					temp.fireProjectiles(world, player, player.getActiveHand(), bow, 1.6F, 0.75F);
					chargeShots(bow, 3);
					temp.setCharged(bow, true);
					// Consume an extra 2 arrows
					temp.getHeldAmmo(player, CrossbowItem.ARROWS).shrink(2);
					;
				}
			}
		}
		
		public static void chargeShots(ItemStack bow, int cnt)
		{
			CompoundNBT compoundnbt1;
			ListNBT listnbt;
			for (int i = 0; i < cnt; i++)
			{
				CompoundNBT compoundnbt = bow.getOrCreateTag();
				if (compoundnbt.contains("ChargedProjectiles", 9))
				{
					listnbt = compoundnbt.getList("ChargedProjectiles", 10);
				} else
				{
					listnbt = new ListNBT();
				}
				compoundnbt1 = new CompoundNBT();
				bow.write(compoundnbt1);
				listnbt.add(compoundnbt1);
				compoundnbt.put("ChargedProjectiles", listnbt);
			}
		}
	}
	
}

//SilkTouchEnchantment
