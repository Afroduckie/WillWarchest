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
	private static boolean doTwo;
	
	@SuppressWarnings("static-access")
	public DoTotemAnim(ItemStack stack) 
	{
		this.item = stack;
		doTwo = false;
	}
	
	public DoTotemAnim(ItemStack stack, ItemStack stack2) 
	{
		this.item = stack;
		this.item2 = stack2;
		doTwo = true;
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
			World world = context.get().getSender().world;
			float f = world.getGameTime();
			if(!doTwo)
				Minecraft.getInstance().gameRenderer.displayItemActivation(item);
			else
			{
				Minecraft.getInstance().gameRenderer.displayItemActivation(item);
				for(int i = 0;i<20;i++) {} //Attempt at a delay
				Minecraft.getInstance().gameRenderer.displayItemActivation(item2);
			}
	    });
		context.get().setPacketHandled(true);
	}
}
