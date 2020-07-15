package net.betterpvp.clans.farming.bee;

import de.tr7zw.changeme.nbtapi.NBTTileEntity;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.farming.bee.nms.CustomBee;
import net.betterpvp.clans.worldevents.types.nms.BossPolarBear;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilLocation;
import net.betterpvp.core.utility.UtilMath;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PolarBear;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BeeListener extends BPVPListener<Clans> {


    public BeeListener(Clans instance) {
        super(instance);
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BEEHIVE) {
            e.setCancelled(true);
            Bee theBee = (Bee) e.getEntity();
            CustomBee customBee = new CustomBee(((CraftWorld) e.getEntity().getWorld()).getHandle());
            Bee bee = customBee.spawn(e.getEntity().getLocation());
            bee.setCannotEnterHiveTicks(theBee.getCannotEnterHiveTicks());
            bee.setHasNectar(theBee.hasNectar());
            bee.setHasStung(theBee.hasStung());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
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

                    BeeData bee = new BeeData(e.getBlock().getLocation());
                    pClan.getBeeData().add(bee);
                    BeeRepository.saveBeeData(pClan, bee);

                }
            }

        }
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
        if (e.getType() == UpdateEvent.UpdateType.MIN_16) {
            for (Clan clan : ClanUtilities.getClans()) {
                if (clan.getBeeData().isEmpty()) continue;
                int beeCount = 0;
                for (String chunks : clan.getTerritory()) {
                    Chunk chunk = UtilFormat.stringToChunk(chunks);
                    for (Entity bee : chunk.getEntities()) {
                        if (bee instanceof Bee) {
                            beeCount++;
                        }
                    }
                }

                for (BeeData d : clan.getBeeData()) {
                    NBTTileEntity tileEntity = new NBTTileEntity(d.getLoc().getBlock().getState());
                    if(tileEntity != null) {
                        beeCount += tileEntity.getCompoundList("Bees").size();
                    }
                }

                int beesToSpawn = Clans.getOptions().getMaxBees() - beeCount;
                for (int x = 0; x < Math.min(clan.getBeeData().size(), beesToSpawn); x++) {
                    CustomBee bee = new CustomBee(((CraftWorld) clan.getHome().getWorld()).getHandle());
                    bee.spawn(clan.getBeeData().get(UtilMath.randomInt(0, clan.getBeeData().size() - 1)).getLoc().clone().add(0, 1, 0));
                }

            }
        }
    }
}
