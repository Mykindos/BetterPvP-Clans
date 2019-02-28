package net.betterpvp.clans.classes.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegenerateEnergyEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player p;
    private double energy;

    public RegenerateEnergyEvent(Player p, double energy) {
        this.p = p;
        this.energy = energy;
    }

    public Player getPlayer() {
        return p;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


}
