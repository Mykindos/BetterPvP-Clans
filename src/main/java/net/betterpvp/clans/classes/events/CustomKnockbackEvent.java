package net.betterpvp.clans.classes.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomKnockbackEvent extends Event {


    private static final HandlerList handlers = new HandlerList();

    private LivingEntity damagee, damager;
    private double damage;
    public CustomDamageEvent d;

    public CustomKnockbackEvent(LivingEntity damagee, LivingEntity damager, double damage, CustomDamageEvent d) {
        this.damagee = damagee;
        this.damager = damager;
        this.damage = damage;
        this.d = d;
    }

    public LivingEntity getDamagee() {
        return damagee;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double d) {
        this.damage = d;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
