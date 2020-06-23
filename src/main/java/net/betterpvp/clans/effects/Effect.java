package net.betterpvp.clans.effects;

import java.util.UUID;

public class Effect {
    private UUID player;
    private EffectType type;
    private long length;
    private long rawLength;
    private int level;

    public Effect(UUID player, EffectType type, long length) {
        this.player = player;
        this.type = type;
        this.rawLength = length;
        this.length = System.currentTimeMillis() + length;
    }

    public Effect(UUID player, EffectType type, int level, long length) {
        this.player = player;
        this.type = type;
        this.rawLength = length;
        this.length = System.currentTimeMillis() + length;
        this.level = level;
    }

    public boolean hasExpired() {
        return length - System.currentTimeMillis() <= 0;
    }

    public UUID getPlayer() {
        return player;
    }

    public EffectType getType() {
        return type;
    }

    public long getLength() {
        return length;
    }

    public long getRawLength() {
        return rawLength;
    }

    public int getLevel() {
        return level;
    }

}
