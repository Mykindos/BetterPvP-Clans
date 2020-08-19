package net.betterpvp.clans.general;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.fancymessage.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class ServerListener extends BPVPListener<Clans> {
    public ServerListener(Clans instance) {
        super(instance);
    }

    private static final String[] tips = new String[]{
            "Show your support and donate! Type " + ChatColor.GREEN + ChatColor.BOLD + "/buy",
            "Vote daily to receive a" + ChatColor.YELLOW + " voting crate " + ChatColor.GRAY + "(Win up to 50k, TNT and MORE!" + ChatColor.YELLOW + " (/vote)",
            "Don't forget to buy clan energy! If you run out your clan will be disbanded. ('" + ChatColor.YELLOW + "/c shop" + ChatColor.GRAY + "')",
            "Not sure where fields are located? Type " + ChatColor.YELLOW + "/coords " + ChatColor.GRAY + "to find out!",
            "Want to learn more about specific features? View our collection of indepth guides! " + ChatColor.YELLOW + "https://betterpvp.net/forum/pages/Guides/",
            "You can teleport to the boss arena's by clicking on the " + ChatColor.YELLOW + "Boss Teleport NPC's" + ChatColor.GRAY + " at Fields!",
            "Sign up on our forums! " + ChatColor.GREEN.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "http://betterpvp.net/forum",
            "Curious to see how you are performing? You can view your class ELO rating by typing " + ChatColor.YELLOW + "/ratings",
            "Not knowing the rules is not an excuse! It will not protect you. " + ChatColor.YELLOW + "/rules",
            "Did you know that the top rated player for each class gets a set of unique wings to flex at the end of the season? " + ChatColor.YELLOW + "/topratings <class>",
            "Join our Discord! " + ChatColor.AQUA.toString() + ChatColor.BOLD + "https://discord.gg/Q4XvxH7",
            "Don't forget to complete your daily quests! (/daily)",
            "Keep BetterPvP alive by donating! Type" + ChatColor.GREEN + ChatColor.BOLD + " /buy",
            "The farming levels are dependent on your clans level!",
            "Can't find the shops? Type " + ChatColor.YELLOW + "/coords " + ChatColor.GRAY + "to find out!",
            "Attempting to bypass MAH may result in a permanant ban!",
            "Sign up on our forums! " + ChatColor.GREEN.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "http://betterpvp.net/forum",
            "Receive bonus online rewards by linking your discord account! " + ChatColor.YELLOW + "/link",
            "Visit our website - " + ChatColor.GREEN.toString() + ChatColor.BOLD + ChatColor.UNDERLINE + "http://betterpvp.net",
    };

    private int count;

    @EventHandler
    public void sendTips(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.MIN_16) {
            if (count >= tips.length - 1) {
                count = 0;
            }

            UtilMessage.broadcast("Tips", tips[count]);
            count++;
        }
    }
}
