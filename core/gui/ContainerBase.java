package spacecraft.core.gui;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.PacketContainerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

public abstract class ContainerBase extends Container {
	protected World world;
	protected EntityPlayer player;
	
	public ContainerBase(World world, EntityPlayer player) {
		this.world = world;
		this.player = player;
	}
	
	public abstract void onGuiEvent(int param);
	public void sendGuiEvent(int param) {
		if (world.isRemote) {
			sendNetworkEvent(PacketContainerEvent.createPacket(player, NetworkHelper.GUIEVENT, param));
		}
	}
	
	public abstract void onServerNetworkEvent(PacketContainerEvent packet);
	public abstract void onClientNetworkEvent(PacketContainerEvent packet);
	
	public void sendNetworkEvent(PacketContainerEvent packet) {
		if (world.isRemote)
			PacketDispatcher.sendPacketToServer(packet);
		else
			PacketDispatcher.sendPacketToPlayer(packet, (Player) player);
	}
	
	public void onNetworkEvent(PacketContainerEvent packet) {
		if (world.isRemote)
			onClientNetworkEvent(packet);
		else
			onServerNetworkEvent(packet);
	}
	
	protected void addPlayerSlots(EntityPlayer par1Player) {
		addPlayerSlots(par1Player, 0, 0, 0, false);
	}
	
	protected void addPlayerSlots(EntityPlayer par1Player, int startFrom, int x, int y, boolean useStatic) {
		int var3, var4;
		for (var3 = 0; var3 < 1; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(par1Player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18 + x, 84 + var3 * 18 + y));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			//if (useStatic && var3 == par1Player.inventory.currentItem)
				//TODO create SlotStatic if necessary
			//	this.addSlotToContainer(new Slot(par1Player.inventory, var3, 8 + var3 * 18 + x, 142 + y));
			//else
			//	this.addSlotToContainer(new Slot(par1Player.inventory, var3, 8 + var3 * 18 + x, 142 + y));
		}
	}
}
