package net.betterpvp.clans.combat.combatlog;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CombatTag {

    public static List<CombatTag> tagged = new ArrayList<CombatTag>();

    private UUID uuid;
    private Long time;

    public CombatTag(UUID uuid) {
        this.uuid = uuid;
        this.time = System.currentTimeMillis() + 32000L;
        tagged.add(this);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public static CombatTag getCombatTag(UUID uuid) {
        for (CombatTag combat : tagged) {
            if (combat.getUUID().equals(uuid)) {
                return combat;
            }
        }
        return null;
    }
}