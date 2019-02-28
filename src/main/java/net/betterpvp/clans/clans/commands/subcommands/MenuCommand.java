package net.betterpvp.clans.clans.commands.subcommands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.menus.ClanMenu;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MenuCommand implements IClanCommand {

    public Clans i;

    public MenuCommand(Clans i) {
        this.i = i;
    }

    @Override
    public void run(Player player, String[] args) {
        if (ClanUtilities.getClan(player) != null) {
            final ClanMenu menu = new ClanMenu(player, ClanUtilities.getClan(player));
            player.openInventory(menu.getInventory());
            new BukkitRunnable() {
                @Override
                public void run() {
                    menu.construct();
                }
            }.runTaskLater(i, 2);
        }
    }

    @Override
    public String getName() {

        return "Menu";
    }


}
