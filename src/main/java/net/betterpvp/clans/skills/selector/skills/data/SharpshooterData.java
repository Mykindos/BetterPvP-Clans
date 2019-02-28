package net.betterpvp.clans.skills.selector.skills.data;

import java.util.UUID;

public class SharpshooterData {


    private UUID uuid;
    private int charge;
    private long lastHit;

    public SharpshooterData(UUID uuid) {
        this.uuid = uuid;
        this.charge = 0;
        this.lastHit = System.currentTimeMillis();
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
        setCharge(getCharge() + 1);
    }

    public Long getLastHit() {
        return lastHit;
    }

    public void setLastHit(long lastHit) {
        this.lastHit = lastHit;
    }


}
