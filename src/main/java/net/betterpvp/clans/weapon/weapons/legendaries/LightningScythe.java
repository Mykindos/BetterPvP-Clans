package net.betterpvp.clans.weapon.weapons.legendaries;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.weapons.LightningScytheData;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LightningScythe extends Weapon {

    public LightningScythe(Clans i) {
        super(i, Material.DIAMOND_HOE, (byte) 0, ChatColor.RED + "Lightning Scythe",
                new String[]{"",
                        ChatColor.GRAY + "Damage: " + ChatColor.YELLOW + "10 (AoE)",
                        ChatColor.GRAY + "Active: " + ChatColor.YELLOW + "Lightning Strike",
                        "",
                        ChatColor.RESET + "This mysterious weapon is believed",
                        ChatColor.RESET + "to call the power of the gods",
                        ChatColor.RESET + "and strike lightning on your enemies.",
                        ""}, true, 2.0);
    }

    private List<LightningScytheData> chargeData = new ArrayList<>();


    public LightningScytheData getData(Player p) {
        for (LightningScytheData lsd : chargeData) {
            if (lsd.getPlayer().equals(p)) {
                return lsd;
            }
        }
        return null;
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (e.getPlayer().getInventory().getItemInMainHand() == null) return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.DIAMOND_HOE) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isThisWeapon(e.getPlayer())) {
                skill(e.getPlayer());
            }


        }
    }


    public void skill(Player p) {


        if (RechargeManager.getInstance().add(p, getName(), 7.5, true)) {
            if (Energy.use(p, getName(), 40.0, true)) {
                Block b = p.getTargetBlock((Set<Material>) null, 50);
                if (b.getType() != null) {


                    b.getLocation().getWorld().spigot().strikeLightning(b.getLocation(), true);
                    b.getLocation().getWorld().spigot().strikeLightning(b.getLocation(), true);
                    for (LivingEntity z : UtilPlayer.getAllInRadius(b.getLocation(), 3)) {
                        LogManager.addLog(z, p, "Lightning Strike");
                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(z, p, null, DamageCause.LIGHTNING, 15, false));

                    }
                    p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
                    for (Player d : UtilPlayer.getInRadius(b.getLocation(), 100)) {
                        d.getWorld().playSound(d.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
                    }


                    UtilMessage.message(p, "Lightning Scythe", "You used " + ChatColor.GREEN + "Lightning Strike");
                    p.getInventory().getItemInMainHand().setDurability(
                            (short) (p.getInventory().getItemInMainHand().getDurability() + 5));
                    if (p.getInventory().getItemInMainHand().getDurability() >= 1561) {
                        p.getInventory().setItemInHand(new ItemStack(Material.AIR));
                    }


                }

            }
        }

    }




	/*
	@EventHandler
	public void updateCharge(UpdateEvent e){
		if(e.getType() == UpdateType.TICK){
			Iterator<LightningScytheData> it = chargeData.iterator();
			while(it.hasNext()){
				LightningScytheData next = it.next();
				if(next.getPlayer() == null){
					it.remove();
					return;
				}
				if(next.getPlayer().isHandRaised()){
					if(UtilTime.elapsed(next.getLastCharge(), 1000)){
						if(next.getCharge() < 100){

							next.setCharge(next.getCharge() + 25);
							next.setLastCharge(System.currentTimeMillis());
							UtilMessage.message(next.getPlayer(), "Lightning Scythe", "Charge: " + ChatColor.YELLOW + next.getCharge() + "%");
						}
					}
				}else{
					if(!UtilTime.elapsed(next.getStartTime(), 250)) continue;
					Block b = next.getPlayer().getTargetBlock((Set<Material>) null, 50);
					if (b.getType() != null){


						b.getLocation().getWorld().spigot().strikeLightning(b.getLocation(), true);
						b.getLocation().getWorld().spigot().strikeLightning(b.getLocation(), true);
						for(LivingEntity z : UtilPlayer.getAllInRadius(b.getLocation(), 3)){
							LogManager.addLog(z, next.getPlayer(), "Lightning Strike");
							Bukkit.getPluginManager().callEvent(new CustomDamageEvent(z,next.getPlayer(), null, DamageCause.LIGHTNING, 11 * (next.getCharge() * 0.01) + 4 , false));

						}
						next.getPlayer().playSound(next.getPlayer().getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
						for(Player d : UtilPlayer.getInRadius(b.getLocation(), 100)){
							d.getWorld().playSound(d.getLocation(), Sound.AMBIENCE_THUNDER, 1.0F, 1.0F);
						}






						UtilMessage.message(next.getPlayer(), "Lightning Scythe", "You used " + ChatColor.GREEN + "Lightning Strike (" + next.getCharge() + "%)" );


					}
					it.remove();
				}
			}
		}
	}
	 */

	/*
	public static HashMap<UUID, UUID> lastDamage = new HashMap<>();
	@EventHandler
	public void damage(EntityDamageByEntityEvent e){
		if(e.getEntity() instanceof Player){
			if(e.getDamager() instanceof LightningStrike){
				LightningStrike l =  (LightningStrike) e.getDamager();
				Player p = Bukkit.getPlayer(lastDamage.get(l.getUniqueId()));
				if(p != null){
					Player d = (Player) e.getEntity();
					if(!ClanUtilities.canHurt(d, p)){
						e.setCancelled(true);
						return;
					}

					lastDamage.remove(l.getUniqueId());
					e.setDamage(8);
					LogManager.addLog(d, p, "Lightning Strike");




				}
			}
		}
	}
	(
	 */

}
