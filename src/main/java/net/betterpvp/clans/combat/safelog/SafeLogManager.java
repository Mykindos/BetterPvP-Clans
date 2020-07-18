package net.betterpvp.clans.combat.safelog;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.combat.combatlog.CombatLog;
import net.betterpvp.clans.combat.combatlog.CombatTag;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;

import java.util.Collections;
import java.util.ListIterator;

public class SafeLogManager extends BPVPListener<Clans> {

    public SafeLogManager(Clans i) {
        super(i);
    }

    @EventHandler
    public void handleDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (SafeLog.isLoggingOut(player)) {
                SafeLog.getSafeLog(player).cancel();
                UtilMessage.message(player, "Log", "Log cancelled due to damage.");
            }
        }
    }

    @EventHandler
    public void handleInteract(PlayerInteractEvent event) {
        if (SafeLog.isLoggingOut(event.getPlayer())) {
            SafeLog.getSafeLog(event.getPlayer()).cancel();
            UtilMessage.message(event.getPlayer(), "Log", "Log cancelled due to interaction.");
        }
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        if (SafeLog.isLoggingOut(event.getPlayer())) {
            SafeLog.getSafeLog(event.getPlayer()).cancel();
        }
    }

    @EventHandler
    public void handleKick(PlayerKickEvent event) {
        if (SafeLog.isLoggingOut(event.getPlayer())) {
            SafeLog.getSafeLog(event.getPlayer()).cancel();
        }
    }

    @EventHandler
    public void handlePickup(PlayerPickupItemEvent event) {
        if (SafeLog.isLoggingOut(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        if (SafeLog.isLoggingOut(event.getPlayer())) {
            SafeLog.getSafeLog(event.getPlayer()).cancel();
        }
    }

    @EventHandler
    public synchronized void update(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.TICK) {
            if (SafeLog.loggers.isEmpty()) {
                return;
            }

            ListIterator<SafeLog> iterator = Collections.synchronizedList(SafeLog.loggers).listIterator();
            while (iterator.hasNext()) {
                SafeLog log = iterator.next();
                Player player = log.getPlayer();

                if (player == null) {
                    iterator.remove();
                    return;
                }

                if (log.getSeconds() < 0) {
                    iterator.remove();
                    return;
                }

                if (UtilMath.offset(log.getLocation(), player.getLocation()) > 0.5D) {
                    UtilMessage.message(player, "Log", "Log cancelled due to movement.");
                    iterator.remove();
                    return;
                }

                if (log.getSeconds() <= 0) {
                    if (UtilTime.elapsed(log.getTime(), 1000L)) {
                        if (CombatTag.getCombatTag(player.getUniqueId()) != null) {
                            CombatTag.tagged.remove(CombatTag.getCombatTag(player.getUniqueId()));
                        }

                        if (CombatLog.getCombatLog(player) != null) {
                            CombatLog.loggers.remove(CombatLog.getCombatLog(player));
                        }
                        if (UtilClans.hasValuables(player)) {
                            iterator.remove();
                            return;
                        }

                        Gamer c = GamerManager.getOnlineGamer(player);
                        c.setSafeLogged(true);
                        //player.kickPlayer(ChatColor.RED + "[Log] " + ChatColor.GRAY + "Safely Logged Out");
                        ByteArrayDataOutput out = ByteStreams.newDataOutput();
                        out.writeUTF("KickPlayer");
                        out.writeUTF(player.getName());
                        out.writeUTF("You safe logged.");
                        player.sendPluginMessage(getInstance(), "BungeeCord", out.toByteArray());
                        iterator.remove();


                        return;
                    }
                }

                log.updateLog();
            }
        }
    }
}