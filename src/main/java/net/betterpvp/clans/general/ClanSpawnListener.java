package net.betterpvp.clans.general;

import io.github.bananapuncher714.cartographer.core.api.events.chunk.ChunkLoadedEvent;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.map.ClanPixelProvider;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.commands.events.SpawnTeleportEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.Titles;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.*;

public class ClanSpawnListener extends BPVPListener<Clans> {
    private WeakHashMap<Player, Long> spawns;

    public ClanSpawnListener(Clans instance) {
        super(instance);
        spawns = new WeakHashMap<>();
    }

    @EventHandler
    public void onSpawn(SpawnTeleportEvent e) {
        if (spawns.containsKey(e.getPlayer())) {
            UtilMessage.message(e.getPlayer(), "Spawn", "You are already teleporting to spawn.");
            return;
        }

        if (UtilClans.hasValuables(e.getPlayer())) {
            UtilMessage.message(e.getPlayer(), "Spawn", "Unable to teleport with valuable items in your inventory.");
            return;
        }

        Clan clan = ClanUtilities.getClan(e.getPlayer().getLocation());
        if (clan != null) {
            UtilMessage.message(e.getPlayer(), "Spawn", "You can only teleport to spawn from the wilderness.");
            return;
        }

        if(!e.getPlayer().getWorld().getName().equals("world")){
            UtilMessage.message(e.getPlayer(), "Spawn", "You can only teleport to spawn from the main world.");
            return;
        }

        UtilMessage.message(e.getPlayer(), "Spawn", "Teleporting to spawn in " + ChatColor.GREEN + "15 seconds, " + ChatColor.GRAY + "do not move!");
        spawns.put(e.getPlayer(), System.currentTimeMillis() + 15000);

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (spawns.containsKey(e.getPlayer())) {
           if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY()
                    || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {

                UtilMessage.message(e.getPlayer(), "Spawn", "Teleport cancelled.");
                spawns.remove(e.getPlayer());
            }

        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            Iterator<Map.Entry<Player, Long>> it = spawns.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Player, Long> next = it.next();
                if (next.getKey() == null) {
                    it.remove();
                    continue;
                }

                Titles.sendTitle(next.getKey(), 0, 20, 20, "", ChatColor.YELLOW + "Teleporting in " + ChatColor.GREEN +
                        (int) (Math.max(0, UtilTime.convert((next.getValue()) - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1)))
                        + " " + UtilTime.getTimeUnit2(next.getValue() - System.currentTimeMillis()));

                if(UtilClans.hasValuables(next.getKey())){
                    UtilMessage.message(next.getKey(), "Spawn", "You cannot teleport to spawn with valuables.");
                    it.remove();
                    continue;
                }

                if (next.getValue() - System.currentTimeMillis() <= 0) {
                    if (RechargeManager.getInstance().add(next.getKey(), "Spawn", 300, true, false)) {
                        next.getKey().teleport(Bukkit.getWorld("world").getSpawnLocation());
                        UtilMessage.message(next.getKey(), "Spawn", "You teleported to spawn.");
                        it.remove();
                    }
                }
            }
        }
    }

}
