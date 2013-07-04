package spacecraft.core.item;

import spacecraft.core.block.BlockPortalSC;
import spacecraft.core.block.tile.TileEntityPortalSC;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.world.TeleportManager;
import spacecraft.core.world.TeleporterInfo;
import spacecraft.core.world.WorldLinkInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemTeleportCrystal extends ItemBase {
	private static final String COUNT = "count";
	private static final String TIME = "time";

	public ItemTeleportCrystal() {
		super(ItemTeleportCrystal.class);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (par2World.isRemote) return par1ItemStack;
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
		TeleporterInfo info = ItemLocator.getTeleporterInfo(par1ItemStack);
		info.type = TeleportManager.TELEPORTCRYSTAL;
		WorldLinkInfo.addToWorld(par2World, var13, var14, var15, info, BlockPortalSC.class);
		TileEntityPortalSC tile = (TileEntityPortalSC) par2World.getBlockTileEntity(var13, var14, var15);
		tile.countLeft = getCount(par1ItemStack);
		tile.setTimeLeft(getTime(par1ItemStack));
		return par1ItemStack;
	}
	
	public static void setCount(ItemStack itemStack, int value) {
		NBTTagCompound nbt = itemStack.stackTagCompound;
		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemStack.stackTagCompound = nbt;
		}
		nbt.setInteger(COUNT, value);
	}
	
	public static void setTime(ItemStack itemStack, int value) {
		NBTTagCompound nbt = itemStack.stackTagCompound;
		if (nbt == null) {
			nbt = new NBTTagCompound();
			itemStack.stackTagCompound = nbt;
		}
		nbt.setInteger(TIME, value);
	}
	
	private static int getCount(ItemStack itemStack) {
		NBTTagCompound nbt = itemStack.stackTagCompound;
		if (nbt == null || !nbt.hasKey(COUNT))
			return 1;
		return nbt.getInteger(COUNT);
	}
	
	private static int getTime(ItemStack itemStack) {
		NBTTagCompound nbt = itemStack.stackTagCompound;
		if (nbt == null || !nbt.hasKey(TIME))
			return 1;
		return nbt.getInteger(TIME);
	}
}
