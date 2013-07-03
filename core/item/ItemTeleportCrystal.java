package spacecraft.core.item;

import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.block.tile.TileEntityPortalSC;
import spacecraft.core.utility.RegistryHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemTeleportCrystal extends ItemBase {

	public ItemTeleportCrystal() {
		super(ItemTeleportCrystal.class);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		//no aim
		float var4 = 1.0F;
		double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
		double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
		double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
		//boolean var11 = this.isFull == 0;
		MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);//var11);
		if (var12 == null) {
			return par1ItemStack;
		}
		
		if (var12.typeOfHit != EnumMovingObjectType.TILE) {
			return par1ItemStack;
		}
		
		int var13 = var12.blockX;
		int var14 = var12.blockY;
		int var15 = var12.blockZ;
		
		if (!par2World.canMineBlock(par3EntityPlayer, var13, var14, var15))
			return par1ItemStack;
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
			return par1ItemStack;
		//set portal block and link info
		//par2World.setBlock(var13, var14, var15, RegistryHelper.getBlockId(BlockPortalSC.class), 0, 3);
		return par1ItemStack;
	}
}
