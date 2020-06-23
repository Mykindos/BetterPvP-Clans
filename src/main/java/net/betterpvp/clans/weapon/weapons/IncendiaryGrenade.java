package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class IncendiaryGrenade extends Weapon {

    public static HashMap<Location, Long> areas = new HashMap<Location, Long>();
    public static List<Item> items = new ArrayList<Item>();

    public IncendiaryGrenade(Clans i) {
        super(i, Material.MAGMA_CREAM, (byte) 0, ChatColor.YELLOW + "Incendiary Grenade", new String[]{
                ChatColor.GRAY + "Left-Click: " + ChatColor.YELLOW + "Throw",
                ChatColor.GRAY + "  " + "Burns people who enter the blast area"}, false, 0);
    }

    @EventHandler
    public void onGrenadeUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.MAGMA_CREAM) return;


        if (isThisWeapon(player)) {
            if (ClanUtilities.canCast(player)) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (RechargeManager.getInstance().add(player, "Incendiary Grenade", 10, true)) {
                        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.MAGMA_CREAM));
                        UtilItem.remove(player, Material.MAGMA_CREAM, 1);
                        UtilItem.setItemNameAndLore(item.getItemStack(), Integer.toString(UtilMath.randomInt(10000)), new String[]{});
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity(player.getLocation().getDirection().multiply(1.8));
                        items.add(item);
                    }
                }
            }
        }
    }

    @EventHandler
    public void updateCreams(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.TICK) {
            Iterator<Item> iterator = items.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                Location location = item.getLocation();

                ParticleEffect.EXPLOSION_HUGE.display(location);

                if (UtilBlock.getBlockUnder(item.getLocation()).getType() != Material.AIR) {
                    areas.put(location, System.currentTimeMillis() + 6000L);
                    ParticleEffect.SMOKE_LARGE.display(location);

                    location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
                    item.remove();
                    iterator.remove();
                }
            }

            Iterator<Location> areaIterator = areas.keySet().iterator();
            while (areaIterator.hasNext()) {
                Location location = areaIterator.next();
                Long time = areas.get(location);
                Random random = new Random();

                if (System.currentTimeMillis() >= time) {
                    areaIterator.remove();
                    return;
                }

                for (int i = 0; i < 16; i++) {
                    Location displayLocation = new Location(location.getWorld(), (float) location.getX() + 0.5F, (float) (location.getY() + random.nextDouble() * 2.0F),
                            (float) location.getZ());
                    ParticleEffect.FLAME.display(displayLocation, (float) random.nextGaussian(), 0.0F, (float) random.nextGaussian(), 0.1F, 1, null);

                }

                location.getWorld().playSound(location, Sound.BLOCK_GRASS_BREAK, 0.6F, 0.0F);

                for (LivingEntity entity : UtilPlayer.getInRadius(location, 2)) {
                    entity.setFireTicks(60);
                }

            }
        }
    }
}
