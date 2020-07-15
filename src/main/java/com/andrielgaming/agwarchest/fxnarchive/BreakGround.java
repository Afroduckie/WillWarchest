package com.andrielgaming.agwarchest.fxnarchive;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BreakGround
{
	//This function was an attempt to create Attuned Flame. It caused a giant hole in the ground and set it on fire.
	//Might be useful for future boss mobs griefing events or something, will have to be slightly reworked.
	
	/*@SubscribeEvent
		public static void doStuff(ProjectileImpactEvent event)
		{
			if(pass && player != null && event.getEntity() instanceof ArrowEntity)
			{
				AbstractArrowEntity arr = (AbstractArrowEntity)event.getEntity();
				arr.setFire(9999);
				World world = arr.world;
				for(int i = 0;i<100;i++)
				{
					BlockPos rand = new BlockPos(arr.getPosXRandom(5), arr.getPosY(), arr.getPosZRandom(5));
					BlockState stat = world.getBlockState(rand);
					if(stat.getBlock() == Blocks.AIR)
					{
						//Look at next 3 blocks down
						BlockState d1 = world.getBlockState(rand.down());
						BlockState d2 = world.getBlockState(rand.down().down());
						BlockState d3 = world.getBlockState(rand.down().down().down());
						
						if(d1.getBlock() != Blocks.AIR && !(d1.getBlock() instanceof BushBlock))
						{//AbstractBlock
							world.setBlockState(rand, Blocks.field_235335_bO_.getDefaultState());
						}
						if(d2.getBlock() != Blocks.AIR && !(d2.getBlock() instanceof BushBlock))
						{
							world.setBlockState(rand.down(), Blocks.field_235335_bO_.getDefaultState());
						}
						if(d3.getBlock() != Blocks.AIR && !(d3.getBlock() instanceof BushBlock))
						{
							world.setBlockState(rand.down().down(), Blocks.field_235335_bO_.getDefaultState());
						}
					}
				}
			}*/
}
