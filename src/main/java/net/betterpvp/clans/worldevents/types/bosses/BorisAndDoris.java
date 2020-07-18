package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.events.CustomKnockbackEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.bosses.ads.PolarBearCub;
import net.betterpvp.clans.worldevents.types.bosses.ads.SnowGolemMinion;
import net.betterpvp.clans.worldevents.types.bosses.ads.WitherMinion;
import net.betterpvp.clans.worldevents.types.nms.BossPolarBear;
import net.betterpvp.clans.worldevents.types.nms.BossSnowGolem;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
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
                new Location(world, -5.5, 140, 85.5),
                new Location(world, 26.5, 140, 114.5),
                new Location(world, 38.5, 140, 126.5),
                new Location(world, 13.5, 140, 103.5)
        };
    }

    @Override
    public void spawn() {

        doris = null;
        boris = null;
        phase = BorisDorisPhase.PHASE_ONE;

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
        if (ent == null) return false;
        if (boris != null) {
            if (ent.equals(boris)) {
                return true;
            }
        }

        if (doris != null) {
            if (ent.equals(doris)) {
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
    public void onDeath(EntityDeathEvent e) {
        if (isActive()) {
            if (isBoss(e.getEntity())) {
                if (doris != null && e.getEntity().equals(doris)) {
                    if (boris == null || boris.getHealth() <= 0) {
                        announceDeath(e);
                    }
                } else if (boris != null && e.getEntity().equals(boris)) {
                    if (doris == null || doris.getHealth() <= 0) {
                        announceDeath(e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onIncreasedDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (isBoss(e.getDamager())) {
                if (doris != null && !doris.isDead() && boris != null && !boris.isDead()) {
                    if (boris.getLocation().distance(doris.getLocation()) < 15) {
                        e.setDamage(e.getDamage() * 2);
                    }
                }

            }
        }
    }

    @EventHandler
    public void snowmanSpawn(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateEvent.UpdateType.SEC_30) {
                if (boris != null && !boris.isDead()) {
                    getMinions().removeIf(m -> m.getEntity() == null || (m.getEntity() != null && m.getEntity().isDead()));
                    long count = getMinions().stream().filter(m -> m instanceof SnowGolemMinion).count();
                    if (count < 3) {
                        spawnSnowmen((int) (3 - count));
                    }
                }
            }
        }
    }

    @EventHandler
    public void bossDamageUpdate(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() != null) {
                if (isBoss(e.getDamagee())) {
                    if (doris != null) {
                        if (e.getDamagee().equals(doris)) {
                            doris.setCustomName(getDorisName() + "  " + ChatColor.GREEN
                                    + (int) doris.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) doris.getMaxHealth());

                            if (phase == BorisDorisPhase.PHASE_ONE) {
                                if (UtilPlayer.getHealthPercentage(doris) < 50) {
                                    spawnCubs(3);
                                    phase = BorisDorisPhase.PHASE_TWO;
                                }
                            }
                        }
                    }
                    if (boris != null) {
                        if (e.getDamagee().equals(boris)) {
                            boris.setCustomName(getBorisName() + "  " + ChatColor.GREEN
                                    + (int) boris.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) boris.getMaxHealth());
                        }
                    }
                }
            }

            if (e.getDamager() != null) {
                if (isBoss(e.getDamager())) {
                    if (doris != null) {
                        if (e.getDamager().equals(doris)) {
                            LogManager.addLog(e.getDamagee(), e.getDamager(), getDorisName(), "");

                        }
                    }
                    if (boris != null) {
                        if (e.getDamager().equals(boris)) {
                            LogManager.addLog(e.getDamagee(), e.getDamager(), getBorisName(), "");
                        }
                    }

                }
            }
        }

    }

    @EventHandler
    public void onHealthDoris(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if (isActive()) {
                if (phase != BorisDorisPhase.PHASE_TWO) return;
                if (doris != null && !doris.isDead()) {
                    long count = getMinions().stream().filter(m -> m instanceof PolarBearCub && !m.getEntity().isDead()).count();
                    for (int i = 0; i < count; i++) {
                        doris.setHealth(Math.min(doris.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), doris.getHealth() + 1));
                    }

                    doris.setCustomName(getDorisName() + "  " + ChatColor.GREEN
                            + (int) doris.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) doris.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

                    if (doris.getHealth() == doris.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                        phase = BorisDorisPhase.PHASE_ONE;
                        if (boris != null && !boris.isDead()) {
                            boris.setHealth(boris.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                            boris.setCustomName(getBorisName() + "  " + ChatColor.GREEN
                                    + (int) boris.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) boris.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                        }

                        UtilMessage.broadcast("World Event", getBossName() + ChatColor.GRAY + " has reset as Doris' cubs were not killed fast enough.");

                        getMinions().forEach(m -> {
                            m.getEntity().setHealth(0);
                            m.getEntity().remove();
                        });

                        getMinions().removeIf(m -> m.getEntity() == null || m.getEntity().isDead());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCubDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (phase == BorisDorisPhase.PHASE_TWO) {
                if (getMinions().isEmpty()) return;
                if (e.getDamager() instanceof PolarBear) {
                    WorldEventMinion minion = getMinion(e.getDamager());
                    if (minion != null && minion instanceof PolarBearCub) {
                        e.setDamage(3);
                    }
                }
            }
        }
    }

    private void spawnCubs(int amount) {
        for (int i = 0; i < amount; i++) {
            BossPolarBear cub = new BossPolarBear(((CraftWorld) world).getHandle());
            PolarBear cubEntity = cub.spawnPolarBear(doris.getLocation());
            PolarBearCub sm = new PolarBearCub(cubEntity);
            if (doris != null && doris.getTarget() != null) {
                ((PolarBear) sm.getEntity()).setTarget(doris.getTarget());
            }

            getMinions().add(sm);
        }
    }

    private void spawnSnowmen(int amount) {
        for (int i = 0; i < amount; i++) {
            BossSnowGolem golem = new BossSnowGolem(((CraftWorld) world).getHandle());
            Snowman golemEntity = golem.spawnGolem(boris.getLocation());
            SnowGolemMinion sm = new SnowGolemMinion(golemEntity);
            if (boris != null && boris.getTarget() != null) {
                ((Snowman) sm.getEntity()).setTarget(boris.getTarget());
            }

            getMinions().add(sm);
        }
    }

    @EventHandler
    public void onHitBySnowball(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() instanceof Player) {
                if (e.getProjectile() != null) {
                    if (e.getProjectile() instanceof Snowball) {
                        e.getDamagee().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 1));
                    }
                }
            } else {
                if (e.getProjectile() != null) {
                    if (e.getProjectile() instanceof Snowball) {
                        if (isBoss(e.getDamagee()) || isMinion(e.getDamagee()))
                            e.setCancelled("Snowmen are immune to snowballs");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickupSnowball(EntityPickupItemEvent e) {
        if (isActive()) {
            if (e.getItem().getWorld().equals(world)) {
                if (e.getItem().getItemStack().getType() == Material.SNOWBALL) {
                    e.setCancelled(true);
                    e.getItem().remove();
                }
            }
        }

    }

    @EventHandler
    public void onBossTarget(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateEvent.UpdateType.SLOW) {
                if (doris != null && !doris.isDead()) {
                    if (doris.getTarget() == null) {
                        if (boris != null && !boris.isDead()) {
                            if (boris.getTarget() != null) {
                                doris.setTarget(boris.getTarget());
                            }
                        }
                    }
                }

                if (boris != null && !boris.isDead()) {
                    if (boris.getTarget() == null) {
                        if (doris != null && !doris.isDead()) {
                            if (doris.getTarget() != null) {
                                boris.setTarget(doris.getTarget());
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onReducedKB(CustomKnockbackEvent e) {
        if (isActive()) {
            if (isBoss(e.getDamagee())) {
                e.setDamage(0);
            }

            if(isBoss(e.getDamager())){
                e.setDamage(0);
            }
        }
    }

    @EventHandler
    public void onSetTarget(CustomDamageEvent e) {
        if (isActive()) {
            if (isBoss(e.getDamagee())) {
                if (e.getDamager() instanceof Player) {
                    PolarBear bear = (PolarBear) e.getDamagee();
                    bear.setTarget(e.getDamager());
                }
            }
        }
    }

    @EventHandler
    public void onGolemKnockback(CustomKnockbackEvent e) {
        if (isActive()) {
            if (isMinion(e.getDamagee()) && e.getDamagee() instanceof Snowman) {
                e.setDamage(e.getDamage() * 0.2);
            }
        }
    }

    @EventHandler
    public void onSetIce(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FAST) {
            if (isActive()) {
                if (boris != null && boris.getHealth() > 0) {
                    setIce(boris);
                }
                if (doris != null && doris.getHealth() > 0) {
                    setIce(doris);
                }

                if (!getMinions().isEmpty()) {
                    for (WorldEventMinion wem : getMinions()) {
                        if (wem.getEntity() != null && wem.getEntity().getHealth() > 0 && wem.getEntity() instanceof PolarBearCub) {
                            setIce(wem.getEntity());
                        }
                    }
                }

            }
        }
    }

    /**
     * Replaces water that is close to the Boss & Minions with ice for 10 seconds
     *
     * @param e Entity to check
     */
    private void setIce(LivingEntity e) {
        for (Block b : UtilBlock.getInRadius(e.getLocation(), 5, 2).keySet()) {

            if (b.getType() == Material.WATER) {
                if (e.getLocation().getBlock().isLiquid()) continue;
                if (b.getRelative(BlockFace.UP).getType() != Material.AIR) continue;

                new BlockRestoreData(b, b.getType(), (byte) 0, 10000L);
                b.setType(Material.ICE);
            }
        }
    }

    /**
     * Ticking damage in the icy cold water in the Doris & Boris Arena
     *
     * @param e UpdateEvent
     */
    @EventHandler
    public void onColdWaterDamage(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FAST) {
            if (isActive()) {
                for (Player player : world.getPlayers()) {
                    if (UtilBlock.isInLiquid(player)) {
                        CustomDamageEvent ev = new CustomDamageEvent(player, null, null, EntityDamageEvent.DamageCause.CUSTOM, 3.0, false, "Hypothermia");
                        ev.setIgnoreArmour(true);
                        Bukkit.getPluginManager().callEvent(ev);
                    }
                }
            }
        }
    }

    private String getBorisName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Boris";
    }

    private String getDorisName() {
        return ChatColor.RED.toString() + ChatColor.BOLD + "Doris";
    }


    @Override
    public Location[] getTeleportLocations() {
        return locs;
    }

    @Override
    public void removeBoss() {
        if (doris != null) {
            doris.remove();
        }

        if (boris != null) {
            boris.remove();
        }
    }

    private enum BorisDorisPhase {
        PHASE_ONE,
        PHASE_TWO
    }
}
