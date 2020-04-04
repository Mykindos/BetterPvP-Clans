package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.entity.Player;

public class EnchantsCommand extends Command {

    public EnchantsCommand() {
        super("enchants", new String[]{}, Rank.ADMIN);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args == null){
            for(Weapon w : WeaponManager.weapons){
                if(w instanceof EnchantedWeapon){
                    if(w.getMaterial().name().contains("HELMET") || w.getMaterial().name().contains("SWORD")){
                        player.getInventory().addItem(w.createWeapon());
                    }
                }
            }
        }else{
            if(args.length == 1){
                for(Weapon w : WeaponManager.weapons){
                    if(w instanceof EnchantedWeapon){
                        if(w.getMaterial().name().toLowerCase().contains(args[0].toLowerCase())){
                            player.getInventory().addItem(w.createWeapon());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void help(Player player) {
    }

}