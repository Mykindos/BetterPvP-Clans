package net.betterpvp.clans.worldevents.types.environmental;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Environmental;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class FishingFrenzy extends Environmental {

    public FishingFrenzy(Clans i) {
        super(i, "FishingFrenzy", WEType.ENVIRONMENTAL, 30);

    }

    @Override
    public void spawn() {
        setStartTime(System.currentTimeMillis());
    }

    @Override
    public String getDisplayName() {

        return ChatColor.GREEN.toString() + ChatColor.BOLD + "Fishing Frenzy";
    }

    @Override
    public Location[] getTeleportLocations() {

        return null;
    }

    @Override
    public void subAnnounce() {
        Bukkit.broadcastMessage(ChatColor.GREEN.toString() + ChatColor.BOLD + "For the duration of the event, you catch 3x fish at Lake or Fields!");
    }

}
