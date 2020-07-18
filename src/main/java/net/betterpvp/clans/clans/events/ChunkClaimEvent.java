package net.betterpvp.clans.clans.events;

import net.betterpvp.clans.clans.Clan;
import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChunkClaimEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Clan clan;
    private Chunk chunk;

    public ChunkClaimEvent(Clan clan, Chunk chunk) {
        this.clan = clan;
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

    public Clan getClan() {
        return clan;
    }
}
