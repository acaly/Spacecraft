package spacecraft.core.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import spacecraft.core.block.common.TileEntityWatchable;
import spacecraft.core.block.tile.machines.ILinkableMachine;
import spacecraft.core.block.tile.machines.LinkMaintainerMachines;
import spacecraft.core.block.tile.machines.LinkMaintainerMachines.MachineInfo;
import spacecraft.core.item.ItemLocator;
import spacecraft.core.world.TeleporterInfo;

public class TileEntityLinkMaintainer extends TileEntityWatchable {
	public static final String INVENTORY = "container.linkmaintainer";
	
	public static final int LINKSTATUS = 0;
	public static final int LINK_IDLE = 0, LINK_LINKING = 1, LINK_LINKED = 2;
	
	private static final String LANG_LS = "tile.linkmaintainer.inf.st.";
	public static final String LANG_LINKSTATUS[]
			= new String[] {LANG_LS + "0", LANG_LS + "1", LANG_LS + "2"};
	

	public TileEntityLinkMaintainer() {
		super(INVENTORY, 1, 1);
		syncData[0] = new LinkMaintainerMachines(0);
	}

	private TeleporterInfo info;
	private boolean watchedByLink;
	
	//TODO modify other tileentities like this
	public void switchStatus() {
		if (info != null) {
			info = null;
			this.setWatch(info);
			watchedByLink = false;
		} else {
			info = ItemLocator.getTeleporterInfo(inventory.getStackInSlot(0));
			if (info == null) return;
			this.setWatch(info);
			if (!(aim instanceof TileEntityLinkMaintainer)) {
				info = null;
				this.setWatch(info);
			}
			watchedByLink = (aim != null && aim.aim == this);
		}
		updateStatus();
	}
	
	private void updateStatus() {
		if (info == null) {
			vars[LINKSTATUS] = LINK_IDLE;
		} else if (watchAvailable && watchedByLink) {
			vars[LINKSTATUS] = LINK_LINKED;
		} else {
			vars[LINKSTATUS] = LINK_LINKING;
		}
	}

	public void refreshMachines() {
		if (worldObj.isRemote) throw new RuntimeException();
		
		//TODO stop all links
		
		LinkMaintainerMachines machines = (LinkMaintainerMachines) syncData[0];
		machines.clear();

		int i, j, k;
		TileEntity tile;
		MachineInfo machine;
		for (i = xCoord - 1; i <= xCoord + 1; ++i) {
		for (j = yCoord - 1; j <= yCoord + 1; ++j) {
		for (k = zCoord - 1; k <= zCoord + 1; ++k) {
			tile = worldObj.getBlockTileEntity(i, j, k);
			if (tile == null || !(tile instanceof ILinkableMachine)) continue;
			machine = new MachineInfo();
			machine.x = i;
			machine.y = j;
			machine.z = k;
			machine.blockID = worldObj.getBlockId(i, j, k);
			//machine.channel = 0;
			machines.newMachine(machine);
		}
		}
		}
	}
	
	@Override
	protected void onVarChanged(int id, int value) {}

	@Override
	protected void onWatched(TileEntityWatchable from) {
		if (from == aim) {
			watchedByLink = true;
			updateStatus();
		}
	}
	
	@Override
	protected void onUnwatched(TileEntityWatchable from) {
		if (from == aim) {
			watchedByLink = false;
			updateStatus();
		}
	}
	
	@Override
	protected void onWatchLoad() {
		super.onWatchLoad();
		watchedByLink = (aim != null && aim.aim == this);
		refreshMachines();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		info = watching;
	}
}
