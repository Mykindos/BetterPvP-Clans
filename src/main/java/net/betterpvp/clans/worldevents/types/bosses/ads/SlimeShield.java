package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Slime;

public class SlimeShield extends SlimeBase {

    public SlimeShield(Slime s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getDisplayName() {

        return ChatColor.AQUA + "Slime Shield";
    }

    @Override
    public double getMaxHealth() {

        return 5;
    }

    @Override
    public int getSize() {

        return 2;
    }

}
