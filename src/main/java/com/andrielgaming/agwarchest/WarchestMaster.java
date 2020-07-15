package com.andrielgaming.agwarchest;

import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.andrielgaming.agwarchest.init.BlockInit;
import com.andrielgaming.agwarchest.init.EnchantInit;
import com.andrielgaming.agwarchest.init.ItemInit;
import com.andrielgaming.agwarchest.init.ModEntityTypes;
import com.andrielgaming.agwarchest.network.AGWarchestPacketHandler;
import com.andrielgaming.agwarchest.util.render.C4EntityRender;
import com.andrielgaming.agwarchest.util.render.MCRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("agwarchest")
@Mod.EventBusSubscriber(modid = WarchestMaster.MOD_ID, bus = Bus.MOD)
@SuppressWarnings("unused")
public class WarchestMaster
{
	// Master class for William's Warchest
	// This actually does the things
	// These comments are probably useless
	
	private static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "agwarchest";
	public static WarchestMaster instance;

	public WarchestMaster()
	{
		// Initialize modEventBus for registration process
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::setup);

		// Register everything for mod loading
		// The two IMCs are currently unused but need to be there in case I have inter-mod compat issues
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPostRegisterEntities);

		// Register everything that is threadsafe
		ItemInit.ITEMS.register(modEventBus);
		BlockInit.BLOCKS.register(modEventBus);
		ModEntityTypes.ENTITY_TYPES.register(modEventBus);
		EnchantInit.ENCHANTMENTS.register(modEventBus);
		instance = this;

		// Registers serverside and passes the event bus
		MinecraftForge.EVENT_BUS.register(this);
		
		// Register packet handler and its channel
		AGWarchestPacketHandler ag = new AGWarchestPacketHandler("agwar");
		AGWarchestPacketHandler.register();
	}

	// Registration for less threadsafe entries, such as dispenser behaviors
	@SubscribeEvent
	public void setup(final FMLCommonSetupEvent event)
	{
		LOGGER.info("Registering dispenser behaviors!");
		BlockInit.registerDispenserBehaviorsBlocks();
		ModEntityTypes.registerMobAttributes();
	}

	// Registration for least threadsafe entries
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPostRegisterEntities(final FMLCommonSetupEvent event)
	{

	}
	
	// Registration for clientside-only entries that aren't threadsafe, such as renderers
	@SubscribeEvent
	public void doClientStuff(final FMLClientSetupEvent event)
	{
		ModEntityTypes.registerEntityWorldSpawns();
		ModEntityTypes.registerEntitySpawnPlacements();
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.C4_DUMMY_ENTITY.get(), new C4EntityRender.RenderFactory());
		RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MOLTEN_CREEPER.get(), new MCRenderer.RenderFactory());
	}

	// Primarily for inter-mod communique, none right now
	private void enqueueIMC(final InterModEnqueueEvent event)
	{

	}

	// Read above
	private void processIMC(final InterModProcessEvent event)
	{
		
	}

	// Called on the event bus when the server is fully booted
	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent event)
	{ LOGGER.info("HELLO from server starting"); }
}
