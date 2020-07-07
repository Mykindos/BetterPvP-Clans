package net.betterpvp.clans.classes.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.mysql.BuildRepository;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FixBuildsCommand extends Command {

    public FixBuildsCommand() {
        super("fixbuilds", new String[]{}, Rank.DEVELOPER);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args != null) {
            if(args[0].equals("ALL")) {
                for (Gamer gamer : GamerManager.getGamers()) {
                    if (gamer.getBuilds().isEmpty()) {
                        gamer.loadDefaults();
                    }
                }
            }else{
                Player p = Bukkit.getPlayer(args[0]);
                if(p != null) {
                    Gamer gamer = GamerManager.getOnlineGamer(p);
                    if(gamer != null){
                        gamer.loadDefaults();
                        UtilMessage.message(p, "Builds", "Your builds have been reset!");
                    }
                }
            }
        }
    }

    @Override
    public void help(Player player) {

    }
}
