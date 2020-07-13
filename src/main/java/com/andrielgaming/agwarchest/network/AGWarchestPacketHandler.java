package com.andrielgaming.agwarchest.network;

import com.andrielgaming.agwarchest.WarchestMaster;
import com.andrielgaming.agwarchest.util.packettype.DoTotemAnim;
import com.google.common.base.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class AGWarchestPacketHandler 
{
	private AGWarchestPacketHandler network;// = new AGWarchestPacketHandler();
	private static final String PROTOCOL_VERSION = "1";
	public static SimpleChannel SIMPLEPACK;/* = NetworkRegistry.ChannelBuilder
	.named(new ResourceLocation(WarchestMaster.MOD_ID, "agwarchest"))
	.clientAcceptedVersions(PROTOCOL_VERSION::equals)
	.serverAcceptedVersions(PROTOCOL_VERSION::equals)
	.networkProtocolVersion(() -> PROTOCOL_VERSION)
	.simpleChannel();;*/
	
	public AGWarchestPacketHandler(String channel)
	{
		//network = new AGWarchestPacketHandler("agwar");
		SIMPLEPACK = NetworkRegistry.ChannelBuilder
				.named(new ResourceLocation(WarchestMaster.MOD_ID, channel))
				.clientAcceptedVersions(PROTOCOL_VERSION::equals)
				.serverAcceptedVersions(PROTOCOL_VERSION::equals)
				.networkProtocolVersion(() -> PROTOCOL_VERSION)
				.simpleChannel();;
	}
	
	public static void handle(DoTotemAnim msg, Supplier<NetworkEvent.Context> ctx) 
	{
	    ctx.get().enqueueWork(() -> {
	        // Work that needs to be threadsafe (most work)
	        PlayerEntity sender = ctx.get().getSender(); // the client that sent this packet
	        // do stuff
	    });
	    ctx.get().setPacketHandled(true);
	}
	
	 public static void register() 
	 {
		 int id = 0;
		 
		 SIMPLEPACK.messageBuilder(DoTotemAnim.class, id)
         .encoder(DoTotemAnim::encode)
         .decoder(DoTotemAnim::decode)
         .consumer(DoTotemAnim::handle)
         .add();
		 SIMPLEPACK.registerMessage(id++, DoTotemAnim.class, DoTotemAnim::encode, DoTotemAnim::decode, DoTotemAnim::handle);
	    	/* SIMPLEPACK.messageBuilder(DoTotemAnim.class, id)
             .encoder(DoTotemAnim::encode)
             .decoder(DoTotemAnim::decode)
             .consumer(DoTotemAnim::handle)
             .add();*/
	  }

	  public static void sendToClient(DoTotemAnim packet, PlayerEntity player) 
	  {
		  SIMPLEPACK.sendTo(packet, Minecraft.getInstance().getConnection().getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
	  }

	  public static void sendToServer(DoTotemAnim packet) 
	  {
	      SIMPLEPACK.sendToServer(packet);
	  }
	  
	  public static void sendTo(DoTotemAnim msg, ServerPlayerEntity player)
	  {
		  SIMPLEPACK.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
	  }
}
