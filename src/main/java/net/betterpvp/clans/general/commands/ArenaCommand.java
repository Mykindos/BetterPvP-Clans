package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ArenaCommand extends Command {

    public ArenaCommand() {
        super("arena", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (Clans.getOptions().isHub()) {
            Clan lClan = ClanUtilities.getClan(player.getLocation());
            if (lClan == null) {
                player.teleport(new Location(player.getWorld(), -101.5, 59, -54.5, -160.2f, -3.5f));
            }
        }
    }

    @Override
    public void help(Player player) {

    }
}
