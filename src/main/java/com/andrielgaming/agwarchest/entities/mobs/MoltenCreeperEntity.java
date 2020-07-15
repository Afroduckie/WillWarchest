package com.andrielgaming.agwarchest.entities.mobs;

import java.util.Collection;
import java.util.Random;
import javax.annotation.Nullable;
import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.goals.MoltenCreeperSwellGoal;
import com.andrielgaming.agwarchest.world.FluidExplosion;
import net.minecraft.block.Blocks;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IChargeableMob;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IExplosionContext;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.DifficultyChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.NetworkHooks;

@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.FORGE)
public class MoltenCreeperEntity extends MonsterEntity implements IChargeableMob
{
	private static final DataParameter<Integer> STATE = EntityDataManager.createKey(MoltenCreeperEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(MoltenCreeperEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(MoltenCreeperEntity.class, DataSerializers.BOOLEAN);
	private int lastActiveTime;
	private int timeSinceIgnited;
	private int fuseTime = 40;
	private int explosionRadius = 5;
	private final Random random = new Random();

	@SuppressWarnings("incomplete-switch")
	public MoltenCreeperEntity(EntityType<? extends MoltenCreeperEntity> type, World worldIn)
	{
		super(type, worldIn);

		switch(worldIn.getDifficulty())
		{
			case EASY:
				fuseTime = 40;
				break;
			case NORMAL:
				fuseTime = 35;
				break;
			case HARD:
				fuseTime = 25;
				break;
		}
		if(this.random.nextInt(6) == 0) this.dataManager.set(POWERED, true);
		this.setPathPriority(PathNodeType.WATER, -1.0F);
		this.setPathPriority(PathNodeType.LAVA, 0.0F);
		this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
		this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
		this.stepHeight = 1.0F;
	}

	@SubscribeEvent
	@SuppressWarnings("incomplete-switch")
	public void updateFuse(DifficultyChangeEvent e)
	{
		switch(e.getDifficulty())
		{
			case EASY:
				fuseTime = 40;
				break;
			case NORMAL:
				fuseTime = 35;
				break;
			case HARD:
				fuseTime = 25;
				break;
		}
	}

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(1, new SwimGoal(this));
		this.goalSelector.addGoal(2, new MoltenCreeperSwellGoal(this));
		this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, StriderEntity.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
		this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}

	public static boolean func_234314_c_(EntityType<MoltenCreeperEntity> p_234314_0_, IWorld p_234314_1_, SpawnReason p_234314_2_, BlockPos p_234314_3_, Random p_234314_4_)
	{
		BlockPos.Mutable blockpos$mutable = p_234314_3_.func_239590_i_();

		while(true)
		{
			blockpos$mutable.move(Direction.UP);
			if(!p_234314_1_.getFluidState(blockpos$mutable).isTagged(FluidTags.LAVA))
			{ break; }
		}
		return p_234314_1_.getBlockState(blockpos$mutable).getBlock() == Blocks.AIR;
	}

	@Override
	protected boolean isDespawnPeaceful()
	{ return true; }

	protected boolean shouldDrown()
	{ return false; }

	public boolean isPowered()
	{ return this.dataManager.get(POWERED); }

	@Override
	public float getBrightness()
	{ return 10.0F; }

	// DEBUG VALUE, TURN OFF FOR RELEASE
	@Override
	public boolean isGlowing()
	{ return false; }

	protected IParticleData getParticleType()
	{ return ParticleTypes.FLAME; }

	public static AttributeModifierMap.MutableAttribute func_234278_m_()
	{ return MonsterEntity.func_234295_eP_().func_233815_a_(Attributes.field_233821_d_, 0.25D); }

	@Override
	public int getMaxFallHeight()
	{ return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F); }

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier)
	{
		boolean flag = super.onLivingFall(distance, damageMultiplier);
		this.timeSinceIgnited = (int)((float)this.timeSinceIgnited + distance * 1.5F);
		if(this.timeSinceIgnited > this.fuseTime - 5)
		{ this.timeSinceIgnited = this.fuseTime - 5; }
		return flag;
	}

	@Override
	protected void registerData()
	{
		super.registerData();
		this.dataManager.register(STATE, -1);
		this.dataManager.register(POWERED, false);
		this.dataManager.register(IGNITED, false);
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		super.writeAdditional(compound);
		if(this.dataManager.get(POWERED))
		{ compound.putBoolean("powered", true); }
		compound.putShort("Fuse", (short)this.fuseTime);
		compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
		compound.putBoolean("ignited", this.hasIgnited());
	}

	@Override
	public void readAdditional(CompoundNBT compound)
	{
		super.readAdditional(compound);
		this.dataManager.set(POWERED, compound.getBoolean("powered"));
		if(compound.contains("Fuse", 99))
		{ this.fuseTime = compound.getShort("Fuse"); }

		if(compound.contains("ExplosionRadius", 99))
		{ this.explosionRadius = compound.getByte("ExplosionRadius"); }

		if(compound.getBoolean("ignited"))
		{ this.ignite(); }
	}

	@Override
	public void tick()
	{
		if(this.isAlive())
		{
			this.lastActiveTime = this.timeSinceIgnited;
			if(this.hasIgnited())
			{ this.setMCreeperState(1); }

			int i = this.getMCreeperState();
			if(i > 0 && this.timeSinceIgnited == 0)
			{ this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 2.25F, 0.35F); }

			this.timeSinceIgnited += i;
			if(this.timeSinceIgnited < 0)
			{ this.timeSinceIgnited = 0; }

			if(this.timeSinceIgnited >= this.fuseTime)
			{
				this.timeSinceIgnited = this.fuseTime;
				this.explode();
			}
			// EndermanEntity
			if(this.isInLava() || world.getBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ())).getBlock() == Blocks.LAVA)
			{ this.dataManager.set(POWERED, true); }

			if(this.isBurning())
			{ this.extinguish(); }

			if(world.getBlockState(new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ())).getBlock() == Blocks.LAVA)
			{
				this.onGround = true;
				this.setMotion(this.getMotion().mul(1.0D, 0, 1.0D));
			}
		}
		super.tick();
	}

	@Override
	public void livingTick()
	{
		if(this.rand.nextInt(650) == 0 && !this.isSilent())
		{
			this.world.playSound(this.getPosX() + 0.5D, this.getPosY() + 0.5D, this.getPosZ() + 0.5D, SoundEvents.ENTITY_BLAZE_DEATH, this.getSoundCategory(), 0.05F + this.rand.nextFloat(), this.rand.nextFloat() * 0.3F + 0.05F, true);
			this.world.playSound(this.getPosX() + 0.5D, this.getPosY() + 0.5D, this.getPosZ() + 0.5D, SoundEvents.ENTITY_RAVAGER_DEATH, this.getSoundCategory(), 0.05F + this.rand.nextFloat(), this.rand.nextFloat() * 0.3F + 0.05F, true);
		}
		world.addParticle(ParticleTypes.FLAME, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
		if(this.rand.nextInt(15) == 0)
		{
			world.addParticle(ParticleTypes.DRIPPING_LAVA, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
		}
		if(this.dataManager.get(IGNITED) || this.getMCreeperState() == 1)
		{
			for(int i = 0; i < 3; i++)
			{
				this.world.addParticle(ParticleTypes.LAVA, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), 0.0D, 0.0D, 0.0D);
			}
		}
		super.livingTick();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn)
	{ return SoundEvents.ENTITY_CREEPER_HURT; }

	@Override
	protected SoundEvent getDeathSound()
	{ return SoundEvents.ENTITY_CREEPER_DEATH; }

	@Override
	protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn)
	{
		//This function will be for dropping skulls if I ever decide to implement that
		
		/*super.dropSpecialItems(source, looting, recentlyHitIn);
		Entity entity = source.getTrueSource();
		if(entity != this && entity instanceof MoltenCreeperEntity)
		{
			MoltenCreeperEntity c = (MoltenCreeperEntity)entity;
			// if (creeperentity.ableToCauseSkullDrop()) {
			// creeperentity.incrementDroppedSkulls();
			this.entityDropItem(Items.LAVA_BUCKET);
			// }*/
		//}
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn)
	{ return true; }

	public boolean isCharged()
	{ return this.dataManager.get(POWERED); }

	public boolean isPrimed()
	{ return this.dataManager.get(IGNITED); }

	@OnlyIn(Dist.CLIENT)
	public float getMCreeperFlashIntensity(float partialTicks)
	{
		return MathHelper.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
	}

	public int getMCreeperState()
	{ return this.dataManager.get(STATE); }

	public void setMCreeperState(int state)
	{ this.dataManager.set(STATE, state); }

	@Override
	public void onStruckByLightning(LightningBoltEntity lightningBolt)
	{
		super.onStruckByLightning(lightningBolt);
		this.dataManager.set(POWERED, true);
	}

	@Override
	protected ActionResultType func_230254_b_(PlayerEntity p_230254_1_, Hand p_230254_2_)
	{
		ItemStack itemstack = p_230254_1_.getHeldItem(p_230254_2_);
		if(itemstack.getItem() == Items.FLINT_AND_STEEL)
		{
			this.world.playSound(p_230254_1_, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
			if(!this.world.isRemote)
			{
				this.ignite();
				itemstack.damageItem(1, p_230254_1_, (p_213625_1_) ->
				{ p_213625_1_.sendBreakAnimation(p_230254_2_); });
			}

			return ActionResultType.func_233537_a_(this.world.isRemote);
		}
		else
		{
			return super.func_230254_b_(p_230254_1_, p_230254_2_);
		}
	}

	private void explode()
	{
		FluidExplosion.Mode explosion$mode = this.isCharged() ? FluidExplosion.Mode.LAVA : FluidExplosion.Mode.FIRE;// net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world,
																													// this)
																													// ?
																													// FluidExplosion.Mode.LAVA
																													// :
																													// FluidExplosion.Mode.NONE;
		float f = this.isCharged() ? 1.5F : 1.0F;
		this.dead = true;
		this.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float)this.explosionRadius * f, explosion$mode);
		this.remove();
		this.spawnLingeringCloud();
	}

	public FluidExplosion createExplosion(@Nullable Entity entityIn, double xIn, double yIn, double zIn, float explosionRadius, FluidExplosion.Mode modeIn)
	{
		return this.func_230546_a_(entityIn, (DamageSource)null, (IExplosionContext)null, xIn, yIn, zIn, explosionRadius, false, modeIn);
	}

	@SubscribeEvent
	public FluidExplosion func_230546_a_(@Nullable Entity p_230546_1_, @Nullable DamageSource p_230546_2_, @Nullable IExplosionContext p_230546_3_, double p_230546_4_, double p_230546_6_, double p_230546_8_, float p_230546_10_, boolean p_230546_11_, FluidExplosion.Mode p_230546_12_)
	{
		FluidExplosion explosion = new FluidExplosion(this.world, p_230546_1_, p_230546_2_, p_230546_3_, p_230546_4_, p_230546_6_, p_230546_8_, p_230546_10_, p_230546_11_, p_230546_12_);
		// if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this,
		// explosion)) return explosion;
		explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}

	private void spawnLingeringCloud()
	{
		Collection<EffectInstance> collection = this.getActivePotionEffects();
		if(!collection.isEmpty())
		{
			AreaEffectCloudEntity areaeffectcloudentity = new AreaEffectCloudEntity(this.world, this.getPosX(), this.getPosY(), this.getPosZ());
			areaeffectcloudentity.setRadius(3.5F);
			areaeffectcloudentity.setRadiusOnUse(-0.5F);
			areaeffectcloudentity.setWaitTime(10);
			areaeffectcloudentity.setDuration(areaeffectcloudentity.getDuration() / 2);
			areaeffectcloudentity.setRadiusPerTick(-areaeffectcloudentity.getRadius() / (float)areaeffectcloudentity.getDuration());

			for(EffectInstance effectinstance : collection)
			{ areaeffectcloudentity.addEffect(new EffectInstance(effectinstance)); }

			this.world.addEntity(areaeffectcloudentity);
		}

	}

	public boolean hasIgnited()
	{ return this.dataManager.get(IGNITED); }

	public void ignite()
	{ this.dataManager.set(IGNITED, true); }

	@Override
	protected PathNavigator createNavigator(World worldIn)
	{ return new MoltenCreeperEntity.MCLavaPathNavigator(this, worldIn); }

	@Override
	public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn)
	{ return worldIn.getBlockState(pos).getFluidState().isTagged(FluidTags.LAVA) ? 10.0F : 10.0F; }

	// @Override
	public IPacket<?> createSpawnPacket()
	{ return NetworkHooks.getEntitySpawningPacket(this); }

	/**
	 * Returns true if an entity is able to drop its skull due to being blown up by
	 * this creeper.
	 * 
	 * Does not test if this creeper is charged; the caller must do that. However,
	 * does test the doMobLoot gamerule.
	 */
	/*
	 * public boolean ableToCauseSkullDrop() { return this.isCharged() &&
	 * this.droppedSkulls < 1; }
	 * 
	 * public void incrementDroppedSkulls() { ++this.droppedSkulls; }
	 */

	static class MCLavaPathNavigator extends GroundPathNavigator
	{
		MCLavaPathNavigator(MoltenCreeperEntity p_i231565_1_, World p_i231565_2_)
		{ super(p_i231565_1_, p_i231565_2_); }

		protected PathFinder getPathFinder(int p_179679_1_)
		{
			this.nodeProcessor = new WalkNodeProcessor();
			return new PathFinder(this.nodeProcessor, p_179679_1_);
		}

		protected boolean func_230287_a_(PathNodeType p_230287_1_)
		{
			return p_230287_1_ != PathNodeType.LAVA && p_230287_1_ != PathNodeType.DAMAGE_FIRE && p_230287_1_ != PathNodeType.DANGER_FIRE
					? super.func_230287_a_(p_230287_1_)
					: true;
		}

		public boolean canEntityStandOnPos(BlockPos pos)
		{ return this.world.getBlockState(pos).isIn(Blocks.LAVA) || super.canEntityStandOnPos(pos); }
	}

}
