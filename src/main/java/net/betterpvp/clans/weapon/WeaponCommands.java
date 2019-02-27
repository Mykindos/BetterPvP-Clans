package net.betterpvp.clans.weapon;

import net.betterpvp.clans.client.Rank;
import net.betterpvp.clans.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class WeaponCommands extends Command{
	public WeaponCommands() {
		super("legendaryset", new String[]{}, Rank.ADMIN);
	}

	@Override
	public void execute(Player player, String[] args) {

		for(Weapon weapon : Weapon.weapons){
			if(weapon.isLegendary()){
				player.getInventory().addItem(weapon.createWeapon());
			}
		}

		/*
		player.getInventory().addItem(UtilPlayer.createItem(Material.IRON_SWORD, 1, ChatColor.RED + "Magnetic Blade", "",
				ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7",
				ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Magnetic Pull",
				ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "Negative 40%",
				"",
				ChatColor.RESET + "The Magnetic Blade is said to be able",
				ChatColor.RESET + "to pull nearby objects towards itself",
				ChatColor.RESET + "with unstoppable force.",
				""));

		player.getInventory().addItem(UtilPlayer.createItem(Material.BOW, 1, ChatColor.RED + "Meteor Bow", "",
				ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "10 (AoE)",
				ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Explosive Arrows",
				"",
				ChatColor.RESET + "The mythical bow that reigned down",
				ChatColor.RESET + "hell from the heavens. Each shot",
				ChatColor.RESET + "is as deadly as a meteor.",
				""));

		player.getInventory().addItem(UtilPlayer.createItem(Material.IRON_SWORD, 1, ChatColor.RED + "Wind Blade", "",
				ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7",
				ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Wind Rider",
				ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "300%",
				"",
				ChatColor.RESET + "Once owned by the God Zephyrus,",
				ChatColor.RESET + "it is rumoured the Wind Blade",
				ChatColor.RESET + "grants its owner flight.",
				""));

		player.getInventory().addItem(UtilPlayer.createItem(Material.IRON_HOE, 1, ChatColor.RED + "Lightning Scythe",
				"",
				ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "10 (AoE)",
				ChatColor.GRAY + "Active: " + ChatColor.YELLOW + "Lightning Strike",
				"",
				ChatColor.RESET + "This mysterious weapon is believed",
				ChatColor.RESET + "to call the power of the gods",
				ChatColor.RESET + "and strike lightning on your enemies.",
				""));

		player.getInventory().addItem(UtilPlayer.createItem(Material.IRON_SWORD, 1, ChatColor.RED + "Alligators Tooth",
				"",
				ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7 + 2 in Water",
				ChatColor.GRAY + "Ability: " + ChatColor.YELLOW + "Alliagtor Rush",
				ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Water Breathing",
				ChatColor.GRAY + "Knockback: " + ChatColor.YELLOW + "100%",
				"",
				ChatColor.RESET + "A blade forged from hundreds of ",
				ChatColor.RESET + "alligators teeth. It's powers allow ",
				ChatColor.RESET + "its owner to swim with great speed, ",
				ChatColor.RESET + "able to catch any prey.",
				""));
		 */
		UtilMessage.message(player, "Legendaries", "All legendaries have been added to your inventory");


	}

	@Override
	public void help(Player player) {
		// TODO Auto-generated method stub

	}


}
