package spacecraft.core.item;

import java.util.List;

import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.world.TeleportManager;
import spacecraft.core.world.TeleporterInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringTranslate;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;

public class ItemLocator extends ItemBase {
	public static final String LOCATION = "location";
	public static final String LANG_LOCATION = "item.locator.inf.location";

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
		
		if (!par3EntityPlayer.isSneaking()) {
		
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
		}
		
		TeleporterInfo info = new TeleporterInfo();
		info.dimension = par2World.provider.dimensionId;
		info.owner = par3EntityPlayer.username;
		info.type = TeleportManager.NONE;
		info.x = var13;
		info.y = var14;
		info.z = var15;
		
		getOrCreateNBT(par1ItemStack).setCompoundTag(LOCATION, info.writeToNBT());
		NetworkHelper.sendPlayerMessage(par3EntityPlayer, "Get location!");
	}
	
	public static TeleporterInfo getTeleporterInfo(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.stackTagCompound;
		if (tag == null || !tag.hasKey(LOCATION)) {
			return null;
		}
		return TeleporterInfo.readFromNBT(tag.getCompoundTag(LOCATION));
	}
	
	public static void setTeleportInfo(ItemStack itemStack, TeleporterInfo info) {
		if (info == null) return;
		NBTTagCompound tag = itemStack.stackTagCompound;
		if (tag == null) {
			tag = new NBTTagCompound();
			itemStack.stackTagCompound = tag;
		}
		tag.setCompoundTag(LOCATION, info.writeToNBT());
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		TeleporterInfo info = getTeleporterInfo(par1ItemStack);
		if (info == null) return;
		par3List.add(StringTranslate.getInstance().translateKeyFormat(
				LANG_LOCATION, WorldProvider.getProviderForDimension(info.dimension).getDimensionName(),
				info.x, info.y, info.z));
	}
}
