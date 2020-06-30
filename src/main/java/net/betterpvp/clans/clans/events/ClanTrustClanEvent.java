package net.betterpvp.clans.clans.events;

import net.betterpvp.clans.clans.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;


public class ClanTrustClanEvent extends ClanRelationshipEvent {

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


    public ClanTrustClanEvent(Player p, Clan playersClan, Clan targetClan) {
        super(playersClan, targetClan);
        this.p = p;

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
