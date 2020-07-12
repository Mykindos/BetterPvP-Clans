package net.betterpvp.clans.networking;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.mykindos.MAH.user.MAHManager;
import me.mykindos.MAH.user.MAHUser;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.networking.events.NetworkMessageEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QueueCommand extends Command implements Listener {

    private Plugin instance;
    private List<UUID> queue;
    private List<UUID> reserved;
    private boolean queueEnabled;
    private boolean canJoin;
    private boolean reservedSlotAllowed;

    public QueueCommand(Plugin instance) {
        super("queue", new String[]{}, Rank.PLAYER);
        this.instance = instance;
        queue = new ArrayList<>();
        reserved = new ArrayList<>();
        queueEnabled = true;
    }

    @Override
    public void execute(Player player, String[] args) {
        // TODO implement proper queue
        if (args == null) {
            if (ClientUtilities.getOnlineClient(player).hasRank(Rank.MODERATOR, false)) {
                connectPlayer(player);
            }
        } else {
            if (args.length == 1) {
                if (ClientUtilities.getOnlineClient(player).hasRank(Rank.ADMIN, false)) {
                    if (args[0].equalsIgnoreCase("toggle")) {
                        queueEnabled = !queueEnabled;
                        UtilMessage.message(player, "Queue", "Status: " + ChatColor.GREEN + queueEnabled);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onNetworkMessage(NetworkMessageEvent e){
        if(e.getChannel().equals("Bungee")){
            if(e.getMessage().equals("SlotAvailable")){
                canJoin = true;
            }else if(e.getMessage().equalsIgnoreCase("ReservedSlotAllowed")){
                reservedSlotAllowed = true;
            }
        }
    }

    @Override
    public void help(Player player) {
    }

    @EventHandler
    public void onTexturepackStatus(PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            if (Clans.getOptions().isHub()) {
                // TODO add MAH check


                MAHUser user = MAHManager.getOnlineMAHUser(e.getPlayer());
                Client client = user.getClient();
                if (client.hasRank(Rank.MODERATOR, false)) {
                    UtilMessage.message(e.getPlayer(), "Queue", "Type " + ChatColor.GREEN + "/queue" + ChatColor.GRAY + " to join the server (Mod+ only).");
                    return;
                }

                if(user.isForced() && !user.isAuthenticated()){
                    UtilMessage.message(e.getPlayer(), "MAH", "You need to authenticate with MAH before joining the queue!");
                    return;
                }
                if(client.hasDonation("ReservedSlot")){
                    UtilMessage.message(e.getPlayer(), "Queue", "As you have a reserved slot, you skip the queue.");
                    if (!reserved.contains(e.getPlayer().getUniqueId())) {
                        reserved.add(e.getPlayer().getUniqueId());
                    }
                    return;
                }
                if (!queue.contains(e.getPlayer().getUniqueId())) {
                    queue.add(e.getPlayer().getUniqueId());
                    UtilMessage.message(e.getPlayer(), "Clans", "You have joined the queue! Current Position: " + ChatColor.GREEN + queue.size());
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(e.getPlayer()));
                }
            }
        }
    }

    @EventHandler
    public void onProcessQueue(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK_2) {
            if (Clans.getOptions().isHub()) {
                if (queueEnabled) {
                    if(reservedSlotAllowed) {
                        if (!reserved.isEmpty()) {
                            Player player = Bukkit.getPlayer(reserved.get(0));
                            if (player != null) {
                                connectPlayer(player);
                            }
                        }
                    }
                    reservedSlotAllowed = false;

                    if (queue.isEmpty()) return;
                    if(canJoin) {
                        Player player = Bukkit.getPlayer(queue.get(0));
                        if (player != null) {
                            connectPlayer(player);
                        }
                        canJoin = false;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Clans.getOptions().isHub()) {
            if (queue.contains(e.getPlayer().getUniqueId())) {
                queue.remove(e.getPlayer().getUniqueId());
                Bukkit.getOnlinePlayers().forEach(player -> {
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(player));
                });

            }

            if(reserved.contains(e.getPlayer().getUniqueId())){
                reserved.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onScoreboardUpdate(ScoreboardUpdateEvent e) {

        if (Clans.getOptions().isHub()) {


            Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());

            Scoreboard scoreboard = e.getPlayer().getScoreboard();
            if (scoreboard != null) {
                if (gamer != null) {

                    Objective side = scoreboard.getObjective("BetterPvP");
                    if (side == null) {
                        side = scoreboard.registerNewObjective("BetterPvP", "dummy", "BetterPvP");
                        side.setDisplaySlot(DisplaySlot.SIDEBAR);
                        side.setDisplayName(ChatColor.GOLD.toString() + ChatColor.BOLD + "  2020 Season 1  ");
                    }


                    for (String s : scoreboard.getEntries()) {
                        scoreboard.resetScores(s);
                    }

                    if(queue.contains(e.getPlayer().getUniqueId())) {
                        side.getScore("§r").setScore(3);
                        side.getScore(ChatColor.GRAY.toString() + ChatColor.BOLD + "Position in queue:").setScore(2);
                        side.getScore(ChatColor.GREEN.toString() + ChatColor.BOLD + (queue.indexOf(e.getPlayer().getUniqueId()) + 1)).setScore(1);

                    }
                }
            }
        }
    }

    private void connectPlayer(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("clans");

        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }
}
