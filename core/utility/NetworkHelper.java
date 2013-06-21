package spacecraft.core.utility;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class NetworkHelper {
	
	public static void sendPlayerMessage(EntityPlayer player, String text) {
		if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP) player).sendChatToPlayer(text);
		} else if (player instanceof EntityPlayerSP) {
			((EntityPlayerSP) player).addChatMessage(text);
		}
	}
}
