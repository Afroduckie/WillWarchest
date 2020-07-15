package com.andrielgaming.agwarchest.enchantments;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.EnchantInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
	
	@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
	public static class AttunedFlameEquipped
	{
		private static PlayerEntity player;
		private static boolean pass = false;
		
		// Flame II sets the enemy on fire with Soul Fire instead of regular fire
		@SubscribeEvent
		public static void doStuff(LivingHurtEvent event)
		{
			if(event.getSource().isProjectile() && event.getSource().getTrueSource() instanceof PlayerEntity)
			{
				player = (PlayerEntity)event.getSource().getTrueSource();
				ItemStack bowstack = player.getHeldItemMainhand();
				if(bowstack.getItem() instanceof BowItem && bowstack.isEnchanted()	&& EnchantmentHelper.getEnchantments(bowstack).containsKey(EnchantInit.ATTUNED_FLAME.get()))
				{
					pass = true;
				}
			}
		}//Blocks
		
		@SubscribeEvent
		public static void doStuff(ProjectileImpactEvent event)
		{
			if(pass && player != null && event.getEntity() instanceof ArrowEntity)
			{
				AbstractArrowEntity arr = (AbstractArrowEntity)event.getEntity();
				arr.setFire(9999);
				World world = arr.world;
				for(int i = 0;i<100;i++)
				{
					BlockPos rand = new BlockPos(arr.getPosXRandom(5), arr.getPosY(), arr.getPosZRandom(5));
					BlockState stat = world.getBlockState(rand);
					if(stat.getBlock() == Blocks.AIR)
					{
						//Look at next 3 blocks down
						BlockState d1 = world.getBlockState(rand.down());
						BlockState d2 = world.getBlockState(rand.down().down());
						BlockState d3 = world.getBlockState(rand.down().down().down());
						
						if(d1.getBlock() != Blocks.AIR && !(d1.getBlock() instanceof BushBlock))
						{//AbstractBlock
							world.setBlockState(rand, Blocks.field_235335_bO_.getDefaultState());
						}
						if(d2.getBlock() != Blocks.AIR && !(d2.getBlock() instanceof BushBlock))
						{
							world.setBlockState(rand.down(), Blocks.field_235335_bO_.getDefaultState());
						}
						if(d3.getBlock() != Blocks.AIR && !(d3.getBlock() instanceof BushBlock))
						{
							world.setBlockState(rand.down().down(), Blocks.field_235335_bO_.getDefaultState());
						}
					}
				}
			}
		}
	}
}
