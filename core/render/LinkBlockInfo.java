package spacecraft.core.render;

import java.util.Arrays;

import net.minecraft.tileentity.TileEntity;

public class LinkBlockInfo {
	public int[] id;
	public int[] metadata;
	public TileEntity tileEntity;
	
	public boolean equals(LinkBlockInfo par) {
		return Arrays.equals(id, par.id) && Arrays.equals(metadata, par.metadata);
	}
}
