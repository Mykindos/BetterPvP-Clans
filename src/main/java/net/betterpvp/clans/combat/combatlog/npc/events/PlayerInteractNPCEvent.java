package net.betterpvp.clans.combat.combatlog.npc.events;

import net.betterpvp.clans.combat.combatlog.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerInteractNPCEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
    private Player player;
    private NPC npc;

    public PlayerInteractNPCEvent(Player player, NPC npc) {
        this.isCancelled = false;
        this.player = player;
        this.npc = npc;
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

    public NPC getNPC() {
        return this.npc;
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