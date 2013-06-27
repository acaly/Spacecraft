package spacecraft.core.render;

import java.util.Arrays;

public class LinkBlockInfo {
	public int[] id;
	public int[] metadata;
	
	public boolean equals(LinkBlockInfo par) {
		return Arrays.equals(id, par.id) && Arrays.equals(metadata, par.metadata);
	}
}
