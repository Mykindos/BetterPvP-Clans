package net.betterpvp.clans.clans.events;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.core.client.Client;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ClanKickMemberEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    @Override
    public HandlerList getHandlers() {
        // TODO Auto-generated method stub
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private Player p;
    private Client target;
    private Clan clan;

    public ClanKickMemberEvent(Player p, Client target, Clan clan){
        this.p = p;
        this.target = target;
        this.clan = clan;
    }

    public Clan getClan(){ return clan; }

    public Client getTarget(){
        return target;
    }

    public Player getPlayer(){
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
