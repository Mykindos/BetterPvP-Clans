package net.betterpvp.clans.worldevents.types.nms;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.PlayerInteractManager;
import net.minecraft.server.v1_15_R1.WorldServer;

public class CustomPlayer extends EntityPlayer {

    public CustomPlayer(MinecraftServer minecraftserver,
                        WorldServer worldserver, GameProfile gameprofile,
                        PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);

    }

}
