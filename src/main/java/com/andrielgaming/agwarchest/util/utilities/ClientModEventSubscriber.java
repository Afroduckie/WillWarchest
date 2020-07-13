package com.andrielgaming.agwarchest.util.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.entities.misc.PrimedC4;
import com.andrielgaming.agwarchest.init.ModEntityTypes;
import com.andrielgaming.agwarchest.util.render.C4EntityRender;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid=WarchestMaster.MOD_ID, bus=Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventSubscriber 
{
	private static final Logger LOGGER = LogManager.getLogger();
	@SubscribeEvent
	public void onFMLClientSetupEvent(final FMLClientSetupEvent event) 
	{
		System.out.println("ClientModEventSubscriber is working!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
