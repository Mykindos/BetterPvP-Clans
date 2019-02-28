package net.betterpvp.clans.weapon.weapons;

import org.bukkit.entity.Player;

public class LightningScytheData {

    private Player p;
    private int charge;
    private long startTime;
    private long lastCharge;

    public LightningScytheData(Player p, int charge) {
        this.p = p;
        this.charge = charge;
        this.startTime = System.currentTimeMillis();
        this.lastCharge = System.currentTimeMillis();
    }

    public Player getPlayer() {
        return p;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getLastCharge() {
        return lastCharge;
    }

    public void setLastCharge(long l) {
        this.lastCharge = l;
    }

}
