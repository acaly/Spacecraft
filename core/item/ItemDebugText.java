package spacecraft.core.item;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import spacecraft.core.utility.NetworkHelper;
import spacecraft.core.utility.RegistryHelper;
import spacecraft.core.utility.WorldSavedDataSC;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		}
		return par1ItemStack;
    }
}
