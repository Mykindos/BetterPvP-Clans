package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;

import net.betterpvp.clans.classes.events.CustomKnockbackEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.bosses.ads.SkeletonMinion;
import net.betterpvp.clans.worldevents.types.bosses.ads.WitherMinion;
import net.betterpvp.clans.worldevents.types.nms.BossWither;
import net.betterpvp.clans.worldevents.types.nms.BossWitherSkull;

import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.betterpvp.core.utility.restoration.BlockRestoreData;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R1.EntityLiving;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.Map.Entry;

public class Witherton extends Boss {

    private Wither wither;

    public WeakHashMap<Player, Integer> bomb = new WeakHashMap<>();
    public List<TrackingSkull> skulls = new ArrayList<>();
    private long lastShatter, lastExplosion;
    private Location[] locs;
    private Location[] minionLocs;
    private WithertonPhase phase = WithertonPhase.PHASE_ONE;

    private World world;

    public Witherton(Clans i) {
        super(i, "Witherton", WEType.BOSS);
        world = Bukkit.getWorld("bossworld");
        locs = new Location[]{
                new Location(world, 702.5, 135, 192.5),
                new Location(world, 699.5, 135, 123.5),
                new Location(world, 615.5, 135, 126.5),
                new Location(world, 618.5, 135, 199.5),
                new Location(world, 655.5, 135, 199.5)
        };

        minionLocs = new Location[]{
                new Location(world, -304, 46, -60),
                new Location(world, -326, 46, -60)
        };
    }

    @Override
    public Location getSpawn() {
        return new Location(world, -315, 46, -60);
    }

    @Override
    public double getBaseDamage() {

        return 3;
    }

    @Override
    public String getBossName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Charles Witherton";
    }

    @Override
    public EntityType getEntityType() {

        return EntityType.WITHER;
    }

    @Override
    public double getMaxHealth() {

        return 2000;
    }

    @Override
    public LivingEntity getBoss() {

        return wither;
    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Charles Witherton";
    }

    @Override
    public void spawn() {

        wither = null;
        phase = WithertonPhase.PHASE_ONE;

        if (getBoss() == null) {
            if (!getSpawn().getChunk().isLoaded()) {
                getSpawn().getChunk().load();
            }
            BossWither bs = new BossWither(((CraftWorld) world).getHandle());
            wither = bs.spawn(getSpawn());

            wither.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
            wither.setHealth(getMaxHealth());
            wither.setCustomName(getBossName());
            wither.setCustomNameVisible(true);
            wither.setRemoveWhenFarAway(false);
            wither.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, -100));

            //wither.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));


            lastExplosion = System.currentTimeMillis();
            lastShatter = System.currentTimeMillis();
            skulls.clear();
            bomb.clear();

        }

    }

    private void spawnMinions() {
        for (Location loc : minionLocs) {
            world.spigot().strikeLightning(loc, false);
            WitherSkeleton s = (WitherSkeleton) getBoss().getWorld().spawnEntity(loc, EntityType.WITHER_SKELETON);
            WitherMinion sm = new WitherMinion(s);

            Player target = UtilPlayer.getClosest(loc);
            if (target != null) {
                s.setTarget(target);
            }

            getMinions().add(sm);
        }

    }

    @EventHandler
    public void onBossRegen(EntityRegainHealthEvent e) {
        if (isActive()) {
            if (e.getEntity().equals(getBoss())) {
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void minionKnockback(CustomKnockbackEvent e) {
        if (isActive()) {

            if (e.getDamagee().equals(getBoss())) {
                e.setDamage(0);
            }

            if (getMinions().isEmpty()) return;
            if (e.getDamagee() instanceof Player) {
                if (isMinion(e.getDamager())) {
                    e.setDamage(3);
                }
            }
        }
    }

    @EventHandler
    public void onMinionDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (isMinion(e.getDamager())) {
                e.setDamage(5);
            }
        }
    }

    @EventHandler
    public void monitorMinion(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            if (isActive()) {
                for (WorldEventMinion m : getMinions()) {
                    if (m.getEntity().isDead()) continue;
                    if (m.getEntity().getLocation().getY() < 30) {
                        m.getEntity().setHealth(0);
                        m.getEntity().remove();
                    }
                }

                getMinions().removeIf(m -> m.getEntity() == null || m.getEntity().isDead());
            }
        }
    }

    @EventHandler
    public void onMinionDeath(EntityDeathEvent e){
        if(isActive()){
            if(isMinion(e.getEntity())){
                e.getDrops().clear();
            }
        }
    }

    @EventHandler
    public void onMinionLoot(EntityPickupItemEvent e) {
        if (isActive()) {
            if (isMinion(e.getEntity())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTarget(EntityTargetEvent e) {
        if (isActive()) {
            if (e.getEntity().equals(getBoss())) {
                if (!(e.getTarget() instanceof Player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDamageBoss(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee().equals(getBoss())) {
                if (!getMinions().isEmpty()) {
                    e.setCancelled("Witherton shield is up");
                }
            }
        }
    }

    @EventHandler
    public void monitorPhase(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            if (isActive()) {
                double health = getBoss().getHealth();
                if (phase == WithertonPhase.PHASE_ONE) {
                    if (health < 1500) {
                        spawnMinions();
                        phase = WithertonPhase.PHASE_TWO;
                    }
                } else if (phase == WithertonPhase.PHASE_TWO) {
                    if (health < 1000) {
                        spawnMinions();
                        phase = WithertonPhase.PHASE_THREE;
                    }
                } else if (phase == WithertonPhase.PHASE_THREE) {
                    if (health < 500) {
                        spawnMinions();
                        phase = WithertonPhase.PHASE_FOUR;
                    }
                }
            }
        }
    }


    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void bonusDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() == null || e.getDamager() == null) return;
            if (e.getDamager() != null) {
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();

                    if (e.getDamagee() == getBoss() || isMinion(e.getDamagee())) {
                        PlayerStat stat = ClientUtilities.getOnlineClient(p).getStats();
                        int kills = stat.getWithertonKills();
                        double modifier = kills * 2;
                        double modifier2 = modifier >= 10 ? 0.01 : 0.1;

                        e.setDamage(e.getDamage() * (1 + (modifier * modifier2)));
                    }
                }
            }
        }
    }

     */

    @EventHandler
    public void onDamageBySkullWitherton(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() != null) {
                if (e.getDamagee() == getBoss()) {
                    if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
                        e.setCancelled("Self damage");
                    }
                }
            }

            if (e.getDamager() != null) {
                if (e.getDamager() == getBoss()) {
                    if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
                        if (e.getDamagee() instanceof Player) {
                            Player player = (Player) e.getDamagee();
                            if (RechargeManager.getInstance().add(player, "Witherton-Bombs", 10, false)) {
                                if (!bomb.containsKey(player)) {
                                    bomb.put(player, 2);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageMelee(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() != null) {
                if (e.getDamagee().equals(getBoss())) {

                    e.setDamage(e.getDamage() * 2);
                    if (UtilMath.randDouble(0, 100) > 90) {
                        if (e.getDamager() instanceof Player) {
                            Player player = (Player) e.getDamager();

                            EffectManager.addEffect(player, EffectType.SHOCK, 2500);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 50, 1));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSlam(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateType.SLOWEST) {
                if (UtilTime.elapsed(lastShatter, UtilMath.randomInt(15, 45))) {
                    getBoss().getWorld().playSound(getBoss().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
                    lastShatter = System.currentTimeMillis();
                    for (LivingEntity ent : getBoss().getWorld().getLivingEntities()) {
                        if (getBoss().equals(ent)) continue;
                        if (isMinion(ent)) continue;
                        if (UtilMath.offset(ent, getBoss()) < 15) {
                            if (UtilBlock.isGrounded(ent)) {
                                Clan cLoc = ClanUtilities.getClan(ent.getLocation());
                                if (cLoc == null || cLoc instanceof AdminClan) {
                                    new BlockRestoreData(ent.getLocation().getBlock(), Material.CRYING_OBSIDIAN, (byte) 0, 2000);
                                    ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1f, 1f);
                                    ent.setVelocity(new Vector(0, 2, 0));
                                    //LogManager.addLog(ent, getBoss(), "Shatter");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExplode(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateType.SLOWEST) {
                if (UtilTime.elapsed(lastExplosion, UtilMath.randomInt(25, 60))) {
                    lastExplosion = System.currentTimeMillis();

                    for (int i = 0; i < 100; i++) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(isActive()) {
                                for (int q = 0; q < 3; q++) {
                                    float x = (float) (Math.random() * 1.25);
                                    float z = (float) (Math.random() * 1.25);

                                        Item ex = getBoss().getWorld().dropItem(getBoss().getEyeLocation(), new ItemStack(Material.ENDER_PEARL));
                                        ThrowableManager.addThrowable(ex, getBoss(), "Witherton-Bomb", 3000l);


                                        if (Math.random() > 0.5) x = x - (x * 2);
                                        if (Math.random() > 0.5) z = z - (z * 2);

                                        ex.setVelocity(new Vector(x, 0.9f, z));

                                    }
                                }
                            }
                        }.runTaskLater(getInstance(), i * 2);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPickupBomb(ThrowableCollideEntityEvent e) {
        if (e.getThrowable().getSkillName().equalsIgnoreCase("Witherton-Bomb")) {
            if (e.getCollision() instanceof Player) {
                Player player = (Player) e.getCollision();
                EffectManager.addEffect(player, EffectType.VULNERABILITY, 30000);

            }
        }
    }

    @EventHandler
    public void processBombs(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateType.FASTER) {
                Iterator<Entry<Player, Integer>> it = bomb.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Player, Integer> next = it.next();

                    if (next.getValue() <= 0) {
                        it.remove();
                        continue;
                    }

                    if (next.getKey().isDead()) {
                        it.remove();
                        continue;
                    }

                    next.setValue(next.getValue() - 1);


                    if (next.getKey().getLocation().distance(getBoss().getLocation()) < 100) {


                        Vector v = next.getKey().getLocation().subtract(0, 2, 0).toVector().subtract(getBoss().getLocation().toVector());
                        BossWitherSkull bws = new BossWitherSkull(((CraftWorld) getBoss().getWorld()).getHandle(),
                                ((EntityLiving) ((CraftEntity) getBoss()).getHandle()),
                                v.getX(), v.getY(), v.getZ(), getBoss());
                        WitherSkull f = bws.spawn(getBoss().getEyeLocation());
                        f.setCharged(true);

                        skulls.add(new TrackingSkull(next.getKey(), f));
                    }
                }

            } else if (e.getType() == UpdateType.TICK) {
                ListIterator<TrackingSkull> it = skulls.listIterator();
                while (it.hasNext()) {
                    TrackingSkull next = it.next();
                    if (next.s.isDead() || next.s == null) {
                        it.remove();
                        continue;
                    }

                    if (next.p == null || next.p.isDead()) {
                        next.s.remove();
                        it.remove();
                        continue;
                    }

                    if (next.s.getTicksLived() > 400) {
                        next.s.remove();
                        it.remove();
                        continue;
                    }


                    UtilVelocity.velocity(next.s, UtilVelocity.getTrajectory(next.s, next.p), 0.6, false, 0, 0.2, 1, true);

                }
            }
        }
    }

    @EventHandler
    public void shootSkull(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() == null || e.getDamager() == null) return;
            if (e.getDamagee() == getBoss()) {
                if (e.getCause() != DamageCause.ENTITY_EXPLOSION) {

                    if (e.getDamager() instanceof Player) {
                        Wither w = (Wither) e.getDamagee();
                        w.setTarget(e.getDamager());

                    }

                    if (UtilMath.randDouble(0, 100) > 80) {
                        Vector v = e.getDamager().getLocation().subtract(0, 2, 0).toVector().subtract(e.getDamagee().getLocation().toVector());
                        BossWitherSkull bws = new BossWitherSkull(((CraftWorld) e.getDamagee().getWorld()).getHandle(),
                                ((EntityLiving) ((CraftEntity) e.getDamagee()).getHandle()),
                                v.getX(), v.getY(), v.getZ(), e.getDamagee());
                        WitherSkull f = bws.spawn(e.getDamagee().getEyeLocation());
                        f.setVelocity(f.getVelocity().multiply(5));
                    }


					/*
				double vecX = e.getDamager().getLocation().getX() - e.getDamagee().getLocation().getX();
				double vecY = e.getDamager().getLocation().getY() - e.getDamagee().getLocation().getY();
				double vecZ = e.getDamager().getLocation().getZ() - e.getDamagee().getLocation().getZ();

				Vector v = new Vector(vecX, vecY, vecZ);
					 */
                    //UtilVelocity.velocity(f, UtilVelocity.getTrajectory(e.getDamagee(), e.getDamager()), 0.1, false, 0.1, 0, 0, false);


                    //f.setVelocity(v);
                }
            }
        }
    }


    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (isActive()) {
            if (getBoss() != null && e.getEntity() == getBoss()) {

                Iterator<WorldEventMinion> it = getMinions().iterator();
                while (it.hasNext()) {

                    it.remove();
                }


                announceDeath(e);
                wither = null;

            } else if (e.getEntity() instanceof CaveSpider) {
                Iterator<WorldEventMinion> it = getMinions().iterator();
                while (it.hasNext()) {

                    it.remove();

                }

            }
        }
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (getBoss() != null && !getBoss().isDead()) {
                if (e.getDamager() == getBoss()) {
                    e.setDamage(getBaseDamage());

                } else if (e.getDamagee() == getBoss()) {
                    if (e.getDamager() instanceof Player) {
                        Player p = (Player) e.getDamager();
                        if (EffectManager.hasProtection(p)) {
                            e.setCancelled("PVP Protection");
                            UtilMessage.message(p, "World Event", "You must disable your protection to damage bosses. (/protection)");
                            return;
                        }
                        wither.setTarget(e.getDamager());


                    }
                }
            }
        }
    }


    @EventHandler
    public void onEnvironmentDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() == getBoss() || isMinion(e.getDamagee())) {
                if (e.getCause() == DamageCause.FALL) {
                    e.setCancelled("Witherton - Immune to Fall");
                } else if (e.getCause() == DamageCause.SUFFOCATION) {
                    e.setCancelled("Witherton - Immune to Suffocation");
                }
            }
        }
    }

    @EventHandler
    public void witherDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (getBoss() != null) {
                if (e.getDamagee() == getBoss()) {
                    getBoss().setCustomName(getBossName() + "  " + ChatColor.GREEN + (int) getBoss().getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) getBoss().getMaxHealth());
                }
                if (e.getDamager() == getBoss()) {

                    LogManager.addLog(e.getDamagee(),
                            e.getDamager(),
                            getBossName(), "");
                }
            }
        }
    }


    @EventHandler
    public void checkBoss(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateType.MIN_01) {
                if (getBoss() == null || getBoss().isDead()) {
                    setActive(false);
                }
            }
        }
    }

    @EventHandler
    public void inWater(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            if (isActive()) {
                if (getBoss() != null) {
                    if (getBoss().getLocation().getBlock().isLiquid()) {
                        heal(5);

                    }
                }
            }
        }
    }

    private enum WithertonPhase {
        PHASE_ONE,
        PHASE_TWO,
        PHASE_THREE,
        PHASE_FOUR
    }


    public class TrackingSkull {

        public Player p;
        public WitherSkull s;

        public TrackingSkull(Player p, WitherSkull s) {
            this.p = p;
            this.s = s;
        }
    }


    @Override
    public Location[] getTeleportLocations() {

        return locs;
    }


}
