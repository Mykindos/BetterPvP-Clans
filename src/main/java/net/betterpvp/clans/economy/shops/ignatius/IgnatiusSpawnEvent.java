package net.betterpvp.clans.economy.shops.ignatius;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IgnatiusSpawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }



}
