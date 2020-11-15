package net.betterpvp.clans.cosmetics.menu.menus;

import net.betterpvp.clans.cosmetics.CosmeticType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class ParticleEffectMenu extends CosmeticSubMenu {

    public ParticleEffectMenu(Player player) {
        super(player, 9, ChatColor.RED.toString() + ChatColor.BOLD + "  -  Particle Effects  -  ", CosmeticType.PARTICLE);
    }

}
