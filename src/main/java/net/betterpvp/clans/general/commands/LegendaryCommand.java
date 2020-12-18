package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.general.commands.menu.LegendaryMenu;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LegendaryCommand extends Command {

    public LegendaryCommand() {
        super("legendary", new String[] {}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        if(Clans.getOptions().isHub()){
            return;
        }
        if(!Clans.getOptions().isKitmap()){
            return;
        }
        Gamer gamer = GamerManager.getOnlineGamer(player);
		if(gamer.getClient().isDiscordLinked()) {
			if(RechargeManager.getInstance().isCooling(player.getName(), "Legendary-Kit")){
				UtilMessage.message(player, "Legendary", "You cannot use this yet! Remaining: "
						+ ChatColor.GREEN + RechargeManager.getInstance().getRemainingString(player.getName(), "Legendary-Kit"));
			}else {
				player.openInventory(new LegendaryMenu(player).getInventory());
			}
		}else {
			UtilMessage.message(player, "Legendary", "You need to link your discord account before you can redeem this. " + ChatColor.GREEN + "/link");
		}

    }

    @Override
    public void help(Player player) {
    }
}
