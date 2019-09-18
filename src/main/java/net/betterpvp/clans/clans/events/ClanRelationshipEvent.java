package net.betterpvp.clans.clans.events;

import net.betterpvp.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ClanRelationshipEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Clan clanA, clanB;

    public ClanRelationshipEvent(Clan clanA, Clan clanB) {
        this.clanA = clanA;
        this.clanB = clanB;
    }


    public Clan getClanA() {
        return clanA;
    }

    public Clan getClanB() {
        return clanB;
    }


    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

}
