package net.betterpvp.clans.worldevents.types.nms;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class BossSpider extends Spider {

    public BossSpider(Level world) {
        super(EntityType.SPIDER, world);

    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    public CraftEntity spawn(Location loc) {
        this.absMoveTo(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        this.level.addFreshEntity(this, SpawnReason.CUSTOM);
        return getBukkitEntity();
    }


}
