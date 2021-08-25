package net.betterpvp.clans.farming.bee.nms;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityBee;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CustomBee extends EntityBee {

    public CustomBee(World world) {
        super(EntityTypes.g, world);
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
