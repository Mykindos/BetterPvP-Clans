package net.betterpvp.clans.clans.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ClanCreateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Player p;
    private String clanName;

    public ClanCreateEvent(Player p, String clanName) {
        this.p = p;
        this.clanName = clanName;
    }

    public String getClanName() {
        return clanName;
    }

    public Player getPlayer() {
        return p;
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
