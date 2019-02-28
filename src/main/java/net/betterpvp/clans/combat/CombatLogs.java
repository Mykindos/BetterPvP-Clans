package net.betterpvp.clans.combat;

import org.bukkit.entity.LivingEntity;

public class CombatLogs {

    private LivingEntity damager;
    private String cause;
    private String name;
    private Long expiry;

    public CombatLogs(LivingEntity damager, String name, String cause) {
        this.damager = damager;
        this.name = name;
        if (cause != null) {
            this.cause = cause;
        } else {
            this.cause = "";
        }

        this.expiry = System.currentTimeMillis();
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

}
