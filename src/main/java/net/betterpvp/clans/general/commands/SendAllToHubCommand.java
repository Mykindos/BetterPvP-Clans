package net.betterpvp.clans.general.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.betterpvp.clans.Clans;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendAllToHubCommand extends Command {

    private Clans instance;

    public SendAllToHubCommand(Clans instance) {
        super("sendalltohub", new String[]{}, Rank.DEVELOPER);
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] strings) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.equals(player)) continue;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("hub");

            p.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
        }
    }

    @Override
    public void help(Player player) {

    }
}
