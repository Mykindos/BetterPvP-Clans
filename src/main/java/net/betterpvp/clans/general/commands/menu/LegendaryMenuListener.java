package net.betterpvp.clans.general.commands.menu;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;

public class LegendaryMenuListener extends BPVPListener<Clans> {

    public LegendaryMenuListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    public void onClick(ButtonClickEvent e){
        if(e.getMenu() instanceof LegendaryMenu){
            UtilItem.insert(e.getPlayer(), WeaponManager.getWeapon(ChatColor.stripColor(e.getButton().getName())).createWeapon(true));
            e.getPlayer().closeInventory();
            RechargeManager.getInstance().add(e.getPlayer(), "Legendary-Kit", 60 * 60 * 24, false, false);
        }
    }
}
