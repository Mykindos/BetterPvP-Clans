package net.betterpvp.clans.worldevents.types.TimedEvents;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.CombatLogs;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.crates.Crate;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.types.Timed;
import net.betterpvp.clans.worldevents.types.TimedEvents.ads.*;
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
import org.bukkit.block.Chest;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class VillagePillage extends Timed {

    private Location loc;
    private Location[] mobSpawnLocations;
    private Location[] villagerSpawnLocations;
    private Location[] locs;
    private World world;
    private double score;
    private BossBar bossBar;
    private WeakHashMap<ItemStack, Double> lootTable = new WeakHashMap<>();

    public VillagePillage(Clans i) {
        super(i, "villagepillage", WEType.TIMED, 15);

        world = Bukkit.getWorld("bossworld");
        loc = new Location(world, 27, 203, -142);

        bossBar = Bukkit.createBossBar(ChatColor.WHITE + "Remaining health: " + ChatColor.YELLOW + score, BarColor.RED,
                BarStyle.SOLID);

        mobSpawnLocations = new Location[]{

                createLocation(93.5, 202, -100.5),
                createLocation(97.5, 202, -141.5),
                createLocation(93.5, 202, -175.5),
                createLocation(68.5, 202, -198.5),
                createLocation(32.5, 202, -215.5),
                createLocation(-11.5, 202, -209.5),
                createLocation(-41.5, 202, -179.5),
                createLocation(-44.5, 202, -148.5),
                createLocation(-41.5, 201, -104.5),
                createLocation(-7, 201.5, -65.5),
                createLocation(-27.5, 202, -65.5),
                createLocation(65.5, 202, -75.5)

        };

        villagerSpawnLocations = new Location[]{
                createLocation(29.5, 203, -101.5),
                createLocation(5.5, 203, -117.5),
                createLocation(-8.5, 202, -138.5),
                createLocation(-13.5, 203, -163.5),
                createLocation(10.5, 204, -178.5),
                createLocation(15.5, 203, -129.5),
                createLocation(37.5, 203, -126.5),
                createLocation(12.5, 203, -127.5)
        };


        locs = new Location[]{
                createLocation(11.5, 203, -151.5),
                createLocation(28.5, 203, -165.5),
                createLocation(38.5, 203, -153.5),
                createLocation(48.5, 203, -131.5),
                createLocation(37.5, 203, -123.5),
                createLocation(17.5, 203, -122.5),
                createLocation(-7.5, 202, -116.5),
                createLocation(-8.5, 203, -135.5),
                createLocation(-10.5, 203, -152.5),
                createLocation(8.5, 215, -152.5),
                createLocation(38.5, 215, -115.5)

        };

    }

    @Override
    public String getDisplayName() {

        return ChatColor.RED.toString() + ChatColor.BOLD + "Village Pillage";
    }


    private Location createLocation(double x, double y, double z) {
        return new Location(world, x, y, z);
    }

    @Override
    public void spawn() {
        setStartTime(System.currentTimeMillis());
        UtilMessage.broadcast("World Event", "The " + ChatColor.YELLOW + "Village Pillage" + ChatColor.GRAY + " event has begun! ");
        UtilMessage.broadcast("World Event", "Protect the villagers from the assault to earn better rewards!");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.1F, 1F);
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                if(!isActive()){
                    this.cancel();
                    return;
                }

                spawnVillagers();
            }
        }.runTaskTimer(getInstance(), 0, 20 * 60);

        new BukkitRunnable(){
            @Override
            public void run() {
                if(!isActive()){
                    this.cancel();
                    return;
                }

                spawnMobs();
            }
        }.runTaskTimer(getInstance(), 20 * 90, 20 * 60);
        score = 10_000;
    }

    private void spawnVillagers() {
        long activeVillagers = world.getLivingEntities().stream().filter(l -> l instanceof Villager).count();
        for(int i = (int) activeVillagers; i < 25; i++) {
            Villager villager = (Villager) world.spawnEntity(villagerSpawnLocations[UtilMath.randomInt(villagerSpawnLocations.length - 1)], EntityType.VILLAGER);
            getMinions().add(new EventVillager(villager));

        }
    }

    private void spawnMobs() {

        long activePillagers = world.getLivingEntities().stream().filter(l -> l instanceof Pillager || l instanceof Ravager).count();
        for(int i = (int) activePillagers; i < 25; i++) {
            Ravager ravager = null;

            Location sLoc = mobSpawnLocations[UtilMath.randomInt(mobSpawnLocations.length - 1)];

            if(UtilMath.randomInt(10) == 5){
                ravager = (Ravager) world.spawnEntity(sLoc, EntityType.RAVAGER);
                getMinions().add(new EventRavager(ravager));
            }


            Pillager pillager = (Pillager) world.spawnEntity(sLoc, EntityType.PILLAGER);
            getMinions().add(new EventPillager(pillager));




            List<LivingEntity> targets = world.getLivingEntities().stream().filter(l -> l instanceof Villager).collect(Collectors.toList());
            Villager target = (Villager) targets.get(UtilMath.randomInt(targets.size() - 1));

            if(target != null){
                if(ravager != null){
                    ravager.addPassenger(pillager);
                    ravager.setTarget(target);
                }
                pillager.setTarget(target);
            }

        }

        long activeVindicators = world.getLivingEntities().stream().filter(l -> l instanceof Vindicator ).count();
        for(int i = (int) activeVindicators; i < 7; i++) {
            Vindicator vindicator = (Vindicator) world.spawnEntity( mobSpawnLocations[UtilMath.randomInt(mobSpawnLocations.length - 1)], EntityType.VINDICATOR);
            getMinions().add(new EventVindicator(vindicator));


            List<LivingEntity> targets = world.getLivingEntities().stream().filter(l -> l instanceof Villager).collect(Collectors.toList());
            Villager target = (Villager) targets.get(UtilMath.randomInt(targets.size() - 1));

            if(target != null){
                vindicator.setTarget(target);
            }
        }

        for(Player player : world.getPlayers()){
            player.playSound(player.getLocation(), Sound.EVENT_RAID_HORN, 2f, 1f);
        }

    }


    @EventHandler
    public void announceTimeRemaining(UpdateEvent e) {
        if (e.getType() == UpdateType.MIN_02) {
            if (isActive()) {
                UtilMessage.broadcast("World Event", "The " + ChatColor.YELLOW + "Village Pillage" + ChatColor.GRAY + " event has " + ChatColor.GREEN
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
                    endEvent();
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
    public void minionDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() instanceof Villager) {
                if (isMinion(e.getDamagee())) {
                    if (e.getCause() == DamageCause.FALL) {
                        e.setCancelled("Avoid Fall - Village Pillage");
                    }

                    if (e.getDamagee() instanceof Pillager && e.getDamager() instanceof Pillager) {
                        e.setCancelled("Pillagers cannot damage each other");
                    }

                    if(e.getDamager() instanceof Player){
                        e.setCancelled("Players can't damage villagers");
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
    public void pillagerDeath(EntityDeathEvent e) {
        if (isActive()) {
            if (isMinion(e.getEntity())) {
                e.getDrops().clear();
                if (e.getEntity() instanceof Villager) {
                    if (e.getEntity().getWorld().getName().equalsIgnoreCase("bossworld")) {
                        score -= 50;
                    }
                }
            }
        }
    }


    @Override
    public Location[] getTeleportLocations() {
        return locs;
    }

    @EventHandler
    public void updateBossbar(UpdateEvent e) {
        if (isActive()) {
            if (e.getType() == UpdateType.SEC) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getWorld().getName().equalsIgnoreCase("bossworld")) {
                        bossBar.addPlayer(player);
                    } else {
                        bossBar.removePlayer(player);
                    }
                }

                bossBar.setProgress((score / 10_000));
                bossBar.setTitle(ChatColor.RED + "Remaining health: " + ChatColor.YELLOW + (int) score);

                if (score <= 0) {
                    endEvent();
                }
            }
        }
    }

    private void endEvent() {
        setActive(false);

        UtilMessage.broadcast("World Event", "The " + ChatColor.YELLOW + "Village Pillage" + ChatColor.GRAY + " event has finished!");
        UtilMessage.broadcast("World Event", ChatColor.GRAY + "Final score: " + ChatColor.GREEN + (int) score);

        bossBar.removeAll();
        for (WorldEventMinion wem : getMinions()) {
            wem.getEntity().remove();
        }

        getMinions().clear();

        if (score > 0) {
            processRewards();
        }else{
            UtilMessage.broadcast("World Event", "As the final score was below 0, no rewards were earned.");
        }
    }


    private void processRewards() {

        lootTable.clear();

        lootTable.put(UtilClans.updateNames(new ItemStack(Material.IRON_INGOT, 32)), 10D);
        lootTable.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND, 32)), 10D);
        lootTable.put(UtilClans.updateNames(new ItemStack(Material.LEATHER, 32)), 10D);
        lootTable.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_INGOT, 32)), 10D);
        lootTable.put(UtilClans.updateNames(new ItemStack(Material.GOLD_INGOT, 32)), 10D);
        lootTable.put(UtilClans.updateNames(new ItemStack(Material.EMERALD, 32)), 10D);

        if (score >= 2000) {
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND_AXE, 3)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND_SWORD, 3)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.GOLDEN_AXE, 3)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.GOLDEN_SWORD, 3)), 10D);
        }

        if (score >= 4000) {
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_SWORD, 2)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_AXE, 2)), 10D);
        }

        if (score >= 6000) {
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.IRON_BLOCK, 7)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.DIAMOND_BLOCK, 7)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.LEATHER, 64)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.NETHERITE_BLOCK, 7)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.GOLD_BLOCK, 7)), 10D);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.EMERALD_BLOCK, 7)), 10D);
        }

        if (score >= 8000) {
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.TNT, 2)), 5.0);
            lootTable.put(UtilClans.updateNames(new ItemStack(Material.MUSIC_DISC_PIGSTEP, 1)), 3.0);
        }

        if (score >= 9000) {
            for (Weapon w : WeaponManager.weapons) {
                if (w instanceof ILegendary) {
                    lootTable.put(UtilClans.updateNames(w.createWeapon()), 0.6);
                }
            }
        }

        double sumWeights = 0; // total sum of the weights of items

        // loop through all items adding each weighted value
        // NB: the values DO NOT have to equal 1
        for (Map.Entry<ItemStack, Double> map : lootTable.entrySet()) {
            sumWeights += map.getValue();
        }

        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < Math.max(1, (score / 1000)); i++) {

            double randNum = UtilMath.randDouble(0, sumWeights);
            double sum = 0;
            for (Map.Entry<ItemStack, Double> map : lootTable.entrySet()) {
                sum += map.getValue();
                if (randNum <= sum) {
                    Weapon weapon = WeaponManager.getWeapon(map.getKey());

                    if (weapon != null && weapon instanceof ILegendary) {
                        items.add(weapon.createWeapon(true));
                        UtilMessage.broadcast("Legendary Loot", ChatColor.YELLOW + "A " + weapon.getName() + ChatColor.YELLOW + " was dropped at the world event!");
                        Log.write("Legendary", weapon.getName() + " dropped from world event");
                        break;
                    } else {
                        items.add(map.getKey());
                        break;
                    }
                }
            }
        }

        Block block = world.getBlockAt(1, 204, -140);
        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            Inventory inv = chest.getInventory();

            inv.addItem(items.toArray(new ItemStack[items.size()]));
        }else{
            System.out.println("No chest found at location for Village Pillage event");
        }


    }

}
