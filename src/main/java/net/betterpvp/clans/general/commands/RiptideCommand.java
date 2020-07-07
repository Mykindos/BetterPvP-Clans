package net.betterpvp.clans.general.commands;

import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.minecraft.server.v1_16_R1.DataWatcher;
import net.minecraft.server.v1_16_R1.DataWatcherRegistry;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RiptideCommand extends Command {

    private List<UUID> active;
    private final byte entityByte = 0x0;

    public RiptideCommand() {
        super("riptide", new String[]{}, Rank.ADMIN);
        active = new ArrayList<>();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(active.contains(player.getUniqueId())) {
            EntityPlayer entityPlayer = (EntityPlayer) ((CraftPlayer) player).getHandle();
            DataWatcher dataWatcher = entityPlayer.getDataWatcher();
            dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) ~0x04);
            active.remove(player.getUniqueId());
        }else{
            EntityPlayer entityPlayer = (EntityPlayer) ((CraftPlayer) player).getHandle();
            DataWatcher dataWatcher = entityPlayer.getDataWatcher();
            dataWatcher.set(DataWatcherRegistry.a.a(7), (byte) 0x04);
            active.add(player.getUniqueId());
        }
    }

    @Override
    public void help(Player player) {

    }
}
