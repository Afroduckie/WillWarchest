package com.andrielgaming.agwarchest.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.blocks.C4;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import com.andrielgaming.agwarchest.init.BlockInit;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid=WarchestMaster.MOD_ID, bus=Bus.MOD, value = Dist.CLIENT)
public class Detonator extends Item
{
	/*
	 	C4 Detonator Item
	 	In game use: "Arms" C4 blocks when it right-clicks them. Right-clicking an already armed block disarms it.
	 	Innate use: "Detonates" armed C4 blocks
	 	
	 	onItemUse: Grabs the BlockPos of targeted block, stores it in local ArrayList if its from a C4 instance, removes it
	 		from ArrayList if it is already there. This is the "arming" and "disarming" functionality.
	 		
	 	onItemRightClick: If crouching, toggles the fuse value between 0, 3, 5, 9, 12, sec
 	 */
	@OnlyIn(Dist.CLIENT)
	private int timer = 0;
	@OnlyIn(Dist.CLIENT)
	private ArrayList<BlockPos> c4blocks = new ArrayList<BlockPos>();
	public static BlockPos temp;
	
	public Detonator(Properties properties) 
	{
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) 
	{
		tooltip.add(new StringTextComponent("§n§l§5C4 Remote Detonator"));
		tooltip.add(new StringTextComponent("§b§oAin't your grandad's fireworks!"));
		tooltip.add(new StringTextComponent("§1-----------------------------------------------------"));
		tooltip.add(new StringTextComponent("§cRight-Click any C4 to arm it"));
		tooltip.add(new StringTextComponent("§9Right-Click any C4 again to disarm it"));
		tooltip.add(new StringTextComponent("§6Right-Click anwhere to detonated armed explosives"));
		tooltip.add(new StringTextComponent("§6Right-Click while sneaking to adjust the timer"));
		tooltip.add(new StringTextComponent("§1-----------------------------------------------------"));
		tooltip.add(new StringTextComponent("§o§eWarning: Primed C4 is extremely volatile and may"));
		tooltip.add(new StringTextComponent("§o§edetonate prematurely if bumped!"));
	}
	
	@Override
	public Item asItem() 
	{
		return super.asItem();
	}
	
	//Right-click on block
	@Override
	//@OnlyIn(Dist.CLIENT)
	public ActionResultType onItemUse(ItemUseContext context) 
	{
		 World world = context.getWorld();
		 //player = context.getPlayer();
		 BlockPos blockpos = context.getPos();
	     BlockState blockstate = world.getBlockState(blockpos);
	     if(context.getWorld().getBlockState(context.getPos()).getBlock() == BlockInit.C4_BLOCK.get()) //!context.getWorld().isRemote && 
	     {
	    	if(!c4blocks.contains(blockpos))
	    	{	
	    		world.playSound(blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.BLOCKS, 1.0F, 2.5F, true);
	    		if(!world.isRemote) 
	    		{
	    			c4blocks.add(blockpos);
	    			return ActionResultType.SUCCESS;
	    		}
	    	}
	    	else if(c4blocks.contains(blockpos))
	    	{
	    		world.playSound(blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.BLOCKS, 1.0F, 0.6F, true);
	    		if(!world.isRemote) 
	    		{
	    			c4blocks.remove(blockpos);
	    			return ActionResultType.SUCCESS;
	    		}
	    	}
	    	else
	    	{
	    		world.playSound(context.getPlayer(), blockpos, SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.BLOCKS, 1.0F, 0.25F);
	    		if(!world.isRemote)
	    		{
	    			try
		    		{
		    			c4blocks.remove(blockpos);
		    			return ActionResultType.SUCCESS;
		    		}
		    		catch(NullPointerException n)
		    		{
		    			return ActionResultType.FAIL;	
		    		}	
	    		}
	    	}
	     }
	     else
	    	 return ActionResultType.FAIL;
		return ActionResultType.FAIL;
	}

	//Innate right-click behavior
	//Right-click without crouching sets the detonator off. Crouch-clicking sets the fuse time. 
	@Override
	//@OnlyIn(Dist.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
	{		
		if(worldIn.isRemote)
			return ActionResult.resultFail(playerIn.getHeldItem(handIn));
		
		if(playerIn.isSneaking())
		{
			switch(timer)
			{
				case 0:
					timer = 3;
					playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1.0F, 0.68F);
					playerIn.sendMessage(new StringTextComponent("§4§6Fuse Timer set to "+timer+" seconds."), playerIn.getUniqueID());
					return ActionResult.resultPass(playerIn.getHeldItem(handIn));
				case 3:
					timer = 5;
					playerIn.sendMessage(new StringTextComponent("§4§2Fuse Timer set to "+timer+" seconds."), playerIn.getUniqueID());
					playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1.0F, 0.88F);
					return ActionResult.resultPass(playerIn.getHeldItem(handIn));
				case 5:
					timer = 9;
					playerIn.sendMessage(new StringTextComponent("§4§3Fuse Timer set to "+timer+" seconds."), playerIn.getUniqueID());
					playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1.0F, 1.15F);
					return ActionResult.resultPass(playerIn.getHeldItem(handIn));
				case 9:
					timer = 12;
					playerIn.sendMessage(new StringTextComponent("§4§4Fuse Timer set to "+timer+" seconds."), playerIn.getUniqueID());
					playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1.0F, 1.33F);
					return ActionResult.resultPass(playerIn.getHeldItem(handIn));
				case 12:
					timer = 0;
					playerIn.sendMessage(new StringTextComponent("§4§5Fuse Timer set to instant."), playerIn.getUniqueID());
					playerIn.playSound(SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 1.0F, 0.10F);
					return ActionResult.resultPass(playerIn.getHeldItem(handIn));
			}
		}
		else
		{
			playerIn.playSound(SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 1.0F, 1.0F);		
			if(!worldIn.isRemote && c4blocks.size()>0)
			{
				detonateInstances(worldIn, timer);
				return ActionResult.resultPass(playerIn.getHeldItem(handIn));
			}
		}		
		return ActionResult.resultFail(playerIn.getHeldItem(handIn));
	}
		
	@OnlyIn(Dist.CLIENT)
	public void reInitC4InstanceArray()
	{
		c4blocks = new ArrayList<BlockPos>();
	}
	
	public void detonateInstances(World world, int fuse)
	{
		//int offset = c4blocks.size();
		for (BlockPos i : c4blocks) 
		{
			if(!world.isRemote && world.getBlockState(i).getBlock() instanceof C4 && world.getBlockState(i).getBlock() != null && i != null && world.getBlockState(i).getBlock() != Blocks.AIR)
			{	
				world.removeBlock(i, false);
				PrimedC4 c4ent = new PrimedC4((timer*20), world, (double)i.getX() + 0.5D, (double)i.getY(), (double)i.getZ() + 0.5D);
	   	 		c4ent.setFuse(timer*20);
	   	 		world.addEntity(c4ent);
			}
			if(world.getBlockState(i).getBlock() == Blocks.AIR)
				world.removeBlock(i, true);
		}
		c4blocks = new ArrayList<BlockPos>();
	}

	@OnlyIn(Dist.DEDICATED_SERVER)
	public void removeBlocksServerSide(World worldIn, BlockPos i)
	{
		worldIn.removeBlock(i, false);
	}
	
	@Override
	public int getBurnTime(ItemStack itemStack) {
		return 6000;
	}
}
