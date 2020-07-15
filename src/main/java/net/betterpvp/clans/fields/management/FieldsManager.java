package net.betterpvp.clans.fields.management;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.fields.mysql.FieldsRepository;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import sun.reflect.generics.repository.FieldRepository;


public class FieldsManager extends BPVPListener<Clans> {

    public FieldsManager(Clans i) {
        super(i);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
            Clan c = ClanUtilities.getClan(e.getBlockPlaced().getLocation());
            if (c != null) {
                if (c.getName().equals("Fields")) {
                    Material m = e.getBlockPlaced().getType();
                    if (m == Material.GOLD_ORE || m == Material.DIAMOND_ORE || m == Material.IRON_ORE
                            || m == Material.COAL_ORE || m == Material.LAPIS_ORE || m == Material.EMERALD_ORE
                            || m == Material.REDSTONE_ORE || m == Material.REDSTONE_ORE
                            || m == Material.ENDER_CHEST || m == Material.PUMPKIN || m == Material.MELON
                    || m == Material.GILDED_BLACKSTONE) {
                        FieldsRepository.saveOre(e.getBlockPlaced());
                        FieldsRepository.blocks.put(e.getBlockPlaced().getLocation(), e.getBlockPlaced().getType());
                    }
                }
            }


        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
            Clan c = ClanUtilities.getClan(e.getBlock().getLocation());
            if (c != null) {
                if (c.getName().equals("Fields")) {
                    FieldsRepository.deleteOre(e.getBlock());
                    if(FieldsRepository.blocks.containsKey(e.getBlock().getLocation())) {
                        FieldsRepository.blocks.remove(e.getBlock().getLocation());
                    }
                }
            }
        }
    }

}
