package net.betterpvp.clans.clans.map;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.configs.ConfigManager;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ToggleMapCommand extends Command {

    public ToggleMapCommand() {
        super("togglemap", new String[]{}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] strings) {
        Clans.getOptions().toggleAdvancedMap();
        UtilMessage.message(player, "Advanced Map", Clans.getOptions().isAdvancedMap() + "");
    }

    @Override
    public void help(Player player) {

    }
}
