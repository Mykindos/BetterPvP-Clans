package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.events.RoleChangeEvent;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.SeverData;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Sever extends Skill implements InteractSkill {

    private Set<UUID> active = new HashSet<>();

    public Sever(Clans i) {
        super(i, "Sever", "Assassin", getSwords,
                rightClick,
                3, true, true);

    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to activate",
                "",
                "Your next hit applies a " + ChatColor.GREEN + (level) + ChatColor.GRAY + " second bleed",
                "dealing 1 heart per second",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }


    @EventHandler
    public void onChange(RoleChangeEvent e) {
        active.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (!(e.getDamager() instanceof Player)) return;
        if (!(e.getDamagee() instanceof Player)) return;

        if (e.getCause() == DamageCause.ENTITY_ATTACK) {
            Player ent = (Player) e.getDamagee();
            Player p = (Player) e.getDamager();
            if (ClanUtilities.canHurt(ent, p)) {
                if (usageCheck(p)) {
                    if (Arrays.asList(getMaterials()).contains(p.getInventory().getItemInMainHand().getType())) {
                        if (active.contains(p.getUniqueId())) {
                            Weapon w = WeaponManager.getWeapon(p.getInventory().getItemInMainHand());
                            if (w != null && w instanceof ILegendary) return;


                            int level = getLevel(p);
                            new SeverData(getInstance(), ent, p, (level));
                            UtilMessage.message(p, getClassType(), "You severed " + ChatColor.GREEN + ent.getName() + ChatColor.GRAY + ".");
                            UtilMessage.message(ent, getClassType(), "You have been severed by " + ChatColor.GREEN + p.getName() + ChatColor.GRAY + ".");
                            active.remove(p.getUniqueId());

                            RechargeManager.getInstance().removeCooldown(p.getName(), getName(), true);
                            if (RechargeManager.getInstance().add(p, getName(), getRecharge(level), showRecharge())) {

                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        return hasSkill(player, this);
    }

    @Override
    public double getRecharge(int level) {

        return 20;
    }

    @Override
    public float getEnergy(int level) {

        return 20;
    }

    @Override
    public void activate(Player player, Gamer gamer) {
        active.add(player.getUniqueId());
        UtilMessage.message(player, getClassType(), "You prepared " + ChatColor.GREEN + getName(getLevel(player)));
    }
}
