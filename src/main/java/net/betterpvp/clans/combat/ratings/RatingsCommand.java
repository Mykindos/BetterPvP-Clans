package net.betterpvp.clans.combat.ratings;

import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.RoleManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RatingsCommand extends Command {

    public RatingsCommand() {
        super("rating", new String[]{"ratings"}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] args) {

        if(args == null){
            UtilMessage.message(player, ChatColor.GREEN.toString() + ChatColor.BOLD + "Your ratings");
            Gamer gamer = GamerManager.getOnlineGamer(player);
            for(Role role : Role.roles){
                UtilMessage.message(player, ChatColor.YELLOW + role.getName() + " - " + ChatColor.WHITE + gamer.getRating(role));
            }
        }else{
            if(args.length == 1){
                Gamer gamer = GamerManager.getGamer(args[0]);
                if(gamer != null) {
                    UtilMessage.message(player, ChatColor.GREEN.toString() + ChatColor.BOLD + gamer.getClient().getName() + " ratings");

                    for (Role role : Role.roles) {
                        UtilMessage.message(player, ChatColor.YELLOW + role.getName() + " - " + ChatColor.WHITE + gamer.getRating(role));
                    }
                }else{
                    UtilMessage.message(player, "Ratings", "Player does not exist.");
                }
            }
        }
    }

    @Override
    public void help(Player player) {

    }
}
