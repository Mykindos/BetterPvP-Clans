package net.betterpvp.clans.skills.selector.skills.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HuntersThrillData {

    public static List<HuntersThrillData> data = new ArrayList<HuntersThrillData>();

    private UUID uuid;
    private int charge;
    private long lastHit;

    public HuntersThrillData(UUID uuid) {
        this.uuid = uuid;
        this.charge = 0;
        this.lastHit = System.currentTimeMillis();
        data.add(this);
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

    public static HuntersThrillData getHuntersThrillData(UUID uuid) {
        for (HuntersThrillData hunter : data) {
            if (hunter.getUUID().equals(uuid)) {
                return hunter;
            }
        }
        return null;
    }
}
