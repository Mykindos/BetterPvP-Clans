package net.betterpvp.clans.worldevents.types.TimedEvents;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.CombatLogs;
import net.betterpvp.clans.combat.LogManager;

import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Timed;
import net.betterpvp.clans.worldevents.types.TimedEvents.ads.UndeadSkeleton;
import net.betterpvp.clans.worldevents.types.TimedEvents.ads.UndeadZombie;
import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.nms.BossSkeleton;
import net.betterpvp.clans.worldevents.types.nms.BossZombie;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.WeakHashMap;

public class UndeadCamp extends Timed {

    private long lastChestOpened;
    private Location loc;
    private Location[] chestLocations;
    private Location[] mobSpawnLocations;
    private Location[] locs;
    private World world;

    private WeakHashMap<Player, Integer> killCount = new WeakHashMap<>();

    public UndeadCamp(Clans i) {
        super(i, "UndeadCamp", WEType.TIMED, 30);

        world = Bukkit.getWorld("bossworld2");
        loc = new Location(world, -144, 72, -480);

        chestLocations = new Location[]{
                createLocation(136.5, 32, 215.5),
                createLocation(128.5, 32, 212.5),
                createLocation(126.5, 32, 207.5),
                createLocation(127.5, 32, 223.5),
                createLocation(143.5, 32, 221.5),
                createLocation(158.5, 32, 207.5),
                createLocation(155.5, 43, 196.5),
                createLocation(133.5, 43, 187.5),
                createLocation(139.5, 52, 209.5),
                createLocation(143.5, 52, 215.5),
                createLocation(142.5, 52, 200.5),
                createLocation(148.5, 65, 199.5),
                createLocation(154.5, 65, 208.5),
                createLocation(152.5, 65, 215.5),
                createLocation(145.5, 65, 205.5),
                createLocation(138.5, 65, 200.5),
                createLocation(140.5, 75, 197.5),
                createLocation(137.5, 75, 206.5),
                createLocation(145.5, 75, 210.5),
                createLocation(153.5, 75, 215.5),
                createLocation(151.5, 75, 201.5),
                createLocation(139.5, 91, 209.5),
                createLocation(138.5, 91, 199.5),
                createLocation(149.5, 91, 209.5),
                createLocation(147.5, 91, 197.5),
                createLocation(165.5, 32, 223.5),
                createLocation(115.5, 32, 181.5),
                createLocation(123.5, 32, 167.5),
                createLocation(115.5, 32, 149.5),
                createLocation(117.5, 32, 151.5),
                createLocation(124.5, 38, 125.5),
                createLocation(130.5, 38, 119.5),
                createLocation(142.5, 32, 142.5),
                createLocation(154.5, 32, 154.5),

                createLocation(159.5, 37, 135.5),
                createLocation(155.5, 37, 138.5),
                createLocation(139.5, 36, 131.5),
                createLocation(158.5, 33, 128.5),
                createLocation(157.5, 32, 165.5),
                createLocation(133.5, 44, 165.5),
                createLocation(157.5, 32, 136.5),
                createLocation(152.5, 52, 129.5),
                createLocation(145.5, 52, 134.5),
                createLocation(137.5, 52, 132.5),
                createLocation(138.5, 52, 121.5),
                createLocation(155.5, 52, 117.5),
                createLocation(136.5, 66, 129.5),
                createLocation(139.5, 66, 123.5),
                createLocation(137.5, 66, 115.5),
                createLocation(145.5, 66, 128.5),
                createLocation(152.5, 66, 121.5),
                createLocation(158.5, 32, 195.5),
                createLocation(81.5, 42.5, 154.5),
                createLocation(101.5, 42, 205.5),
                createLocation(112.5, 42, 219.5),


        };

        mobSpawnLocations = new Location[]{

                createLocation(151.5, 41.5, 179.5),
                createLocation(170.5, 32, 203.5),
                createLocation(131.5, 44, 172.5),
                createLocation(145.5, 66, 132.5),
                createLocation(193.5, 33, 127.5),
                createLocation(142.5, 75, 209.5),
                createLocation(142.5, 92, 200.5),
                createLocation(143.5, 75, 203.5),
                createLocation(63.5, 33, 170.5),
                createLocation(149.5, 32, 156.5),
                createLocation(63.5, 33, 160.5),
                createLocation(175.5, 33, 144.5),
                createLocation(82.5, 38.5, 152.5),
                createLocation(105.5, 32, 204.5),
                createLocation(111.5, 32, 216.5)

        };


        locs = new Location[]{
                createLocation(121.5, 32, 124.5),
                createLocation(116.5, 32, 165.5),
                createLocation(120.5, 32, 212.5),
                createLocation(146.5, 32, 207.5),
                createLocation(154.5, 37, 198.5),
                createLocation(175.5, 32, 179.5),
                createLocation(204.5, 32, 168.5),
                createLocation(177.5, 35, 120.5),
                createLocation(152.5, 56, 122.5),
                createLocation(130.5, 51, 140.5),
                createLocation(82.5, 40, 202.5),
                createLocation(66.5, 33, 185.5),
                createLocation(80.5, 34, 119.5),
                createLocation(100.5, 32, 222.5)

        };
		/*
		chestLocations = new Location[]{createLocation(-236.5, 77, -278.5), createLocation(-231.5, 81, -260.5), createLocation(-234.5, 77, -261.5),
				createLocation(-243.5, 88, -260.5), createLocation(-248.5,77,-254.5), createLocation(-251.5, 81, -253.5), createLocation(-233.5, 82, -273.5),
				createLocation(-235.5, 96, -287.5), createLocation(-248.5, 82, -285.5), createLocation(-236.5, 77, -278.5), createLocation(-266.5, 77, -259.5),
				createLocation(-258.5, 88, -284.5), createLocation(-232.5, 77, -269.5), createLocation(-251.5, 77, -269.5), createLocation(-247.5, 77, -282.5)};

		mobSpawnLocations = new Location[]{createLocation(-234.5, 77, -276.5), createLocation(-252.5, 77, -272.5), createLocation(-259.5, 87.5, -282.5),
				createLocation(-265.5, 77, -265.5), createLocation(-247.5, 77, -285.5), createLocation(-267.5, 77, -280)};
		 */
    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Undead Camp";
    }


    private Location createLocation(double x, double y, double z) {
        return new Location(world, x, y, z);
    }

    @Override
    public void spawn() {
        setStartTime(System.currentTimeMillis());
        UtilMessage.broadcast("World Event", "The " + ChatColor.YELLOW + "Undead Camp" + ChatColor.GRAY + " event has begun! ");
        lastChestOpened = System.currentTimeMillis() - 120000;
        for (Location l : chestLocations) {
            if (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            l.getBlock().setType(Material.ENDER_CHEST);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 0.1F, 1F);
        }

        spawnMobs();

        killCount.clear();
    }

    private void spawnMobs() {
        int count = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().getName().equalsIgnoreCase("bossworld2")) {
                if (count < 4) {
                    BossZombie uz = new BossZombie(((CraftWorld) world).getHandle());
                    Zombie undeadZombie = uz.spawnZombie(mobSpawnLocations[UtilMath.randomInt(mobSpawnLocations.length - 1)]);
                    undeadZombie.setTarget(p);
                    getMinions().add(new UndeadZombie(undeadZombie));


                    BossSkeleton us = new BossSkeleton(((CraftWorld) world).getHandle());
                    Skeleton undeadArcher = us.spawn(mobSpawnLocations[UtilMath.randomInt(mobSpawnLocations.length - 1)]);
                    undeadArcher.setTarget(p);
                    getMinions().add(new UndeadSkeleton(undeadArcher));
                }
                count++;
            }
        }
    }

    @EventHandler
    public void crackChest(PlayerInteractEvent e) {
        if (isActive()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                    if (e.getClickedBlock().getWorld().getName().equalsIgnoreCase("bossworld2")) {
                        if (killCount.containsKey(e.getPlayer())) {
                            if (killCount.get(e.getPlayer()) >= 3) {
                                breakChest(e.getClickedBlock());
                                killCount.put(e.getPlayer(), killCount.get(e.getPlayer()) - 3);
                            } else {
                                UtilMessage.message(e.getPlayer(), "Undead Camp", "You need to kill "
                                        + ChatColor.GREEN + (3 - killCount.get(e.getPlayer())) + ChatColor.GRAY + " more undead to open an ender chest.");
                            }
                        } else {
                            UtilMessage.message(e.getPlayer(), "Undead Camp", "You need to kill " + ChatColor.GREEN + "3"
                                    + ChatColor.GRAY + " more undead to open an ender chest.");

                        }

                    }
                }
            }


        }
    }

    @EventHandler
    public void onMinionCombust(EntityCombustEvent e) {
        if (isActive()) {

            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity ent = (LivingEntity) e.getEntity();
                if (isMinion(ent)) {
                    if (ent instanceof Skeleton) {
                        ent.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                    } else {
                        ent.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                    }
                }
            }
        }
    }

    private void breakChest(Block b) {
        int amount = UtilMath.randomInt(15, 30);
        ItemStack[] gems = {new ItemStack(Material.EMERALD, amount), new ItemStack(Material.DIAMOND, amount),
                new ItemStack(Material.IRON_INGOT, amount), new ItemStack(Material.GOLD_INGOT, amount), new ItemStack(Material.LEATHER, amount)};
        world.dropItem(b.getLocation(), gems[UtilMath.randomInt(gems.length - 1)]);
        if (UtilMath.random.nextDouble() > 0.98) {
            world.dropItem(b.getLocation(), WEManager.getRandomItem());
            for (Player p : UtilPlayer.getInRadius(b.getLocation(), 25)) {
                UtilMessage.message(p, "Undead Camp", "A special item has dropped from a chest!");
            }
        }

        double rand = Math.random();
        if (rand > 0.95) {
            world.dropItem(b.getLocation(), new ItemStack(Material.GOLD_RECORD));
        } else if (rand > 0.9) {
            world.dropItem(b.getLocation(), new ItemStack(Material.DIAMOND_SWORD));
        } else if (rand > 0.8) {
            world.dropItem(b.getLocation(), new ItemStack(Material.GOLD_SWORD));
        } else if (rand > 0.7) {
            world.dropItem(b.getLocation(), new ItemStack(Material.DIAMOND_AXE));
        }
        b.setType(Material.AIR);
    }

    @EventHandler
    public void announceTimeRemaining(UpdateEvent e) {
        if (e.getType() == UpdateType.MIN_02) {
            if (isActive()) {
                UtilMessage.broadcast("World Event", "The " + ChatColor.YELLOW + "Undead Camp" + ChatColor.GRAY + " event has " + ChatColor.GREEN
                        + UtilTime.convert((getStartTime() + getLength()) - System.currentTimeMillis(), TimeUnit.MINUTES, 1) + " minutes " + ChatColor.GRAY + "remaining! "
                );
            }
        }

    }

    @EventHandler
    public void checkTimeup(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            if (isActive()) {
                if (UtilTime.elapsed(getStartTime(), getLength())) {
                    UtilMessage.broadcast("World Event", "The " + ChatColor.YELLOW + "Undead Camp" + ChatColor.GRAY + " event has finished!");
                    setActive(false);
                    for (WorldEventMinion wem : getMinions()) {
                        wem.getEntity().remove();
                    }

                    for (Location loc : chestLocations) {
                        loc.getBlock().setType(Material.AIR);
                    }

                    getMinions().clear();
                }
            }
        }
    }

    @EventHandler
    public void spawnMobs(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC_30) {
            if (isActive()) {
                if (getMinions().size() < loc.getWorld().getPlayers().size() * 5) {
                    spawnMobs();
                }
            }
        }
    }

    @EventHandler
    public void removeDead(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            Iterator<WorldEventMinion> it = getMinions().iterator();
            while (it.hasNext()) {
                WorldEventMinion next = it.next();
                if (next.getEntity().isDead()) {
                    it.remove();
                }
            }
        }
    }

    @EventHandler
    public void zombieLeap(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            if (isActive()) {
                for (WorldEventMinion wem : getMinions()) {
                    if (wem instanceof UndeadZombie) {

                        if (Math.random() > 0.9 || (wem.getEntity().getLocation().getBlock().isLiquid() && Math.random() > 0.6)) {
                            UndeadZombie undeadZombie = (UndeadZombie) wem;
                            Zombie z = (Zombie) undeadZombie.getEntity();
                            if (z.getTarget() != null) {
                                double dist = UtilMath.offset(z.getTarget(), z);

                                if (dist <= 3 || dist > 16)
                                    return;

                                double power = 0.8 + (1.2 * ((dist - 3) / 13d));

                                //Leap
                                UtilVelocity.velocity(z, UtilVelocity.getTrajectory(z, z.getTarget()),
                                        power, false, 0, 0.2, 0.8, true);

                                //Effect
                                z.getWorld().playSound(z.getLocation(), Sound.ZOMBIE_HURT, 1f, 2f);
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void minionDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() instanceof Skeleton || e.getDamagee() instanceof Zombie) {
                if (isMinion(e.getDamagee())) {
                    if (e.getDamagee() instanceof Zombie) {
                        if (e.getCause() == DamageCause.FALL) {
                            e.setCancelled("Avoid Fall - Undead Warrior");
                        }
                        if (e.getDamager() instanceof Skeleton) {
                            e.setCancelled("Immune to skele damage");
                        }
                        UndeadZombie uz = (UndeadZombie) getMinion(e.getDamagee());
                        uz.getEntity().setCustomName(uz.getDisplayName() + "  " + ChatColor.GREEN + (int) uz.getEntity().getHealth()
                                + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) uz.getEntity().getMaxHealth());
                    } else {
                        if (e.getDamager() instanceof Skeleton) {
                            e.setCancelled("Immune to other skele damage");
                        }
                        UndeadSkeleton uz = (UndeadSkeleton) getMinion(e.getDamagee());
                        uz.getEntity().setCustomName(uz.getDisplayName() + "  " + ChatColor.GREEN + (int) uz.getEntity().getHealth()
                                + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) uz.getEntity().getMaxHealth());
                    }

                }
            }
            if (e.getDamagee() instanceof Player) {
                if (isMinion(e.getDamager())) {
                    LogManager.addLog(e.getDamagee(), e.getDamager(), getMinion(e.getDamager()).getDisplayName(), "");
                }
            }
        }
    }

    @Override
    public Location getSpawn() {
        return loc;
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void undeadDeath(EntityDeathEvent e) {
        if (isActive()) {
            if (isMinion(e.getEntity())) {
                e.getDrops().clear();
                if (e.getEntity() instanceof Zombie) {
                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.IRON_HELMET));
                    }

                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.IRON_CHESTPLATE));
                    }

                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.IRON_LEGGINGS));
                    }

                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.IRON_BOOTS));
                    }
                    world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.IRON_INGOT, UtilMath.randomInt(3, 7)));
                } else if (e.getEntity() instanceof Skeleton) {
                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.CHAINMAIL_HELMET));
                    }

                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                    }

                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.CHAINMAIL_LEGGINGS));
                    }

                    if (Math.random() > 0.97) {
                        world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.CHAINMAIL_BOOTS));
                    }
                    world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.EMERALD, UtilMath.randomInt(3, 7)));
                }


                CombatLogs killer = LogManager.getKiller(e.getEntity());
                if (killer != null && killer.getDamager() instanceof Player) {
                    Player p = (Player) killer.getDamager();
                    if (p != null) {
                        if (killCount.containsKey(p)) {
                            killCount.put(p, killCount.get(p) + 1);
                        } else {
                            killCount.put(p, 1);
                        }

                        if (killCount.get(p) >= 5) {
                            UtilMessage.message(p, "Undead Camp", "You can now open an ender chest!");
                        }
                    }
                }
            }
        }
    }


    @Override
    public Location[] getTeleportLocations() {
        return locs;
    }

}
