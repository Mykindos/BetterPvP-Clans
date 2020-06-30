package net.betterpvp.clans.worldevents.types.bosses;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.bosses.ads.*;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.UtilVelocity;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R1.EntitySlime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftSlime;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SlimeKing extends Boss {

    private Slime fullSlime;
    public List<SlimeBase> allSlimes = new ArrayList<>();
    private List<SlimeRocket> rockets = new ArrayList<>();
    private Location[] locs;

    private int shield = 6;

    public SlimeKing(Clans i) {
        super(i, "SlimeKing", WEType.BOSS);
        World w = Bukkit.getWorld("bossworld");
        locs = new Location[]{
                new Location(w, -111.5, 14.0, -13.5),
                new Location(w, -154.5, 14, -12.5),
                new Location(w, -167.5, 14, -56.5),
                new Location(w, -93.5, 14, -50.5),
                new Location(w, -132.5, 14, 1.5)

        };
    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Slime King";
    }


    @Override
    public Location getSpawn() {
        return new Location(Bukkit.getWorld("bossworld"), -132.5, 15, -55.5);
    }

    @EventHandler
    public void checkFinished(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC_30) {
            if (allSlimes.isEmpty()) {
                rockets.clear();
                setActive(false);
                fullSlime = null;
            } else {
                for (SlimeBase sb : allSlimes) {
                    if (!sb.getEntity().isDead()) {
                        return;
                    }
                }

                setActive(false);
            }
        }
    }

    @Override
    public LivingEntity getBoss() {

        if (fullSlime != null && !fullSlime.isDead()) {
            return fullSlime;
        }


        for (SlimeBase ba : allSlimes) {
            if (ba.getEntity() != null && !ba.getEntity().isDead()) {
                return ba.getEntity();
            }
        }

        return null;
    }


    @EventHandler
    public void Death(EntityDeathEvent e) {
        if (isActive()) {
            if (isSlimeKing(e.getEntity())) {

                SlimeBase sb = getSlimeKing((Slime) e.getEntity());
                World w = e.getEntity().getWorld();
                if (sb != null) {
                    if (sb instanceof SlimeFull) {

                        for (int i = 0; i < 2; i++) {
                            Slime halfSlime = (Slime) w
                                    .spawnEntity(e.getEntity().getLocation(),
                                            getEntityType());

                            allSlimes.add(new SlimeHalf(halfSlime));
                        }
                        allSlimes.remove(sb);


                    } else if (sb instanceof SlimeHalf) {
                        for (int i = 0; i < 4; i++) {
                            allSlimes.add(new SlimeQuarter((Slime) w
                                    .spawnEntity(e.getEntity().getLocation(),
                                            getEntityType())));
                        }

                        allSlimes.remove(sb);


                    } else if (sb instanceof SlimeShield) {
                        allSlimes.remove(sb);
                    } else if (sb instanceof SlimeQuarter) {
                        allSlimes.remove(sb);
                    }
                }

            }
            if (allSlimes.isEmpty() || isDead()) {
                announceDeath(e);
                fullSlime = null;
                allSlimes.clear();

            }

        }


    }

    public boolean isDead() {

        if (allSlimes.isEmpty()) return true;
        for (SlimeBase sb : allSlimes) {
            if (sb.getEntity() != null) {
                if (!sb.getEntity().isDead()) {
                    return false;
                }
            }
        }
        return true;
    }


    @EventHandler
    public void onRocketLaunch(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getProjectile() != null) {
                if (e.getDamagee() instanceof Slime) {
                    if (isSlimeKing(e.getDamagee())) {
                        if (UtilMath.randomInt(10) > 8) {
                            for (SlimeBase sb : allSlimes) {
                                if (!(sb instanceof SlimeShield)) {
                                    rockets.add(new SlimeRocket(sb.getEntity().getLocation(), e.getDamager()));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void updateRockets(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            if (isActive() || !rockets.isEmpty()) {

                ListIterator<SlimeRocket> it = rockets.listIterator();
                while (it.hasNext()) {
                    SlimeRocket next = it.next();
                    if (next.getSlime().isDead() || next.getSlime() == null) {
                        it.remove();
                        return;
                    }
                    next.update();
                }

            }
        }
    }


    public boolean isSlimeKing(LivingEntity s) {
        for (SlimeBase sb : allSlimes) {

            if (sb.getSlime().getUniqueId().equals(s.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    public SlimeBase getSlimeKing(LivingEntity e) {
        for (SlimeBase sb : allSlimes) {
            if (sb.getSlime().getUniqueId().equals(e.getUniqueId())) {
                return sb;
            }
        }

        return null;
    }

    public boolean isShieldActive() {
        for (SlimeBase sb : allSlimes) {
            if (sb instanceof SlimeShield) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void spawn() {


        fullSlime = null;
        if (!getSpawn().getChunk().isLoaded()) {
            getSpawn().getChunk().load();
        }
        fullSlime = (Slime) Bukkit.getWorld("bossworld").spawnEntity(getSpawn(),
                getEntityType());
        fullSlime.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 1));


        allSlimes.add(new SlimeFull(fullSlime));


        for (int z = 0; z < shield; z++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Slime slimey = (Slime) getBoss().getWorld().spawnEntity(getBoss().getLocation().add(0, 5, 0),
                            getEntityType());

                    allSlimes.add(new SlimeShield(slimey));
                }
            }.runTaskLater(getInstance(), z * 10);
        }

    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() instanceof Slime) {
                if (isSlimeKing((Slime) e.getDamagee())) {
                    if (isShieldActive()) {
                        if (getSlimeKing((Slime) e.getDamagee()) instanceof SlimeFull) {

                            e.setCancelled("Slime Shield still active");
                        }


                    }
                    EntitySlime es = ((CraftSlime) e.getDamagee()).getHandle();
                    es.setGoalTarget(es.getLastDamager());
                }
            }

            if (e.getDamager() instanceof Slime) {
                if (isSlimeKing(e.getDamager())) {
                    e.setDamage(getBaseDamage());

                }
            }
        }
    }

    private long start;

    @EventHandler
    public void entDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() == null || e.getDamager() == null) return;
            if (e.getDamagee() instanceof Slime) {
                if (isSlimeKing(e.getDamagee())) {

                    if (UtilMath.randomInt(100) > 90) {
                        start = System.currentTimeMillis();
                    }

                    LivingEntity ent = e.getDamagee();
                    SlimeBase slime = getSlimeKing((Slime) ent);
                    if (!(slime instanceof SlimeShield)) {
                        ent.setCustomName(slime.getDisplayName() + "  " + ChatColor.GREEN + (int) ent.getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) ent.getMaxHealth());
                    }
                }
            }
            if (e.getDamagee() instanceof Player) {
                if (e.getDamager() instanceof Slime) {
                    if (isSlimeKing(e.getDamager())) {
                        SlimeBase slime = getSlimeKing(e.getDamager());

                        LogManager.addLog(e.getDamagee(),
                                e.getDamager(),
                                slime.getDisplayName(), "");
                    }
                }
            }
        }
    }


    @EventHandler
    public void orbitSlimes(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateEvent.UpdateType.TICK) {

                if (!isShieldActive()) {
                    return;
                }
                ListIterator<SlimeBase> it = allSlimes.listIterator();
                while (it.hasNext()) {
                    SlimeBase sb = it.next();
                    if (sb instanceof SlimeShield) {
                        Slime s = sb.getSlime();
                        if (s == null || s.isDead()) {
                            it.remove();
                        }
                        double oX = Math.sin(s.getTicksLived() / 10) * 12;
                        double oY = 4;
                        double oZ = Math.cos(s.getTicksLived() / 10) * 12;
                        //s.teleport(getBoss().getLocation().add(oX, oY, oZ));

                        UtilVelocity.velocity(s, UtilVelocity.getTrajectory(s.getLocation(),
                                getBoss().getLocation().add(oX, oY, oZ)), 0.6, false, 0, 0.1, 1.5, true);
                    }
                }


            } else if (e.getType() == UpdateEvent.UpdateType.FASTEST) {
                if (!UtilTime.elapsed(start, 10000)) {

                    for (SlimeBase s : allSlimes) {
                        if (!(s instanceof SlimeShield)) {
                            if (!s.getSlime().isDead()) {


                                for (LivingEntity p : UtilPlayer.getAllInRadius(s.getSlime().getLocation(), 15)) {
                                    if (p instanceof Player) {
                                        Location loc = p.getLocation();
                                        if (loc.add(0, 2, 0).getBlock().getType() != Material.AIR || loc.add(0, 3, 0).getBlock().getType() != Material.AIR
                                                || loc.getBlock().isLiquid()) {
                                            if (UtilMath.randomInt(100) > 95) {

                                                for (SlimeBase sb : allSlimes) {
                                                    if (!(sb instanceof SlimeShield)) {
                                                        rockets.add(new SlimeRocket(sb.getSlime().getLocation(), p));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                spawnOoze(s, (float) 0.9);


                            }
                        }
                    }

                }

            }
        }
    }


    private void spawnOoze(SlimeBase s, float y) {
        float x = (float) (Math.random());
        float z = (float) Math.random();

        Item slime = s.getSlime().getWorld().dropItem(s.getSlime().getEyeLocation(), new ItemStack(Material.SLIME_BALL));
        ThrowableManager.addThrowable(slime, s.getSlime(), "Ooze", 3000l);


        if (Math.random() > 0.5) x = x - (x * 2);
        if (Math.random() > 0.5) z = z - (z * 2);

        slime.setVelocity(new Vector(x, y, z));
        slime.setPickupDelay(100000);
    }

    @EventHandler
    public void slipperyFloor(ThrowableHitGroundEvent e) {
        if (isActive()) {
            if (isSlimeKing(e.getThrowable().getThrower())) {

                Block b = e.getThrowable().getItem().getLocation().add(0, -1, 0).getBlock();
                if (e.getThrowable().getItem().getLocation().getBlock().getType().name().contains("LEAVES")
                        || b.getType().name().contains("LEAVES")) {
                    new BlockRestoreData(e.getThrowable().getItem().getLocation().add(0, -1, 0).getBlock(), b.getType(), (byte) 0, 30000);
                    b.setType(Material.AIR);
                } else {
                    new BlockRestoreData(e.getThrowable().getItem().getLocation().add(0, -1, 0).getBlock(), b.getType(), (byte) 0, 2000);
                    b.setType(Material.SLIME_BLOCK);
                }

                e.getThrowable().getItem().remove();

            }
        }
    }

    @EventHandler
    public void onPickup(ThrowableCollideEntityEvent e) {
        if (isActive()) {
            if (e.getThrowable().getItem().getItemStack().getType() == Material.SLIME_BALL && e.getThrowable().getSkillName().equals("Ooze")) {
                if (e.getCollision() instanceof Player) {
                    Player p = (Player) e.getCollision();
                    EffectManager.addEffect(p, EffectType.SHOCK, 2000);
                }
                if (e.getCollision() instanceof Slime) {
                    return;
                }

                e.getCollision().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
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

                    if (e.getDamagee() == getBoss() || isSlimeKing(e.getDamagee())) {

                        if (EffectManager.hasProtection(p)) {
                            e.setCancelled("PVP Protection");
                            UtilMessage.message(p, "World Event", "You must disable your protection to damage bosses. (/protection)");
                            return;
                        }
                        PlayerStat stat = ClientUtilities.getOnlineClient(p).getStats();
                        int kills = stat.getSlimeKingKills();
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
    public void onSplit(SlimeSplitEvent e) {

        if (e.getEntity().getCustomName() != null) {
            e.setCancelled(true);
        }

    }

    @Override
    public double getBaseDamage() {

        return 10;
    }

    @Override
    public String getBossName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Slime King";
    }

    @Override
    public EntityType getEntityType() {

        return EntityType.SLIME;
    }


    @Override
    public double getMaxHealth() {

        return 500;
    }

	
	/*
	@EventHandler
	public void checkPos(UpdateEvent e) {
		if(e.getType() == UpdateType.SEC) {
			if(isActive()) {
				if(getBoss() != null && !getBoss().isDead()) {
					Clan a = ClanUtilities.getClan(getBoss().getLocation());
					if(a == null || !a.getName().equalsIgnoreCase("Outskirts") && !a.getName().equalsIgnoreCase("Fields") && !a.getName().equalsIgnoreCase("Lake")) {

						List<Location> locs = new ArrayList<>();
						for(int i = 0; i < 64; i++) {
							Location loc = getBoss().getLocation().clone().add(i, 0, 0);
							Clan aClan = ClanUtilities.getClan(loc);
							if(aClan != null && aClan.getName().equalsIgnoreCase("Outskirts")) {
								locs.add(loc);
								break;
							}
						}

						for(int i = 0; i > -64; i--) {
							Location loc = getBoss().getLocation().clone().add(i, 0, 0);
							Clan bClan = ClanUtilities.getClan(loc);
							if(bClan != null && bClan.getName().equalsIgnoreCase("Outskirts")) {
								locs.add(loc);
								break;
							}
						}

						for(int i = 0; i < 64; i++) {
							Location loc = getBoss().getLocation().clone().add(0, 0, i);
							Clan cClan = ClanUtilities.getClan(loc);
							if(cClan != null && cClan.getName().equalsIgnoreCase("Outskirts")) {
								locs.add(loc);
								break;
							}
						}

						for(int i = 0; i > -64; i--) {
							Location loc = getBoss().getLocation().clone().add(0, 0, i);
							Clan dClan = ClanUtilities.getClan(loc);
							if(dClan != null && dClan.getName().equalsIgnoreCase("Outskirts")) {
								locs.add(loc);
								break;
							}
						}

						if(locs.size() > 0) {

							Collections.sort(locs, new Comparator<Location>(){

								@Override
								public int compare(Location a, Location b) {

									return (int) getBoss().getLocation().distance(a) - (int) getBoss().getLocation().distance(b);
								}

							});

							getBoss().teleport(locs.get(0).getWorld().getHighestBlockAt(locs.get(0)).getLocation());
							
						}

					}

				}

				if(!allSlimes.isEmpty()) {
					for(SlimeBase wem : allSlimes) {
						if(wem.getEntity() != null && !wem.getEntity().isDead()) {
							Clan a = ClanUtilities.getClan(wem.getEntity().getLocation());
							if(a == null || !a.getName().equalsIgnoreCase("Outskirts") && !a.getName().equalsIgnoreCase("Fields") && !a.getName().equalsIgnoreCase("Lake")) {
									List<Location> locs = new ArrayList<>();
									for(int i = 0; i < 64; i++) {
										Location loc = wem.getEntity().getLocation().clone().add(i, 0, 0);
										Clan aClan = ClanUtilities.getClan(loc);
										if(aClan != null && aClan.getName().equalsIgnoreCase("Outskirts")) {
											locs.add(loc);
											break;
										}
									}

									for(int i = 0; i > -64; i--) {
										Location loc = wem.getEntity().getLocation().clone().add(i, 0, 0);
										Clan bClan = ClanUtilities.getClan(loc);
										if(bClan != null && bClan.getName().equalsIgnoreCase("Outskirts")) {
											locs.add(loc);
											break;
										}
									}

									for(int i = 0; i < 64; i++) {
										Location loc = wem.getEntity().getLocation().clone().add(0, 0, i);
										Clan cClan = ClanUtilities.getClan(loc);
										if(cClan != null && cClan.getName().equalsIgnoreCase("Outskirts")) {
											locs.add(loc);
											break;
										}
									}

									for(int i = 0; i > -64; i--) {
										Location loc = wem.getEntity().getLocation().clone().add(0, 0, i);
										Clan dClan = ClanUtilities.getClan(loc);
										if(dClan != null && dClan.getName().equalsIgnoreCase("Outskirts")) {
											locs.add(loc);
											break;
										}
									}

									if(locs.size() > 0) {

										Collections.sort(locs, new Comparator<Location>(){

											@Override
											public int compare(Location a, Location b) {

												return (int) 	wem.getEntity().getLocation().distance(a) - (int) 	wem.getEntity().getLocation().distance(b);
											}

										});
										
										wem.getEntity().teleport(locs.get(0).getWorld().getHighestBlockAt(locs.get(0)).getLocation());

										
									}
								}
							
						}
					}
				}
			}
		}
	}
	*/

    @Override
    public Location[] getTeleportLocations() {

        return locs;
    }


}
