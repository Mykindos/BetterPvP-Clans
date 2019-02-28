package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.client.PlayerStat;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.nms.BossWither;
import net.betterpvp.clans.worldevents.types.nms.BossWitherSkull;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BlockRestoreData;
import net.betterpvp.core.framework.RechargeManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilVelocity;
import net.betterpvp.mah.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
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

    public Witherton(Clans i) {
        super(i, "Witherton", WEType.BOSS);
        World w = Bukkit.getWorld("bossworld2");
        locs = new Location[]{
                new Location(w, 702.5, 135, 192.5),
                new Location(w, 699.5, 135, 123.5),
                new Location(w, 615.5, 135, 126.5),
                new Location(w, 618.5, 135, 199.5),
                new Location(w, 655.5, 135, 199.5)
        };
    }

    @Override
    public Location getSpawn() {
        return new Location(Bukkit.getWorld("bossworld2"), 660.5, 135, 162.5);
    }

    @Override
    public double getBaseDamage() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public String getBossName() {
        // TODO Auto-generated method stub
        return ChatColor.RED.toString() + ChatColor.BOLD + "Charles Witherton";
    }

    @Override
    public EntityType getEntityType() {
        // TODO Auto-generated method stub
        return EntityType.WITHER;
    }

    @Override
    public double getMaxHealth() {
        // TODO Auto-generated method stub
        return 2000;
    }

    @Override
    public LivingEntity getBoss() {
        // TODO Auto-generated method stub
        return wither;
    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Charles Witherton";
    }

    @Override
    public void spawn() {

        wither = null;


        if (getBoss() == null) {
            if (!getSpawn().getChunk().isLoaded()) {
                getSpawn().getChunk().load();
            }
            BossWither bs = new BossWither(((CraftWorld) Bukkit.getWorld("bossworld2")).getHandle());
            wither = bs.spawn(getSpawn());

            wither.setMaxHealth(getMaxHealth());
            wither.setHealth(getMaxHealth());
            wither.setCustomName(getBossName());
            wither.setCustomNameVisible(true);
            wither.setRemoveWhenFarAway(false);
            wither.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));

            lastExplosion = System.currentTimeMillis();
            lastShatter = System.currentTimeMillis();
            skulls.clear();
            bomb.clear();

        }

    }

    @EventHandler
    public void onTarget(EntityTargetEvent e) {
        if (isActive()) {
            if (e.getEntity() == getBoss()) {
                if (!(e.getTarget() instanceof Player)) {
                    e.setCancelled(true);
                }
            }
        }
    }

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
                                    bomb.put(player, 3);
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
                if (e.getDamagee() == getBoss()) {
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
                    getBoss().getWorld().playSound(getBoss().getLocation(), Sound.ENDERMAN_SCREAM, 1f, 1f);
                    lastShatter = System.currentTimeMillis();
                    for (LivingEntity ent : getBoss().getWorld().getLivingEntities()) {
                        if (getBoss() == ent) continue;
                        if (UtilMath.offset(ent, getBoss()) < 15) {
                            if (UtilBlock.isGrounded(ent)) {
                                Clan cLoc = ClanUtilities.getClan(ent.getLocation());
                                if (cLoc == null || cLoc instanceof AdminClan) {
                                    new BlockRestoreData(ent.getLocation().getBlock(), 7, (byte) 0, 2000);
                                    ent.getWorld().playSound(ent.getLocation(), Sound.PISTON_EXTEND, 1f, 1f);
                                    ent.setVelocity(new Vector(0, 2, 0));
                                    LogManager.addLog(ent, getBoss(), "Shatter");
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
                    e.setCancelled("Broodmother - Immune to Fall");
                } else if (e.getCause() == DamageCause.SUFFOCATION) {
                    e.setCancelled("Broodmother - Immune to Suffocation");
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
