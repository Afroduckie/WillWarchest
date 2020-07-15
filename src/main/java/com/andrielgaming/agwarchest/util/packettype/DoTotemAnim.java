package com.andrielgaming.agwarchest.util.packettype;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class DoTotemAnim
{
	// Server-to-client packet for performing the Totem of Undying activation
	// 	animation for the passed in ItemStack. Takes any valid ItemStack, 
	//	but validate item sender-side, not here

	private static ItemStack item;

	@SuppressWarnings("static-access")
	public DoTotemAnim(ItemStack stack)
	{ this.item = stack; }

	@SuppressWarnings("static-access")
	public static void encode(DoTotemAnim msg, PacketBuffer buf)
	{ buf.writeItemStack(msg.item); }

	public static DoTotemAnim decode(PacketBuffer buf)
	{ return new DoTotemAnim(buf.readItemStack()); }

	public static void handle(DoTotemAnim msg, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(() ->
		{ 
			Minecraft.getInstance().gameRenderer.displayItemActivation(item); 
		});
		context.get().setPacketHandled(true);
	}
}
