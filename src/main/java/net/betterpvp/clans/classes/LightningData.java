package net.betterpvp.clans.classes;

import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;

public class LightningData {

    private LightningStrike strike;
    private LivingEntity caster;

    public LightningData(LightningStrike strike, LivingEntity caster) {
        this.strike = strike;
        this.caster = caster;
    }

    public LightningStrike getStrike() {
        return strike;
    }

    public LivingEntity getCaster() {
        return caster;
    }

}
