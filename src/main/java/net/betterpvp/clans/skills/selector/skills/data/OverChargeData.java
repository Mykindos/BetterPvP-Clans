package net.betterpvp.clans.skills.selector.skills.data;

import java.util.UUID;

public class OverChargeData {


    private UUID uuid;
    private int charge;
    private long lastCharge;
    private int increment;
    private int maxCharge;

    public OverChargeData(UUID uuid, int increment, int maxCharge) {
        this.uuid = uuid;
        this.charge = 0;
        this.lastCharge = System.currentTimeMillis();
        this.increment = increment;
        this.maxCharge = maxCharge;

    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
        this.charge = charge;
    }

    public void addCharge() {
        if (getCharge() <= getMaxCharge()) {
            setCharge(getCharge() + getIncrement());
        }
    }

    public long getLastCharge() {
        return lastCharge;
    }

    public void setLastCharge(long lastCharge) {
        this.lastCharge = lastCharge;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public void setMaxCharge(int maxCharge) {
        this.maxCharge = maxCharge;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }


}
