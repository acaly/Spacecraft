package spacecraft.core.block.common;

import java.util.Iterator;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.PacketContainerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class ContainerBase<T extends TileEntityInventory> extends Container {
	protected World world;
	protected EntityPlayer player;
	protected T tileEntity;
	protected int progress[];
	private int lastProgress[];
	private int progressCount;
	
	public ContainerBase(World world, EntityPlayer player, int x, int y, int z) {
		this.world = world;
		this.player = player;
		tileEntity = (T) world.getBlockTileEntity(x, y, z);
		initProgress();
	}
	
	public abstract void onGuiEvent(int param);
	public void sendGuiEvent(int param) {
		if (world.isRemote) {
			sendNetworkEvent(PacketContainerEvent.createPacket(player, NetworkHelper.GUIEVENT, param));
		}
	}
	
	public void onServerNetworkEvent(PacketContainerEvent packet){}
	public void onClientNetworkEvent(PacketContainerEvent packet){}
	
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
		for (var3 = 0; var3 < 3; ++var3) {
			for (var4 = 0; var4 < 9; ++var4) {
				this.addSlotToContainer(new Slot(par1Player.inventory, var4 + var3 * 9 + 9, 8 + var4 * 18 + x, 84 + var3 * 18 + y));
			}
		}

		for (var3 = 0; var3 < 9; ++var3) {
			if (useStatic && var3 == par1Player.inventory.currentItem)
				//TODO create SlotStatic if necessary
				this.addSlotToContainer(new Slot(par1Player.inventory, var3, 8 + var3 * 18 + x, 142 + y));
			else
				this.addSlotToContainer(new Slot(par1Player.inventory, var3, 8 + var3 * 18 + x, 142 + y));
		}
	}
	
	protected void initProgress() {
		int count = tileEntity.vars.length;
		progress = new int[count];
		lastProgress = new int[count];
		this.progressCount = count;
	}
	
	protected void refreshProgress() {
		int count = progressCount;
		for (int i = 0; i < count; ++i) {
			progress[i] = tileEntity.vars[i];
		}
	}
	
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting) {
		super.addCraftingToCrafters(par1ICrafting);
		refreshProgress();
		for (int i = 0; i < progressCount; ++i) {
			par1ICrafting.sendProgressBarUpdate(this, i, progress[i]);
		}
	}
	
	@Override
	public void updateProgressBar(int id, int value) {
		tileEntity.setVar(id, value);
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		refreshProgress();
		Iterator i = this.crafters.iterator();
		int j;
		while (i.hasNext()) {
			ICrafting craft = (ICrafting)i.next();
			for (j = 0; j < progressCount; ++j) {
				if (progress[j] != lastProgress[j]) {
					craft.sendProgressBarUpdate(this, j, progress[j]);
				}
			}
		}
		
		for (j = 0; j < progressCount; ++j) {
			lastProgress[j] = progress[j];
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
}
