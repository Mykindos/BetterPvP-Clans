package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.InsuranceType;
import net.betterpvp.clans.clans.insurance.Insurance;
import net.betterpvp.clans.clans.mysql.InsuranceRepository;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;

import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;


public class InsuranceListener extends BPVPListener<Clans> {

    public InsuranceListener(Clans i) {
        super(i);
    }

    public static ConcurrentLinkedQueue<Insurance> rollback = new ConcurrentLinkedQueue<>();

    @EventHandler
    public void removeOld(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.MIN_32) {
            new Thread(() -> {
                for (Clan c : ClanUtilities.clans) {
                    ListIterator<Insurance> it = c.getInsurance().listIterator();
                    while (it.hasNext()) {
                        Insurance i = it.next();
                        if (i != null) {
                            if (UtilTime.elapsed(i.getTime(), 86400000)) {

                                it.remove();
                            }
                        }
                    }
                }
                InsuranceRepository.removeInsurance();
            }).start();

        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK) {
            Insurance i = rollback.poll();
            if (i != null) {

                if (i.getMaterial() == Material.IRON_BLOCK
                        || i.getMaterial() == Material.DIAMOND_BLOCK || i.getMaterial() == Material.GOLD_BLOCK
                        || i.getMaterial() == Material.NETHERITE_BLOCK
                        || i.getMaterial() == Material.TNT || i.getMaterial() == Material.ENCHANTING_TABLE
                        || i.getMaterial() == Material.REDSTONE_BLOCK
                        || i.getMaterial() == Material.ICE
                        || i.getMaterial() == Material.WATER
                        || i.getMaterial() == Material.LAVA
                        || i.getMaterial() == Material.AIR
                        || i.getMaterial() == Material.BEEHIVE
                        || i.getMaterial().name().contains("CHEST")) {
                    return;
                }

                if (UtilTime.elapsed(i.getTime(), 86400000)) {
                    for (Clan c : ClanUtilities.clans) {
                        c.getInsurance().remove(i);
                    }
                    return;
                }


                if (i.getLocation().getBlock().getType() == i.getMaterial()) {
                    return;
                }

                if (i.getType() == InsuranceType.PLACE) {
                    i.getLocation().getBlock().setType(Material.AIR);
                } else if (i.getType() == InsuranceType.BREAK) {
                    i.getLocation().getBlock().setType(i.getMaterial());
                    BlockData data = Bukkit.createBlockData(i.getData());
                    i.getLocation().getBlock().setBlockData(data);
                    //i.getLocation().getBlock().setBlockData();
                }
            }

        }
    }

}
