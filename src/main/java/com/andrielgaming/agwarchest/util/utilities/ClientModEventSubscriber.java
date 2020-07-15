package com.andrielgaming.agwarchest.util.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.andrielgaming.agwarchest.WarchestMaster;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.MOD, value = Dist.CLIENT)
public final class ClientModEventSubscriber
{
	// Currently no function, but is here waiting for when I need it

	private static final Logger LOGGER = LogManager.getLogger();

	@SubscribeEvent
	public void onFMLClientSetupEvent(final FMLClientSetupEvent event)
	{ System.out.println("ClientModEventSubscriber is working!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~"); }
}
