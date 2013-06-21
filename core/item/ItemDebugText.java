package spacecraft.core.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import spacecraft.core.world.SpaceManager;
import spacecraft.core.world.TeleporterInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemDebugText extends ItemBase {

	public ItemDebugText() {
		super(ItemDebugText.class);
		this.setUnlocalizedName("DebugText");
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			WorldSavedDataSC data = WorldSavedDataSC.forWorld(par2World);
			NetworkHelper.sendPlayerMessage(par3EntityPlayer, data.debugString);
			data.debugString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			data.markDirty();
			placeBlock(par1ItemStack, par2World, (EntityPlayerMP) par3EntityPlayer);
		}
		return par1ItemStack;
    }
	
	public boolean placeBlock(ItemStack par1ItemStack, World par2World, EntityPlayerMP par3EntityPlayer) {

    	//no aim
        float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
        double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
        double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;

        MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);//var11);
        if (var12 == null)
            return false;
        
        if (var12.typeOfHit != EnumMovingObjectType.TILE)
        	return false;

        int var13 = var12.blockX;
        int var14 = var12.blockY;
        int var15 = var12.blockZ;

        if (!par2World.canMineBlock(par3EntityPlayer, var13, var14, var15))
            return false;
        if (var12.sideHit == 0)
            --var14;
        if (var12.sideHit == 1)
            ++var14;
        if (var12.sideHit == 2)
            --var15;
        if (var12.sideHit == 3)
            ++var15;
        if (var12.sideHit == 4)
            --var13;
        if (var12.sideHit == 5)
            ++var13;

        if (!par3EntityPlayer.canPlayerEdit(var13, var14, var15, var12.sideHit, par1ItemStack))
            return false;
        
        //par2World.setBlock(var13, var14, var15, RegistryHelper.getId(BlockPortalSC.class), 0, 3);
        
        TeleporterInfo info = new TeleporterInfo();
        info.dimension = 0;
        info.owner = par3EntityPlayer.username;
        info.type = 0;
        info.x = var13 + 10;
        info.y = var14 + 5;
        info.z = var15;
        
        //TeleporterInfo info = SpaceManager.createDebugSpaceAndTeleporter();
        BlockPortalSC.setPortalBlock(par2World, var13, var14, var15, info);
        return true;
	}
}
