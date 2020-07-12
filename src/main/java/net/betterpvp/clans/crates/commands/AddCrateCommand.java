package net.betterpvp.clans.crates.commands;

import net.betterpvp.clans.crates.Crate;
import net.betterpvp.clans.crates.CrateManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.command.IServerCommand;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddCrateCommand extends Command implements IServerCommand {

    public AddCrateCommand() {
        super("addcrate", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(Player player, String[] args) {
        serverCmdExecute(player, args);
    }

    @Override
    public void help(Player player) {

    }

    @Override
    public void serverCmdExecute(CommandSender commandSender, String[] args) {
        if (args != null) {
            if (args.length >= 2) {
                Crate c = CrateManager.getCrate(args[1]);
                if (c != null) {
                    Player p = Bukkit.getPlayer(args[0]);
                    if (p != null) {
                        int amount = 1;
                        if (args.length == 3) {
                            amount = Integer.valueOf(args[2]);
                        }

                        for (int i = 0; i < amount; i++) {
                            p.getInventory().addItem(c.getCrate());
                        }
                        UtilMessage.message(p, ChatColor.stripColor(c.getName()), "You received " + ChatColor.GREEN + amount + ChatColor.GRAY + " " + c.getName() + "(s)");
                    }
                }
            }

        }
    }
}
