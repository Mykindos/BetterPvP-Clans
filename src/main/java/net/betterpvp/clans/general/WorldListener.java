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
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.skills.selector.page.ClassSelectionPage;
import net.betterpvp.clans.skills.selector.skills.ChannelSkill;
import net.betterpvp.clans.skills.selector.skills.ranger.Longshot;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.*;
import net.betterpvp.clans.weapon.weapons.legendaries.MeteorBow;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.client.commands.admin.OfflineCommand;
import net.betterpvp.core.client.commands.events.SpawnTeleportEvent;
import net.betterpvp.core.client.mysql.ClientRepository;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.CoreLoadedEvent;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.punish.PunishManager;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Gate;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.*;

public class WorldListener extends BPVPListener<Clans> {
    public WorldListener(Clans instance) {
        super(instance);
    }


    @EventHandler
    public void onTreeBreak(BlockBreakEvent e) {


        if (e.getBlock().getType().name().contains("LOG")) {
            if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) return;
            Block down = e.getBlock().getRelative(BlockFace.DOWN);
            if ((down.getType() == Material.GRASS_BLOCK || down.getType() == Material.DIRT) && e.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                return;
            }

            if (e.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                return;
            }

            cut(e.getBlock(), e.getPlayer());
        }
    }


    public void cut(Block block, Player player) {
        if (ClanUtilities.getClan(block.getLocation()) == null) {
            BlockFace[] arraySome = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            Block b3 = block;
            List<Block> blocks2 = new LinkedList<>();
            boolean found;
            do {
                found = false;
                BlockFace[] arrayOfBlockFace1;
                int j = (arrayOfBlockFace1 = arraySome).length;
                for (int i = 0; i < j; i++) {
                    BlockFace bf = arrayOfBlockFace1[i];
                    if ((b3.getRelative(bf).getType().name().contains("_LOG"))) {
                        b3 = b3.getRelative(bf);
                        if ((!blocks2.contains(b3)) && (b3.getRelative(BlockFace.UP).getType() != Material.AIR)) {
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
                    if ((b.getType().name().contains("_LOG"))) {
                        if (ClanUtilities.getClan(block.getLocation()) == null) {
                            for (int i = -3; i < 0; i++) {
                                Block down = b.getRelative(0, i, 0);
                                if (down.getType().name().contains("LEAVES")) {
                                    down.setType(Material.AIR);
                                }
                            }

                            if ((b.getRelative(BlockFace.DOWN).getType() == Material.GRASS_BLOCK || b.getRelative(BlockFace.DOWN).getType() == Material.DIRT)) {
                                continue;
                            }
                            Material mat = b.getType();
                            Byte data = Byte.valueOf(b.getData());
                            Location loc = b.getLocation();
                            b.setType(Material.AIR);
                            FallingBlock ent = b.getWorld().spawnFallingBlock(loc.add(0.5D, 0.0D, 0.5D), Bukkit.createBlockData(mat));
                            ent.setDropItem(false);
                        }
                    }
                }
            }
        }
    }

    public void getTree(Block anchor, ArrayList<Block> logs, ArrayList<Block> leaves) {
        if (logs.size() > 500) {
            return;
        }
        Block nextAnchor = null;

        nextAnchor = anchor.getRelative(BlockFace.NORTH);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_EAST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.EAST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_EAST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_WEST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.WEST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_WEST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        anchor = anchor.getRelative(BlockFace.UP);

        nextAnchor = anchor.getRelative(BlockFace.NORTH);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_EAST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.EAST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_EAST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SOUTH_WEST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.WEST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.NORTH_WEST);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
        nextAnchor = anchor.getRelative(BlockFace.SELF);
        if (((nextAnchor.getType().name().contains("_LOG"))) && (!logs.contains(nextAnchor))) {
            logs.add(nextAnchor);
            getTree(nextAnchor, logs, leaves);
        } else if (((nextAnchor.getType().name().contains("LEAVE"))) && (!logs.contains(nextAnchor))) {
            leaves.add(nextAnchor);
        }
    }

    /*
     * Stops players from lighting fires on stuff like grass, wood, etc.
     * Helps keep the map clean
     */
    @EventHandler
    public void blockFlint(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getPlayer().getItemInHand().getType() == Material.FLINT_AND_STEEL) {
                if (e.getClickedBlock().getType() != Material.TNT && e.getClickedBlock().getType() != Material.NETHERRACK) {
                    UtilMessage.message(e.getPlayer(), "Flint and Steel", "You may not use Flint and Steel on this block type!");
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * Speeds up the night time
     *
     * @param e
     */
    @EventHandler
    public void onTimeUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK_2) {
            World world = Bukkit.getWorld("world");
            if (world.getTime() > 13000) {
                world.setTime(world.getTime() + 20);
            }
        }
    }

    /*
     * Stops players from filling buckets with water or lava, and also breaks the bucket.
     */
    @EventHandler
    public void handleBucket(PlayerBucketFillEvent event) {
        event.setCancelled(true);
        UtilMessage.message(event.getPlayer(), "Game", "Your " + ChatColor.YELLOW + "Bucket" + ChatColor.GRAY + " broke!");
        event.getPlayer().getInventory().setItemInHand(new ItemStack(Material.IRON_INGOT, event.getPlayer().getItemInHand().getAmount() * 3));
    }


    /**
     * Opens the Build Management menu
     *
     * @param e
     */
    @EventHandler
    public void onOpenBuildManager(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType() == Material.ENCHANTING_TABLE) {

                e.getPlayer().openInventory(new ClassSelectionPage(e.getPlayer()).getInventory());
                e.setCancelled(true);
            }
        }
    }

    /*
     * Stops leaf decay in admin clan territory
     */
    @EventHandler
    public void stopLeafDecay(LeavesDecayEvent event) {
        if (!getInstance().hasStarted()) {
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
    public void setSunny(WeatherChangeEvent e) {
        World world = e.getWorld();
        if (!world.hasStorm()) e.setCancelled(true);

    }

    /*
     * Stops players from placing items such a levers and buttons on the outside of peoples bases
     * This is required, as previously, players could open the doors to an enemy base.
     */
    @EventHandler
    public void onAttachablePlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.LEVER || e.getBlock().getType().name().contains("_BUTTON")) {
            if (e.getBlockAgainst() != null) {
                Clan c = ClanUtilities.getClan(e.getBlockAgainst().getLocation());
                if (c != null) {
                    Clan d = ClanUtilities.getClan(e.getPlayer());
                    if (d != null && c == d) {
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
            player.getWorld().playEffect(player.getLocation(), Effect.BLAZE_SHOOT, 0, 15);
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.SPONGE, 15);
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.SPONGE, 15);
            player.getWorld().playEffect(player.getLocation(), Effect.STEP_SOUND, Material.SPONGE, 15);
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
                        || UtilBlock.getBlockUnder(player.getLocation()).getType().name().contains("WOOL")) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /*
     * Reduces the damage from bows by 30%
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBowDamage(CustomDamageEvent e) {
        if (e.getProjectile() != null) {
            if (e.getProjectile() instanceof Arrow) {
                double reduce = 0.70;
                if (e.getDamager() instanceof Player) {
                    Role r = Role.getRole((Player) e.getDamager());
                    if (r != null) {
                        if (!(r instanceof Ranger)) {
                            reduce = 0.50;
                        }
                    }
                }
                e.setDamage(e.getDamage() * reduce);

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

        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.FURNACE || block.getType() == Material.HOPPER
                || block.getType() == Material.DROPPER || block.getType() == Material.DISPENSER || block.getType() == Material.SHULKER_BOX
                || block.getType() == Material.LOOM || block.getType() == Material.SMOKER
                || block.getType() == Material.GRINDSTONE || block.getType() == Material.LECTERN || block.getType() == Material.BLAST_FURNACE
                || block.getType() == Material.STONECUTTER
                || block.getType() == Material.BARREL
                || block.getType() == Material.SMITHING_TABLE) {

            if (block.getLocation().getY() > 200) {
                UtilMessage.message(player, "Restriction", "You can only place chests lower than 200Y!");
                event.setCancelled(true);
            }
        }
    }

    /*
     * Prevent obsidian from being broken by non admins
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (ClientUtilities.getOnlineClient(event.getPlayer()).isAdministrating()) {
            return;
        }

        if (event.getBlock().getType() == Material.OBSIDIAN) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            if (ClanUtilities.getClan(event.getBlock().getLocation()) != null) {
                if (ClanUtilities.getClan(event.getBlock().getLocation()) instanceof AdminClan) {
                    UtilMessage.message(player, "Server", "You cannot break " + ChatColor.YELLOW + "Obsidian" + ChatColor.GRAY + ".");
                    return;
                }
            }
            event.getBlock().setType(Material.AIR);
            UtilMessage.message(player, "Server", "You cannot break " + ChatColor.YELLOW + "Obsidian" + ChatColor.GRAY + ".");
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
    public void preventTnTIgniting(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = e.getClickedBlock();
            if (b.getType() == Material.TNT) {
                Clan c = ClanUtilities.getClan(b.getLocation());
                if (c != null) {
                    if (c instanceof AdminClan) {
                        if (!ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
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

            if (event.getInventory().getType() == InventoryType.ANVIL) {
                event.setCancelled(true);
            }

            if (event.getInventory().getType() == InventoryType.BEACON) {
                event.setCancelled(true);
            }

            if (event.getInventory().getType() == InventoryType.BREWING
                    || event.getInventory().getType() == InventoryType.DISPENSER
                    || event.getInventory().getType() == InventoryType.CARTOGRAPHY
                    || event.getInventory().getType() == InventoryType.GRINDSTONE
                    || event.getInventory().getType() == InventoryType.LECTERN
                    || event.getInventory().getType() == InventoryType.SHULKER_BOX
                    || event.getInventory().getType() == InventoryType.LOOM) {
                UtilMessage.message(player, "Game", ChatColor.YELLOW + UtilFormat.cleanString(event.getInventory().getType().toString()) + ChatColor.GRAY + " is disabled.");
                event.setCancelled(true);
            }

            if (event.getInventory().getType() == InventoryType.ENCHANTING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent e) {
        if (e.getDestination().getType() == InventoryType.HOPPER) {

            if (e.getDestination() == null) {
                e.setCancelled(true);
            }

        } else if (e.getSource().getType() == InventoryType.HOPPER) {

            if (e.getSource() == null) {
                e.setCancelled(true);
            }
        }
    }


    /*
     * Stops crops from being trampled by mobs and players
     */
    @EventHandler
    public void soilChangePlayer(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock().getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }
    }


    /*
     * Stops non players from passing through portals
     * When we had the nether, people would pull the boss in.
     */
    @EventHandler
    public void onPortal(EntityPortalEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void clearArmourStands(CoreLoadedEvent e) {
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
     * Turns lapis into water when placed.
     */
    @EventHandler
    public void onLapisPlace(BlockPlaceEvent event) {


        if (event.getBlock().getType() == Material.LAPIS_BLOCK) {
            if (!ClientUtilities.getOnlineClient(event.getPlayer()).isAdministrating()) {
                if (PunishManager.isBuildLocked(event.getPlayer().getUniqueId())) return;
                Clan aClan = ClanUtilities.getClan(event.getBlock().getLocation());
                Clan bClan = ClanUtilities.getClan(event.getPlayer());

                if (aClan == null) {
                    if (event.getBlock().getLocation().getY() > 32) {
                        UtilMessage.message(event.getPlayer(), "Clans", "You can only place water in your own territory.");
                        event.setCancelled(true);
                        return;
                    }
                }

                if (bClan == null || !aClan.getName().equalsIgnoreCase(bClan.getName())) {
                    if (event.getBlock().getLocation().getY() > 32) {
                        UtilMessage.message(event.getPlayer(), "Clans", "You can only place water in your own territory.");
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            if (event.getBlock().getY() > 120) {
                UtilMessage.message(event.getPlayer(), "Clans", "You can only place water below 120Y");
                event.setCancelled(true);
                return;
            }
            Block block = event.getBlock();
            block.setType(Material.WATER);
            block.getLocation().getWorld().playSound(block.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0F, 1.0F);
            // block.getWorld().playEffect(block.getLocation(), Effect.SPLASH, 0);
            block.getState().update();
        }
    }

    /*
     * When transferring worlds, legendary and epic items would lose there artificial glow effect
     * This fixes that.
     */
    @EventHandler
    public void checkGlow(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SLOWEST) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                for (ItemStack i : p.getInventory().getContents()) {
                    if (i != null) {
                        if (i.hasItemMeta()) {
                            Weapon w = WeaponManager.getWeapon(i);
                            if (w != null) {
                                if (w instanceof ILegendary) {
                                    ILegendary legend = (ILegendary) w;
                                    if (legend.isTextured()) {
                                        continue;
                                    }
                                } else if (w instanceof EnchantedWeapon) {
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
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSwordDamage(CustomDamageEvent e) {

        if (e.getDamagee() instanceof Player) {
            Player damagee = (Player) e.getDamagee();
            Role damageeRole = Role.getRole(damagee);
            if (damageeRole != null) {
                if (damageeRole instanceof Assassin) {
                    if (!Clans.getOptions().isAssassinKnockbackEnabled()) {
                        e.setKnockback(false);
                    }


                }

            }
        }
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            Role r = Role.getRole(p);
            if (r == null) {
                e.setDamage(e.getDamage() * 0.80);
            } else {
                if (r instanceof Assassin) {
                    if (!Clans.getOptions().isAssassinDealKb()) {
                        e.setKnockback(false);
                    }
                }
            }
            if (p.getInventory().getItemInMainHand() != null) {
                Material m = p.getInventory().getItemInMainHand().getType();
                if (UtilItem.isSword(m)) {

                    if (m == Material.DIAMOND_SWORD) {
                        e.setDamage(5);
                    } else if (m == Material.GOLDEN_SWORD) {
                        e.setDamage(6);
                    } else if (m == Material.IRON_SWORD) {
                        e.setDamage(4.5);

                        Weapon w = WeaponManager.getWeapon(p.getInventory().getItemInMainHand());
                        if (w != null) {
                            if (w instanceof EnchantedWeapon) {
                                EnchantedWeapon enchantedWeapon = (EnchantedWeapon) w;

                                e.setDamage(e.getDamage() + enchantedWeapon.getBonus());

                            }
                        }
                    } else if (m == Material.STONE_SWORD) {
                        e.setDamage(3);
                    } else if (m == Material.WOODEN_SWORD) {
                        e.setDamage(2);
                    }

                } else if (UtilItem.isAxe(m)) {
                    if (m == Material.DIAMOND_AXE) {
                        e.setDamage(4);
                    } else if (m == Material.GOLDEN_AXE) {
                        e.setDamage(5);
                    } else if (m == Material.IRON_AXE) {
                        e.setDamage(3);
                    } else if (m == Material.STONE_AXE) {
                        e.setDamage(2);
                    } else if (m == Material.WOODEN_AXE) {
                        e.setDamage(1);
                    }
                } else if (m == Material.TRIDENT) {
                    e.setDamage(2);
                } else if (m == Material.DIAMOND_SHOVEL) {
                    e.setDamage(2);
                } else {
                    e.setDamage(e.getDamage() * 0.75);
                }
            } else {
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

        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Client c = ClientUtilities.getOnlineClient(player);


        if (c != null) {
            if (!c.isAdministrating()) {
                if (block.getType() == Material.OBSIDIAN || block.getType() == Material.BEDROCK || block.getType() == Material.WATER_BUCKET
                        || block.getType() == Material.SPAWNER || block.getType() == Material.COBWEB || block.getType() == Material.BREWING_STAND
                        || block.getType() == Material.BREWING_STAND || block.getType().name().contains("_BED")) {
                    UtilMessage.message(player, "Server", "You cannot place " + ChatColor.YELLOW
                            + WordUtils.capitalizeFully(block.getType().toString()) + ChatColor.GRAY + ".");
                    event.setCancelled(true);
                    return;
                }

                Clan clan = ClanUtilities.getClan(player);

                if (ClanUtilities.getClan(block.getLocation()) != clan && ClanUtilities.getClan(block.getLocation()) != null) {
                    return;
                }

                if (event.getBlock().getType() == Material.OAK_DOOR
                        || event.getBlock().getType() == Material.ACACIA_DOOR
                        || event.getBlock().getType() == Material.SPRUCE_DOOR
                        || event.getBlock().getType() == Material.BIRCH_DOOR
                        || event.getBlock().getType() == Material.JUNGLE_DOOR
                        || event.getBlock().getType() == Material.DARK_OAK_DOOR) {
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.IRON_DOOR));
                    UtilMessage.message(event.getPlayer(), "Game", "Please use " + ChatColor.YELLOW + "Iron Doors" + ChatColor.GRAY + " (You can right click to open them).");
                } else if (event.getBlock().getType() == Material.OAK_TRAPDOOR) {
                    event.getBlock().setType(Material.AIR);
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.IRON_TRAPDOOR));
                    UtilMessage.message(event.getPlayer(), "Game", "Please use " + ChatColor.YELLOW + "Iron Trap Doors" + ChatColor.GRAY + " (You can right click to open them).");
                }

            }
        }
    }

    /**
     * Prevent players setting a new spawn
     *
     * @param e event
     */
    @EventHandler
    public void disableBeds(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getType().name().contains("_BED")) {
                e.setCancelled(true);
            }
        }
    }

    /*
     * Stops players from taking stuff off armour stands and item frames in
     * admin territory
     */
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand || e.getRightClicked() instanceof ItemFrame) {

            Clan c = ClanUtilities.getClan(e.getRightClicked().getLocation());
            if (c != null) {
                if (c instanceof AdminClan) {
                    if (!ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {

                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    /*
     * Logs the location and player of chests that are opened in the wilderness
     * Useful for catching xrayers.
     */
    @EventHandler
    public void onInt(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Material m = e.getClickedBlock().getType();
            if (m == Material.CHEST || m == Material.TRAPPED_CHEST
                    || m == Material.FURNACE || m == Material.DROPPER || m == Material.CAULDRON
                    || m == Material.SHULKER_BOX || m == Material.BARREL) {
                if (ClanUtilities.getClan(e.getPlayer().getLocation()) == null) {

                    int x = (int) e.getClickedBlock().getLocation().getX();
                    int y = (int) e.getClickedBlock().getLocation().getY();
                    int z = (int) e.getClickedBlock().getLocation().getZ();
                    Log.write("Trap Chest", e.getPlayer().getName() + " opened a chest at " + e.getClickedBlock().getWorld().getName() + ", " + x + "," + y + "," + z);
                }
            }
        }
    }

    @EventHandler
    public void updateTimePlayed(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_60) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Client c = ClientUtilities.getOnlineClient(p);

                c.setTimePlayed(c.getTimePlayed() + 1);

                ClientRepository.updateTimePlayed(c);
            }

        }
    }


    @EventHandler
    public void onPlaceTNT(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.TNT) {
            if (EffectManager.hasEffect(e.getPlayer(), EffectType.PROTECTION)) {
                EffectManager.removeEffect(e.getPlayer(), EffectType.PROTECTION);
            }
        }
    }

    /*
     * Allows players to dye leather armour
     * As well as blocks certain items from being crafted such as TNT
     */
    @EventHandler
    public void craftItem(PrepareItemCraftEvent e) {

        if (e.getRecipe() != null) {
            if (e.getRecipe().getResult() != null) {

                if (e.getRecipe().getResult().getType() == Material.LEATHER_CHESTPLATE
                        || e.getRecipe().getResult().getType() == Material.LEATHER_LEGGINGS
                        || e.getRecipe().getResult().getType() == Material.LEATHER_HELMET
                        || e.getRecipe().getResult().getType() == Material.LEATHER_BOOTS) {

                    return;

                }

                if (e.getRecipe().getResult().getType().name().contains("BANNER")) {
                    return;
                }

                Material itemType = e.getRecipe().getResult().getType();
                if (itemType == Material.TNT || itemType == Material.DISPENSER
                        || itemType == Material.SLIME_BLOCK || itemType == Material.COMPASS
                        || itemType == Material.PISTON || itemType == Material.PISTON_HEAD || itemType == Material.ENCHANTING_TABLE
                        || itemType == Material.GLASS_PANE
                        || itemType == Material.BREWING_STAND || itemType == Material.GOLDEN_APPLE || itemType == Material.GOLDEN_CARROT
                        || itemType == Material.ANVIL
                        || itemType.name().toLowerCase().contains("boat")) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                } else {
                    e.getInventory().setResult(UtilItem.updateNames(e.getRecipe().getResult()));
                }

            }
        }


    }

    @EventHandler
    public void spawnTeleport(SpawnTeleportEvent e) {
        if (UtilClans.hasValuables(e.getPlayer())) {
            UtilMessage.message(e.getPlayer(), "Spawn", "Unable to teleport with valuable items in your inventory.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreakGate(BlockBreakEvent e) {

        if (e.getBlock().getType().name().contains("GATE")) {
            Clan aClan = ClanUtilities.getClan(e.getPlayer());
            Clan bClan = ClanUtilities.getClan(e.getBlock().getLocation());

            Gate g = (Gate) e.getBlock().getState().getData();
            if (g.isOpen()) {
                if (bClan != null) {
                    if (aClan == null || (aClan != null && !aClan.getName().equalsIgnoreCase(bClan.getName()))) {
                        if (e.getPlayer().getLocation().distance(e.getBlock().getLocation()) < 1.5) {

                            UtilVelocity.velocity(e.getPlayer(), UtilVelocity.getTrajectory(e.getPlayer().getLocation(), UtilClans.closestWildernessBackwards(e.getPlayer())),
                                    0.5, true, 0.25, 0.25, 0.25, false);

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
    public void onBrew(BrewEvent e) {
        e.setCancelled(true);
    }

    /*
     * Stops players from breaking Item Frames in admin territory
     */
    @EventHandler
    public void onBreak(HangingBreakByEntityEvent e) {
        Clan c = ClanUtilities.getClan(e.getEntity().getLocation());
        if (c != null) {
            if (c instanceof AdminClan) {
                if (e.getRemover() instanceof Player) {
                    if (ClientUtilities.getOnlineClient((Player) e.getRemover()).isAdministrating()) {
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
    public void armorStand(PlayerArmorStandManipulateEvent e) {

        Clan c = ClanUtilities.getClan(e.getRightClicked().getLocation());
        if (c != null) {
            if (c instanceof AdminClan) {
                if (!ClientUtilities.getClient(e.getPlayer()).isAdministrating()) {
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
    public void onArmorStandDeath(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame) {

            Clan c = ClanUtilities.getClan(e.getEntity().getLocation());
            if (c != null) {
                if (c instanceof AdminClan) {
                    if (e.getDamager() instanceof Player) {
                        if (ClientUtilities.getClient((Player) e.getDamager()).isAdministrating()) {
                            return;
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    /*
     * Stops players from interacting with item frames and armour stands (left click)
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.ITEM_FRAME || e.getClickedBlock().getType() == Material.ARMOR_STAND) {
                Clan c = ClanUtilities.getClan(e.getClickedBlock().getLocation());
                if (c != null) {
                    if (c instanceof AdminClan) {
                        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
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
        if (event.getEntity().getCustomName() == null) {
            if (event.getEntityType() != EntityType.PLAYER) {
                drops.clear();
            }
            if (event.getEntityType() == EntityType.CHICKEN) {
                drops.add(new ItemStack(Material.CHICKEN, 1));
                drops.add(new ItemStack(Material.FEATHER, 2 + UtilMath.randomInt(1)));
            } else if (event.getEntityType() == EntityType.COW) {
                drops.add(new ItemStack(Material.BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.LEATHER, 1 + UtilMath.randomInt(2)));
            }
            if (event.getEntityType() == EntityType.MUSHROOM_COW) {
                drops.add(new ItemStack(Material.BEEF, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.RED_MUSHROOM, 2 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.OCELOT) {
                int rand = UtilMath.randomInt(10);
                if (rand == 0 || rand == 1 || rand == 2) {
                    drops.add(new ItemStack(Material.LEATHER, 1 + UtilMath.randomInt(2)));
                } else if (rand == 3 || rand == 4 || rand == 5) {
                    drops.add(new ItemStack(Material.COD, 2 + UtilMath.randomInt(2)));
                } else if (rand == 6 || rand == 7) {
                    drops.add(new ItemStack(Material.COAL, 1 + UtilMath.randomInt(2)));
                } else {
                    drops.add(new ItemStack(Material.COD, 10 + UtilMath.randomInt(10)));
                }
                drops.add(new ItemStack(Material.BONE, 4 + UtilMath.randomInt(4)));

            } else if (event.getEntityType() == EntityType.PIG) {
                drops.add(new ItemStack(Material.PORKCHOP, 1 + UtilMath.randomInt(2)));
            } else if (event.getEntityType() == EntityType.SHEEP) {
                drops.add(new ItemStack(Material.WHITE_WOOL, 1 + UtilMath.randomInt(3)));
                drops.add(new ItemStack(Material.WHITE_WOOL, 1 + UtilMath.randomInt(4)));
            } else if (event.getEntityType() == EntityType.VILLAGER) {
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(3)));
            } else if (event.getEntityType() == EntityType.BLAZE) {
                drops.add(new ItemStack(Material.BLAZE_ROD, 1));
                drops.add(new ItemStack(Material.BONE, 6 + UtilMath.randomInt(7)));
            } else if (event.getEntityType() == EntityType.CAVE_SPIDER) {

                drops.add(new ItemStack(Material.COBWEB, 1));
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
                if (z.getEquipment().getItemInMainHand().getType() == Material.GOLDEN_AXE) {
                    drops.add(new ItemStack(Material.GOLDEN_AXE));
                }
                drops.add(new ItemStack(Material.BONE, 2 + UtilMath.randomInt(2)));
                if (UtilMath.randomInt(50) > 48) {
                    ItemStack[] temp = {new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_BOOTS),
                            new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS)};
                    drops.add(temp[UtilMath.randomInt(temp.length - 1)]);
                }
                if (UtilMath.randomInt(100) > 90) {
                    drops.add(new ItemStack(Material.GOLDEN_PICKAXE));
                } else if (UtilMath.randomInt(1000) > 990) {
                    drops.add(new ItemStack(Material.GOLDEN_SWORD));
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
                drops.add(new ItemStack(Material.COBWEB, 1));
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


        if (!(event.getEntity() instanceof Player)) {
            for (ItemStack t : drops) {
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), t);
            }

        }
    }


    /*
     * Updates the names of items that are picked up from the ground (sets there name to be yellow from wh ite)
     * Other than enchanted armour
     */
    @EventHandler
    public void onPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Weapon w = WeaponManager.getWeapon(e.getItem().getItemStack());
            if (w != null) {
                if (w instanceof EnchantedWeapon) {

                    return;
                }

            }
            UtilClans.updateNames(e.getItem().getItemStack());
        }
    }

    /*
     * Stops magma cubes from splitting
     */
    @EventHandler
    public void onMagmaSplit(SlimeSplitEvent e) {
        if (e.getEntity() instanceof MagmaCube) {
            e.setCancelled(true);
        }
    }


    /*
     * Stops players from shooting bows in safe territory
     * Also stops players from shooting bows while in water
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            Clan c = ClanUtilities.getClan(p.getLocation());
            if (c instanceof AdminClan) {
                AdminClan ac = (AdminClan) c;
                if (ac.isSafe()) {
                    e.setCancelled(true);
                    return;
                }
            }

            if (p.getLocation().getBlock().getType() == Material.WATER) {
                UtilMessage.message(p, "Skill", "You cannot shoot bows in water!");
                if (Longshot.getArrows().containsKey((Arrow) e.getProjectile())) {
                    Longshot.getArrows().remove((Arrow) e.getProjectile());
                }
                e.setCancelled(true);
                return;
            }

            Role r = Role.getRole(p);
            if (r != null) {
                Weapon wep = WeaponManager.getWeapon(p.getItemInHand());
                if (wep != null && wep instanceof MeteorBow) {
                    return;

                }

                if (r instanceof Assassin || r instanceof Ranger) {
                    return;
                }
                UtilMessage.message(p, "Bow", "Only Assassin's and Rangers can use bows!");
                e.setCancelled(true);
            }
        }
    }


    /*
     * Throws out red dye everywhere when players die
     * Creates a blood splatter effect
     */
    public static HashMap<Item, Long> blood = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        for (int i = 0; i < 10; i++) {
            final Item item = e.getEntity().getWorld().dropItem(e.getEntity().getEyeLocation(), new ItemStack(Material.RED_DYE, 1));
            item.setVelocity(new Vector((Math.random() - 0.5D) * 0.5, Math.random() * 0.5, (Math.random() - 0.5D) * 0.5));
            item.setPickupDelay(Integer.MAX_VALUE);
            blood.put(item, System.currentTimeMillis());
        }

    }

    /**
     * No hand swapping!
     *
     * @param e the event
     */
    @EventHandler
    public void onSwapHand(PlayerSwapHandItemsEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onJoinShield(PlayerJoinEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (UtilClans.isUsableWithShield(e.getPlayer().getInventory().getItemInMainHand())) {
                    Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());

                    if (gamer != null) {
                        Role role = Role.getRole(e.getPlayer());
                        if (role != null) {
                            RoleBuild build = gamer.getActiveBuild(role.getName());
                            if (build != null) {
                                if (gamer.getActiveBuild(role.getName()).getActiveSkills().stream().anyMatch(s -> s != null && s instanceof ChannelSkill)) {
                                    e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                                }
                            }
                        }

                        Weapon weapon = WeaponManager.getWeapon(e.getPlayer().getInventory().getItemInMainHand());
                        if (weapon != null && weapon instanceof ChannelWeapon) {
                            e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                        }

                    }
                }
            }
        }.runTaskLater(getInstance(), 20);

    }

    @EventHandler
    public void onPickupGiveShield(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            ItemStack item = e.getItem().getItemStack();
            if (item != null) {
                if (UtilClans.isUsableWithShield(item)) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.getInventory().getItemInMainHand().getType() == item.getType()) {
                                Role role = Role.getRole(player);
                                if (role != null) {
                                    Gamer gamer = GamerManager.getOnlineGamer(player);
                                    if (gamer != null) {
                                        if (gamer.getActiveBuild(role.getName()).getActiveSkills().stream().anyMatch(s -> s != null && s instanceof ChannelSkill)) {
                                            player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                                            return;
                                        }
                                    }
                                }

                                Weapon weapon = WeaponManager.getWeapon(item);
                                if (weapon != null && weapon instanceof ChannelWeapon) {
                                    player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                                }
                            }
                        }
                    }.runTaskLater(getInstance(), 10);

                }
            }
        }
    }

    @EventHandler
    public void onItemSwap(PlayerItemHeldEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItem(e.getNewSlot());
        if (item != null) {
            if (UtilClans.isUsableWithShield(item)) {
                if (e.getPlayer().getInventory().getItemInOffHand().getType() != Material.SHIELD) {
                    Role role = Role.getRole(e.getPlayer());
                    if (role != null) {
                        Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
                        if (gamer != null) {
                            RoleBuild build = gamer.getActiveBuild(role.getName());
                            if (build != null) {
                                if (build.getActiveSkills().stream().anyMatch(s -> s != null && s instanceof ChannelSkill)) {
                                    e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                                    return;
                                }
                            }

                        }
                    }

                    Weapon weapon = WeaponManager.getWeapon(item);
                    if (weapon != null && weapon instanceof ChannelWeapon) {
                        e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
                    }

                } else {
                    // Remove if not a channel weapon
                    Weapon weapon = WeaponManager.getWeapon(item);
                    if (weapon != null && !(weapon instanceof ChannelWeapon)) {
                        e.getPlayer().getInventory().setItemInOffHand(null);
                    }
                }
            } else {
                if (e.getPlayer().getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                    e.getPlayer().getInventory().setItemInOffHand(null);
                }
            }
        }
    }


    @EventHandler
    public void onPickupShield(EntityPickupItemEvent e) {
        if (e.getItem().getItemStack().getType() == Material.SHIELD) {
            e.setCancelled(true);
            e.getItem().remove();
        }
    }

    @EventHandler
    public void onClickOffhand(InventoryClickEvent e) {

        if (e.getClickedInventory() != null) {
            if (e.getCurrentItem() != null) {
                if (e.getCurrentItem().getType() == Material.SHIELD) {
                    e.setCancelled(true);
                }
            }
            if (e.getSlot() == 40) {
                //  e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void removeOffhand(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                ItemStack offhand = player.getInventory().getItemInOffHand();
                if (offhand != null) {
                    if (offhand.getType() != Material.SHIELD && offhand.getType() != Material.AIR) {
                        ItemStack temp = player.getInventory().getItemInOffHand().clone();
                        player.getInventory().setItemInOffHand(null);
                        UtilItem.insert(player, temp);
                    }
                }

            });
        }
    }

    /*
     * Stops ground items from being destroyed from things like lava, fire, lightning, etc.
     */
    @EventHandler
    public void itemDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Item) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropOffhand(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() == Material.SHIELD) {
            e.setCancelled(true);
        }
    }

    /*
     * Makes sure the blood items get removed after 500 milliseconds
     */
    @EventHandler
    public void blood(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FASTEST) {
            if (blood.isEmpty()) {
                return;
            }
            Iterator<Map.Entry<Item, Long>> it = blood.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Item, Long> next = it.next();
                if (UtilTime.elapsed(next.getValue(), 500)) {
                    next.getKey().remove();
                    it.remove();
                }
            }
        }
    }


    @EventHandler
    public void onTravelHub(ButtonClickEvent e) {
        if (e.getMenu() instanceof TravelHubMenu) {
            if (e.getButton().getName().equals(ChatColor.RED + "Red Spawn")) {
                Clan redSpawn = ClanUtilities.getClan("Red Spawn");
                Clan myClan = ClanUtilities.getClan(e.getPlayer().getLocation());

                if (myClan != null) {
                    if (myClan.getName().equalsIgnoreCase(redSpawn.getName())) {
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You are already at Red Spawn.");
                    } else {
                        e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -300.5, 130, -300.5));
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Red Spawn.");
                    }
                }
            } else if (e.getButton().getName().equals(ChatColor.AQUA + "Blue Spawn")) {
                Clan blueSpawn = ClanUtilities.getClan("Blue Spawn");
                Clan myClan = ClanUtilities.getClan(e.getPlayer().getLocation());

                if (myClan != null) {
                    if (myClan.getName().equalsIgnoreCase(blueSpawn.getName())) {
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You are already at Blue Spawn.");
                    } else {
                        e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 300.5, 130, 300.5));
                        UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Blue Spawn.");
                    }
                }
            } else if (e.getButton().getName().equals(ChatColor.AQUA + "Blue Shop")) {
                e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), 224.5, 70, -82.5));
                UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Blue Shop.");
            } else if (e.getButton().getName().equals(ChatColor.RED + "Red Shop")) {
                e.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -159.5, 74, 245.5));
                UtilMessage.message(e.getPlayer(), "Travel Hub", "You teleported to Red Shop.");
            }
        }
    }


    /*
     * Stops admins who havent logged in yet from clicking in there inventory
     */
    @EventHandler
    public void onInvOpen(InventoryClickEvent e) {
        Client c = ClientUtilities.getOnlineClient(e.getWhoClicked().getUniqueId());
        if (c != null) {
            if (c.hasRank(Rank.ADMIN, false)) {
                if (!c.isLoggedIn()) {
                    e.setCancelled(true);
                    e.getWhoClicked().closeInventory();
                }
            }
        }
    }

    @EventHandler
    public void onTNTPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType() == Material.TNT) {
            if (!Clans.getOptions().isTNTEnabled()) {
                UtilMessage.message(e.getPlayer(), "TNT", "TNT is disabled for the first 3 days of each season.");
                e.setCancelled(true);
            }
        }
    }

    /*
     * Stops admins who havent logged in yet from moving at all
     */
    @EventHandler
    public void onMoveAdmin(PlayerMoveEvent e) {
        if (e.getFrom().getBlockX() != e.getTo().getBlockX()
                || e.getFrom().getBlockY() != e.getTo().getBlockY()
                || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            Client c = ClientUtilities.getOnlineClient(e.getPlayer());
            if (c.hasRank(Rank.ADMIN, false)) {
                if (!c.isLoggedIn()) {
                    e.setCancelled(true);
                }
            }
        }
    }


    /*
     * Stops players from breaking other clans bases with pistons on the outside
     */
    @EventHandler
    public void onPistonEvent(BlockPistonExtendEvent e) {
        for (Block b : e.getBlocks()) {
            Clan c = ClanUtilities.getClan(b.getLocation());
            Clan d = ClanUtilities.getClan(e.getBlock().getLocation());
            if (c != null && d == null || d != c) {
                e.setCancelled(true);
            }
        }
    }

    /*
     * Stops players from breaking other clans bases with pistons on the outside
     */
    @EventHandler
    public void onPistonEvent(BlockPistonRetractEvent e) {
        for (Block b : e.getBlocks()) {
            Clan c = ClanUtilities.getClan(b.getLocation());
            Clan d = ClanUtilities.getClan(e.getBlock().getLocation());
            if (c != null && d == null || d != c) {

                e.setCancelled(true);
            }
        }
    }

    /*
     * Increases the amount of melee damage the ranger class takes by 20%
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRangerMeleeTake(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player p = (Player) e.getDamagee();
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                Role r = Role.getRole(p);
                if (r != null) {
                    if (r instanceof Ranger) {
                        e.setDamage(e.getDamage() * 1.20);
                    }
                }
            }
        }
    }


    /*
     * Sets a players food level to max when they join the server
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer() != null) {
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
    public void hideOffliners(PlayerJoinEvent e) {
        for (UUID p : OfflineCommand.offline) {
            Player d = Bukkit.getPlayer(p);
            if (d != null) {
                e.getPlayer().hidePlayer(d);
            }
        }

    }

    /*
     * Removes arrows when they hit the ground, or a player
     * Keeps the entity count low (good for performance)
     */
    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            arrow.remove();
        }
    }

    /*
     * Gives players money when they kill animals and monsters
     */
    @EventHandler
    public void onMobKillMoney(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            if (e.getEntity().getCustomName() != null) return;
            if (LogManager.getKiller(e.getEntity()) != null) {
                if (LogManager.getKiller(e.getEntity()).getDamager() instanceof Player) {
                    Player killer = (Player) LogManager.getKiller(e.getEntity()).getDamager();

                    if (e.getEntity() instanceof Animals) {
                        int amount = UtilMath.randomInt(50, 100);
                        Gamer g = GamerManager.getOnlineGamer(killer);

                        UtilMessage.message(killer, "Reward", "You received " + ChatColor.GREEN + "$" + amount);

                        g.addCoins(amount);
                    } else if (e.getEntity() instanceof Monster) {
                        int amount = UtilMath.randomInt(100, 225);
                        Gamer g = GamerManager.getOnlineGamer(killer);

                        UtilMessage.message(killer, "Reward", "You received " + ChatColor.GREEN + "$" + amount);
                        g.addCoins(amount);
                    }
                }
            }
        }
    }

    @EventHandler
    public void stopSpawns(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL) {
            if (e.getEntityType() == EntityType.PHANTOM) {
                e.setCancelled(true);
            }
        }
    }


}
