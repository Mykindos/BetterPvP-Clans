package net.betterpvp.clans.general;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.crates.Crate;
import net.betterpvp.clans.crates.CrateManager;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.networking.NetworkReceiver;
import net.betterpvp.core.networking.events.NetworkMessageEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

public class VoteListener extends BPVPListener<Clans> {

    private List<String> queue;

    public VoteListener(Clans instance) {
        super(instance);
        queue = new ArrayList<>();
    }

    @EventHandler
    public void onVote(VotifierEvent e) {
        Vote vote = e.getVote();
        System.out.println("Vote from: " + e.getVote().getUsername());
        NetworkReceiver.sendGlobalNetworkMessage("Vote", "AddVote-!-" + vote.getUsername());
    }

    @EventHandler
    public void onNetworkMessage(NetworkMessageEvent e) {
        if (e.getChannel().equals("Vote")) {
            String[] args = e.getMessage().split("-!-");
            if (!Clans.getOptions().isHub()) {
                if (args[0].equals("AddVote")) {
                    queue.add(args[1].toLowerCase());
                }
            }
        }
    }


    @EventHandler
    public void onProcessVote(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SLOW) {
            Crate crate = CrateManager.getCrate("Voting Crate");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (queue.contains(player.getName().toLowerCase())) {
                    long count = queue.stream().filter(p -> player.getName().equalsIgnoreCase(p)).count();
                    for (int i = 0; i < count; i++) {
                        player.getInventory().addItem(crate.getCrate().clone());
                        UtilMessage.broadcast("Vote", ChatColor.YELLOW + player.getName() + ChatColor.GRAY
                                + " voted and received a " + ChatColor.GREEN + "Voting Crate" + ChatColor.GRAY + ".");
                    }

                    Log.write("Vote", player.getName() + " voted " + count + " times.");

                    queue.removeIf(p -> player.getName().equalsIgnoreCase(p));
                }
            }
        }
    }
}
