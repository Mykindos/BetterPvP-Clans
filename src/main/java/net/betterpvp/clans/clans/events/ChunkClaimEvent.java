package net.betterpvp.clans.clans.events;

import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChunkClaimEvent extends Event{

	private static final HandlerList handlers = new HandlerList();

	private Chunk chunk;

	public ChunkClaimEvent(Chunk chunk){
		this.chunk = chunk;
	}

	public Chunk getChunk() {
		return chunk;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
