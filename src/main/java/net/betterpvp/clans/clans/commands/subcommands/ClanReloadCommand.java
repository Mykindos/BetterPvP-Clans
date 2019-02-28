package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.entity.Player;

public class ClanReloadCommand extends Command {

    private Clans i;

    public ClanReloadCommand(Clans i) {
        super("clanreload", new String[]{"creload"}, Rank.ADMIN);
        this.i = i;
    }

    @Override
    public void execute(Player player, String[] args) {
        for (Clan c : ClanUtilities.clans) {
            c.getAlliances().clear();
            c.getEnemies().clear();
            c.getTerritory().clear();
            c.getMembers().clear();
        }
        ClanUtilities.getClans().clear();

        ClanRepository.loadClans(i);

    }

    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }


}
