package net.betterpvp.clans.clans.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.UUID;

public class ClanSpyCommand extends Command implements Listener {

    private ArrayList<UUID> spying = new ArrayList<>();

    public ClanSpyCommand() {
        super("ccspy", new String[]{}, Rank.ADMIN);
    }

    @Override
    public void execute(Player p, String[] args) {
        if(spying.contains(p.getUniqueId())){
            UtilMessage.message(p, "Clan Spy", "Clan spy has been disabled!");
            spying.remove(p.getUniqueId());
        }else{
            UtilMessage.message(p, "Clan Spy", "Clan spy has been enabled!");
            spying.add(p.getUniqueId());
        }

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();

        if (ClanChatCommand.enabled.contains(player.getName()) || AllyChatCommand.enabled.contains(player.getName())) {
            Clan c = ClanUtilities.getClan(player);
            if(c != null) {
                for(Player p : Bukkit.getOnlinePlayers()){
                    if(spying.contains(p.getUniqueId())){

                        UtilMessage.message(p, "Clan Spy", ChatColor.GREEN + player.getName() + " " + ChatColor.GRAY + e.getMessage());

                    }
                }
            }

        }


    }



    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }
}