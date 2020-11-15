package net.betterpvp.clans.farming.bee;

import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ClanDeleteEvent;
import net.betterpvp.clans.farming.bee.nms.CustomBee;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeeListener extends BPVPListener<Clans> {


    public BeeListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BEEHIVE) {
            Clan clan = ClanUtilities.getClan(e.getEntity().getLocation());
            if (clan != null) {
                if (!clan.isOnline()) {
                    e.setCancelled(true);
                    return;
                }
                if (clan.getBeeData().isEmpty()) return;
                Bee theBee = (Bee) e.getEntity();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (theBee != null) {
                            theBee.setAnger(0);
                        }
                    }
                }.runTaskLater(getInstance(), 5l);


            } else {
                e.setCancelled(true);
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlaceBeehive(BlockPlaceEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getBlock().getType() != Material.BEEHIVE) return;

        Clan pClan = ClanUtilities.getClan(e.getPlayer());
        if (pClan != null) {
            Clan lClan = ClanUtilities.getClan(e.getBlock().getLocation());
            if (lClan != null) {
                if (pClan.equals(lClan)) {
                    if (pClan.getBeeData().size() < 5 + (pClan.getLevel() * 5)) {
                        BeeData bee = new BeeData(e.getBlock().getLocation());
                        pClan.getBeeData().add(bee);
                        BeeRepository.saveBeeData(pClan, bee);
                    } else {
                        UtilMessage.message(e.getPlayer(), "Farming", "Your clan can only have up to "
                                + ChatColor.GREEN + (5 + (pClan.getLevel() * 5)) + ChatColor.GRAY + " beehives.");
                        e.setCancelled(true);
                    }

                }
            }

        }
    }

    @EventHandler
    public void onEnterBlock(EntityEnterBlockEvent e) {
        if (e.getBlock().getType() == Material.BEEHIVE) {
            if (e.getEntity() instanceof Bee) {
                e.setCancelled(true);
                Bee bee = (Bee) e.getEntity();
                bee.setHasNectar(false);
            }
        }
    }

    @EventHandler
    public void onDisband(ClanDeleteEvent e) {
        BeeRepository.wipeBees(e.getClan());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreakBeehive(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getBlock().getType() != Material.BEEHIVE) return;
        Clan lClan = ClanUtilities.getClan(e.getBlock().getLocation());
        if (lClan != null) {
            lClan.getBeeData().removeIf(b -> b.getLoc().equals(e.getBlock().getLocation()));
            BeeRepository.removeBeeData(e.getBlock().getLocation());
        }
    }


    @EventHandler
    public void onSpawn(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_08) {
            for (Clan clan : ClanUtilities.getClans()) {
                try {
                    if (!clan.isOnline()) continue;
                    if (clan.getBeeData().isEmpty()) continue;
                    if (clan.getHome() == null) continue;
                    int beeCount = getBeeCount(clan);

                    int beesToSpawn = Clans.getOptions().getMaxBees() - beeCount;
                    World world = clan.getHome().getWorld();
                    for (int x = beeCount; x < Math.min(clan.getBeeData().size(), beesToSpawn); x++) {
                        world.spawnEntity(clan.getBeeData().get(0).getLoc().clone().add(0, 1, 0), EntityType.BEE);
                    }
                } catch (Exception ex) {

                }

            }
        }
    }

    @EventHandler
    public void updateHives(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_01) {
            for (Clan clan : ClanUtilities.getClans()) {
                if (clan.getBeeData().isEmpty()) continue;
                boolean online = clan.isOnline();
                List<BeeData> remove = new ArrayList<>();
                for (BeeData data : clan.getBeeData()) {
                    if (data.isHarvestable()) continue;
                    if (!online) {
                        data.updateHarvestTime();
                        continue;
                    }
                    if (data.getHarvestTime() <= System.currentTimeMillis()) {
                        Block hive = Bukkit.getWorld("world").getBlockAt(data.getLoc());
                        if (hive.getType() == Material.BEEHIVE) {
                            Beehive hiveData = (Beehive) hive.getBlockData();
                            hiveData.setHoneyLevel(hiveData.getMaximumHoneyLevel());
                            hive.setBlockData(hiveData);

                            data.setHarvestable(true);
                        }else{
                            remove.add(data);
                        }
                    }
                }

                remove.forEach(b -> {
                    clan.getBeeData().remove(b);
                    BeeRepository.removeBeeData(b.getLoc());
                });
            }
        }
    }

    @EventHandler
    public void onHarvest(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getPlayer().getInventory().getItemInMainHand() == null) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.SHEARS) return;
        if (e.getClickedBlock().getType() != Material.BEEHIVE) return;

        Clan pClan = ClanUtilities.getClan(e.getPlayer());
        Clan lClan = ClanUtilities.getClan(e.getClickedBlock().getLocation());

        if (pClan != null && lClan != null) {
            if (pClan.equals(lClan)) {
                Optional<BeeData> opt = pClan.getBeeData().stream().filter(d -> d.getLoc().equals(e.getClickedBlock().getLocation())).findFirst();
                if (opt.isPresent()) {
                    BeeData data = opt.get();
                    Beehive hiveData = (Beehive) e.getClickedBlock().getBlockData();
                    if (hiveData.getHoneyLevel() >= hiveData.getMaximumHoneyLevel()) {
                        data.setHarvestable(false);
                        data.updateHarvestTime();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKillWildernessBees(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_08) {
            for (LivingEntity ent : Bukkit.getWorld("world").getLivingEntities()) {
                if (ent instanceof Bee) {
                    Clan lClan = ClanUtilities.getClan(ent.getLocation());
                    if (lClan == null) {
                        ent.setHealth(0);
                    }
                }
            }
        }else if(e.getType() == UpdateEvent.UpdateType.MIN_60){
            for (LivingEntity ent : Bukkit.getWorld("world").getLivingEntities()) {
                if (ent instanceof Bee) {
                    ent.setHealth(0);
                }
            }
        }
    }

    private int getBeeCount(Clan clan) {
        int beeCount = 0;

        beeCount += getLivingBeeCount(clan);


        /*
        for (BeeData d : clan.getBeeData()) {
            try {
                NBTTileEntity tileEntity = new NBTTileEntity(d.getLoc().getBlock().getState());
                if (tileEntity != null) {
                    beeCount += tileEntity.getCompoundList("Bees").size();
                }
            } catch (Exception ex) {

            }
        }*/


        return beeCount;
    }

    private int getLivingBeeCount(Clan clan) {
        int beeCount = 0;
        for (String chunks : clan.getTerritory()) {
            Chunk chunk = UtilFormat.stringToChunk(chunks);
            for (Entity bee : chunk.getEntities()) {
                if (bee instanceof Bee) {
                    beeCount++;
                }
            }
        }

        return beeCount;
    }
}
