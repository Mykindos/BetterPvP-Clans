package net.betterpvp.clans.worldevents;


import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.Environmental;
import net.betterpvp.clans.worldevents.types.TimedEvents.UndeadCamp;
import net.betterpvp.clans.worldevents.types.TimedEvents.VillagePillage;
import net.betterpvp.clans.worldevents.types.bosses.*;
import net.betterpvp.clans.worldevents.types.environmental.FishingFrenzy;
import net.betterpvp.clans.worldevents.types.environmental.MiningMadness;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class WEManager extends BPVPListener<Clans> {

    private static List<WorldEvent> worldEvents = new ArrayList<>();
    private static List<Drop> dropList = new ArrayList<>();


    private static Location[] returnLocs;
    private static Location[] bossLocs;

    private WorldEvent lastWorldEvent = null;

    public WEManager(Clans i) {
        super(i);

        loadItems();
        addWorldEvent(new SlimeKing(i));
        addWorldEvent(new Broodmother(i));
        addWorldEvent(new SkeletonKing(i));
        addWorldEvent(new Witherton(i));
        addWorldEvent(new BorisAndDoris(i));

        addWorldEvent(new FishingFrenzy(i));
        addWorldEvent(new MiningMadness(i));

        World world = Bukkit.getWorld("world");
        returnLocs = new Location[]{new Location(world, -17, 68, -96),
                new Location(world, 3, 58, -98),
                new Location(world, -6, 44, -75),
                new Location(world, 25, 43, -24)};
        bossLocs = new Location[]{new Location(world, -44.5, 14, -105.5),
                new Location(world, -44.5, 14, -11.5),
                new Location(world, 35.5, 14, -11.5),
                new Location(world, 35.5, 14, -105.5)

        };
        addWorldEvent(new UndeadCamp(i));
        addWorldEvent(new VillagePillage(i));


        new BukkitRunnable() {

            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() >= Clans.getOptions().MinPlayersForWorldEvent()) {
                    if (!isWorldEventActive()) {
                        List<WorldEvent> events = getWorldEvents().stream()
                                .filter(w -> {
                                    if(lastWorldEvent != null){
                                        if(w.getName().equalsIgnoreCase(lastWorldEvent.getName())){
                                            return false;
                                        }
                                    }
                                    return true;
                                }).collect(Collectors.toList());

                        WorldEvent we = events.get(ThreadLocalRandom.current().nextInt(getWorldEvents().size()));
                        if (!we.getSpawn().getChunk().isLoaded()) {
                            we.getSpawn().getChunk().load();
                        }
                        we.spawn();
                        we.setActive(true);
                        announce();
                        lastWorldEvent = we;
                    }
                }
            }

        }.runTaskTimer(i, 1200L, 72000L);


    }

    public static void teleportToBoss(Player p) {
        Gamer c = GamerManager.getOnlineGamer(p);
        if (c != null) {

            if (UtilTime.elapsed(c.getLastDamaged(), 15000)) {


                p.teleport(bossLocs[UtilMath.randomInt(bossLocs.length)]);
                UtilMessage.message(p, "World Event", "You teleported to the Boss Arena");
            }
        }
    }

    public static void teleportOutOfBoss(Player p) {
        Gamer c = GamerManager.getOnlineGamer(p);
        if (c != null) {

            if (UtilTime.elapsed(c.getLastDamaged(), 15000)) {


                p.teleport(returnLocs[UtilMath.randomInt(returnLocs.length)]);
                UtilMessage.message(p, "World Event", "You teleported back to Fields.");
            }

        }
    }


    public static List<WorldEvent> getWorldEvents() {
        return worldEvents;
    }

    public void addWorldEvent(WorldEvent we) {
        if (!getWorldEvents().contains(we)) {
            worldEvents.add(we);
        }
    }

    /**
     * Check if a specific world event is active
     * @param event WorldEvent name
     * @return True if the event is currently active
     */
    public static boolean isEventActive(String event) {
        for (WorldEvent we : getWorldEvents()) {
            if (we.getName().equalsIgnoreCase(event)) {
                if (we.isActive()) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Get the currently active WorldEvent
     * @return the currently active WorldEvent
     */
    public static WorldEvent getActiveWorldEvent() {
        for (WorldEvent we : getWorldEvents()) {
            if (we.isActive()) {
                return we;
            }
        }

        return null;
    }

    /**
     * Quick check to see if a worldevent is currently active
     * @return true if event is active
     */
    public static boolean isWorldEventActive() {
        return getActiveWorldEvent() != null;
    }

    /**
     * Get WorldEvent object by name
     * @param name Name of WorldEvent
     * @return WorldEvent object matching name
     */
    public static WorldEvent getWorldEvent(String name) {
        for (WorldEvent we : getWorldEvents()) {
            if (we.getName().equalsIgnoreCase(name)) {
                return we;
            }
        }

        return null;
    }


    public static void announce() {
        if (isWorldEventActive()) {
            if (getActiveWorldEvent() instanceof Boss) {
                Boss b = (Boss) getActiveWorldEvent();
                b.lastDamaged = System.currentTimeMillis();
                Location loc = b.getSpawn();
                if (loc != null && b.getBoss() != null) {
                    UtilMessage.broadcast("World Event", ChatColor.YELLOW + ChatColor.stripColor(b.getBossName()) + ChatColor.GRAY + " has entered the world. ");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        Titles.sendTitle(p, 20, 20, 20, ChatColor.YELLOW + ChatColor.stripColor(b.getBossName()), ChatColor.GRAY + "Has entered the world.");
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.1F, 1F);
                        Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(p));
                    }
                }
            } else if (getActiveWorldEvent() instanceof Environmental) {
                Environmental e = (Environmental) getActiveWorldEvent();
                UtilMessage.broadcast("World Event", ChatColor.YELLOW + ChatColor.stripColor(e.getDisplayName()) + ChatColor.GRAY + " has started! ");
                e.subAnnounce();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Titles.sendTitle(p, 20, 20, 20, ChatColor.YELLOW + ChatColor.stripColor(e.getDisplayName()), ChatColor.GRAY + "has started!");
                    p.playSound(p.getLocation(), Sound.ENTITY_WOLF_HOWL, 1.5F, 1F);
                    Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(p));
                }
            }

            UtilDiscord.sendWebhook(Clans.getOptions().getDiscordChatWebhook(), "SERVER", getActiveWorldEvent().getName() + " has started.");
        }
    }




    private void loadItems() {
        for (Weapon weapons : WeaponManager.weapons) {
            if (weapons instanceof ILegendary) {
                dropList.add(new Drop(weapons.createWeapon(true), weapons.getChance()));
            }

            if (weapons instanceof EnchantedWeapon) {
                dropList.add(new Drop(weapons.createWeapon(), weapons.getChance()));
            }
        }

    }

    public static ItemStack getRandomItem() {

        double sumWeights = 0; // total sum of the weights of items

        Collections.shuffle(dropList);
        // loop through all items adding each weighted value
        // NB: the values DO NOT have to equal 1
        for (Drop map : dropList) {
            sumWeights += map.getChance();
        }


        double randNum = UtilMath.randDouble(0, sumWeights);
        double sum = 0;
        for (Drop map : dropList) {
            sum += map.getChance();
            if (randNum <= sum) {

                return map.getItemStack();
            }
        }

        return null;
    }

}
