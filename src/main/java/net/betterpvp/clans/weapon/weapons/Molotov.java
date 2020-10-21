package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Molotov extends Weapon {

    public static HashMap<Location, Long> areas = new HashMap<Location, Long>();
    public static List<Item> items = new ArrayList<Item>();

    public Molotov(Clans i) {
        super(i, Material.EXPERIENCE_BOTTLE, (byte) 0, ChatColor.YELLOW + "Molotov", new String[]{
                ChatColor.GRAY + "Left-Click: " + ChatColor.YELLOW + "Throw",
                ChatColor.GRAY + "  " + "Creates a dangerous fire zone for 5 seconds"}, false, 0);
    }

    @EventHandler
    public void onGrenadeUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.EXPERIENCE_BOTTLE) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

            event.setCancelled(true);
            return;

        }


        if (isThisWeapon(player)) {
            if (ClanUtilities.canCast(player)) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (RechargeManager.getInstance().add(player, "Molotov", 10, true)) {
                        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE));
                        ThrowableManager.addThrowable(item, player, "Molotov", 6000);
                        UtilItem.remove(player, Material.EXPERIENCE_BOTTLE, 1);
                        UtilItem.setItemNameAndLore(item.getItemStack(), Integer.toString(UtilMath.randomInt(10000)), new String[]{});
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity(player.getLocation().getDirection().multiply(1.8));

                        Gamer gamer = GamerManager.getOnlineGamer(player);
                        if(gamer != null){
                            gamer.setStatValue(ChatColor.stripColor(getName()), gamer.getStatValue(ChatColor.stripColor(getName())) + 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCollideGround(ThrowableHitGroundEvent e) {
        if (e.getThrowable().getSkillName().equalsIgnoreCase("Molotov")) {
            final Item throwable = e.getThrowable().getItem();

            for (int i = 0; i < 4; i++) {
                throwable.getWorld().playSound(throwable.getLocation(), Sound.BLOCK_GLASS_BREAK, 1f, 1f);
            }

            for (int i = 0; i < 60; i++) {
                new BukkitRunnable() {
                    @Override
                    public void run() {


                        Item item = throwable.getWorld().dropItem(throwable.getLocation(), new ItemStack(Material.BLAZE_POWDER));
                        item.getWorld().playSound(item.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.3F, 0.0F);
                        ThrowableManager.addThrowable(item, e.getThrowable().getThrower(), "Molotov Flames", (5000));
                        item.setVelocity(new Vector(UtilMath.randDouble(-0.6, 0.6), UtilMath.randDouble(0, 0.5), UtilMath.randDouble(-0.6, 0.6)));

                    }
                }.runTaskLater(getInstance(), 1 + i);
            }

            e.getThrowable().getItem().remove();
        }

    }

    @EventHandler
    public void onCollidePlayer(ThrowableCollideEntityEvent e) {
        if (e.getThrowable().getSkillName().equalsIgnoreCase("Molotov Flames")) {
            if (e.getCollision().getFireTicks() > 0) {
                return;
            }
            e.getCollision().setFireTicks(80);
            LogManager.addLog(e.getCollision(), e.getThrowable().getThrower(), "Molotov", 0);

        }
    }

}
