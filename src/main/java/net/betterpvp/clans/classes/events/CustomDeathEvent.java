package net.betterpvp.clans.classes.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomDeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player killed, killer;

    public CustomDeathEvent(Player killed, Player killer) {
        this.killed = killed;
        this.killer = killer;
    }

    public Player getKilled() {
        return killed;
    }

    public Player getKiller() {
        return killer;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
