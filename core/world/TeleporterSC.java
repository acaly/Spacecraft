package spacecraft.core.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleporterSC extends Teleporter {
	private TeleporterInfo info;
	private World world;

	public TeleporterSC(WorldServer par1WorldServer, TeleporterInfo info) {
		super(par1WorldServer);
		this.info = info;
		this.world = par1WorldServer;
	}

	@Override
	public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
		info.placeEntity(par1Entity, world);
	}
	
	@Override
    public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
		info.placeEntity(par1Entity, world);
		return true;
	}
	
	@Override
    public boolean makePortal(Entity par1Entity) {
		return false;
	}
	
	@Override
    public void removeStalePortalLocations(long par1) {}
}
