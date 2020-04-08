package net.betterpvp.clans.clans.commands;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ClanRelationshipEvent;
import net.betterpvp.clans.clans.mysql.EnemyRepository;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SetDomCommand extends Command {

    public SetDomCommand() {
        super("setdom", new String[] {"setwps", "setdominance"}, Rank.ADMIN);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args != null) {
            if(args.length == 3) {
                Clan aClan = ClanUtilities.getClan(args[0]);
                Clan bClan = ClanUtilities.getClan(args[1]);
                int points = Integer.valueOf(args[2]);

                if(aClan != null && bClan != null) {
                    if(aClan.isEnemy(bClan)) {
                        aClan.getDominance(bClan).setPoints(points);
                        EnemyRepository.updateDominance(aClan.getDominance(bClan));
                        EnemyRepository.updateDominance(bClan.getDominance(aClan));

                        Bukkit.getPluginManager().callEvent(new ClanRelationshipEvent(aClan, bClan));

                        UtilMessage.message(player, "Admin", "Updated dominance for " + ChatColor.GREEN + aClan.getName()
                                + ChatColor.GRAY + " and " + ChatColor.GREEN + bClan.getName());
                    }
                }
            }
        }
    }

    @Override
    public void help(Player player) {
    }

}