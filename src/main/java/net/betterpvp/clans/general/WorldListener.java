package net.betterpvp.clans.general;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.classes.roles.Ranger;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.economy.shops.menu.TravelHubMenu;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.selector.page.ClassSelectionPage;
import net.betterpvp.clans.skills.selector.skills.ranger.Longshot;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.weapon.weapons.legendaries.MeteorBow;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.commands.admin.OfflineCommand;
import net.betterpvp.core.client.mysql.ClientRepository;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.CoreLoadedEvent;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Gate;
import org.bukkit.util.Vector;

import java.util.*;

public class WorldListener extends BPVPListener<Clans> {
    public WorldListener(Clans instance) {
        super(instance);
    }


    /**
     * Speeds up the night time
     * @param e
     */
    @EventHandler
    public void onTimeUpdate(UpdateEvent e) {
        if(e.getType() == UpdateEvent.UpdateType.TICK_2){
            World world = Bukkit.getWorld("world");
            if(world.getTime() > 13000){
                world.setTime(world.getTime() + 20);
            }
        }
    }

    /**
     * Opens the Build Management menu
     * @param e
     */
    @EventHandler
    public void onOpenBuildManager(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType() == Material.ENCHANTMENT_TABLE) {

                e.getPlayer().openInventory(new ClassSelectionPage(e.getPlayer()).getInventory());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onGamemodeAdventure(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.SEC){
            for(Player p : Bukkit.getOnlinePlayers()){
                if(!p.isOp()){
                    p.setGameMode(GameMode.ADVENTURE);
                }
            }
        }
    }

    /*
     * Stops leaf decay in admin clan territory
     */
    @EventHandler
    public void stopLeafDecay(LeavesDecayEvent event) {
        if(!getInstance().hasStarted()){
            event.setCancelled(true);
            return;
        }
        Clan clan = ClanUtilities.getClan(event.getBlock().getLocation());
        if (clan != null) {

            if (clan instanceof AdminClan) {
                event.setCancelled(true);
            }
        }
    }

    /*
     * Prevents the weather changing, rain causes unnecessary fps drops, and looks bad.
     */
    @EventHandler
    public void setSunny(WeatherChangeEvent e){
        World world = e.getWorld();
        if(!world.hasStorm()) e.setCancelled(true);

    }

    /*
     * Stops players from placing items such a levers and buttons on the outside of peoples bases
     * This is required, as previously, players could open the doors to an enemy base.
     */
    @EventHandler
    public void onAttachablePlace(BlockPlaceEvent e){
        if(e.getBlock().getType() == Material.LEVER || e.getBlock().getType() == Material.STONE_BUTTON || e.getBlock().getType() == Material.WOOD_BUTTON){
            if(e.getBlockAgainst() != null){
                Clan c = ClanUtilities.getClan(e.getBlockAgainst().getLocation());
                if(c != null){
                    Clan d = ClanUtilities.getClan(e.getPlayer());
                    if(d != null && c == d){
                        return;
                    }

                    e.setCancelled(true);
                }
            }
        }
    }


    /*
     * The code for sponge springs.
     * When a player is standing on a sponge, and right clicks it below them, they get shot up into the air.
     */
    @EventHandler
    public void interactSpring(PlayerInteractEvent event) {


        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (event.getClickedBlock().getType() != Material.SPONGE) {
            return;
        }

        final Player player = event.getPlayer();

        if (UtilMath.offset(player.getLocation(), event.getClickedBlock().getLocation().add(0.5D, 1.5D, 0.5D)) > 0.6D) {
            return;
        }

        if (RechargeManager.getInstance().add(player, "Sponge", 0.8, false)) {

            player.setVelocity(new Vector(0.0D, 1.8D, 0.0D));
            player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0,15);
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19,15);
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19,15);
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, 19,15);
            event.setCancelled(true);
        }
    }


    /*
     * Prevent fall damage when landing on wool or sponge
     */
    @EventHandler
    public void onSafeFall(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player player = (Player) e.getEntity();
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (UtilBlock.getBlockUnder(player.getLocation()).getType() == Material.SPONGE
                        || UtilBlock.getBlockUnder(player.getLocation()).getType() == Material.WOOL) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /*
     * Players were creating chest rooms at sky limit, making them a lot harder to raid.
     * This requires all forms of item storage to be placed below 200y.
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

       // PlayerStat stat = ClientUtilities.getOnlineClient(player).getStats();
      //  stat.blocksPlaced = stat.blocksPlaced+ 1;

        if(block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.FURNACE || block.getType() == Material.HOPPER
                || block.getType() == Material.DROPPER || block.getType() == Material.DISPENSER){
            if(block.getLocation().getY() > 200){
                UtilMessage.message(player, "Restriction", "You can only place chests lower than 200Y!");
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        if (event.getBlock().getType() != Material.DIRT) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            event.setCancelled(true);
        }
    }


    /*
     * Prevents players from igniting TNT in Admin Protected Areas
     */
    @EventHandler
    public void preventTnTIgniting(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Block b = e.getClickedBlock();
            if(b.getType() == Material.TNT){
                Clan c = ClanUtilities.getClan(b.getLocation());
                if(c != null){
                    if(c instanceof AdminClan){
                        if(!ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()){
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }


    /*
     * Stops players from opening certain inventories related to blocks,
     * Such as brewing stands, and enchanting tables.
     */
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();



            if(event.getInventory().getType() == InventoryType.ENCHANTING){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e) {
        if(e.getDestination().getType() == InventoryType.HOPPER) {

            if(e.getDestination() == null) {
                e.setCancelled(true);
            }

        }else if(e.getSource().getType() == InventoryType.HOPPER) {

            if(e.getSource() == null) {
                e.setCancelled(true);
            }
        }
    }


    /*
     * Stops crops from being trampled by mobs and players
     */
    @EventHandler
    public void soilChangePlayer(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL){
            if(event.getClickedBlock().getType() == Material.SOIL) {
                event.setCancelled(true);
            }
        }
    }


    /*
     * Stops non players from passing through portals
     * When we had the nether, people would pull the boss in.
     */
    @EventHandler
    public void onPortal(EntityPortalEvent e){
        if(!(e.getEntity() instanceof Player)){
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void clearArmourStands(CoreLoadedEvent e){
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof ArmorStand) {
                    ArmorStand stand = (ArmorStand) entity;

                    if (!stand.isVisible()) {
                        entity.remove();
                    }
                }
            }

        }
    }


    /*
     * When transferring worlds, legendary and epic items would lose there artificial glow effect
     * This fixes that.
     */
    @EventHandler
    public void checkGlow(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.SLOWEST){
            for(Player p : Bukkit.getOnlinePlayers()){
                for(ItemStack i : p.getInventory().getContents()){
                    if(i != null){
                        if(i.hasItemMeta()){
                            Weapon w = WeaponManager.getWeapon(i);
                            if(w != null){
                                if(w.isLegendary() || w instanceof EnchantedWeapon){

                                    UtilItem.addGlow(i);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * Sets the damage for all tools
     * Removes knockback from assassins
     * Increases the durability of Assassin and Paladin Sets by having a chance
     * to add 1 durability back
     */
    @EventHandler (priority = EventPriority.LOWEST)
    public void onSwordDamage(CustomDamageEvent e){


        if(e.getDamagee() instanceof Player){
            Player d = (Player) e.getDamagee();
            Role r = Role.getRole(d);
            if(r != null){
                if(r instanceof Assassin){
                    if(!Clans.getOptions().isAssassinKnockbackEnabled()) {
                        e.setKnockback(false);
                    }


                }

            }
        }
        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if(e.getDamager() instanceof Player){
            Player p = (Player) e.getDamager();
            Role r = Role.getRole(p);
            if(r == null){
                e.setDamage(e.getDamage() * 0.80);
            }else {
                if(r instanceof Assassin) {
                    if(!Clans.getOptions().isAssassinDealKb()) {
                        e.setKnockback(false);
                    }
                }
            }
            if(p.getItemInHand() != null){
                Material m = p.getItemInHand().getType();
                if(UtilItem.isSword(m)){

                    if(m == Material.DIAMOND_SWORD){
                        e.setDamage(5);
                    }else if(m == Material.GOLD_SWORD){
                        e.setDamage(6);
                    }else if(m == Material.IRON_SWORD){
                        e.setDamage(4.5);

                        Weapon w = WeaponManager.getWeapon(p.getItemInHand());
                        if(w != null){
                            if(w instanceof EnchantedWeapon){
                                EnchantedWeapon enchantedWeapon = (EnchantedWeapon) w;

                                e.setDamage(e.getDamage() + enchantedWeapon.getBonus());

                            }
                        }
                    }else if(m == Material.STONE_SWORD){
                        e.setDamage(3);
                    }else if(m == Material.WOOD_SWORD){
                        e.setDamage(2);
                    }

                }else if(UtilItem.isAxe(m)){
                    if(m == Material.DIAMOND_AXE){
                        e.setDamage(4);
                    }else if(m == Material.GOLD_AXE){
                        e.setDamage(5);
                    }else if(m == Material.IRON_AXE){
                        e.setDamage(3);
                    }else if(m == Material.STONE_AXE){
                        e.setDamage(2);
                    }else if( m == Material.WOOD_AXE){
                        e.setDamage(1);
                    }

                }else{
                    e.setDamage(e.getDamage() * 0.75);
                }
            }else{
                e.setDamage(1);
            }
        }

    }


    /*
     * Stops players from placing certain types of blocks
     * as well as turns wooden doors into iron doors (to stop door hitting)
     */
    @EventHandler
    public void onBlockCancelPlace(BlockPlaceEvent event) {

        if(event.isCancelled()){
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Client c = ClientUtilities.getOnlineClient(player);


        if(c != null){
            if (!c.isAdministrating()) {
                if (block.getType() == Material.OBSIDIAN || block.getType() == Material.BEDROCK || block.getType() == Material.WATER_BUCKET
                        || block.getType() == Material.MOB_SPAWNER || block.getType() == Material.WEB || block.getType() == Material.BREWING_STAND
                        || block.getType() == Material.BREWING_STAND_ITEM) {
                    UtilMessage.message(player, "Server", "You cannot place " + ChatColor.YELLOW
                            + WordUtils.capitalizeFully(block.getType().toString()) + ChatColor.GRAY + ".");
                    event.setCancelled(true);
                    return;
                }

                Clan clan = ClanUtilities.getClan(player);

                if (ClanUtilities.getClan(block.getLocation()) != clan && ClanUtilities.getClan(block.getLocation()) != null){
                    return;
                }

                if (event.getBlock().getType() == Material.WOODEN_DOOR
                        || event.getBlock().getType() == Material.ACACIA_DOOR
                        || event.getBlock().getType() == Material.SPRUCE_DOOR
                        || event.getBlock().getType() == Material.BIRCH_DOOR
                        || event.getBlock().getType() == Material.JUNGLE_DOOR
                        || event.getBlock().getType() == Material.DARK_OAK_DOOR) {
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.IRON_DOOR));
                    UtilMessage.message(event.getPlayer(), "Game", "Please use " + ChatColor.YELLOW + "Iron Doors" + ChatColor.GRAY + " (You can right click to open them).");
                } else if (event.getBlock().getType() == Material.TRAP_DOOR) {
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.IRON_TRAPDOOR));
                    UtilMessage.message(event.getPlayer(), "Game", "Please use " + ChatColor.YELLOW + "Iron Trap Doors" + ChatColor.GRAY + " (You can right click to open them).");
                }

            }
        }
    }

    /*
     * Stops players from taking stuff off armour stands and item frames in
     * admin territory
     */
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof ArmorStand || e.getRightClicked() instanceof ItemFrame){

            Clan c = ClanUtilities.getClan(e.getRightClicked().getLocation());
            if(c != null){
                if(c instanceof AdminClan){
                    if(!ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()){

                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void updateTimePlayed(UpdateEvent e) {
        if(e.getType() == UpdateEvent.UpdateType.MIN_60) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                Client c = ClientUtilities.getOnlineClient(p);

                c.setTimePlayed(c.getTimePlayed() + 1);

                ClientRepository.updateTimePlayed(c);
            }

        }
    }


    @EventHandler
    public void onPlaceTNT(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.TNT) {
            if(EffectManager.hasEffect(e.getPlayer(), EffectType.PROTECTION)) {
                EffectManager.removeEffect(e.getPlayer(), EffectType.PROTECTION);
            }
        }
    }

    @EventHandler
    public void onBreakGate(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if(e.getClickedBlock().getType().name().contains("GATE")) {
                Clan aClan = ClanUtilities.getClan(e.getPlayer());
                Clan bClan = ClanUtilities.getClan(e.getClickedBlock().getLocation());

                Gate g = (Gate) e.getClickedBlock().getState().getData();

                if(bClan != null) {
                    if(aClan == null || (aClan != null && !aClan.getName().equalsIgnoreCase(bClan.getName()))) {
                        if(e.getPlayer().getLocation().distance(e.getClickedBlock().getLocation()) < 1.5) {

                            Location loc = UtilClans.closestWildernessBackwards(e.getPlayer());
                            if(loc != null) {
                                UtilVelocity.velocity(e.getPlayer(), UtilVelocity.getTrajectory(e.getPlayer().getLocation(), loc),
                                        0.5, true, 0.25, 0.25, 0.25, false);
                            }
                        }
                    }
                }




            }
        }
    }

    /*
     * Stops potions from being brewed (via auto brew methods)
     */
    @EventHandler
    public void onBrew(BrewEvent e){
        e.setCancelled(true);
    }

    /*
     * Stops players from breaking Item Frames in admin territory
     */
    @EventHandler
    public void onBreak(HangingBreakByEntityEvent e){
        Clan c = ClanUtilities.getClan(e.getEntity().getLocation());
        if(c != null){
            if(c instanceof AdminClan){
                if(e.getRemover() instanceof Player){
                    if(ClientUtilities.getOnlineClient((Player) e.getRemover()).isAdministrating()){
                        return;
                    }
                }
                e.setCancelled(true);
            }
        }
    }

    /*
     * Another method of stopping players from taking items or breaking Armour Stands
     * In the tutorial world, or in admin territory
     */
    @EventHandler
    public void armorStand(PlayerArmorStandManipulateEvent e){

        Clan c = ClanUtilities.getClan(e.getRightClicked().getLocation());
        if(c != null){
            if(c instanceof AdminClan){
                if(!ClientUtilities.getClient(e.getPlayer()).isAdministrating()){
                    e.setCancelled(true);
                }
            }
        }
    }


    /*
     * Stops Armour stands from being broken in the tutorial world
     * or admin territory
     */
    @EventHandler
    public void onArmorStandDeath(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame){

            Clan c = ClanUtilities.getClan(e.getEntity().getLocation());
            if(c != null){
                if(c instanceof AdminClan){
                    if(e.getDamager() instanceof Player){
                        if(ClientUtilities.getClient((Player) e.getDamager()).isAdministrating()){
                            return;
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    /*
     * Logs the location and player of chests that are opened in the wilderness
     * Useful for catching xrayers.
     */
    @EventHandler
    public void onInt(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Material m = e.getClickedBlock().getType();
            if(m == Material.CHEST || m== Material.TRAPPED_CHEST
                    || m == Material.FURNACE || m == Material.DROPPER || m == Material.CAULDRON){
                if(ClanUtilities.getClan(e.getPlayer().getLocation()) == null){

                    int x = (int) e.getClickedBlock().getLocation().getX();
                    int y = (int) e.getClickedBlock().getLocation().getY();
                    int z = (int) e.getClickedBlock().getLocation().getZ();
                    Log.write("Trap Chest", e.getPlayer().getName() + " opened a chest at " + x + "," + y + "," + z);
                }
            }
        }
    }

    /*
     * Stops players from interacting with item frames and armour stands (left click)
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.LEFT_CLICK_BLOCK){
            if(e.getClickedBlock().getType() == Material.ITEM_FRAME || e.getClickedBlock().getType() == Material.ARMOR_STAND){
                Clan c = ClanUtilities.getClan(e.getClickedBlock().getLocation());
                if(c != null){
                    if(c instanceof AdminClan){
                        if(ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()){
                            return;
                        }

                        e.setCancelled(true);
                    }
                }
            }
        }


    }




    /*
     * Modifies the drops for just about all mobs in minecraft
     */
    @EventHandler
    public void handleDeath(EntityDeathEvent event) {

        event.setDroppedExp(0);


        List<ItemStack> drops = event.getDrops();
        if(event.getEntity().getCustomName() == null){
            if (event.getEntityType() != EntityType.PLAYER) {
                drops.clear();
            }
            if (event.getEntityType() == EntityType.CHICKEN) {
                drops.add(new ItemStack(Material.RAW_CHICKEN, 1));
                drops.add(new ItemStack(Material.FEATHER, 2 + UtilMath.randomInt(1)));
            } else if (event.getEntityType() == EntityType.COW) {
                drops.add(new ItemStack(Material.RAW_BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.LEATHER, 1 + UtilMath.randomInt(2)));
            }
            if (event.getEntityType() == EntityType.MUSHROOM_COW) {
                drops.add(new ItemStack(Material.RAW_BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.RED_MUSHROOM, 2 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.OCELOT) {
                int rand = UtilMath.randomInt(10);
                if(rand == 0 || rand == 1 || rand == 2) {
                    drops.add(new ItemStack(Material.LEATHER, 1 + UtilMath.randomInt(2)));
                }else if(rand == 3 || rand == 4 || rand == 5) {
                    drops.add(new ItemStack(Material.RAW_FISH, 2 + UtilMath.randomInt(2)));
                }else if(rand == 6 || rand == 7) {
                    drops.add(new ItemStack(Material.COAL, 1 + UtilMath.randomInt(2)));
                }else {
                    drops.add(new ItemStack(Material.RAW_FISH, 10 + UtilMath.randomInt(10)));
                }
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(4)));

            } else if (event.getEntityType() == EntityType.PIG) {
                drops.add(new ItemStack(Material.PORK, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SHEEP) {
                drops.add(new ItemStack(Material.RAW_BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.WOOL, 1 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.VILLAGER) {
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(3)));
            } else if (event.getEntityType() == EntityType.BLAZE) {
                drops.add(new ItemStack(Material.BLAZE_ROD, 1));
                drops.add(new ItemStack(Material.BONE, 6 + UtilMath.randomInt(7)));
            } else if (event.getEntityType() == EntityType.CAVE_SPIDER) {

                drops.add(new ItemStack(Material.WEB, 1));
                drops.add(new ItemStack(Material.STRING, 2 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.SPIDER_EYE, 1));
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(4)));

            } else if (event.getEntityType() == EntityType.CREEPER) {
                drops.add(new ItemStack(Material.COAL, 2 + UtilMath.randomInt(4)));
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(7)));
            } else if (event.getEntityType() == EntityType.ENDERMAN) {
                drops.add(new ItemStack(Material.BONE, 12 + UtilMath.randomInt(8)));
            } else if (event.getEntityType() == EntityType.GHAST) {
                drops.add(new ItemStack(Material.GHAST_TEAR, 1));
                drops.add(new ItemStack(Material.BONE, 16 + UtilMath.randomInt(8)));
            } else if (event.getEntityType() == EntityType.IRON_GOLEM) {
                drops.add(new ItemStack(Material.IRON_INGOT, 2 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.BONE, 12 + UtilMath.randomInt(6)));
            } else if (event.getEntityType() == EntityType.MAGMA_CUBE) {
                drops.add(new ItemStack(Material.MAGMA_CREAM, UtilMath.randomInt(1, 3)));
                drops.add(new ItemStack(Material.BONE, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.PIG_ZOMBIE) {
                PigZombie z = (PigZombie) event.getEntity();
                if(z.getEquipment().getItemInHand().getType() == Material.GOLD_AXE){
                    drops.add(new ItemStack(Material.GOLD_AXE));
                }
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(2)));
                if(UtilMath.randomInt(50) > 48){
                    ItemStack[] temp = {new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_BOOTS),
                            new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS)};
                    drops.add(temp[UtilMath.randomInt(temp.length -1)]);
                }
                if (UtilMath.randomInt(100) > 90) {
                    drops.add(new ItemStack(Material.GOLD_PICKAXE));
                }else if(UtilMath.randomInt(1000) > 990){
                    drops.add(new ItemStack(Material.GOLD_SWORD));
                }
            } else if (event.getEntityType() == EntityType.SILVERFISH) {
                drops.add(new ItemStack(Material.BONE, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SKELETON) {
                drops.add(new ItemStack(Material.ARROW, 4 + UtilMath.randomInt(5)));
                drops.add(new ItemStack(Material.BONE, 3 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.SLIME) {
                drops.add(new ItemStack(Material.SLIME_BALL, 1));
                drops.add(new ItemStack(Material.BONE, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SPIDER) {
                drops.add(new ItemStack(Material.STRING, 2 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.WEB, 1));
                drops.add(new ItemStack(Material.SPIDER_EYE, 1));
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.ZOMBIE) {
                event.getDrops().add(new ItemStack(Material.ROTTEN_FLESH, 1));
                drops.add(new ItemStack(Material.BONE, 3 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.RABBIT) {
                drops.add(new ItemStack(Material.RABBIT_HIDE, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(3)));
            }
        }


        if(!(event.getEntity() instanceof Player)){
            for(ItemStack t : drops){
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), t);
            }

        }
    }




    /*
     * Updates the names of items that are picked up from the ground (sets there name to be yellow from wh ite)
     * Other than enchanted armour
     */
    @EventHandler
    public void onPickup(PlayerPickupItemEvent e){
        Weapon w = WeaponManager.getWeapon(e.getItem().getItemStack());
        if(w != null){
            if(w instanceof EnchantedWeapon){

                return;
            }

        }
        UtilClans.updateNames(e.getItem().getItemStack());
    }

    @EventHandler
    public void onTreeBreak(BlockBreakEvent e) {

        if(e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2) {
            Block down = e.getBlock().getRelative(BlockFace.DOWN);
            if((down.getType() == Material.GRASS || down.getType() == Material.DIRT) && e.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                return;
            }

            if(e.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                return;
            }


			/*
			for(Block b : getConnectedLogs(e.getBlock())) {
				cutTree(b);
			}
			 */
            cut(e.getBlock(), e.getPlayer());
        }
    }

    private void cutTree(Block b) {
        if(ClanUtilities.getClan(b.getLocation()) == null) {
            for(int i = -3; i < 0; i++) {
                Block down = b.getRelative(0, i, 0);
                if(down.getType() == Material.LEAVES
                        || down.getType() == Material.LEAVES_2) {
                    down.setType(Material.AIR);
                }
            }

            if(b.getRelative(BlockFace.DOWN).getType() != Material.DIRT
                    && b.getRelative(BlockFace.DOWN).getType() != Material.GRASS) {
                b.getWorld().spawnFallingBlock(b.getLocation(), b.getType(), b.getData());
                b.setType(Material.AIR);
            }
        }
    }

    private ArrayList<Block> getConnectedLogs(Block block)
    {
        World world = block.getWorld();
        Block[] adjacent = new Block[9];
        ArrayList<Block> logs = new ArrayList<>();

        if(!block.getRelative(BlockFace.UP).getType().name().contains("LOG")) {
            return logs;
        }

        adjacent[0] = world.getBlockAt(UtilLocation.offset(block.getLocation(), 0.0D, 0.0D, -1.0D));
        adjacent[1] = world.getBlockAt(UtilLocation.offset(block.getLocation(), 0.0D, 0.0D, 1.0D));
        adjacent[2] = world.getBlockAt(UtilLocation.offset(block.getLocation(), 1.0D, 0.0D, 0.0D));
        adjacent[3] = world.getBlockAt(UtilLocation.offset(block.getLocation(), -1.0D, 0.0D, 0.0D));
        adjacent[4] = world.getBlockAt(UtilLocation.offset(block.getLocation(), 1.0D, 0.0D, -1.0D));
        adjacent[5] = world.getBlockAt(UtilLocation.offset(block.getLocation(), -1.0D, 0.0D, -1.0D));
        adjacent[6] = world.getBlockAt(UtilLocation.offset(block.getLocation(), 1.0D, 0.0D, 1.0D));
        adjacent[7] = world.getBlockAt(UtilLocation.offset(block.getLocation(), -1.0D, 0.0D, 1.0D));
        adjacent[8] = world.getBlockAt(UtilLocation.offset(block.getLocation(), 0.0D, 1.0D, 0.0D));

        for (int i = 0; i < adjacent.length; i++)
        {

            Block b = adjacent[i];
            if ((b.getType() == Material.LOG) || (b.getType() == Material.LOG_2))
            {
                Block curBlock = b;
                while ((curBlock.getType() == Material.LOG) || (curBlock.getType() == Material.LOG_2))
                {
                    if(!logs.contains(curBlock)) {
                        logs.add(curBlock);
                    }
                    curBlock = world.getBlockAt(UtilLocation.offset(curBlock.getLocation(), 0.0D, 1.0D, 0.0D));

                }
            }




        }
        return logs;
    }


    public void cut(Block block, Player player)
    {
        if(ClanUtilities.getClan(block.getLocation()) == null) {
            BlockFace[] arraySome = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
            Block b3 = block;
            List<Block> blocks2 = new LinkedList<>();
            boolean found;
            do
            {
                found = false;
                BlockFace[] arrayOfBlockFace1;
                int j = (arrayOfBlockFace1 = arraySome).length;
                for (int i = 0; i < j; i++)
                {
                    BlockFace bf = arrayOfBlockFace1[i];
                    if ((b3.getRelative(bf).getType() == Material.LOG) || (b3.getRelative(bf).getType() == Material.LOG_2))
                    {
                        b3 = b3.getRelative(bf);
                        if ((!blocks2.contains(b3)) && (b3.getRelative(BlockFace.UP).getType() != Material.AIR))
                        {
                            blocks2.add(b3);
                            found = true;
                            break;
                        }
                    }
                }
            } while (found);
            ArrayList<Block> blocks = new ArrayList<>();
            ArrayList<Block> leaves = new ArrayList<>();
            getTree(block, blocks, leaves);
            if (blocks2.isEmpty()) {
                for (Block b : blocks) {
                    if ((b.getType() == Material.LOG) || (b.getType() == Material.LOG_2))
                    {
                        if(ClanUtilities.getClan(block.getLocation()) == null) {
                            for(int i = -3; i < 0; i++) {
                                Block down = b.getRelative(0, i, 0);
                                if(down.getType() == Material.LEAVES
                                        || down.getType() == Material.LEAVES_2) {
                                    down.setType(Material.AIR);
                                }
                            }

                            if((b.getRelative(BlockFace.DOWN).getType() == Material.GRASS || b.getRelative(BlockFace.DOWN).getType() == Material.DIRT) ) {
                                continue;
                            }
                            Material mat = b.getType();
                            Byte data = Byte.valueOf(b.getData());
                            Location loc = b.getLocation();
                            b.setType(Material.AIR);
                            FallingBlock ent = b.getWorld().spawnFallingBlock(loc.add(0.5D, 0.0D, 0.5D), mat, data.byteValue());
                            ent.setDropItem(false);
                        }
                    }
                }
            }
        }
    }

    public void getTree(Block anchor, ArrayList<Block> logs, ArrayList<Block> leaves)
    {
        if (logs.size() > 500) {
            return;
        }
        Block nextAnchor = null;

        nextAnchor = anchor.getRelative(BlockFace.NORTH);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_EAST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.EAST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_EAST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_WEST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.WEST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_WEST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        anchor = anchor.getRelative(BlockFace.UP);

        nextAnchor = anchor.getRelative(BlockFace.NORTH);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_EAST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.EAST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_EAST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_WEST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.WEST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_WEST);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SELF);
        if (((nextAnchor.getType().equals(Material.LOG)) || (nextAnchor.getType().equals(Material.LOG_2))) && (!logs.contains(nextAnchor)))
        {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        }
        else if (((nextAnchor.getType().equals(Material.LEAVES)) || (nextAnchor.getType().equals(Material.LEAVES_2))) && (!logs.contains(nextAnchor)))
        {
            leaves.add(nextAnchor);
        }
    }


    private Block[] getAdjacents(Block b) {
        Block[] adjacent = new Block[9];
        World world2 = b.getWorld();
        adjacent[0] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), 0.0D, 0.0D, -1.0D));
        adjacent[1] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), 0.0D, 0.0D, 1.0D));
        adjacent[2] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), 1.0D, 0.0D, 0.0D));
        adjacent[3] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), -1.0D, 0.0D, 0.0D));
        adjacent[4] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), 1.0D, 0.0D, -1.0D));
        adjacent[5] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), -1.0D, 0.0D, -1.0D));
        adjacent[6] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), 1.0D, 0.0D, 1.0D));
        adjacent[7] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), -1.0D, 0.0D, 1.0D));
        adjacent[8] = world2.getBlockAt(UtilLocation.offset(b.getLocation(), 0.0D, 1.0D, 0.0D));
        return adjacent;
    }


    /*
     * Stops magma cubes from splitting
     */
    @EventHandler
    public void onMagmaSplit(SlimeSplitEvent e){
        if(e.getEntity() instanceof MagmaCube){
            e.setCancelled(true);
        }
    }


    /*
     * Stops players from shooting bows in safe territory
     * Also stops players from shooting bows while in water
     */
    @EventHandler (priority= EventPriority.HIGHEST)
    public void onShoot(EntityShootBowEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();

            Clan c = ClanUtilities.getClan(p.getLocation());
            if(c instanceof AdminClan){
                AdminClan ac = (AdminClan) c;
                if(ac.isSafe()){
                    e.setCancelled(true);
                    return;
                }
            }

            if (p.getLocation().getBlock().getType() == Material.WATER || p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                UtilMessage.message(p, "Skill", "You cannot shoot bows in water!");
                if(Longshot.getArrows().containsKey((Arrow) e.getProjectile())){
                    Longshot.getArrows().remove((Arrow) e.getProjectile());
                }
                e.setCancelled(true);
                return;
            }

            Role r = Role.getRole(p);
            if(r != null) {
                Weapon wep = WeaponManager.getWeapon(p.getItemInHand());
                if(wep != null && wep instanceof MeteorBow) {
                    return;

                }

                if(r instanceof Assassin || r instanceof Ranger) {
                    return;
                }
                UtilMessage.message(p, "Bow", "Only Assassin's and Rangers can use bows!");
                e.setCancelled(true);
            }


        }
    }

    /*
     * Removes arrows when they hit the ground, or a player
     * Keeps the entity count low (good for performance)
     */
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event){
        if(event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            arrow.remove();
        }
    }


    /*
     * Throws out red dye everywhere when players die
     * Creates a blood splatter effect
     */
    public static HashMap<Item, Long> blood = new HashMap<>();
    @EventHandler
    public void onDeath(PlayerDeathEvent e){

        for(int i = 0; i < 10; i++){
            final Item item = e.getEntity().getWorld().dropItem(e.getEntity().getEyeLocation(), new ItemStack(Material.INK_SACK, 1, (byte) 1));
            item.setVelocity(new Vector((Math.random() - 0.5D) * 0.5, Math.random() * 0.5, (Math.random() - 0.5D) * 0.5));
            item.setPickupDelay(Integer.MAX_VALUE);
            blood.put(item, System.currentTimeMillis());
        }

    }

    /*
     * Makes sure the blood items get removed after 500 milliseconds
     */
    @EventHandler
    public void blood(UpdateEvent e){
        if(e.getType() == UpdateEvent.UpdateType.FASTEST){
            if(blood.isEmpty()){
                return;
            }
            Iterator<Map.Entry<Item, Long>> it = blood.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<Item, Long> next = it.next();
                if(UtilTime.elapsed(next.getValue(), 500)){
                    next.getKey().remove();
                    it.remove();
                }
            }
        }
    }


    @EventHandler
    public void onTravelHub(ButtonClickEvent e) {
        if(e.getMenu() instanceof TravelHubMenu) {
            if(e.getButton().getName().equals(ChatColor.RED + "Red Spawn")) {
                Clan redSpawn = ClanUtilities.getClan("Red Spawn");
                Clan myClan = ClanUtilities.getClan(e.getPlayer().getLocation());

                if(myClan != null) {
                    if(myClan.getName().equalsIgnoreCase(redSpawn.getName())) {
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You are already at Red Spawn.");
                    }else {
                        e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -300.5, 130, -300.5));
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Red Spawn.");
                    }
                }
            }else if(e.getButton().getName().equals(ChatColor.AQUA + "Blue Spawn")) {
                Clan blueSpawn = ClanUtilities.getClan("Blue Spawn");
                Clan myClan = ClanUtilities.getClan(e.getPlayer().getLocation());

                if(myClan != null) {
                    if(myClan.getName().equalsIgnoreCase(blueSpawn.getName())) {
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You are already at Blue Spawn.");
                    }else {
                        e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 300.5, 130, 300.5));
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Blue Spawn.");
                    }
                }
            }else if(e.getButton().getName().equals(ChatColor.AQUA + "Blue Shop")) {
                e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 224.5,70, -82.5 ));
                UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Blue Shop.");
            }else if(e.getButton().getName().equals(ChatColor.RED + "Red Shop")) {
                e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -159.5,74, 245.5 ));
                UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Red Shop.");
            }
        }
    }




    /*
     * Stops admins who havent logged in yet from clicking in there inventory
     */
    @EventHandler
    public void onInvOpen(InventoryClickEvent e){
        Client c = ClientUtilities.getOnlineClient(e.getWhoClicked().getUniqueId());
        if(c != null) {
            if(c.hasRank(Rank.ADMIN, false)){
                if(!c.isLoggedIn()){
                    e.setCancelled(true);
                    e.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onTNTPlace(BlockPlaceEvent e) {
        if(e.getBlock().getType() == Material.TNT) {
            if(!Clans.getOptions().isTNTEnabled()) {
                UtilMessage.message(e.getPlayer(), "TNT", "TNT is disabled for the first 3 days of each season.");
                e.setCancelled(true);
            }
        }
    }

    /*
     * Stops admins who havent logged in yet from moving at all
     */
    @EventHandler
    public void onMoveAdmin(PlayerMoveEvent e){
        if(e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()){
            Client c = ClientUtilities.getOnlineClient(e.getPlayer());
            if(c.hasRank(Rank.ADMIN, false)){
                if(!c.isLoggedIn()){
                    e.setCancelled(true);
                }
            }
        }
    }



    /*
     * Stops players from breaking other clans bases with pistons on the outside
     */
    @EventHandler
    public void onPistonEvent(BlockPistonExtendEvent e){
        for(Block b : e.getBlocks()){
            Clan c = ClanUtilities.getClan(b.getLocation());
            Clan d = ClanUtilities.getClan(e.getBlock().getLocation());
            if(c != null && d == null || d != c){
                e.setCancelled(true);
            }
        }
    }

    /*
     * Stops players from breaking other clans bases with pistons on the outside
     */
    @EventHandler
    public void onPistonEvent(BlockPistonRetractEvent e){
        for(Block b : e.getBlocks()){
            Clan c = ClanUtilities.getClan(b.getLocation());
            Clan d = ClanUtilities.getClan(e.getBlock().getLocation());
            if(c != null && d == null || d != c){

                e.setCancelled(true);
            }
        }
    }


    /*
     * Gives players money when they kill animals and monsters
     */
    @EventHandler
    public void onMobKillMoney(EntityDeathEvent e){
        if(!(e.getEntity() instanceof Player)){
            if(e.getEntity().getCustomName() != null) return;
            if(LogManager.getKiller(e.getEntity()) != null){
                if(LogManager.getKiller(e.getEntity()).getDamager() instanceof Player){
                    Player killer = (Player) LogManager.getKiller(e.getEntity()).getDamager();
                    Gamer gamer = GamerManager.getOnlineGamer(killer);

                    if(e.getEntity() instanceof Animals){
                        int amount = UtilMath.randomInt(50, 100);

                        Client c = ClientUtilities.getOnlineClient(killer);

                        UtilMessage.message(killer, "Reward", "You received " + ChatColor.GREEN + "$" + amount);

                        gamer.addCoins(amount);
                    }else if(e.getEntity() instanceof Monster){
                        int amount = UtilMath.randomInt(100, 225);

                        UtilMessage.message(killer, "Reward", "You received " + ChatColor.GREEN + "$" + amount);
                        gamer.addCoins(amount);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMapDrop(PlayerDropItemEvent e) {
        if(e.getItemDrop().getItemStack().getType() == Material.MAP) {
            if(!ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
                e.getItemDrop().remove();
            }
        }
    }


    @EventHandler
    public void onMapDeath(PlayerDeathEvent e){

        Iterator<ItemStack> iterator = e.getDrops().iterator();
        while(iterator.hasNext()){
            ItemStack next = iterator.next();
            if(next.getType() == Material.MAP){
                iterator.remove();
            }
        }


    }

    @EventHandler
    public void onMapTransfer(InventoryMoveItemEvent e) {
        if(e.getItem().getType() == Material.MAP) {
            if(e.getSource().getType() == InventoryType.PLAYER) {
                if(e.getDestination() != null) {
                    if(e.getDestination().getType() != InventoryType.PLAYER) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    /*
     * Sets a players food level to max when they join the server
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(e.getPlayer() != null){
            e.getPlayer().setFoodLevel(20);
        }
    }

    /*
     * Stops players from losing hunger
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }



    /*
     * Hides players who are appearing offline (admins) from new players that join
     */
    @EventHandler
    public void hideOffliners(PlayerJoinEvent e){
        for(UUID p : OfflineCommand.offline){
            Player d = Bukkit.getPlayer(p);
            if(d != null){
                e.getPlayer().hidePlayer(d);
            }
        }

    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e){
        if(e.getEntity().getWorld().getLivingEntities().size() > 500){
            if(e.getEntity().getCustomName() != null){
                e.setCancelled(true);
            }
        }
    }


}
