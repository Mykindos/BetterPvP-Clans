package net.betterpvp.clans.skills.selector.skills.data;

import org.bukkit.entity.Player;

public class BlinkData {

    private Player p;
    private int charges;
    private long lastRecharge;

    /**
     * @param p            Player
     * @param charges      Amount of charges the player has
     * @param lastRecharge Epoch time when charge was last added
     */
    public BlinkData(Player p, int charges, long lastRecharge) {
        this.p = p;
        this.charges = charges;
        this.lastRecharge = lastRecharge;
    }


    /**
     * @return Player
     */
    public Player getPlayer() {
        return p;
    }

    public int getCharges() {
        return charges;
    }

    public long getLastRecharge() {
        return lastRecharge;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    public void setLastRecharge(long recharge) {
        this.lastRecharge = recharge;
    }

}
