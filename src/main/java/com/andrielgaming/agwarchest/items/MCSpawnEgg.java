package com.andrielgaming.agwarchest.items;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MCSpawnEgg extends SpawnEggItem
{
	// Molten Creeper's creative mode spawn egg
	// Uses a workaround for adding spawn eggs & dispenser behaviors since items are registered before entities
	protected static final List<MCSpawnEgg> UNADDED_EGGS = new ArrayList<>();
	private final Lazy<? extends EntityType<?>> entityTypeSupplier;

	public MCSpawnEgg(final NonNullSupplier<? extends EntityType<?>> entityTypeSupplier, final int p_i48465_2_, final int p_i48465_3_, final Properties p_i48465_4_)
	{
		super(null, p_i48465_2_, p_i48465_3_, p_i48465_4_);
		this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
		UNADDED_EGGS.add(this);
	}

	public MCSpawnEgg(final RegistryObject<? extends EntityType<?>> entityTypeSupplier, final int p_i48465_2_, final int p_i48465_3_, final Properties p_i48465_4_)
	{
		super(null, p_i48465_2_, p_i48465_3_, p_i48465_4_);
		this.entityTypeSupplier = Lazy.of(entityTypeSupplier);
		UNADDED_EGGS.add(this);
	}

	// This function exists as a workaround to add a modded spawn egg's dispenser behavior w/o causing a thread unsafe crash
	public static void initUnaddedEggs()
	{
		final Map<EntityType<?>, MCSpawnEgg> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b");
		DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior()
		{
			public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
			{
				Direction direction = source.getBlockState().get(DispenserBlock.FACING);
				EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
				entitytype.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
				stack.shrink(1);
				return stack;
			}
		};
		for(final SpawnEggItem egg : UNADDED_EGGS)
		{
			EGGS.put(egg.getType(null), (MCSpawnEgg)egg);
			DispenserBlock.registerDispenseBehavior(egg, defaultDispenseItemBehavior);
		}
		UNADDED_EGGS.clear();
	}

	@Override
	public EntityType<?> getType(@Nullable final CompoundNBT p_208076_1_)
	{ return entityTypeSupplier.get(); }
}
