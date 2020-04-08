package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.ToggleSkill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;


public class SmokeBomb extends Skill implements ToggleSkill {

    public SmokeBomb(Clans i) {
        super(i, "Smoke Bomb", "Assassin", getSwordsAndAxes,
                noActions, 5, true, false);
    }


    //private WeakHashMap<Player, Long> timer = new WeakHashMap<>();
    private WeakHashMap<Player, Integer> smoked = new WeakHashMap<>();

    @EventHandler
    public void preventSmokeDamage(CustomDamageEvent e) {
        if (e.getDamagee() instanceof Player) {
            Player p = (Player) e.getDamagee();
            if (EffectManager.hasEffect(p, EffectType.INVISIBILITY)) {
                if (hasSkill(p, this)) {
                    e.setCancelled("Can't take damage during smoke");
                }
            }
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            Iterator<Entry<Player, Integer>> it = smoked.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Player, Integer> next = it.next();

                Role r = Role.getRole(next.getKey());
                if (r != null && r instanceof Assassin) {
                    if (next.getValue() > 0) {
                        for (int i = 0; i < 5; i++) {
                            ParticleEffect.SMOKE_LARGE.display(next.getKey().getLocation());
                            //next.getKey().getWorld().playEffect(next.getKey().getLocation(), Effect.SMOKE, 4);
                        }
                        next.setValue(next.getValue() - 1);
                    } else {
                        EffectManager.removeEffect(next.getKey(), EffectType.INVISIBILITY);
                        for (Player p : Bukkit.getOnlinePlayers()) {

                            p.showPlayer(next.getKey());


                        }
                        UtilMessage.message(next.getKey(), getClassType(), "You have reappeared.");
                        it.remove();
                    }
                } else {
                    EffectManager.removeEffect(next.getKey(), EffectType.INVISIBILITY);
                    for (Player p : Bukkit.getOnlinePlayers()) {

                        p.showPlayer(next.getKey());
                    }
                    UtilMessage.message(next.getKey(), getClassType(), "You have reappeared.");
                    it.remove();
                }
            }
        }
    }


    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (smoked.containsKey(player)) {
                if (e.getReason() != null) {
                    if (e.getReason().equalsIgnoreCase("Sever")) {
                        return;
                    }
                }

                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.canSee(player)) {
                        p.showPlayer(player);

                    }
                }
                smoked.remove(player);
                EffectManager.removeEffect(player, EffectType.INVISIBILITY);
                UtilMessage.message(player, getClassType(), "You have reappeared.");
            }

        }
    }


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (smoked.containsKey(e.getPlayer())) {
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                smoked.remove(e.getPlayer());
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!p.canSee(e.getPlayer())) {
                        p.showPlayer(e.getPlayer());

                    }
                }
                EffectManager.removeEffect(e.getPlayer(), EffectType.INVISIBILITY);
                UtilMessage.message(e.getPlayer(), getClassType(), "You have reappeared.");
            }
        }
    }




	/*
	private boolean isVisible(Player player){
		for(Player p: Bukkit.getOnlinePlayers()){
			if(!p.canSee(player)){
				return false;
			}
		}
		return true;
	}
	 */

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Instantly vanish before your foes for a",
                "maximum of " + ChatColor.GREEN + (3 + level) + ChatColor.GRAY + " seconds",
                "hitting an enemy or using abilities",
                " will make you reappear",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)};
    }

    @Override
    public boolean usageCheck(Player player) {

        if (!hasSkill(player, this)) {
            return false;
        }

        if (EffectManager.hasEffect(player, EffectType.SILENCE)) {
            UtilMessage.message(player, getClassType(), "You cannot use " + ChatColor.GREEN + getName(getLevel(player)) + ChatColor.GRAY + " while silenced!");
            return false;
        }

        Clan clan = ClanUtilities.getClan(player.getLocation());
        if (clan != null) {
            if (clan instanceof AdminClan) {
                AdminClan adminClan = (AdminClan) clan;

                if (adminClan.isSafe()) {
                    UtilMessage.message(player, getClassType(),
                            "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in Safe Zones.");
                    return false;
                }
            }
        }

        if ( player.getLocation().getBlock().getType() == Material.WATER) {
            UtilMessage.message(player, getClassType(),
                    "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        return true;
    }


    @Override
    public Types getType() {

        return Types.PASSIVE_B;
    }

    @Override
    public double getRecharge(int level) {

        return 45 - ((level - 1) * 2.5);
    }

    @Override
    public float getEnergy(int level) {

        return 85 - ((level - 1) * 5);
    }


    @Override
    public void activateToggle(Player p, Gamer gamer) {
        if (RechargeManager.getInstance().add(p, getName(), getRecharge(getLevel(p)), showRecharge())) {

            p.playSound(p.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 2.0f, 1.f);
            EffectManager.addEffect(p, EffectType.INVISIBILITY, (3 + getLevel(p)) * 1000);
            smoked.put(p, (3 + getLevel(p)) * 2);
            //timer.put(p, System.currentTimeMillis());
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.hidePlayer(p);
            }
            UtilMessage.message(p, getName(), "You used " + ChatColor.GREEN + getName(getLevel(p)));


            for (int i = 0; i < 3; i++) {

                p.getWorld().playSound(p.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 2.0F, 0.5F);
            }

            ParticleEffect.EXPLOSION_HUGE.display(p.getLocation());

            for (Player d : UtilPlayer.getInRadius(p.getLocation(), 2.5)) {
                if (d == p) continue;
                if (ClanUtilities.canHurt(p, d)) {
                    d.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 35, 1));
                }
            }
        }
    }
}
