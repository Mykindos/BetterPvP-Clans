package net.betterpvp.clans.farming.bee.nms;

import net.minecraft.server.v1_16_R1.Entity;
import net.minecraft.server.v1_16_R1.EntityBee;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.World;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CustomBee extends EntityBee {

    public CustomBee(World world) {
        super(EntityTypes.BEE, world);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        return false;
    }

    @Override
    public void anger() {

    }

    public Bee spawn(Location loc) {
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (Bee) getBukkitEntity();
    }

}
