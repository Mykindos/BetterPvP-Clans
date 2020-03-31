package net.betterpvp.clans.clans.events;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.core.client.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class MemberLeaveClanEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers() {

        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Client client;
    private Clan clan;

    public MemberLeaveClanEvent(Client client, Clan clan) {
        this.client = client;
        this.clan = clan;
    }

    public Clan getClan() {
        return clan;
    }

    public Client getClient() {
        return client;
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
