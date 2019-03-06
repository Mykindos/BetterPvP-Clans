package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import org.bukkit.entity.Slime;

public abstract class SlimeBase extends WorldEventMinion {


    public SlimeBase(Slime s) {
        super(s);
        s.setSize(getSize());
        s.setMaxHealth(getMaxHealth());
        s.setHealth(getMaxHealth());

    }

    public Slime getSlime() {
        return (Slime) getEntity();
    }

    public abstract int getSize();

}
