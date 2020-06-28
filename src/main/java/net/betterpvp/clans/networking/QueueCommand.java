package net.betterpvp.clans.networking;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class QueueCommand extends Command {

    private Plugin instance;

    public QueueCommand(Plugin instance) {
        super("queue", new String[]{}, Rank.PLAYER);
        this.instance = instance;
    }

    @Override
    public void execute(Player player, String[] strings) {
        // TODO implement proper queue

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("clans");

        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }

    @Override
    public void help(Player player) {

    }
}
