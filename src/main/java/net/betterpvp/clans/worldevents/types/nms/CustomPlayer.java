package net.betterpvp.clans.worldevents.types.nms;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class CustomPlayer extends EntityPlayer {

    public CustomPlayer(MinecraftServer minecraftserver,
                        WorldServer worldserver, GameProfile gameprofile,
                        PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
        // TODO Auto-generated constructor stub
    }

}
