package net.betterpvp.clans.settings;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ReloadCommand extends Command {


    public ReloadCommand() {
        super("creload", new String[]{}, Rank.OWNER);

    }

    @Override
    public void execute(Player player, String[] args) {
        UtilMessage.message(player, "Server", "Options reloaded.");
        Clans.getOptions().reloadOptions();

    }

    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }

}
