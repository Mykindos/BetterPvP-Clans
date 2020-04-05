package net.betterpvp.clans.skills.selector.skills.ranger;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.events.SkillDequipEvent;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PinDown extends Skill {

    public PinDown(Clans i) {
        super(i, "Pin Down", "Ranger", getBow, leftClick, 5, true, true);

    }

    private List<Arrow> active = new ArrayList<>();

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Left click with bow to instantly fire",
                "an arrow, which gives anybody hit ",
                "Slowness IV for " + ChatColor.GREEN + (level * 1.5) + ChatColor.GRAY + " seconds.",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @EventHandler
    public void onDequip(SkillDequipEvent e) {
        if (e.getSkill() == this) {
            if (active.contains(e.getPlayer().getUniqueId())) {
                active.remove(e.getPlayer().getUniqueId());
            }
        }
    }

    @Override
    public Types getType() {

        return Types.BOW;
    }

    @Override
    public double getRecharge(int level) {

        return 13 - ((level - 1) * 1.5);
    }

    @Override
    public float getEnergy(int level) {

        return 35 - ((level - 1));
    }

    @Override
    public void activateSkill(Player p) {
        UtilItem.remove(p, Material.ARROW, 1);


        Arrow proj = p.launchProjectile(Arrow.class);
        active.add(proj);


        proj.setVelocity(p.getLocation().getDirection().multiply(1.6D));


        UtilMessage.message(p, getClassType(), "You used " + ChatColor.GREEN + getName(getLevel(p)));


        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);
        p.getWorld().playEffect(p.getLocation(), Effect.BOW_FIRE, 0);

    }

    @EventHandler
    public void onDamage(CustomDamageEvent e) {
        if (e.getProjectile() != null) {
            if (e.getProjectile() instanceof Arrow) {
                Arrow a = (Arrow) e.getProjectile();
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();
                    if (hasSkill(p, this)) {
                        if (active.contains(a)) {
                            active.remove(a);


                            LivingEntity ent = e.getDamagee();


                            LogManager.addLog(e.getDamagee(), p, "Pin Down");


                            ent.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) ((getLevel(p) * 1.5) * 20), 3));
                        }

                    }
                }
            }
        }
    }


    @Override
    public boolean usageCheck(Player p) {
        if (p.getLocation().getBlock().isLiquid()) {
            UtilMessage.message(p, getClassType(), "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        if (!p.getInventory().contains(Material.ARROW, 1)) {
            UtilMessage.message(p, getClassType(), "You need atleast 1 Arrow to use " + ChatColor.GREEN + getName());
            return false;
        }
        return true;
    }


}
