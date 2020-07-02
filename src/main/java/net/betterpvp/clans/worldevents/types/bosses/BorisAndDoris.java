package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PolarBear;

public class BorisAndDoris extends Boss {


    private Location[] locs;
    private World world;

    private PolarBear boris;
    private PolarBear doris;

    public BorisAndDoris(Clans i) {
        super(i, "Boris & Doris", WEType.BOSS);
        world = Bukkit.getWorld("bossworld");
        locs = new Location[]{
                new Location(world, 20.5, 13, -99.5),
                new Location(world, 54.5, 15, -98.5),
                new Location(world, 67.5, 17, -85.5),
                new Location(world, 19.5, 22, -126.5),
                new Location(world, 8.5, 41, -100.5),
                new Location(world, 68.5, 44, -107.5)
        };
    }

    @Override
    public double getBaseDamage() {
        return 4;
    }

    @Override
    public String getBossName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Boris & Doris";
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.POLAR_BEAR;
    }

    @Override
    public double getMaxHealth() {
        return 1000;
    }

    @Override
    public LivingEntity getBoss() {
        return null;
    }

    @Override
    public boolean isBoss(LivingEntity ent) {
        return ent.equals(boris) || ent.equals(doris);
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Boris & Doris";
    }

    @Override
    public void spawn() {

    }

    @Override
    public Location[] getTeleportLocations() {
        return locs;
    }
}
