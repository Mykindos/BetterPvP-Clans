package net.betterpvp.clans.clans.events;

import net.betterpvp.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ClanAllyClanEvent extends Event implements Cancellable {

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
    private Clan playersClan, targetClan;

    public ClanAllyClanEvent(Player p, Clan playersClan, Clan targetClan){
        this.p = p;
        this.playersClan = playersClan;
        this.targetClan = targetClan;
    }

    public Player getPlayer(){
        return p;
    }

    public Clan getPlayersClan(){
        return playersClan;
    }

    public Clan getTargetClan(){
        return targetClan;
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
