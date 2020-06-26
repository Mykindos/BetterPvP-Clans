package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class DwarvenPickaxe extends Weapon implements ILegendary {


    public DwarvenPickaxe(Clans i) {
        super(i, Material.DIAMOND_PICKAXE, (byte) 0, ChatColor.RED + "Dwarven Pickaxe",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "0",
                        ChatColor.GRAY + "Active: " + ChatColor.YELLOW + "None",
                        "",
                        ChatColor.GRAY + "This pickaxe will instantly ",
                        ChatColor.GRAY + "break any stone related blocks. ",
                        ""}
                , true, 8.0);

    }

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            if (event.isCancelled()) {
                return;
            }

            if (isThisWeapon(player)) {
                if (ClanUtilities.getClan(block.getChunk()) != null) {
                    if (ClanUtilities.getClan(block.getChunk()) != ClanUtilities.getClan(player)) {
                        return;
                    }
                }


                if (block.getType().toString().contains("STONE") && block.getType() != Material.GLOWSTONE ||
                        block.getType().toString().contains("_ORE") || block.getType().toString().contains("BRICK") || block.getType() == Material.COBBLESTONE_STAIRS
                        || block.getType() == Material.STONE_BRICK_STAIRS
                        || block.getType().name().contains("STONE_SLAB")
                        || block.getType() == Material.IRON_BARS
                        || block.getType() == Material.COAL_BLOCK
                        || block.getType().name().contains("_SLAB")
                        || block.getType() == Material.ANDESITE
                        || block.getType() == Material.GRANITE) {


                    player.getInventory().getItemInMainHand().setDurability((short) 0);


                    Clans.getCoreProtect().logRemoval("Dwarven Pickaxe - " + player.getName(), block.getLocation(), block.getType(), block.getData());


                    block.breakNaturally();
                }
            }
        }

    }

    @Override
    public boolean isTextured() {
        return false;
    }
}
