package spacecraft.core.utility;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkWatchEvent;
import spacecraft.core.world.WorldLinkInfo;

public class EventHandler {
	@ForgeSubscribe
	public void onChunkWatched(ChunkWatchEvent.Watch event) {
		WorldLinkInfo.forWorld(event.player.worldObj).onWorldChunkWatched(event);
	}
	
	@ForgeSubscribe
	public void onChunkUnwatched(ChunkWatchEvent.UnWatch event) {
		WorldLinkInfo.forWorld(event.player.worldObj).onWorldChunkUnwatched(event);
	}

	@ForgeSubscribe
	public void onTextureStitchPost(TextureStitchEvent.Post event) {
		BlockTextureStitched.onPostStitch();
	}
}
