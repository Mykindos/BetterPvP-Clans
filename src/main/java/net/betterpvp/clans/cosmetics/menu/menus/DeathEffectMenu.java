package net.betterpvp.clans.cosmetics.menu.menus;

import net.betterpvp.clans.cosmetics.CosmeticType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class DeathEffectMenu extends CosmeticSubMenu {

    public DeathEffectMenu(Player player) {
        super(player, 9, ChatColor.RED.toString() + ChatColor.BOLD + "  -  Death Effects  -  ", CosmeticType.DEATHEFFECT);
    }

}
