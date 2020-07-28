package net.betterpvp.clans.dailies.progression;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.UUID;

public class Progress {

    private UUID uuid;
    private String questName;
    private boolean complete;

    public Progress(UUID uuid, String questName) {
        this.uuid = uuid;
        this.questName = questName;
    }


    public UUID getUUID() {
        return uuid;
    }

    public String getQuestName() {
        return questName;
    }

    public boolean isComplete() {
        return complete;
    }

    public void onComplete(UUID player) {
        if (Bukkit.getPlayer(player) != null) {
            double fragments = 5;
            UtilMessage.message(Bukkit.getPlayer(player), "Daily", "You have completed " + ChatColor.YELLOW + getQuestName() + ChatColor.GRAY + " and received "
                    + ChatColor.GREEN + "5000 " + ChatColor.GRAY + "coins and " + ChatColor.GREEN + fragments + ChatColor.GRAY + " fragments.");
            Gamer gamer = GamerManager.getOnlineGamer(player);
            if(gamer != null) {
				gamer.addFragments(fragments);
				gamer.addCoins(5000);
			}

        }

        setComplete(true);
    }

    public void setComplete(boolean b) {
        this.complete = b;
    }

}
