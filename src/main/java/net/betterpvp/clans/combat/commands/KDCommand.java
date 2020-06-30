package net.betterpvp.clans.combat.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class KDCommand extends Command {

    public KDCommand() {
        super("kd", new String[] {"ratio"}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null){
            if(args.length == 1){

                Gamer gamer = GamerManager.searchGamer(player, args[0], false);
                if(gamer != null){
                    UtilMessage.message(player, ChatColor.GRAY + "Kill / Death Stats for " + ChatColor.YELLOW + gamer.getClient().getName());
                    UtilMessage.message(player, ChatColor.YELLOW + "Kills: " + ChatColor.GRAY + gamer.getKills());
                    UtilMessage.message(player, ChatColor.YELLOW + "Deaths: " + ChatColor.GRAY + gamer.getDeaths());
                    if(gamer.getDeaths() != 0){
                        UtilMessage.message(player, ChatColor.YELLOW + "KD: " + ChatColor.GRAY + new DecimalFormat("#.00").format(( (double) gamer.getKills() / (double) gamer.getDeaths())));
                    }else{
                        UtilMessage.message(player, ChatColor.YELLOW + "KD: " + ChatColor.GRAY + gamer.getKills());
                    }
                }
            }else{
                UtilMessage.message(player, "Correct Usage: /kd {player}");
            }
        }else{
            Gamer gamer = GamerManager.getGamer(player);
            UtilMessage.message(player, ChatColor.GRAY + "Kill / Death Stats");
            UtilMessage.message(player, ChatColor.YELLOW + "Kills: " + ChatColor.GRAY + gamer.getKills());
            UtilMessage.message(player, ChatColor.YELLOW + "Deaths: " + ChatColor.GRAY + gamer.getDeaths());
            if(gamer.getDeaths() != 0){
                UtilMessage.message(player, ChatColor.YELLOW + "KD: " + ChatColor.GRAY +  new DecimalFormat("#.00").format(((double)gamer.getKills() / (double) gamer.getDeaths())));
            }else{
                UtilMessage.message(player, ChatColor.YELLOW + "KD: " + ChatColor.GRAY + gamer.getKills());
            }
        }

    }

    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }

}