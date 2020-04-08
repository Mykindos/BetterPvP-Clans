package net.betterpvp.clans.skills.selector.skills.data;

import net.minecraft.server.v1_15_R1.EntityArmorStand;
import net.minecraft.server.v1_15_R1.EntityTypes;
import net.minecraft.server.v1_15_R1.World;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * Used for Rupture to ensure entity is invisible when spawned
 */
public class CustomArmorStand extends EntityArmorStand {
    public CustomArmorStand(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    public ArmorStand spawn(Location loc) {

        setInvisible(true);
        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (ArmorStand) getBukkitEntity();
    }
}
