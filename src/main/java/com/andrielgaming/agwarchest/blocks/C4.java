package com.andrielgaming.agwarchest.blocks;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import com.andrielgaming.agwarchest.init.BlockInit;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid=WarchestMaster.MOD_ID, bus=Bus.MOD)
public class C4 extends Block implements IDispenseItemBehavior
{
	public static final BooleanProperty UNSTABLE = BlockStateProperties.UNSTABLE;
	public static int litfuse;

    public C4(AbstractBlock.Properties properties) 
    {
    	super(properties);
    	this.setDefaultState(this.getDefaultState().with(UNSTABLE, Boolean.valueOf(false)));
    }
  
    @Override
    public void catchFire(BlockState state, World world, BlockPos pos, @Nullable net.minecraft.util.Direction face, @Nullable LivingEntity igniter) 
    {
        detonate(200, world, pos, null);
    }
    
    @Override
    public void onExplosionDestroy(World world, BlockPos pos, Explosion explosionIn) 
    {
        if(!world.isRemote) 
        {
        	PrimedC4 c4ent = new PrimedC4(20, world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
       		world.addEntity(c4ent);
        }
    }
    
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) 
    {
        if (!oldState.isIn(state.getBlock()))
        {
           if (worldIn.isBlockPowered(pos)) 
           {
              catchFire(state, worldIn, pos, null, null);
              worldIn.removeBlock(pos, false);
           }
        }
     }

     public void detonate(int fuse, World world, BlockPos pos, PlayerEntity player)
     {
    	 if(!world.isRemote()) 
    	 {
    		PrimedC4 c4ent = new PrimedC4(fuse, world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
    		c4ent.setFuse(fuse);
    		world.addEntity(c4ent);
    	 	world.removeBlock(pos, false);
    	 }
     }
     
     public void detonate(World world, BlockPos pos, @Nullable LivingEntity igniter)
     {
    	 if(!world.isRemote()) 
    	 {
    		PrimedC4 c4ent = new PrimedC4(100, world, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D);
    		//c4ent.setFuse((short)(world.rand.nextInt(PrimedC4.getFuse() / 4) + PrimedC4.getFuse() / 8));
    		world.addEntity(c4ent);
    	 	world.removeBlock(pos, false);
    	 }
     }

     @Override
     public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) 
     {
        if(worldIn.isBlockPowered(pos)) 
        {
        	worldIn.removeBlock(pos, false);
            catchFire(state, worldIn, pos, null, null);
        }
     }

     @Override
     public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) 
     {
        if (!worldIn.isRemote() && !player.isCreative() && state.get(UNSTABLE)) 
        {
           catchFire(state, worldIn, pos, null, null);
        }
        super.onBlockHarvested(worldIn, pos, state, player);
     }

    //When the Detonator right-clicks on this block
    @Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) 
    {//CreeperEntity
    	ItemStack itemstack = player.getHeldItem(handIn);
        Item item = itemstack.getItem();
        if(item == Items.FLINT_AND_STEEL)
        {
        	worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
        	catchFire(state, worldIn, pos, hit.getFace(), player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            return ActionResultType.SUCCESS;
        }
        if(item == Items.FIRE_CHARGE)
        {
        	worldIn.playSound(player, pos, SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
        	catchFire(state, worldIn, pos, hit.getFace(), player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            return ActionResultType.SUCCESS;
        }
        if(item == Items.REDSTONE_TORCH)
        {
        	worldIn.playSound(player, pos, SoundEvents.ITEM_CROSSBOW_LOADING_END, SoundCategory.BLOCKS, 1.0F, RANDOM.nextFloat() * 0.4F + 0.8F);
        	catchFire(state, worldIn, pos, hit.getFace(), player);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
            return ActionResultType.SUCCESS;
        }
		return ActionResultType.FAIL;
     }
     
     @Override
     public boolean canDropFromExplosion(Explosion explosionIn) 
     {
        return false;
     }

     @Override
     protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
     {
        builder.add(UNSTABLE);
     }
     
     @Override
     public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) 
     {
         if (!worldIn.isRemote) 
         {
            Entity entity = projectile.func_234616_v_();
            if (projectile.isBurning()) 
            {
               BlockPos blockpos = hit.getPos();
               catchFire(state, worldIn, blockpos, null, entity instanceof LivingEntity ? (LivingEntity)entity : null);
               worldIn.removeBlock(blockpos, false);
            }
         }
      }

	@Override
	public ItemStack dispense(IBlockSource source, ItemStack stack) 
    {
		return stack;
    }
}


