package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.Throwables;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Web extends Weapon {

    public static List<Item> items = new ArrayList<Item>();

    public Web(Clans i) {
        super(i, Material.COBWEB, (byte) 0, ChatColor.YELLOW + "Throwing Web", new String[]{
                ChatColor.GRAY + "Left-Click: " + ChatColor.YELLOW + "Throw",
                ChatColor.GRAY + "  " + "Creates a Web trap"}, false, 0);
    }

    @EventHandler
    public void onWebUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.COBWEB) return;

        if (isThisWeapon(player)) {
            if (ClanUtilities.canCast(player)) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (RechargeManager.getInstance().add(player, "this throwable", 10, true)) {
                        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.COBWEB));
                        Throwables thro = new Throwables(item, player, "Throwing Web", 10000);
                        thro.getImmunes().add(player);
                        ThrowableManager.getThrowables().add(thro);
                        UtilItem.remove(player, Material.COBWEB, 1);
                        UtilItem.setItemNameAndLore(item.getItemStack(), Integer.toString(UtilMath.randomInt(10000)), new String[]{});
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity(player.getLocation().getDirection().multiply(1.8));
                        items.add(item);

                        Gamer gamer = GamerManager.getOnlineGamer(player);
                        if(gamer != null){
                            gamer.setStatValue(net.md_5.bungee.api.ChatColor.stripColor(getName()), gamer.getStatValue(net.md_5.bungee.api.ChatColor.stripColor(getName())) + 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onGroundCollide(ThrowableHitGroundEvent e) {
        if (e.getThrowable().getSkillName().equalsIgnoreCase("Throwing Web")) {
            for (int i = 0; i < 8; i++) {
                ParticleEffect.ITEM_CRACK.display( e.getThrowable().getItem().getLocation());

            }

            if (UtilBlock.isGrounded(e.getThrowable().getItem())) {
                for (Block block : UtilBlock.getInRadius(e.getThrowable().getItem().getLocation().getBlock(), 1).keySet()) {
                    if (UtilBlock.airFoliage(block)) {
                        if (!block.getType().name().contains("GATE") && !block.getType().name().contains("DOOR")) {
                            new BlockRestoreData(block, Material.COBWEB, (byte) 0, 3000L);

                        }
                    }
                }
            }

            e.getThrowable().getItem().remove();
        }

    }

    @EventHandler
    public void onCollideEntity(ThrowableCollideEntityEvent e) {
        if (e.getThrowable().getSkillName().equalsIgnoreCase("Throwing Web")) {
            for (int i = 0; i < 8; i++) {
                ParticleEffect.ITEM_CRACK.display( e.getThrowable().getItem().getLocation());
            }


            for (Block block : UtilBlock.getInRadius(e.getCollision().getLocation().getBlock(), 1).keySet()) {
                if (UtilBlock.airFoliage(block)) {
                    if (!block.getType().name().contains("GATE") && !block.getType().name().contains("DOOR")) {
                        new BlockRestoreData(block, Material.COBWEB, (byte) 0, 2500L);
                    }
                }
            }

            e.getThrowable().getItem().remove();
        }
    }

	/*
	@EventHandler
	public void updateWebs(UpdateEvent event) {
		if (event.getType() == UpdateEvent.UpdateType.TICK) {
			Iterator<Item> iterator = items.iterator();
			while (iterator.hasNext()) {
				Item item = iterator.next();
				Random random = new Random();

				for (int i = 0; i < 8; i++) {
					item.getLocation().getWorld().playEffect(item.getLocation().add(0, random.nextGaussian(), 0), Effect.TILE_BREAK, 30);
				}

				if (UtilBlock.getBlockUnder(item.getLocation()).getType() != Material.AIR) {
					for (Block block : UtilBlock.getInRadius(item.getLocation().getBlock(), 1).keySet()) {
						if (UtilBlock.airFoliage(block)) {
							new BlockRestoreData(block, 30, (byte) 0, 3000L);
						}
					}

					item.remove();
					iterator.remove();
				}
			}
		}
	}
	 */
}
