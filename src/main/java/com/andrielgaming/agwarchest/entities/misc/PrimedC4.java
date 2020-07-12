package com.andrielgaming.agwarchest.entities.misc;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.init.ModEntityTypes;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.NetworkHooks;

@EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PrimedC4 extends Entity
{
	   private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(PrimedC4.class, DataSerializers.VARINT);
	   private static final Logger LOGGER = LogManager.getLogger();
	   @Nullable
	   private int fuse;
	   private int tickcnt;
	   public boolean armed;

	   public static VoxelShape SHAPE = Block.makeCuboidShape(1,1,1,1,1,1);
	   
	   public PrimedC4(EntityType<? extends PrimedC4> type, World worldIn) 
	   {
	      super(type, worldIn);
	      //LOGGER.info("C4 Entity with ID:: " + this.toString() + " created via console command!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" );
	      this.preventEntitySpawning = true;
	      tickcnt = 0;
	   }
	   
	   public PrimedC4(int fuse, World worldIn, double x, double y, double z) 
	   {
	      this(ModEntityTypes.C4_DUMMY_ENTITY.get(), worldIn);
	      world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_NOTE_BLOCK_BIT, SoundCategory.BLOCKS, 0.6F, 2.0F, true);
	      this.setPosition(x, y, z);
	      double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
	      this.setMotion(-Math.sin(d0) * 0.035D, (double)0.35F, -Math.cos(d0) * 0.035D);
	      this.setFuse(fuse);
	      this.prevPosX = x;
	      this.prevPosY = y;
	      this.prevPosZ = z;
	      tickcnt = 0;
	   }
	   
	   protected boolean canTriggerWalking() 
	   {
		  return false;
	   }
	   
	   protected void registerData() 
	   {
	      this.dataManager.register(FUSE, fuse);
	   }
	   
	   public boolean canBeCollidedWith() 
	   {
		   return true;
	   }
	   
	   public boolean getArmed()
	   {
		   return armed;
	   }
	   
	   public void setArmed(boolean b)
	   {
		   armed = b;
	   }
	   
	   @SuppressWarnings("static-access")
	   public void tick() 
	   {
			   tickcnt++;
			   if (!this.hasNoGravity()) 
			   {
			         this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
			      }

			      this.move(MoverType.SELF, this.getMotion());
			      this.setMotion(this.getMotion().scale(0.98D));
			      if (this.onGround) 
			      {
			         this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
			      }

			      --this.fuse;
			      if (this.fuse <= 0) 
			      {
			         this.remove();
			         if (!this.world.isRemote) 
			         {
			            this.explode();
			         }
			      } 
			      else 
			      {
			         this.func_233566_aG_();
			         if (!this.world.isRemote) 
			         {
			            this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
			         }
			      }
			      
			      if(tickcnt % 20 == 0 || tickcnt == 0)
			      {
			    	  world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.38F, 0.65F, true);
			      }
			      
			      if(tickcnt % 4 == 0 && tickcnt % 20 != 0)
			      {
			    	  world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.BLOCK_STONE_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.25F, 0.45F, true);
			      }
	   }

	   public void explode() 
	   {
	      float f = 18.0F;
	      this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 18.0F, Explosion.Mode.BREAK);
	   }
	   
	   protected void writeAdditional(CompoundNBT compound) 
	   {
	      compound.putShort("Fuse", (short)this.getFuse());
	   }

	   protected void readAdditional(CompoundNBT compound) 
	   {
	      this.setFuse(compound.getShort("Fuse"));
	   }

	   protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) 
	   {
	      return 0.15F;
	   }

	   @OnlyIn(Dist.CLIENT)
	   public void setFuse(int fuseIn) 
	   {
	      this.dataManager.set(FUSE, fuseIn);
	      this.fuse = fuseIn;
	   }

	   public void notifyDataManagerChange(DataParameter<?> key) 
	   {
	      if (FUSE.equals(key)) 
	      {
	         this.fuse = this.getFuseDataManager();
	      }
	   }

	   public int getFuseDataManager() 
	   {
	      return this.dataManager.get(FUSE);
	   }

	   @OnlyIn(Dist.CLIENT)
	   public int getFuse() 
	   {
	      return fuse;
	   }

	   @Override
	   public IPacket<?> createSpawnPacket() 
	   {
	      return NetworkHooks.getEntitySpawningPacket(this);
	   }
	}