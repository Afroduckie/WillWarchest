package com.andrielgaming.agwarchest;

import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.andrielgaming.agwarchest.init.BlockInit;
import com.andrielgaming.agwarchest.init.ItemInit;
import com.andrielgaming.agwarchest.init.ModEntityTypes;
import com.andrielgaming.agwarchest.util.render.C4EntityRender;
import com.andrielgaming.agwarchest.util.render.MCRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
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
@Mod.EventBusSubscriber(modid=WarchestMaster.MOD_ID, bus=Bus.MOD)
public class WarchestMaster
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "agwarchest";
    public static WarchestMaster instance;

    public WarchestMaster() 
    {
    	//Initialize modEventBus for registering shit
    	final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	//final ModLoadingContext modLoadingContext = ModLoadingContext.get();
		modEventBus.addListener(this::setup);
    	
        //Register everything for mod loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onPostRegisterEntities);
       
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        
        instance = this;
        
        //Registers serverside and for other game events
        MinecraftForge.EVENT_BUS.register(this);//TNTRenderer
    }
    
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Registering Blocks Dispenser Behaviors...");
        BlockInit.registerDispenserBehaviorsBlocks();
        ModEntityTypes.registerMobAttributes();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPostRegisterEntities(final FMLCommonSetupEvent event) 
    {
    	
	}
    //FMLServerSetupEvent
    @SubscribeEvent
    public void doClientStuff(final FMLClientSetupEvent event) 
    {
    	ModEntityTypes.registerEntityWorldSpawns();
    	ModEntityTypes.registerEntitySpawnPlacements();
    	RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.C4_DUMMY_ENTITY.get(), new C4EntityRender.RenderFactory());
    	RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.MOLTEN_CREEPER.get(), new MCRenderer.RenderFactory());
    	LOGGER.info("TileEntity and Entity Renderers registered successfully! ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ");
    }    
    
    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        //InterModComms.sendTo("C4Master", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Got IMC {}", event.getIMCStream().map(m->m.getMessageSupplier().get()).collect(Collectors.toList()));
    }

    //Method will be called by EventBus when server starts
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) 
    {    
        LOGGER.info("HELLO from server starting");
    }
}