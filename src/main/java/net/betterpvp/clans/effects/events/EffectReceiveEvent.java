package net.betterpvp.clans.effects.events;


import net.betterpvp.clans.effects.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EffectReceiveEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Effect effect;
    private boolean cancelled;

    public EffectReceiveEvent(Player player, Effect effect) {
        this.player = player;
        this.effect = effect;
    }

    public Player getPlayer() {
        return player;
    }

    public Effect getEffect() {
        return effect;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }


    public void setCancelled(boolean b) {
        this.cancelled = b;
    }
}
