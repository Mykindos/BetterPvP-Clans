package net.betterpvp.clans.economy.commands;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.gamer.mysql.GamerRepository;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MoneyCommand extends Command {

    public MoneyCommand() {
        super("money", new String[]{"balance", "bal", "coins"}, Rank.PLAYER);
    }

    @Override
    public void execute(final Player player, String[] args) {
        if (args == null || args.length == 0) {
            Gamer gamer = GamerManager.getOnlineGamer(player);

            UtilMessage.message(player, "Coins", "Balance = " + ChatColor.YELLOW
                    + UtilFormat.formatNumber(gamer.getCoins()) + " Coins");
            UtilMessage.message(player, "Fragments", "Balance = " + ChatColor.YELLOW
                    + gamer.getFragments() + " Fragments");

            return;
        }else if(args.length == 1){
            Gamer gamer = GamerManager.getGamer(args[0]);

            if(gamer != null){
                UtilMessage.message(player, "Coins", gamer.getClient().getName() + " Balance = " + ChatColor.YELLOW
                        + UtilFormat.formatNumber(gamer.getCoins()) + " Coins");
                UtilMessage.message(player, "Fragments", gamer.getClient().getName() + " Balance = " + ChatColor.YELLOW
                        + gamer.getFragments() + " Fragments");
            }
        }


        if (args[0].equalsIgnoreCase("pay")  || args[0].equalsIgnoreCase("send")) {
            if (args.length == 1) {
                UtilMessage.message(player, "Coins", "Please specify a player to pay.");
                return;
            }


            if (args.length == 2) {
                UtilMessage.message(player, "Coins", "Please specify a amount to pay.");
                return;
            }

            if (args.length == 3) {

                int amount = Integer.parseInt(args[2]);
                if (amount > 0) {
                    Gamer playerGamer = GamerManager.getGamer(player);
                    Gamer targetGamer = GamerManager.getGamer(args[1]);

                    if(targetGamer == null){
                        targetGamer = GamerManager.searchGamer(player, args[1], true);

                    }

                    if(targetGamer != null){
                        if (playerGamer.equals(targetGamer)) {
                            UtilMessage.message(player, "Coins", "You cannot send money to yourself.");
                        } else {
                            if (playerGamer.getCoins() >= amount) {
                                playerGamer.removeCoins(amount);
                                targetGamer.addCoins(amount);
                                GamerRepository.updateCoins(playerGamer);
                                GamerRepository.updateCoins(targetGamer);

                                UtilMessage.message(player, "Coins", "You sent " + ChatColor.YELLOW + UtilFormat.formatter.format(amount) + " Coins"
                                        + ChatColor.GRAY + " to " + ChatColor.YELLOW + targetGamer.getClient().getName() + ChatColor.GRAY + ".");

                                if(targetGamer.getClient().getPlayer() != null){
                                    UtilMessage.message(targetGamer.getClient().getPlayer(), "Coins", "You received " + ChatColor.YELLOW + UtilFormat.formatter.format(amount) + " Coins"
                                            + ChatColor.GRAY + " from " + ChatColor.YELLOW + player.getName() + ChatColor.GRAY + ".");
                                }

                                Log.write("Client", player.getName() + " sent " + UtilFormat.formatter.format(amount) + " coins to " + targetGamer.getClient().getName());
                                Log.write("Client", targetGamer.getClient().getName() + " received " + UtilFormat.formatter.format(amount) + " from " + player.getName());
                            }
                        }
                    }
                } else {
                    UtilMessage.message(player, "Coins", "You must send a positive amount of money.");
                }
            } else {
                UtilMessage.message(player, "Coins", "Cannot parse integer from string [" + args[2] + "].");
            }
        }else if(args[0].equalsIgnoreCase("give")){
            Client d = ClientUtilities.getOnlineClient(player);
            if(d != null){
                if(d.hasRank(Rank.ADMIN, true)){
                    if(args.length == 4){
                        Gamer g = GamerManager.getGamer(args[1]);
                        if(g != null){
                            if(args[3].equalsIgnoreCase("coins")){
                                g.addCoins(Integer.valueOf(args[2]));
                            }else if(args[3].equalsIgnoreCase("fragments")){
                                g.addFragments(Integer.valueOf(args[2]));
                            }else if(args[3].equalsIgnoreCase("battlecoins")){
                                g.addBattleCoins(Integer.valueOf(args[2]));
                            }else{
                                return;
                            }
                            UtilMessage.message(player, "Money", "Added balance to " + g.getClient().getName());
                            GamerRepository.updateGamer(g);
                        }
                    }
                }
            }
        }else if(args[0].equalsIgnoreCase("take")){
            Client d = ClientUtilities.getOnlineClient(player);
            if(d != null){
                if(d.hasRank(Rank.ADMIN, true)){
                    if(args.length == 4){
                        Gamer g = GamerManager.getGamer(args[1]);
                        if(g != null){
                            if(args[3].equalsIgnoreCase("coins")){
                                g.removeCoins(Integer.valueOf(args[2]));
                            }else if(args[3].equalsIgnoreCase("fragments")){
                                g.takeFragments(Integer.valueOf(args[2]));
                            }else if(args[3].equalsIgnoreCase("battlecoins")){
                                g.setBattleCoins(Math.max(0, g.getBattleCoins() - Integer.valueOf(args[2])));
                            }else{
                                return;
                            }
                            UtilMessage.message(player, "Money", "Removed balance fromm " + g.getClient().getName());
                            GamerRepository.updateGamer(g);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void help(Player player) {

    }
}
