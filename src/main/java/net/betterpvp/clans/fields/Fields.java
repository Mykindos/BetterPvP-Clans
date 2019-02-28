package net.betterpvp.clans.fields;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class Fields extends BPVPListener<Clans> {


    private static HashMap<BlockData, Long> blocks = new HashMap<>();
    private final static ItemStack[] ENDERCHEST_ITEMS = new ItemStack[]{new ItemStack(Material.GOLD_SWORD, 1),
            new ItemStack(Material.IRON_PICKAXE, 1), new ItemStack(Material.DIAMOND_SPADE, 1),
            new ItemStack(Material.SPONGE, 1), new ItemStack(Material.LAPIS_BLOCK, 2),
            new ItemStack(Material.DIAMOND_SPADE, 1), new ItemStack(Material.DIAMOND_PICKAXE, 1),
            new ItemStack(Material.FISHING_ROD, 1), new ItemStack(Material.ARROW, 16),
            new ItemStack(Material.DIAMOND_SWORD, 1), new ItemStack(Material.GOLD_AXE, 1),
            new ItemStack(Material.ENDER_PEARL, 3)};

    public Fields(Clans i) {
        super(i);

    }

    public long getRecharge() {
        int size = Bukkit.getServer().getOnlinePlayers().size();
        if (size > 65) {
            return 600000;
        } else if (size > 50) {
            return 750000;
        } else if (size > 35) {
            return 900000;
        } else if (size > 15) {
            return 1050000;
        } else {
            return 1200000;
        }
    }


    @EventHandler
    public void restoreUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC_30) {
            restore(false);
        }
    }


    public void restore(boolean force) {
        Iterator<Entry<BlockData, Long>> iterator = getMap().entrySet().iterator();
        long recharge = getRecharge();
        if (WEManager.isEventActive("MiningMadness")) {
            recharge = recharge / 2;
        }
        while (iterator.hasNext()) {
            Entry<BlockData, Long> next = iterator.next();
            if (UtilTime.elapsed(next.getValue(), recharge) || force) {
                Location loc = next.getKey().getLoc();
                if (loc.getBlock().getType() == Material.AIR) {
                    loc.getBlock().setType(next.getKey().getType());
                    iterator.remove();
                }
            }
        }


    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
            return;
        }
        if (!inFields(e.getBlock())) return;

        if (e.getBlock().getType() == Material.GLOWING_REDSTONE_ORE) {
            e.getBlock().setType(Material.REDSTONE_ORE);
        }

        Block b = e.getBlock();
        Player p = e.getPlayer();

        boolean doubleIt = WEManager.isEventActive("MiningMadness");
        e.setCancelled(true);
        if (p == null) return;
        for (Blocks bc : Blocks.values()) {
            if (b.getType() == bc.getMaterial() || bc == Blocks.REDSTONE && b.getType() == Material.GLOWING_REDSTONE_ORE) {
                getMap().put(new BlockData(b.getLocation(), b.getType()), System.currentTimeMillis());

                if (b.getType() == Blocks.LAPIS.getMaterial()) {
                    Dye dye = new Dye();
                    dye.setColor(DyeColor.BLUE);
                    b.getWorld().dropItemNaturally(b.getLocation(), dye.toItemStack(3));
                } else if (b.getType() == Blocks.ENDERCHEST.getMaterial()) {
                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.LEATHER, UtilMath.randomInt(10)));

                    e.getBlock().getWorld().dropItem(e.getBlock().getLocation(),
                            ENDERCHEST_ITEMS[UtilMath.randomInt(ENDERCHEST_ITEMS.length - 1)].clone());
                } else {
                    ItemStack clone = bc.getDrop().clone();
                    if (doubleIt) {
                        clone.setAmount(clone.getAmount() * 2);
                    }

                    b.getWorld().dropItemNaturally(b.getLocation(), clone);
                }
                b.setType(Material.AIR);

                return;
            }

        }
    }


    public static HashMap<BlockData, Long> getMap() {
        return blocks;
    }

    private boolean inFields(Block b) {
        Clan c = ClanUtilities.getClan(b.getLocation());
        if (c != null) {
            if (c instanceof AdminClan) {
                if (c.getName().contains("Fields")) {
                    return true;
                }
            }
        }
        return false;
    }
}
