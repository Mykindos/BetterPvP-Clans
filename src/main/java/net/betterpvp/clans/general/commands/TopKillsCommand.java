package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopKillsCommand extends Command {

    public TopKillsCommand() {
        super("topkills", new String[] {}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        List<Gamer> temp = new ArrayList<>(GamerManager.getGamers());


        Collections.sort(temp, (o1, o2) -> {
            if(o1 == null || o2 == null) return -1;
            return o2.getKills() - o1.getKills();
        });
        UtilMessage.message(player, ChatColor.GREEN + "Kills Leaderboard");
        for(int i = 0; i < 10; i++) {
            if(temp.size() >= i) {
                Gamer c = temp.get(i);
                if(c != null) {
                    UtilMessage.message(player, ChatColor.WHITE.toString() + (i + 1) + ". " + ChatColor.YELLOW + c.getClient().getName() + " - " + ChatColor.RED + c.getKills());
                }
            }
        }
    }

    @Override
    public void help(Player player) {
    }

}