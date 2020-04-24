package net.betterpvp.clans.general.commands;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;


public class HubCommand extends Command {

    private Clans instance;
    public HubCommand(Clans i) {
        super("hub", new String[] {"lobby"}, Rank.PLAYER);
        this.instance = i;

    }

    @Override
    public void execute(Player player, String[] args) {


        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("hub");

        player.sendPluginMessage(instance, "BungeeCord", out.toByteArray());
    }

    @Override
    public void help(Player player) {
    }

}