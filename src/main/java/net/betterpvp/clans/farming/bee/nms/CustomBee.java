package net.betterpvp.clans.farming.bee.nms;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class CustomBee extends Bee {

    public CustomBee(Level world) {
        super(EntityType.BEE, world);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        return false;
    }

    @Override
    public void startPersistentAngerTimer() {

    }

    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return getBukkitEntity();
    }

}
