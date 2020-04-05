package net.betterpvp.clans.skills.selector.skills.knight;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Arrays;

public class HiltSmash extends Skill {

    public HiltSmash(Clans i) {
        super(i, "Hilt Smash", "Knight", getSwords, noActions
                , 5, true, false);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to Activate.",
                "",
                "Smash the hilt of your sword into",
                "your opponent, dealing " + ChatColor.GREEN + (3 + (level)) + ChatColor.GRAY + " damage",
                "and applying shock for " + ChatColor.GREEN + (((level - 1) * 0.5)) + ChatColor.GRAY + " seconds.",
                "Silences enemy for " + ChatColor.GREEN + ((level / 2) - 0.5) + ChatColor.GRAY + " seconds",
                "",
                "Recharge: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player p = e.getPlayer();

        if (hasSkill(p, this)) {
            if (Arrays.asList(getMaterials()).contains(p.getInventory().getItemInMainHand().getType())) {
                Weapon weapon = WeaponManager.getWeapon(p.getInventory().getItemInMainHand());
                if (weapon == null || weapon instanceof EnchantedWeapon) {

                    if (ClanUtilities.canCast(p)) {

                        if (EffectManager.hasEffect(p, EffectType.SILENCE)) {
                            UtilMessage.message(p, getName(), "You cannot use " + getName() + " while silenced!");
                            return;
                        }
                        int level = getLevel(p);

                        if (RechargeManager.getInstance().add(p, getName(), getRecharge(level), true)) {
                            if (Energy.use(p, getName(), getEnergy(level), true)) {
                                if (e.getRightClicked() instanceof LivingEntity) {
                                    LivingEntity ent = (LivingEntity) e.getRightClicked();
                                    if (UtilMath.offset(p, ent) <= 3.0) {


                                        if (ent instanceof Player) {
                                            Player damagee = (Player) ent;
                                            if (!ClanUtilities.canHurt(p, (Player) ent)) {
                                                UtilMessage.message(p, getClassType(), "You failed " + getName());
                                                return;
                                            }

                                            UtilMessage.message(damagee, getClassType(), ChatColor.YELLOW + p.getName() + ChatColor.GRAY + " hit you with " + ChatColor.GREEN + getName(getLevel(p)));
                                            EffectManager.addEffect(damagee, EffectType.SHOCK, (getLevel(p) * 1000) / 2);
                                            EffectManager.addEffect(damagee, EffectType.SILENCE, (long) (((getLevel(p) * 1000) / 2) - 0.5));
                                        }

                                        UtilMessage.message(p, getClassType(), "You hit " + ent.getName() + " with " + ChatColor.GREEN + getName(getLevel(p)));
                                        LogManager.addLog(ent, p, getName());
                                        Bukkit.getPluginManager().callEvent(new CustomDamageEvent(ent, p, null, DamageCause.ENTITY_ATTACK, 3 + getLevel(p), false));

                                        ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 1.0F, 1.2F);
                                        //ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 , 4));


                                    } else {
                                        UtilMessage.message(p, getClassType(), "You failed " + getName());
                                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1F, 0.1F);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public void activateSkill(Player player) {


    }

    @Override
    public boolean usageCheck(Player player) {

        return false;
    }

    @Override
    public double getRecharge(int level) {

        return 10;
    }

    @Override
    public float getEnergy(int level) {

        return 25;
    }

}
