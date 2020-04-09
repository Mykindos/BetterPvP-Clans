package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.UUID;

public class WaterBottle extends Weapon {

    public static HashMap<UUID, Item> items = new HashMap<UUID, Item>();

    public WaterBottle(Clans i) {
        super(i, Material.POTION, (byte) 0, ChatColor.YELLOW + "Water Bottle", new String[]{
                ChatColor.GRAY + "Left-Click: " + ChatColor.YELLOW + "Throw",
                ChatColor.GRAY + "  " + "Douses Players",
                ChatColor.GRAY + "  " + "Douses Fires",
                "",
                ChatColor.GRAY + "Right-Click: " + ChatColor.YELLOW + "Drink",
                ChatColor.GRAY + "  " + "Douses Self",
                ChatColor.GRAY + "  " + "Fire Resistance I for 4 Seconds"}, false, 0);
    }

    @EventHandler
    public void onWaterDouse(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.POTION) return;
        if (isThisWeapon(player)) {
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                UtilItem.remove(player, Material.POTION, 1);
                Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.POTION));
                UtilItem.setItemNameAndLore(item.getItemStack(), Integer.toString(UtilMath.randomInt(10000)), new String[]{});
                item.setPickupDelay(Integer.MAX_VALUE);
                item.setVelocity(player.getLocation().getDirection().multiply(1.8));
                ThrowableManager.addThrowable(item, player, "Water bottle", 30000);
                items.put(player.getUniqueId(), item);
            } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                UtilItem.remove(player, Material.POTION, 1);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 80, 0));
                player.getWorld().playEffect(player.getEyeLocation(), Effect.STEP_SOUND, Material.WATER);
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0F, 1.0F);
            }

			/*
			new BukkitRunnable(){
				@Override
				public void run(){
					if(player.getInventory().getItemInMainHand() != null
							&& player.getInventory().getItemInMainHand().getType() == Material.POTION){
					if(player.getInventory().getItemInMainHand().getAmount() != 1){

						return;
					}


						player.getInventory().setItemInHand(new ItemStack(Material.AIR));
					}
				}
			}.runTaskLater(getInstance(), 3);
			 */
        }
    }

    @EventHandler
    public void onCollide(ThrowableHitGroundEvent e) {
        if (e.getThrowable().getSkillName().equals("Water bottle")) {
            for (Player p : UtilPlayer.getInRadius(e.getThrowable().getItem().getLocation(), 3.0)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 80, 0));
                p.setFireTicks(0);
                p.getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.WATER);
                p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0F, 1.0F);
            }
            e.getThrowable().getItem().getWorld().playEffect(e.getThrowable().getItem().getLocation(), Effect.STEP_SOUND, Material.WATER);
            e.getThrowable().getItem().getLocation().getWorld().playSound(e.getThrowable().getItem().getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0F, 1.0F);
            e.getThrowable().getItem().remove();
        }
    }

}
