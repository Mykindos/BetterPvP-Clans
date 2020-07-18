package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.v1_16_R1.block.impl.CraftSweetBerryBush;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.NetherWarts;

public class FarmingRake extends Weapon implements ILegendary {

    public FarmingRake(Clans i) {
        super(i, Material.MUSIC_DISC_WARD, (byte) 0, ChatColor.RED + "Rake",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "0",
                        ChatColor.GRAY + "Active: " + ChatColor.YELLOW + "Harvest",
                        "",
                        ChatColor.GRAY + "This mysterious tool will ",
                        ChatColor.GRAY + "automatically harvest and replant any ",
                        ChatColor.GRAY + "crops in your vicinity.",
                        ""}, true, 9.5);
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getPlayer().getInventory().getItemInMainHand() == null) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.MUSIC_DISC_WARD) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isThisWeapon(e.getPlayer())) {
                skill(e.getPlayer());
            }


        }
    }


    @SuppressWarnings("deprecation")
    public void skill(Player p) {


        if (RechargeManager.getInstance().add(p, getName(), 1.0, true)) {
            if (Energy.use(p, getName(), 25.0, true)) {
                for (int x = -5; x < 5; x++) {
                    for (int z = -5; z < 5; z++) {
                        Location loc = new Location(p.getWorld(), p.getLocation().getX() + x, p.getLocation().getY() + 0.1, p.getLocation().getZ() + z);
                        if (loc.getBlock() != null || loc.getBlock().getType() != Material.AIR) {
                            Block b = loc.getBlock();
                            if (b.getType() == Material.POTATO) {
                                if (b.getData() == CropState.RIPE.getData()) {
                                    if (p.getInventory().firstEmpty() == -1) {
                                        p.getWorld().dropItem(b.getLocation(), new ItemStack(Material.POTATO, UtilMath.randomInt(1, 3)));
                                    } else {
                                        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.POTATO, UtilMath.randomInt(1, 3))));
                                    }
                                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 1.f, 1.f);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.POTATOES);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.POTATOES);
                                    b.setType(Material.POTATO);
                                    p.getInventory().removeItem(new ItemStack(Material.POTATO, 1));
                                }
                            } else if (b.getType() == Material.CARROTS) {
                                if (b.getData() == CropState.RIPE.getData()) {
                                    if (p.getInventory().firstEmpty() == -1) {
                                        p.getWorld().dropItem(b.getLocation(), new ItemStack(Material.CARROT, UtilMath.randomInt(1, 3)));
                                    } else {
                                        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.CARROT, UtilMath.randomInt(1, 3))));
                                    }
                                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 1.f, 1.f);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.CARROTS);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.CARROTS);
                                    b.setType(Material.CARROT);
                                    p.getInventory().removeItem(new ItemStack(Material.CARROT, 1));
                                }

                            } else if (b.getType() == Material.WHEAT) {
                                if (b.getData() == CropState.RIPE.getData()) {
                                    p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.WHEAT, 1)));
                                    p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.WHEAT_SEEDS, UtilMath.randomInt(1, 3))));
                                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 1.f, 1.f);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WHEAT);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.WHEAT);
                                    b.setType(Material.WHEAT);
                                    p.getInventory().removeItem(new ItemStack(Material.WHEAT_SEEDS, 1));
                                }
                            } else if (b.getType() == Material.NETHER_WART_BLOCK) {
                                NetherWarts n = (NetherWarts) b.getState().getData();
                                if (n.getState() == NetherWartsState.RIPE) {
                                    if (p.getInventory().firstEmpty() == -1) {
                                        p.getWorld().dropItem(b.getLocation(), new ItemStack(Material.NETHER_WART_BLOCK, UtilMath.randomInt(2, 4)));
                                    } else {
                                        p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.NETHER_WART_BLOCK, UtilMath.randomInt(2, 4))));
                                    }

                                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 1.f, 1.f);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 3);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 3);

                                    b.setType(Material.NETHER_WART_BLOCK);
                                    p.getInventory().removeItem(new ItemStack(Material.NETHER_WART_BLOCK, 1));
                                }
                            }else if(b.getType().name().contains("BEETROOT")){
                                Ageable age = (Ageable) b.getBlockData();

                                if (age.getAge() == age.getMaximumAge()) {

                                    p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.BEETROOT, 1)));
                                    p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.BEETROOT_SEEDS, UtilMath.randomInt(1, 2))));
                                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 0.7f, 1.f);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.BEETROOTS);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.BEETROOTS);

                                    b.setType(Material.BEETROOTS);
                                    p.getInventory().removeItem(new ItemStack(Material.BEETROOT_SEEDS, 1));
                                }
                            }else if(b.getType() == Material.SWEET_BERRY_BUSH){


                                Ageable age = (Ageable) b.getBlockData();

                                if (age.getAge() == age.getMaximumAge()) {
                                    p.getInventory().addItem(UtilClans.updateNames(new ItemStack(Material.SWEET_BERRIES, UtilMath.randomInt(2, 3))));
                                    p.getWorld().playSound(b.getLocation(), Sound.BLOCK_CROP_BREAK, 1.f, 1.f);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.SWEET_BERRY_BUSH);
                                    p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.SWEET_BERRY_BUSH);
                                    age.setAge(1);
                                    b.setBlockData(age);
                                    //b.setType(Material.SWEET_BERRY_BUSH);
                                    //p.getInventory().removeItem(new ItemStack(Material.BEETROOT_SEEDS, 1));
                                }
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
