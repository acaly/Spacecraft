package spacecraft.core.block.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import spacecraft.core.gui.ContainerBase;
import spacecraft.core.utility.PacketContainerEvent;

public class ContainerTeleporter extends ContainerBase {

	public ContainerTeleporter(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player);
	}

	@Override
	public void onServerNetworkEvent(PacketContainerEvent packet) {}

	@Override
	public void onClientNetworkEvent(PacketContainerEvent packet) {}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}
