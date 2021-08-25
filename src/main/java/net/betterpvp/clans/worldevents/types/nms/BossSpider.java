package net.betterpvp.clans.worldevents.types.nms;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossSpider extends EntitySpider {

    public BossSpider(World world) {
        super(EntityTypes.aI, world);

    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    public Spider spawnSpider(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, SpawnReason.CUSTOM);
        return (Spider) getBukkitEntity();
    }


}
