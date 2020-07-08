package net.betterpvp.clans.networking;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.networking.NetworkReceiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SlotListener extends BPVPListener<Clans> {

    public SlotListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK_2) {
            int count = 0;
            if (Bukkit.getServer().hasWhitelist()) {
                return;
            }

            if(!getInstance().hasStarted()){
                return;
            }

            NetworkReceiver.sendNetworkMessage("127.0.0.1", Clans.getOptions().getHubPort(), "Bungee", "ReservedSlotAllowed");

            for (Player p : Bukkit.getOnlinePlayers()) {
                Client pClient = ClientUtilities.getOnlineClient(p);
                if (pClient != null) {
                    if (pClient.hasDonation("ReservedSlot") || pClient.hasRank(Rank.TRIAL_MOD, false)) {
                        count++;
                    }
                }
            }

            if (Bukkit.getOnlinePlayers().size() < Bukkit.getServer().getMaxPlayers() + count) {
                NetworkReceiver.sendNetworkMessage("127.0.0.1", Clans.getOptions().getHubPort(), "Bungee", "SlotAvailable");
                return;
            }
        }
    }
}
