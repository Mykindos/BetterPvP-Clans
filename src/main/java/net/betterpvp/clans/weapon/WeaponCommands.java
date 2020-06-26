package net.betterpvp.clans.weapon;


import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class WeaponCommands extends Command {

    public WeaponCommands() {
        super("legendaryset", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(Player player, String[] args) {

        for (Weapon weapon : WeaponManager.weapons) {
            if (weapon instanceof ILegendary) {
                player.getInventory().addItem(weapon.createWeapon(true));
            }
        }

        UtilMessage.message(player, "Legendaries", "All legendaries have been added to your inventory");


    }

    @Override
    public void help(Player player) {
    }


}
