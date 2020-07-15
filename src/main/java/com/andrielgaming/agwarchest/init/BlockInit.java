package com.andrielgaming.agwarchest.init;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.blocks.C4;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.MOD)
public class BlockInit
{
	// public static final C4 C4BLOCK = null;
	// Initialize DeferredRegister for blocks and blockitmes to be called by the
	// init classes
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WarchestMaster.MOD_ID);

	// Register blocks
	public static final RegistryObject<C4> C4_BLOCK = BLOCKS.register("c4_block", () -> new C4(Block.Properties.create(Material.IRON).hardnessAndResistance(2.7f, 3.7f).sound(SoundType.LANTERN).harvestLevel(2).harvestTool(ToolType.PICKAXE)));

	public static void registerDispenserBehaviorsBlocks()
	{
		DispenserBlock.registerDispenseBehavior(BlockInit.C4_BLOCK.get(), new DefaultDispenseItemBehavior()
		{
			protected ItemStack dispenseStack(IBlockSource source, ItemStack stack)
			{
				World world = source.getWorld();
				BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
				PrimedC4 c4ent = new PrimedC4(160, world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY(), (double)blockpos.getZ() + 0.5D);
				world.addEntity(c4ent);
				world.playSound((PlayerEntity)null, c4ent.getPosX(), c4ent.getPosY(), c4ent.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
				stack.shrink(1);
				return stack;
			}
		});
	}
}
