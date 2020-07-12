package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class DwarvenPickaxe extends Weapon implements ILegendary {


    public DwarvenPickaxe(Clans i) {
        super(i, Material.FIREWORK_STAR, (byte) 0, ChatColor.RED + "Dwarven Pickaxe",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "0",
                        ChatColor.GRAY + "Active: " + ChatColor.YELLOW + "None",
                        "",
                        ChatColor.GRAY + "This pickaxe will instantly ",
                        ChatColor.GRAY + "break any stone related blocks in a 3x3 radius. ",
                        ""}
                , true, 7.0);

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

            if (player.getInventory().getItemInMainHand().getType() != Material.FIREWORK_STAR) {
                return;
            }

            if (isThisWeapon(player)) {

                Clan pClan = ClanUtilities.getClan(player);
                Clan blockClan = ClanUtilities.getClan(block.getChunk());
                if (blockClan != null) {
                    if (!blockClan.equals(pClan)) {
                        return;
                    }
                }



                List<Block> blocks = UtilBlock.getSurrounding(block, true);
                blocks.add(block);
                for (Block b : blocks) {
                    if (b.getType().toString().contains("STONE") && b.getType() != Material.GLOWSTONE ||
                            b.getType().toString().contains("_ORE") || b.getType().toString().contains("BRICK") || b.getType() == Material.COBBLESTONE_STAIRS
                            || b.getType() == Material.STONE_BRICK_STAIRS
                            || b.getType().name().contains("STONE_SLAB")
                            || b.getType() == Material.IRON_BARS
                            || b.getType() == Material.COAL_BLOCK
                            || b.getType().name().contains("_SLAB")
                            || b.getType() == Material.ANDESITE
                            || b.getType() == Material.GRANITE
                            || b.getType() == Material.DIORITE) {


                        Clan bClan = ClanUtilities.getClan(block.getChunk());
                        if (bClan != null) {
                            if (!bClan.equals(pClan)) {
                                return;
                            }
                        }
                        player.getInventory().getItemInMainHand().setDurability((short) 0);


                        Clans.getCoreProtect().logRemoval("Dwarven Pickaxe - " + player.getName(), b.getLocation(), b.getType(), b.getData());


                        b.breakNaturally();
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
