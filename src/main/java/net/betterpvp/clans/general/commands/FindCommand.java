package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.WeakHashMap;

public class FindCommand extends Command implements Listener {

    private WeakHashMap<Player, Player> tracker = new WeakHashMap<>();

    public FindCommand() {
        super("find", new String[]{"track"}, Rank.PLAYER);

        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute(Player player, String[] args) {

        if(args != null){
            for(Player p : Bukkit.getOnlinePlayers()){
                if(p.getName().toLowerCase().contains(args[0].toLowerCase())){
                    if(player.getWorld() != p.getWorld()){
                        UtilMessage.message(player, "Find", "Player is not in this world!");
                        return;
                    }
                    tracker.put(player, p);
                    UtilMessage.message(player, "Find", "Compass pointing to location of " +
                            ClanUtilities.getRelation(ClanUtilities.getClan(player), ClanUtilities.getClan(p)).getPrimary() +p.getName());
                    return;
                }
            }
        }else{
            UtilMessage.message(player, "Correct Usage: '/find {entity name}");
        }

    }

    @EventHandler
    public void onUpdate(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.TICK_2){
            for(Player p : tracker.keySet()){
                if(tracker.get(p) != null){
                    p.setCompassTarget(tracker.get(p).getLocation());
                }
            }
        }
    }




    @Override
    public void help(Player player) {
        // TODO Auto-generated method stub

    }

}
