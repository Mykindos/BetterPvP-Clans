package net.betterpvp.clans.dailies.commands;

import org.bukkit.entity.Player;

import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.clans.dailies.menus.QuestMenu;

public class DailyCommand extends Command{

	public DailyCommand() {
		super("daily", new String[]{"dailies"}, Rank.PLAYER);

	}

	@Override
	public void execute(Player player, String[] args) {
		if(args == null){
			player.openInventory(new QuestMenu(player).getInventory());
		}
	}

	@Override
	public void help(Player player) {
		// TODO Auto-generated method stub

	}

}
