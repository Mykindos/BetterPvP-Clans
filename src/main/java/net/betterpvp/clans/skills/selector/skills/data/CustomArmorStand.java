package net.betterpvp.clans.skills.selector.skills.data;

import net.minecraft.server.v1_16_R1.*;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.CreatureSpawnEvent;

import javax.annotation.Nullable;

/**
 * Used for Rupture to ensure entity is invisible when spawned
 */
public class CustomArmorStand extends EntityArmorStand {
    public CustomArmorStand(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
        setInvisible(true);
    }

    public ArmorStand spawn(Location loc) {


        setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        getWorld().addEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (ArmorStand) getBukkitEntity();
    }

    @Override
    protected SoundEffect getSoundFall(int i) {
        return null;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return null;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return null;
    }

    @Override
    protected void b(ItemStack itemstack) {return;}

    @Override
    protected SoundEffect c(ItemStack itemstack) {
        return null;
    }
}
