package net.betterpvp.clans.general.commands.menu;

import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LegendaryMenu extends Menu {

    public LegendaryMenu(Player player) {
        super(player, 18, ChatColor.GREEN + "Pick a legendary", new Button[]{});
        fillPage();
        construct();
    }

    public void fillPage(){
        int x = 0;
        for(Weapon wep : WeaponManager.weapons){
            if(wep instanceof ILegendary){
                addButton(new Button(x, wep.createWeapon(false), wep.getName(), wep.getLore()));
                x++;
            }


        }
    }

}
