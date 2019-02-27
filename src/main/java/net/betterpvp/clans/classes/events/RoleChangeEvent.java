package net.betterpvp.clans.classes.events;

import net.betterpvp.clans.classes.Role;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RoleChangeEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private Player player;
    private Role role;

    public RoleChangeEvent(Player player, Role role) {
        this.isCancelled = false;
        this.player = player;
        this.role = role;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Role getRole() {
        return this.role;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
        
    }
}
