package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.events.CustomKnockbackEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.nms.BossPolarBear;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BorisAndDoris extends Boss {


    private Location[] locs;
    private World world;

    private PolarBear boris;
    private PolarBear doris;

    private BorisDorisPhase phase = BorisDorisPhase.PHASE_ONE;

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
    public void spawn() {

        doris = null;
        boris = null;

        BossPolarBear dorisBoss = new BossPolarBear(((CraftWorld) world).getHandle());
        doris = dorisBoss.spawnPolarBear(new Location(world, -2.5, 140, 127.5));
        doris.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        doris.setHealth(getMaxHealth());
        doris.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD + "Doris");
        doris.setCustomNameVisible(true);
        doris.setRemoveWhenFarAway(false);
        doris.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

        BossPolarBear borisBoss = new BossPolarBear(((CraftWorld) world).getHandle());
        boris = borisBoss.spawnPolarBear(new Location(world, 32.5, 140, 91.5));
        boris.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        boris.setHealth(getMaxHealth());
        boris.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD + "Boris");
        boris.setCustomNameVisible(true);
        boris.setRemoveWhenFarAway(false);
        boris.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

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
        if (boris == null && doris == null) {
            return null;
        }

        if (boris != null && !boris.isDead()) {
            return boris;
        }

        if (doris != null && !doris.isDead()) {
            return doris;
        }

        return null;

    }

    @Override
    public boolean isBoss(LivingEntity ent) {
        if(boris != null){
            if(ent.equals(boris)){
                return true;
            }
        }

        if(doris != null){
            if(ent.equals(doris)){
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDisplayName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Boris & Doris";
    }


    @EventHandler
    public void onIncreasedDamage(CustomDamageEvent e){
        if(isActive()){
            if(isBoss(e.getDamager())){
                if(doris != null && !doris.isDead() && boris != null && !boris.isDead()){
                    if(boris.getLocation().distance(doris.getLocation()) < 15){
                        e.setDamage(e.getDamage() * 2);
                    }
                }

            }
        }
    }

    @EventHandler
    public void bossDamageUpdate(CustomDamageEvent e) {
        if (isActive()) {
            if (isBoss(e.getDamagee())) {
                if (doris != null) {
                    if (e.getDamagee().equals(doris)) {
                        doris.setCustomName(getDorisName() + "  " + ChatColor.GREEN
                                + (int) doris.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) doris.getMaxHealth());
                    }
                }
                if (boris != null) {
                    if (e.getDamagee().equals(boris)) {
                        boris.setCustomName(getBorisName() + "  " + ChatColor.GREEN
                                + (int) boris.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) boris.getMaxHealth());
                    }
                }
            }

            if (isBoss(e.getDamager())) {
                if (doris != null) {
                    if (e.getDamager().equals(doris)) {
                        LogManager.addLog(e.getDamagee(), e.getDamager(), getDorisName(), "");

                    }
                }
                if(boris != null){
                    if(e.getDamager().equals(boris)){
                        LogManager.addLog(e.getDamagee(), e.getDamager(), getBorisName(), "");
                    }
                }

            }
        }

    }

    @EventHandler
    public void onReducedKB(CustomKnockbackEvent e){
        if(isActive()){
            if(isBoss(e.getDamagee())){
                e.setDamage(e.getDamage() * 0.10);
            }
        }
    }

    @EventHandler
    public void onSetTarget(CustomDamageEvent e){
        if(isActive()){
            if(isBoss(e.getDamagee())) {
                if (e.getDamager() instanceof Player) {
                    PolarBear bear = (PolarBear) e.getDamagee();
                    bear.setTarget(e.getDamager());
                }
            }
        }
    }

    private String getBorisName(){
        return ChatColor.RED.toString() + ChatColor.BOLD + "Boris";
    }

    private String getDorisName(){
        return ChatColor.RED.toString() + ChatColor.BOLD + "Doris";
    }


    @Override
    public Location[] getTeleportLocations() {
        return locs;
    }

    private enum BorisDorisPhase {
        PHASE_ONE,
        PHASE_TWO
    }
}
