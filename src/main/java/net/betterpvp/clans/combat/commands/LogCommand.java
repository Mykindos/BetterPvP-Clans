package net.betterpvp.clans.combat.commands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.safelog.SafeLog;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class LogCommand extends Command {

    public LogCommand() {
        super("quit", new String[]{"log"}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args == null || args.length == 0) {
            if (SafeLog.isLoggingOut(player)) {
                UtilMessage.message(player, "Log", "You are already logging out.");
                return;
            }

            if(UtilClans.hasValuables(player)){
                UtilMessage.message(player, "Log", "You cannot log with valuables!");
                return;
            }

            if(player.isFlying()){
                UtilMessage.message(player, "Log", "You cannot log while flying!");
                return;
            }

            Clan pc = ClanUtilities.getClan(player);

            if(pc != null){

                Clan ec = ClanUtilities.getClan(player.getLocation());
                if(ec != null && ec == pc) {
                    if(System.currentTimeMillis() < pc.getLastTnted()){
                        UtilMessage.message(player, "Log", "You cannot log while under attack!");
                        return ;
                    }
                }


                if(ec != null){

                    if(pc.isEnemy(ec)){

                        UtilMessage.message(player, "Log", "You cannot log in enemy territory!");
                        return;
                    }
                }
            }



            new SafeLog(player);
        }
    }

    @Override
    public void help(Player player) {

    }

}