package com.andrielgaming.agwarchest.items;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.andrielgaming.agwarchest.enchantments.AttunedEnchant;
import com.andrielgaming.agwarchest.network.AGWarchestPacketHandler;
import com.andrielgaming.agwarchest.util.packettype.DoTotemAnim;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
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
			Map<Enchantment, Integer> tempench = EnchantmentHelper.getEnchantments(tools);
			
			//Run a quick loop through the enchantments on this item to make sure Attuned 
			// 	isn't there so tools can't be attuned twice
			for (Map.Entry<Enchantment, Integer> entry : tempench.entrySet()) 
			{
				if(entry.getKey() instanceof AttunedEnchant)
					return ActionResult.resultFail(playerIn.getHeldItem(handIn));
			}
			
			//If that check passes, strengthen the actual enchantments
			for (Map.Entry<Enchantment, Integer> entry : tempench.entrySet()) 
			{
				//Add Blindness effect because 2spooky4mii
				playerIn.addPotionEffect(new EffectInstance(Effects.BLINDNESS, (int)80, (int)0));
				
				//Add Soul particles because am scare
				for(int i = 0;i<80;i++)
				{
					worldIn.addParticle(ParticleTypes.field_239812_C_, playerIn.getPosXRandom(1.75), playerIn.getPosYRandom(), playerIn.getPosZRandom(1.75), 0.0D, 0.0D, 0.0D);
					worldIn.addParticle(ParticleTypes.field_239818_ar_, playerIn.getPosXRandom(1.75), playerIn.getPosYRandom(), playerIn.getPosZRandom(1.75), 0.0D, 0.0D, 0.0D);	  
				}
				
				//Send the Totem Animation packet request so the engine will actually render the stupid animation
				AGWarchestPacketHandler.sendToClient(new DoTotemAnim(playerIn.getItemStackFromSlot(EquipmentSlotType.MAINHAND)), playerIn);
				if(!(entry.getKey() instanceof AttunedEnchant) && tempench.get(entry.getKey()) >= 2)
				{
					tempench.put(entry.getKey(), tempench.get(entry.getKey())*2);
				}
				else if(!(entry.getKey() instanceof AttunedEnchant))
					tempench.put(entry.getKey(), 2);
				
				//Alternate method of adding the Attuned enchantment since my code is being a bitch and wont apply it normally for some reason
				//		that I am probably constantly overlooking
				//tempench.put(new AttunedEnchant(Rarity.COMMON, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND}), 1);
				//Set the new enchantment level
				EnchantmentHelper.setEnchantments(tempench, tools);
				//EnchantingTableTileEntity
				//Debug chat message
				//if(!worldIn.isRemote)
				//	playerIn.sendMessage(new StringTextComponent("§4§6The mystical totem has done something to your tool..."), playerIn.getUniqueID());
				
				//Play spooki noises
				playerIn.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 7.0F, 0.7F);
				playerIn.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.BLOCKS, 0.25F, 0.75F);
				playerIn.playSound(SoundEvents.ENTITY_WITCH_CELEBRATE, SoundCategory.BLOCKS, 0.55F, 0.35F);
				
				//Break the totem
				ItemStack itemstack = playerIn.getHeldItem(handIn);
				itemstack.damageItem(15, playerIn, (p_220000_1_) -> {
		               p_220000_1_.sendBreakAnimation(handIn);
		            });
				playerIn.addStat(Stats.ITEM_USED.get(this));
			}
			//Add the Attuned enchant so this item can't be attuned twice
			//This is giving me some shit for some reason TODO
			tools.addEnchantment(new AttunedEnchant(Rarity.RARE, EnchantmentType.BREAKABLE, new EquipmentSlotType[]{EquipmentSlotType.OFFHAND}), 1);
			
			//Done! Hopefully nothing broke. Except the totem. Cus that's spose to happen.
			return ActionResult.resultPass(playerIn.getHeldItem(handIn));
		}
		return ActionResult.resultFail(playerIn.getHeldItem(handIn));
	}//EnchantedBookItem
	
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
