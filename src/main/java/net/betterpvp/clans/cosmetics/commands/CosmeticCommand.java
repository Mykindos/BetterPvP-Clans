package net.betterpvp.clans.cosmetics.commands;

import net.betterpvp.clans.cosmetics.menu.menus.CosmeticMenu;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.entity.Player;

public class CosmeticCommand extends Command {

    public CosmeticCommand() {
        super("cosmetic", new String[]{"cosmetics"}, Rank.PLAYER);
    }

    @Override
    public void execute(Player player, String[] args) {

        player.openInventory(new CosmeticMenu(player).getInventory());

    }

    @Override
    public void help(Player player) {

    }
}
