package spacecraft.core.utility;

import spacecraft.core.gui.ContainerBase;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

public class NetworkHelper implements IPacketHandler {
	public static final String CHANNEL = "spacecraft";
	public static final int GUIEVENT = 1;	//Server <---- Client
	
	public static void sendPlayerMessage(EntityPlayer player, String text) {
		if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP) player).sendChatToPlayer(text);
		} else if (player instanceof EntityPlayerSP) {
			((EntityPlayerSP) player).addChatMessage(text);
		}
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet instanceof PacketContainerEvent) {
			PacketContainerEvent containerPacket = (PacketContainerEvent) packet;
			containerPacket.readData();
			if (containerPacket.type == GUIEVENT) {
				ContainerBase container = (ContainerBase) MinecraftServer.getServer().
						getConfigurationManager().getPlayerForUsername(containerPacket.player).openContainer;
				container.onGuiEvent(containerPacket.param);
			}
		}
	}
}
