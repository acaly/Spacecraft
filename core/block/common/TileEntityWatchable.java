package spacecraft.core.block.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import spacecraft.core.world.SpaceManager;
import spacecraft.core.world.TeleporterInfo;

public abstract class TileEntityWatchable extends TileEntityInventory {
	private static final String WATCHING = "watch";

	public TileEntityWatchable(String name, int inventorySize, int varCount) {
		super(name, inventorySize, varCount);
	}
	
	protected static final HashMap<List<Integer>, ArrayList<TileEntityWatchable>> watchMap = new HashMap();

	protected static final TileEntityWatchable watch(TeleporterInfo info, TileEntityWatchable from) {
		TileEntityWatchable aim;
		World world = SpaceManager.getWorldForServer(info.dimension);
		if (!world.blockExists(info.x, info.y, info.z)) return null;
		TileEntity tile = world.getBlockTileEntity(info.x, info.y, info.z);
		if (tile == null || !(tile instanceof TileEntityWatchable)) return null;
		aim = (TileEntityWatchable) tile;
		
		List coords = Arrays.asList(info.dimension, info.x, info.y, info.z);
		ArrayList<TileEntityWatchable> watchers = watchMap.get(coords);
		if (watchers == null) {
			watchers = new ArrayList();
			watchMap.put(coords, watchers);
		}
		watchers.add(from);
		
		aim.onWatched(from);
		from.onWatchValidate(aim);
		
		return aim;
	}
	
	//TODO store aim in a map
	protected static final void unwatch(TeleporterInfo info, TileEntityWatchable from) {
		List coords = Arrays.asList(info.dimension, info.x, info.y, info.z);
		if (watchMap.containsKey(coords)) return;
		ArrayList<TileEntityWatchable> watchers = watchMap.get(coords);
		watchers.remove(from);
		
		TileEntityWatchable aim;
		World world = SpaceManager.getWorldForServer(info.dimension);
		if (!world.blockExists(info.x, info.y, info.z)) return;
		TileEntity tile = world.getBlockTileEntity(info.x, info.y, info.z);
		if (tile == null || !(tile instanceof TileEntityWatchable)) return;
		aim = (TileEntityWatchable) tile;
		
		aim.onUnwatched(from);
		from.onWatchInvalidate();
	}
	
	
	
	private TeleporterInfo watching;
	protected void setWatch(TeleporterInfo info) {
		if (watching != null) unwatch(watching, this);
		if (info != null) watch(info, this);
		watching = info;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		if (par1NBTTagCompound.hasKey(WATCHING))
			watching = TeleporterInfo.readFromNBT(par1NBTTagCompound.getCompoundTag(WATCHING));
		else
			watching = null;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		if (watching != null)
			par1NBTTagCompound.setCompoundTag(WATCHING, watching.writeToNBT());
	}
	
	protected boolean loaded;
	
	protected boolean watchAvailable;
	protected TileEntityWatchable aim;
	
	@Override
	public void updateEntity() {
		if (!worldObj.isRemote && !loaded) {
			loaded = true;
			onWatchLoad();
		}
	}
	
	@Override
	public void invalidate() {
		if (!worldObj.isRemote) {
			onWatchUnload();
			loaded = false;
		}
		super.invalidate();
	}
	
	private final void onWatchLoad() {
		if (watching != null) {
			aim = watch(watching, this);
			watchAvailable = (aim != null);
		}
		//validate watchers
		ArrayList<TileEntityWatchable> watchers = watchMap.get(Arrays.asList(
				worldObj.provider.dimensionId, xCoord, yCoord, zCoord));
		for (TileEntityWatchable i : watchers) {
			i.onWatchValidate(this);
		}
	}
	
	private final void onWatchUnload() {
		if (watching != null)
			unwatch(watching, this);
		//invalidate watchers
		ArrayList<TileEntityWatchable> watchers = watchMap.get(Arrays.asList(
				worldObj.provider.dimensionId, xCoord, yCoord, zCoord));
		for (TileEntityWatchable i : watchers) {
			i.onWatchInvalidate();
		}
	}
	
	protected void onWatchValidate(TileEntityWatchable aim) {
		watchAvailable = true;
		this.aim = aim;
	}
	
	protected void onWatchInvalidate() {
		watchAvailable = false;
		aim = null;
	}
	
	protected void onWatched(TileEntityWatchable from) {}
	protected void onUnwatched(TileEntityWatchable from) {}
}
//(in)validate->onWatch(Un)load->static (un)watch->map
//                                               ->onUnwatched
//                             -[map]->onWatch(In)validate