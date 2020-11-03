package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlacialAxe extends Weapon implements ILegendary {

    public GlacialAxe(Clans i) {
        super(i, Material.MUSIC_DISC_FAR, (byte) 0, ChatColor.RED + "Glacial Axe",
                new String[]{"", ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "7",
                        ChatColor.GRAY + "Passive: " + ChatColor.YELLOW + "Water Walking",
                        "",
                        ChatColor.GRAY + "When walking on water, a radius below",
                        ChatColor.GRAY + "you is temporarily transformed into ice", ""}, true, 7.5);

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
            if (e.getPlayer().getInventory().getItemInMainHand() != null) {
                if (isThisWeapon(e.getPlayer())) {
                    for (Block b : UtilBlock.getInRadius(e.getPlayer().getLocation(), 5, 2).keySet()) {

                        Clan clan = ClanUtilities.getClan(b.getLocation());
                        if (clan != null && !(clan instanceof AdminClan)) {
                            continue;
                        }

                        if (b.getType() == Material.WATER) {
                            if (e.getPlayer().getLocation().getBlock().isLiquid()) continue;
                            if (b.getRelative(BlockFace.UP).getType() != Material.AIR) continue;

                            new BlockRestoreData(b, b.getType(), (byte) 0, 2000L);
                            b.setType(Material.ICE);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamage(CustomDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if (event.getDamager() instanceof Player) {

                Player player = (Player) event.getDamager();
                if (player.getInventory().getItemInMainHand() == null) return;
                if (player.getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_FAR) return;
                if (isThisWeapon(player)) {

                    event.setDamage(7);
                    event.getDamagee().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1));

                }
            }
        }


    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FAST) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getInventory().getItemInMainHand() != null) {
                    if (p.getInventory().getItemInMainHand().getType() == Material.MUSIC_DISC_FAR) {
                        if (isThisWeapon(p)) {
                            Location loc = p.getLocation().add(0, -1, 0);

                            if (loc.getBlock().getType() == Material.WATER
                                    || loc.getBlock().getType() == Material.ICE) {
                                new BlockRestoreData(loc.getBlock(), loc.getBlock().getType(), (byte) 0, 1200L);
                                loc.getBlock().setType(Material.ICE);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isTextured() {
        return true;
    }
}


