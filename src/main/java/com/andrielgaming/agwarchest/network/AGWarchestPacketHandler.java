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
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class AGWarchestPacketHandler
{
	private AGWarchestPacketHandler network;
	private static final String PROTOCOL_VERSION = "1";
	public static SimpleChannel SIMPLEPACK;

	public AGWarchestPacketHandler(String channel)
	{
		SIMPLEPACK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(WarchestMaster.MOD_ID, channel)).clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();;
	}

	public static void handle(DoTotemAnim msg, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			// Put any thread unsafe work in here. At the moment, there is nothing
			PlayerEntity sender = ctx.get().getSender(); // Get client sender
			// Will probably need to add some security for the DoTotemAnim packet
		});
		ctx.get().setPacketHandled(true);
	}

	public static void register()
	{
		int id = 0;

		SIMPLEPACK.messageBuilder(DoTotemAnim.class, id).encoder(DoTotemAnim::encode).decoder(DoTotemAnim::decode).consumer(DoTotemAnim::handle).add();
		SIMPLEPACK.registerMessage(id++, DoTotemAnim.class, DoTotemAnim::encode, DoTotemAnim::decode, DoTotemAnim::handle);
	}

	public static void sendToClient(DoTotemAnim packet, PlayerEntity player)
	{
		SIMPLEPACK.sendTo(packet, Minecraft.getInstance().getConnection().getNetworkManager(), NetworkDirection.PLAY_TO_SERVER);
	}

	public static void sendToServer(DoTotemAnim packet)
	{ SIMPLEPACK.sendToServer(packet); }

	public static void sendTo(DoTotemAnim msg, ServerPlayerEntity player)
	{ SIMPLEPACK.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT); }
}
