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
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.UtilTime.TimeUnit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
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
import org.bukkit.inventory.EquipmentSlot;
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

        world = Bukkit.getWorld("bossworld");
        loc = new Location(world, 320.5, 50, 9.5);

        chestLocations = new Location[]{
                createLocation(330.5, 109, 45.5),
                createLocation(320.5, 109, 45.5),
                createLocation(319.5, 109, 35.5),
                createLocation(328.5, 109, 33.5),
                createLocation(334.5, 93, 51.5),
                createLocation(326.5, 93, 46.5),
                createLocation(318.5, 93, 42.5),
                createLocation(321.5, 93, 33.5),
                createLocation(332.5, 93, 37.5),
                createLocation(326.5, 83, 41.5),
                createLocation(319.5, 83, 36.5),
                createLocation(329.5, 83, 35.5),
                createLocation(335.5, 83, 44.5),
                createLocation(333.5, 83, 51.5),
                createLocation(324.5, 70, 51.5),
                createLocation(320.5, 70, 45.5),
                createLocation(323.5, 70, 36.5),
                createLocation(332.5, 70, 46.5),
                createLocation(336.5, 61, 32.5),
                createLocation(314.5, 61, 23.5),
                createLocation(346.5, 50, 59.5),
                createLocation(321.5, 50, 57.5),
                createLocation(308.5, 50, 59.5),
                createLocation(317.5, 50, 51.5),
                createLocation(309.5, 50, 48.5),
                createLocation(307.5, 50, 43.5),
                createLocation(339.5, 50, 43.5),
                createLocation(339.5, 50, 31.5),
                createLocation(296.5, 50, 17.5),
                createLocation(325.5, 50, 20.5),
                createLocation(338.5, 50, 1.5),
                createLocation(304.5, 50, 3.5),
                createLocation(298.5, 50, -12.5),
                createLocation(296.5, 50, -14.5),
                createLocation(323.5, 50, -55.5),
                createLocation(335.5, 50, -49.5),
                createLocation(339.5, 51, -35.5),
                createLocation(338.5, 50, -27.5),
                createLocation(320.5, 54, -32.5),
                createLocation(336.5, 55, -34.5),
                createLocation(340.5, 55, -28.5),
                createLocation(336.5, 55, -25.5),
                createLocation(314.5, 62, 1.5),
                createLocation(305.5, 56, -38.5),
                createLocation(311.5, 56, -44.5),
                createLocation(331.5, 61, -45.5),
                createLocation(333.5, 70, -34.5),
                createLocation(326.5, 70, -29.5),
                createLocation(318.5, 70, -31.5),
                createLocation(319.5, 70, -42.5),
                createLocation(336.5, 70, -46.5),
                createLocation(333.5, 84, -42.5),
                createLocation(326.5, 84, -35.5),
                createLocation(321.5, 84, -30.5),
                createLocation(317.5, 84, -34.5),
                createLocation(320.5, 84, -40.5),
                createLocation(318.5, 84, -48.5)

        };

        mobSpawnLocations = new Location[]{

                createLocation(337.5, 55, -31.5),
                createLocation(326.5, 50, -45.5),
                createLocation(305.5, 50, -51.5),
                createLocation(311.5, 50, -40.5),
                createLocation(308.5, 57, -41.5),
                createLocation(320.5, 71, -35.5),
                createLocation(330.5, 84, -46.5),
                createLocation(323.5, 84, -32.5),
                createLocation(310.5, 69.5, -21.5),
                createLocation(326.5, 50, -3.5),
                createLocation(315.5, 62, 2.5),
                createLocation(313.5, 61, 22.5),
                createLocation(311.5, 69.5, 27.5),
                createLocation(338.5, 55, 32.5),
                createLocation(336.5, 50, 47.5),
                createLocation(308.5, 50, 45.5),
                createLocation(328.5, 83, 37.5),
                createLocation(320.5, 93, 45.5),
                createLocation(321.5, 110, 48.5)

        };


        locs = new Location[]{
                createLocation(365, 49, -38),
                createLocation(333, 49, -69),
                createLocation(296, 49, -64),
                createLocation(285, 49, -30),
                createLocation(278, 49, -1.5),
                createLocation(279, 48, 37),
                createLocation(283, 48, 65),
                createLocation(302, 48, 77),
                createLocation(324, 49, 77),
                createLocation(348, 50, 72),
                createLocation(363, 49, 57),
                createLocation(365, 49, 17),
                createLocation(369, 49, -15)


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
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.1F, 1F);
        }

        spawnMobs();

        killCount.clear();
    }

    private void spawnMobs() {
        int count = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() == GameMode.ADVENTURE) {
                if (p.getWorld().getName().equalsIgnoreCase("bossworld")) {
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
    }

    @EventHandler
    public void crackChest(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (isActive()) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                    if (e.getClickedBlock().getWorld().getName().equalsIgnoreCase("bossworld")) {
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
        if(UtilMath.random.nextDouble() > 0.90) {
            if (UtilMath.random.nextDouble() > 0.98) {
                ItemStack special = WEManager.getRandomItem();
                world.dropItem(b.getLocation(), special);
                for (Player p : UtilPlayer.getInRadius(b.getLocation(), 25)) {
                    UtilMessage.message(p, "Undead Camp", "A special item has dropped from a chest!");

                }
                UtilMessage.broadcast("Legendary", ChatColor.YELLOW + "A "
                        + special.getItemMeta().getDisplayName() + ChatColor.YELLOW + " dropped at the Undead Camp!");
                Log.write("Legendary", special.getItemMeta().getDisplayName() + " dropped at the undead camp.");
            }
        }

        double rand = Math.random();
        if (rand > 0.995) {
            world.dropItem(b.getLocation(), new ItemStack(Material.MUSIC_DISC_PIGSTEP));
        } else if (rand > 0.90) {
            world.dropItem(b.getLocation(), new ItemStack(Material.NETHERITE_AXE));
        } else if (rand > 0.85) {
            world.dropItem(b.getLocation(), new ItemStack(Material.NETHERITE_SWORD));
        } else if (rand > 0.8) {
            world.dropItem(b.getLocation(), new ItemStack(Material.DIAMOND_SWORD));
        } else if (rand > 0.7) {
            world.dropItem(b.getLocation(), new ItemStack(Material.GOLDEN_SWORD));
        } else if (rand > 0.6) {
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
                                z.getWorld().playSound(z.getLocation(), Sound.ENTITY_ZOMBIE_HURT, 1f, 2f);
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
                    LogManager.addLog(e.getDamagee(), e.getDamager(), getMinion(e.getDamager()).getDisplayName(), "", e.getDamage());
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
                    world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.IRON_INGOT, UtilMath.randomInt(2, 5)));
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
                    world.dropItem(e.getEntity().getLocation(), new ItemStack(Material.EMERALD, UtilMath.randomInt(2, 5)));
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
