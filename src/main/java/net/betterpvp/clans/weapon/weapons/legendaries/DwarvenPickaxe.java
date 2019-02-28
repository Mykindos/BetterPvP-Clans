package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DwarvenPickaxe extends Weapon {


    public DwarvenPickaxe(Clans i) {
        super(i, Material.DIAMOND_PICKAXE, (byte) 0, ChatColor.RED + "Dwarven Pickaxe",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "0",
                        ChatColor.GRAY + "Active: " + ChatColor.YELLOW + "None",
                        "",
                        ChatColor.RESET + "This pickaxe will instantly ",
                        ChatColor.RESET + "break any stone related blocks. ",
                        ""}
                , true, 8.0);
        // TODO Auto-generated constructor stub
    }

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event) {
        Player player = event.getPlayer();

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
                        block.getType().toString().contains("ORE") || block.getType().toString().contains("BRICK") || block.getType() == Material.COBBLESTONE_STAIRS
                        || block.getType() == Material.SMOOTH_STAIRS
                        || block.getType().name().contains("STONE_SLAB")
                        || block.getType() == Material.IRON_FENCE
                        || block.getType() == Material.COAL_BLOCK
                        || block.getType() == Material.STONE_SLAB2) {


                    player.getItemInHand().setDurability((short) 0);


                    Clans.getCoreProtect().logRemoval("Dwarven Pickaxe - " + player.getName(), block.getLocation(), block.getType(), block.getData());


                    block.breakNaturally();
                }
            }
        }

    }

}
