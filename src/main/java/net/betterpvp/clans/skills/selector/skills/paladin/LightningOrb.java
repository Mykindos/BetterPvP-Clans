package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.events.ThrowableCollideEntityEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class LightningOrb extends Skill {

    public LightningOrb(Clans i) {
        super(i, "Lightning Orb", "Paladin", getAxes, rightClick, 5, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Launch a lightning orb. Directly hitting a player",
                "will strike all enemies within " + ChatColor.GREEN + (3 + (level * 0.5)) + ChatColor.GRAY + " blocks",
                "with lightning, giving them Slowness II for 4 seconds.",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {

        return Types.AXE;
    }

    @Override
    public double getRecharge(int level) {

        return 25 - ((level * 2));
    }

    @Override
    public float getEnergy(int level) {

        return 30 - (level * 2);
    }

    @Override
    public void activateSkill(Player p) {
        Item orb = p.getWorld().dropItem(p.getEyeLocation().add(p.getLocation().getDirection()), new ItemStack(Material.DIAMOND_BLOCK));
        orb.setVelocity(p.getLocation().getDirection());
        ThrowableManager.addThrowable(orb, p, "Lightning Orb", 5000);

    }

    //private WeakHashMap<Player, Long> lightningCooldown = new WeakHashMap<>();


    @EventHandler
    public void onCollide(ThrowableCollideEntityEvent e) {
        if (e.getThrowable().getSkillName().equals(getName())) {
            if (e.getThrowable().getThrower() instanceof Player) {


                Player thrower = (Player) e.getThrowable().getThrower();
                if (thrower.equals(e.getCollision())) {
                    return;
                }
                e.getThrowable().getItem().remove();


                int level = getLevel(thrower);
                int count = 0;
                for (LivingEntity ent : UtilPlayer.getAllInRadius(e.getThrowable().getItem().getLocation(), 3 + (0.5 * level))) {
                    //if(lightningCooldown.containsKey(ent)) continue;
                    if (count >= 3) continue;
                    if (e.getThrowable().getImmunes().contains(ent)) continue;
                    e.getThrowable().getImmunes().add(ent);
                    if (ent instanceof Player) {
                        Player p = (Player) ent;
                        if (ClanUtilities.canHurt(thrower, p)) {

                            EffectManager.addEffect(p, EffectType.SHOCK, 2000);

                            //	lightningCooldown.put(ent, System.currentTimeMillis());
                        } else {
                            continue;
                        }

                    }

                    LogManager.addLog(ent, thrower, "Lightning Orb");
                    ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 1));

                    thrower.getLocation().getWorld().spigot().strikeLightning(ent.getLocation(), true);
                    Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ent, e.getThrowable().getThrower(), null, DamageCause.CUSTOM, 11, false));


                    ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
                    count++;
                }

            }

            //ThrowableManager.getThrowables().remove(e.getThrowable());
        }
    }

	/*
	@EventHandler
	public void update(UpdateEvent e){
		if(e.getType() == UpdateType.FASTEST){
			Iterator<Entry<Player, Long>> it = lightningCooldown.entrySet().iterator();
			while(it.hasNext()){
				Entry<Player, Long> next = it.next();
				if(UtilTime.elapsed(next.getValue(), 1000)){
					it.remove();
				}
			}
		}
	}
	 */

    @Override
    public boolean usageCheck(Player p) {
        if (p.getLocation().getBlock().isLiquid()) {
            UtilMessage.message(p, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water");
            return false;
        }
        return true;
    }

}
