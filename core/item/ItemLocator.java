package spacecraft.core.item;

import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.world.TeleporterInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemLocator extends ItemBase {
	public static final String LOCATION = "location";

	public ItemLocator() {
		super(ItemLocator.class);
		this.setMaxStackSize(1);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			setLocation(par1ItemStack, par2World, (EntityPlayerMP) par3EntityPlayer);
		}
		return par1ItemStack;
	}
	
	private void setLocation(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		//no aim
		float var4 = 1.0F;
		double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
		double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
		double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;

		MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, false);//var11);
		if (var12 == null)
			return;
		
		if (var12.typeOfHit != EnumMovingObjectType.TILE)
			return;

		int var13 = var12.blockX;
		int var14 = var12.blockY;
		int var15 = var12.blockZ;

		if (!par2World.canMineBlock(par3EntityPlayer, var13, var14, var15))
			return;
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
			return;
		
		TeleporterInfo info = new TeleporterInfo();
		info.dimension = par2World.provider.dimensionId;
		info.owner = par3EntityPlayer.username;
		info.type = 0;
		info.x = var13;
		info.y = var14;
		info.z = var15;
		
		getOrCreateNBT(par1ItemStack).setCompoundTag(LOCATION, info.writeToNBT());
		NetworkHelper.sendPlayerMessage(par3EntityPlayer, "Get location!");
	}
	
	public TeleporterInfo getTeleporterInfo(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.stackTagCompound;
		if (tag == null || !tag.hasKey(LOCATION)) {
			return null;
		}
		return TeleporterInfo.readFromNBT(tag.getCompoundTag(LOCATION));
	}
}
