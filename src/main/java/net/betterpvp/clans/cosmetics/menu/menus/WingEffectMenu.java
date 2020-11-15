package net.betterpvp.clans.cosmetics.menu.menus;

import net.betterpvp.clans.cosmetics.CosmeticType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class WingEffectMenu extends CosmeticSubMenu {

    public WingEffectMenu(Player player) {
        super(player, 9, ChatColor.RED.toString() + ChatColor.BOLD + "  -  Wing Effects  -  ", CosmeticType.WINGS);
    }

}
