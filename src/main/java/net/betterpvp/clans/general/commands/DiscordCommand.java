package net.betterpvp.clans.general.commands;

import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DiscordCommand extends Command {

    public DiscordCommand() {
        super("discord", new String[] {}, Rank.PLAYER);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {
        UtilMessage.message(player, ChatColor.GRAY + "Join our Discord! " + ChatColor.AQUA.toString() + ChatColor.BOLD  + "https://discord.gg/0u0optDwaEARVDyy");
    }

    @Override
    public void help(Player player) {
    }

}