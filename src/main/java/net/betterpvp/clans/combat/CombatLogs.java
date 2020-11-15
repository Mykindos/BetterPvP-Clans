package net.betterpvp.clans.combat;

import org.bukkit.entity.LivingEntity;

public class CombatLogs {

    private LivingEntity damager;
    private String cause;
    private String name;
    private Long expiry;
    private double damage;

    public CombatLogs(LivingEntity damager, String name, String cause, double damage) {
        this.damager = damager;
        this.name = name;
        if (cause != null) {
            this.cause = cause;
        } else {
            this.cause = "";
        }

        this.expiry = System.currentTimeMillis();
        this.damage = damage;
    }


    public LivingEntity getDamager() {
        return damager;
    }

    /**
     * @return Name of damager
     */
    public String getName() {
        return name;
    }

    public String getCause() {
        return cause;
    }

    public long getExpiry() {
        return expiry;
    }

    public double getDamage() {
        return damage;
    }
}
