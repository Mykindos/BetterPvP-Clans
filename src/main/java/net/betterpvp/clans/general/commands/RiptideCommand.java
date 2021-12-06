package net.betterpvp.clans.general.commands;

import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;


import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
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
            net.minecraft.world.entity.player.Player entityPlayer = ((CraftPlayer) player).getHandle();
            SynchedEntityData dataWatcher = entityPlayer.getEntityData();
            dataWatcher.set(EntityDataSerializers.BYTE.createAccessor(7), (byte) ~0x04);
            active.remove(player.getUniqueId());
        }else{
            net.minecraft.world.entity.player.Player entityPlayer = ((CraftPlayer) player).getHandle();
            SynchedEntityData dataWatcher = entityPlayer.getEntityData();
            dataWatcher.set(EntityDataSerializers.BYTE.createAccessor(7), (byte) 0x04);
            active.add(player.getUniqueId());
        }
    }

    @Override
    public void help(Player player) {

    }
}
