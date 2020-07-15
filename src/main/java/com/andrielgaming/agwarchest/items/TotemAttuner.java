package com.andrielgaming.agwarchest.items;

import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.annotation.Nullable;
import com.andrielgaming.agwarchest.init.EnchantInit;
import com.andrielgaming.agwarchest.network.AGWarchestPacketHandler;
import com.andrielgaming.agwarchest.util.packettype.DoTotemAnim;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.IVanishable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//TODO Animation rendering, refer to lines 635, 642 in GameRenderer

public class TotemAttuner extends Item implements IVanishable
{
	private Map<Enchantment, Integer> enchants;

	public TotemAttuner(Properties prop)
	{ super(prop); }

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
	{
		// This is where tooltips are added
		tooltip.add(new StringTextComponent("§n§l§5A strange artifact from a lost era..."));
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving)
	{ return false; }

	@Override
	public int getBurnTime(ItemStack itemStack)
	{ return 6000; }

	/*
	 * Innate right click functionality for Totem of Attunement held item.
	 * ------------------------------- 
	 * Checks the tool, weapon, or armor item held
	 * in the offhand for valid enchantments. If it has not already been "attuned",
	 * it will "attune" the gear item by doubling its enchantment strength, or in
	 * cases of enchants that can't simply be doubled, replace them with new
	 * enchants that functionally act like doubled versions of their vanilla
	 * variants (See Enchantments package)
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		// Grab reference to offhand item
		ItemStack tool = playerIn.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
		Item it = tool.getItem();

		// Check if the enchantment map contains an AttunedEnchant. It it does, the item can't be attuned
		// Checks this first so totem won't break prematurely
		if(EnchantmentHelper.getEnchantments(tool).containsKey(EnchantInit.ATTUNED.get())) return ActionResult.resultFail(playerIn.getHeldItem(handIn));

		// Currently only works on tools and weapons, can add for armor later
		if((it.getGroup() == ItemGroup.TOOLS || it.getGroup() == ItemGroup.COMBAT) && (tool.isEnchanted()))
		{
			// Add Blindness effect because 2spooky4mii
			playerIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, (int)80, (int)0));

			// Add Soul particles because am scare
			for(int i = 0; i < 80; i++)
			{
				worldIn.addParticle(ParticleTypes.field_239812_C_, playerIn.getPosXRandom(1.75), playerIn.getPosYRandom(), playerIn.getPosZRandom(1.75), 0.0D, 0.0D, 0.0D);
				worldIn.addParticle(ParticleTypes.PORTAL, playerIn.getPosXRandom(1.75), playerIn.getPosYRandom(), playerIn.getPosZRandom(1.75), 0.0D, 0.0D, 0.0D);
			}

			// Send the Totem Animation packet request so the engine will actually render
			// the stupid animation
			AGWarchestPacketHandler.sendToClient(new DoTotemAnim(playerIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND)), playerIn);

			// Play spooki noises
			playerIn.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 2.5F, 0.7F);
			playerIn.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 0.4F, 0.75F);
			playerIn.playSound(SoundEvents.ENTITY_WITCH_CELEBRATE, SoundCategory.BLOCKS, 1.0F, 0.25F);

			// Break the totem
			ItemStack itemstack = playerIn.getHeldItem(handIn);
			itemstack.damageItem(15, playerIn, (p_220000_1_) ->
			{ p_220000_1_.sendBreakAnimation(handIn); });
			playerIn.addStat(Stats.ITEM_USED.get(this));

			// Grab a reference to the item's enchantments
			enchants = EnchantmentHelper.getEnchantments(tool);

			// Iterate through all enchants. Double nonsingle enchants and remove single
			// enchants.
			for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet())
			{
				if(enchants.get(entry.getKey()) > 1) enchants.put(entry.getKey(), entry.getValue() * 2);
				else if(enchants.get(entry.getKey()) <= 1) enchants.put(entry.getKey(), 2);
			}

			// Check for and add complex attuned enchantments, store result in a stack for
			// later use
			Stack<Enchantment> complex = this.processComplex(tool);

			// Iterate through the stack of processed singles to add the complex attuned
			// variants as well as the single-levels
			while(!complex.isEmpty())
			{ enchants.put(complex.pop(), 1); }

			// Apply adjusted enchantments
			EnchantmentHelper.setEnchantments(enchants, tool);

			// Add the Attuned enchant so this item can't be attuned twice
			tool.addEnchantment(EnchantInit.ATTUNED.get(), 1);

			// Done! Hopefully nothing broke. Except the totem. Cus that's spose to happen.
			return ActionResult.resultPass(playerIn.getHeldItem(handIn));
		}
		return ActionResult.resultFail(playerIn.getHeldItem(handIn));
	}

	// Takes the passed in ItemStack and applies the attuned variants for all
	// complex enchants
	// Returns a stack of enchants
	private Stack<Enchantment> processComplex(ItemStack item)
	{
		Stack<Enchantment> complex = new Stack<>();

		// Double check the item can be attuned
		if(enchants.containsKey(EnchantInit.ATTUNED.get())) return null;

		// Check for instances of each of the other complex enchantments. There isn't a
		// pretty way to do this, so... yeah
		if(enchants.containsKey(Enchantments.CHANNELING))
		{
			enchants.remove(Enchantments.CHANNELING);
			complex.push(EnchantInit.ATTUNED_CHANNELING.get());
		}
		if(enchants.containsKey(Enchantments.FLAME))
		{
			enchants.remove(Enchantments.FLAME);
			complex.push(EnchantInit.ATTUNED_FLAME.get());
		}
		if(enchants.containsKey(Enchantments.INFINITY))
		{
			enchants.remove(Enchantments.INFINITY);
			complex.push(EnchantInit.ATTUNED_INFINITY.get());
		}
		if(enchants.containsKey(Enchantments.MENDING))
		{
			enchants.remove(Enchantments.MENDING);
			complex.push(EnchantInit.ATTUNED_MENDING.get());
		}
		if(enchants.containsKey(Enchantments.MULTISHOT))
		{
			enchants.remove(Enchantments.MULTISHOT);
			complex.push(EnchantInit.ATTUNED_MULTISHOT.get());
		}
		if(enchants.containsKey(Enchantments.SILK_TOUCH))
		{
			enchants.remove(Enchantments.SILK_TOUCH);
			complex.push(EnchantInit.ATTUNED_SILKTOUCH.get());
		}
		return complex;
	}
}
