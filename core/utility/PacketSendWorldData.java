package spacecraft.core.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

public class PacketSendWorldData extends Packet250CustomPayload {
	public static final int WORLDDATA = 1;
	public static final int CHANGEDATA = 2;
	public int type;
	public int dimension;
	public NBTTagCompound nbt;
	
	protected PacketSendWorldData(byte[] data) {
		super(NetworkHelper.CHANNEL, data);
	}

	public static PacketSendWorldData createPacket(int type, int dimension, NBTTagCompound data) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(buffer);
			os.writeInt(type);
			os.writeInt(dimension);
			data.writeNamedTag(data, os);
			os.close();
			return new PacketSendWorldData(buffer.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void readData() {
		try {
			ByteArrayInputStream buffer = new ByteArrayInputStream(data);
			DataInputStream is = new DataInputStream(buffer);
			type = is.readInt();
			dimension = is.readInt();
			nbt = (NBTTagCompound) NBTTagCompound.readNamedTag(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendToWorld() {
		//on client!
		if (type == WORLDDATA) {
			WorldSavedDataSC.onReceivedWorldData(dimension, nbt);
		} else {
			WorldSavedDataSC.onReceivedData(dimension, nbt);
		}
	}
}
