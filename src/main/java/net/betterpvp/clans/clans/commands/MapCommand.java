package net.betterpvp.clans.clans.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.map.MapListener;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class MapCommand extends Command {

    public MapCommand() {
        super("map", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (Clans.getOptions().isAdvancedMap()) {
            if (player.getInventory().contains(MapListener.map)) {
                UtilMessage.message(player, "Map", "You already have a map in your inventory");
                return;
            }
            player.getInventory().addItem(MapListener.map.clone());
            UtilMessage.message(player, "Map", "A map has been added to your inventory.");
        }
    }

    @Override
    public void help(Player player) {

    }
}
