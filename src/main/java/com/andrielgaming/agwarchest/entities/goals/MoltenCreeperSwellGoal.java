package com.andrielgaming.agwarchest.entities.goals;

import java.util.EnumSet;

import com.andrielgaming.agwarchest.entities.mobs.MoltenCreeperEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

public class MoltenCreeperSwellGoal extends Goal 
{
	   private final MoltenCreeperEntity swellingMCreeper;
	   private LivingEntity mCreeperAttackTarget;

	   public MoltenCreeperSwellGoal(MoltenCreeperEntity entitycreeperIn) 
	   {
	      this.swellingMCreeper = entitycreeperIn;
	      this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	   }

	/**
	    * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	    * method as well.
	    */
	   public boolean shouldExecute() 
	   {
	      LivingEntity livingentity = this.swellingMCreeper.getAttackTarget();
	      return this.swellingMCreeper.getMCreeperState() > 0 || livingentity != null && this.swellingMCreeper.getDistanceSq(livingentity) < 9.0D;
	   }

	   /**
	    * Execute a one shot task or start executing a continuous task
	    */
	   public void startExecuting() 
	   {
	      this.swellingMCreeper.getNavigator().clearPath();
	      this.mCreeperAttackTarget = this.swellingMCreeper.getAttackTarget();
	   }

	   /**
	    * Reset the task's internal state. Called when this task is interrupted by another one
	    */
	   public void resetTask() 
	   {
	      this.mCreeperAttackTarget = null;
	   }

	   /**
	    * Keep ticking a continuous task that has already been started
	    */
	   public void tick() 
	   {
	      if (this.mCreeperAttackTarget == null) 
	      {
	         this.swellingMCreeper.setMCreeperState(-1);
	      } 
	      else if (this.swellingMCreeper.getDistanceSq(this.mCreeperAttackTarget) > 49.0D) 
	      {
	         this.swellingMCreeper.setMCreeperState(-1);
	      } 
	      else if (!this.swellingMCreeper.getEntitySenses().canSee(this.mCreeperAttackTarget)) 
	      {
	         this.swellingMCreeper.setMCreeperState(-1);
	      }
	      else 
	      {
	         this.swellingMCreeper.setMCreeperState(1);
	      }
	   }
	}