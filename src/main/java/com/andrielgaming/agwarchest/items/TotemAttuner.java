package com.andrielgaming.agwarchest.items;

import java.util.List;
import java.util.Map;

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
	//private static final DataParameter<Integer> ATTUNERANIM = EntityDataManager.createKey(PlayerEntity.class, DataSerializers.VARINT);

	public TotemAttuner(Properties prop)
	{
		super(prop);
	}

	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) 
	{
		tooltip.add(new StringTextComponent("§n§l§5A strange artifact from a lost era..."));
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{
		//Grab reference to offhand item
		ItemStack tools = playerIn.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
		Item it = tools.getItem();
		
		//Currently only works on tools and weapons, can add for armor later
		if((it.getGroup() == ItemGroup.TOOLS || it.getGroup() == ItemGroup.COMBAT) && (tools.isEnchanted()))
		{
			/*Map<Enchantment, Integer> tempench = EnchantmentHelper.getEnchantments(tools);
			Map<Enchantment, Integer> tempench2 = EnchantmentHelper.getEnchantments(tools);
			
			//Run a quick loop through the enchantments on this item to make sure Attuned 
			// 	isn't there so tools can't be attuned twice
			for (Map.Entry<Enchantment, Integer> entry : tempench2.entrySet()) 
			{
				if(entry.getKey() instanceof AttunedEnchant)
					return ActionResult.resultFail(playerIn.getHeldItem(handIn));
			}*/
			
			//Add Blindness effect because 2spooky4mii
			playerIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, (int)80, (int)0));
			
			//Add Soul particles because am scare
			for(int i = 0;i<80;i++)
			{
				worldIn.addParticle(ParticleTypes.field_239812_C_, playerIn.getPosXRandom(1.75), playerIn.getPosYRandom(), playerIn.getPosZRandom(1.75), 0.0D, 0.0D, 0.0D);
				worldIn.addParticle(ParticleTypes.PORTAL, playerIn.getPosXRandom(1.75), playerIn.getPosYRandom(), playerIn.getPosZRandom(1.75), 0.0D, 0.0D, 0.0D);	  
			}
			
			//Send the Totem Animation packet request so the engine will actually render the stupid animation
			AGWarchestPacketHandler.sendToClient(new DoTotemAnim(playerIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND)), playerIn);
			
			//Play spooki noises
			playerIn.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 2.5F, 0.7F);
			playerIn.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 0.4F, 0.75F);
			playerIn.playSound(SoundEvents.ENTITY_WITCH_CELEBRATE, SoundCategory.BLOCKS, 1.0F, 0.25F);
			
			//Break the totem
			ItemStack itemstack = playerIn.getHeldItem(handIn);
			itemstack.damageItem(15, playerIn, (p_220000_1_) -> {
	               p_220000_1_.sendBreakAnimation(handIn);
	            });
			playerIn.addStat(Stats.ITEM_USED.get(this));
			
			//Add any attuned variants
			//tools.addEnchantment(EnchantInit.ATTUNED_MULTISHOT.get(), 2);
			
			//Add the Attuned enchant so this item can't be attuned twice
			tools.addEnchantment(EnchantInit.ATTUNED.get(), 1);

			//Done! Hopefully nothing broke. Except the totem. Cus that's spose to happen.
			return ActionResult.resultPass(playerIn.getHeldItem(handIn));
		}
		return ActionResult.resultFail(playerIn.getHeldItem(handIn));
	}
			
	
	private Enchantment processEnchant(ItemStack item, PlayerEntity player, Hand handIn)
	{
		//Check for instances of the Attuned enchant, return null if found
		Map<Enchantment, Integer> magic = EnchantmentHelper.getEnchantments(item);
		if(magic.containsKey(EnchantInit.ATTUNED.get()))
			return null;		//Essentially tells Totem that this item can't be attuned again
		//Check for instances of each of the other complex enchantments. There isn't a pretty way to do this, so... yeah
		if(magic.containsKey(Enchantments.CHANNELING))
		{
			magic.remove(Enchantments.CHANNELING);
			magic.put(EnchantInit.ATTUNED_CHANNELING.get(), 1);
		}
		if(magic.containsKey(Enchantments.FLAME))
			magic.put(processComplex(item, player, handIn, Enchantments.FLAME, magic), magic.get(Enchantments.FLAME));
		if(magic.containsKey(Enchantments.INFINITY))
			magic.put(processComplex(item, player, handIn, Enchantments.INFINITY, magic), magic.get(Enchantments.INFINITY));
		if(magic.containsKey(Enchantments.MENDING))
			magic.put(processComplex(item, player, handIn, Enchantments.MENDING, magic), magic.get(Enchantments.MENDING));
		if(magic.containsKey(Enchantments.MULTISHOT))
			magic.put(processComplex(item, player, handIn, Enchantments.MULTISHOT, magic), magic.get(Enchantments.MULTISHOT));
		if(magic.containsKey(Enchantments.SILK_TOUCH))
			magic.put(processComplex(item, player, handIn, Enchantments.SILK_TOUCH, magic), magic.get(Enchantments.SILK_TOUCH));
		
		
		
		return null;	
	}
	
	private Enchantment processComplex(ItemStack item, PlayerEntity player, Hand handIn, Enchantment en, Map<Enchantment, Integer> magic)
	{
		if(magic.containsKey(Enchantments.CHANNELING))
			
		if(magic.containsKey(Enchantments.FLAME))
			
		if(magic.containsKey(Enchantments.INFINITY))
			
		if(magic.containsKey(Enchantments.MENDING))
			
		if(magic.containsKey(Enchantments.MULTISHOT))
			
		if(magic.containsKey(Enchantments.SILK_TOUCH))
		{}
		return null;
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) 
	{
	   return false;
	}
	
	//@Override
	public int getBurnTime(ItemStack itemStack) 
	{
		return 6000;
	}
}
