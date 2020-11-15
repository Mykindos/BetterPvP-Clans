package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossSpider extends EntitySpider {

    public BossSpider(World world) {
        super(EntityTypes.SPIDER, world);

    }

    @Override
    public boolean eM() {
        return false;
    }

    public Spider spawnSpider(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Spider) getBukkitEntity();
    }


}
