package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.BlockRestoreData;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.UtilMath;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GlacialPrison extends Skill {

    public GlacialPrison(Clans i) {
        super(i, "Glacial Prison", "Paladin", getSwords, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to Activate",
                "",
                "Launches an orb, trapping any players",
                "within 5 blocks of it in a prison of ice for 5 seconds",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @EventHandler
    public void onHopperPickup(InventoryPickupItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        if (items.contains(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 25 - ((level - 1) * 2);
    }

    @Override
    public float getEnergy(int level) {

        return 40 - ((level - 1) * 3);
    }

    private List<Item> items = new ArrayList<>();

    @Override
    public void activateSkill(Player p) {
        Item item = p.getWorld().dropItem(p.getEyeLocation(), new ItemStack(Material.ICE, 1, (byte) 15));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(p.getLocation().getDirection().multiply(1.3));
        items.add(item);

    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.FASTEST) {
            ListIterator<Item> it = items.listIterator();
            while (it.hasNext()) {
                Item i = it.next();
                if (i.isOnGround()) {
                    for (Location loc : UtilMath.sphere(i.getLocation(), 5, true)) {
                        if (loc.getBlock().getType() == Material.AIR) {
                            new BlockRestoreData(loc.getBlock(), 30, (byte) 0, 5000L);
                            loc.getBlock().setType(Material.ICE);
                        }
                    }
                    i.remove();
                    it.remove();
                }
            }
        }
    }


    @Override
    public boolean usageCheck(Player p) {

        return true;
    }

    @Override
    public boolean requiresShield() {

        return false;
    }

}
