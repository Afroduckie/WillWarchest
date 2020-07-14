package com.andrielgaming.agwarchest.util.packettype;

import java.util.function.Supplier;

import com.andrielgaming.agwarchest.items.TotemAttuner;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class DoTotemAnim
{
	private static ItemStack item;
	private static ItemStack item2;
	
	@SuppressWarnings("static-access")
	public DoTotemAnim(ItemStack stack) 
	{
		this.item = stack;
	}
	
	@SuppressWarnings("static-access")
	public static void encode(DoTotemAnim msg, PacketBuffer buf) 
	{
		buf.writeItemStack(msg.item);
	}
	
	public static DoTotemAnim decode(PacketBuffer buf) 
	{
		return new DoTotemAnim(buf.readItemStack());
	}
	
	public static void handle(DoTotemAnim msg, Supplier<NetworkEvent.Context> context) 
	{
		context.get().enqueueWork(() -> 
		{
			Minecraft.getInstance().gameRenderer.displayItemActivation(item);
	    });
		context.get().setPacketHandled(true);
	}
}
