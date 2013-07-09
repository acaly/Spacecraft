package spacecraft.core.block.tile.machines;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import spacecraft.core.block.common.SyncDataTileEntity;

public class LinkMaintainerMachines extends SyncDataTileEntity {
	
	public static class MachineInfo {
		public int x, y, z;
		public int blockID;
		//public int metadata;
		public int channel;
	}
	
	public static final int CHANNELCOUNT = 8;
	public static final String NBT_DATA = "data";
	
	public Map<List<Integer>, MachineInfo> machines = new HashMap();
	public MachineInfo[] channels = new MachineInfo[CHANNELCOUNT];

	public LinkMaintainerMachines(int id) {
		super(id);
	}
	
	public void clear() {
		machines.clear();
		channels = new MachineInfo[CHANNELCOUNT];
	}
	
	public void newMachine(MachineInfo m) {
		//assert m.channel == 0;
		machines.put(Arrays.asList(m.x, m.y, m.z), m);
	}
	
	public void removeMachine(int x, int y, int z) {
		List coords = Arrays.asList(x, y, z);
		MachineInfo m = machines.get(coords);
		machines.remove(coords);
		if (m != null && m.channel != 0) {
			channels[m.channel] = null;
		}
	}
	
	public MachineInfo getMachine(int x, int y, int z) {
		return machines.get(Arrays.asList(x, y, z));
	}
	
	public MachineInfo getMachine(int channel) {
		return channels[channel];
	}
	
	public void setChannel(int x, int y, int z, int channel) {
		MachineInfo m = machines.get(Arrays.asList(x, y, z));
		if (m == null) throw new RuntimeException();
		if (channel == 0) {
			channels[m.channel] = null;
			m.channel = 0;
		} else {
			if (channels[channel] != null) throw new RuntimeException();
			channels[m.channel] = null;
			m.channel = channel;
			channels[channel] = m;
		}
	}
	
	public int getNextAvailableChannel(int c) {
		int i = c + 1;
		while (i != c) {
			if (channels[i] == null) return i;
			if (++i == CHANNELCOUNT) i = 1;
		}
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		int[] data = nbt.getIntArray(NBT_DATA);
		int i = 0 ;
		MachineInfo m;
		while (i < data.length) {
			m = new MachineInfo();
			m.x = data[i++];
			m.y = data[i++];
			m.z = data[i++];
			m.blockID = data[i++];
			m.channel = data[i++];
			
			machines.put(Arrays.asList(m.x, m.y, m.z), m);
			if (m.channel != 0) channels[m.channel] = m;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		int[] data = new int[5 * machines.size()];
		int i = 0;
		for (MachineInfo m : machines.values()) {
			data[i++] = m.x;
			data[i++] = m.y;
			data[i++] = m.z;
			data[i++] = m.blockID;
			data[i++] = m.channel;
		}
		nbt.setIntArray(NBT_DATA, data);
	}

}
