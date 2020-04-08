package net.betterpvp.clans.skills.selector.skills.paladin;


import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilVelocity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Cyclone extends Skill implements InteractSkill {


    public Cyclone(Clans i) {
        super(i, "Cyclone", "Paladin", getSwords,
                rightClick, 5, true, true);

    }


    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to Activate.",
                "",
                "Pulls all enemies within",
                ChatColor.GREEN.toString() + (7 + level) + ChatColor.GRAY + " blocks towards you",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level),
                "Energy: " + ChatColor.GREEN + getEnergy(level)
        };
    }

    @Override
    public Types getType() {
        return Types.SWORD;
    }


    @Override
    public boolean usageCheck(Player player) {
        if (player.getLocation().getBlock().getType() == Material.WATER ) {
            UtilMessage.message(player, "Skill", "You cannot use " + ChatColor.GREEN + getName() + ChatColor.GRAY + " in water.");
            return false;
        }

        return true;
    }


    @Override
    public double getRecharge(int level) {

        return 12 - ((level - 1) * 1);
    }


    @Override
    public float getEnergy(int level) {

        return 40 - ((level - 1) * 5);
    }


    @Override
    public void activate(Player p, Gamer gamer) {
        Vector x = p.getLocation().toVector();
        x.setY(x.getY() + 2);

        int level = getLevel(p);
        UtilMessage.message(p, getName(), "You used " + ChatColor.GREEN + getName(level) + ChatColor.GRAY + ".");
        for (LivingEntity target : UtilPlayer.getAllInRadius(p.getLocation(), (7 + level))) {
            if (target instanceof ArmorStand) continue;
            if (!target.getName().equalsIgnoreCase(p.getName())) {
                if (target instanceof Player) {
                    if (!ClanUtilities.canHurt(p, (Player) target)) continue;
                    UtilMessage.message(target, "Cyclone", ChatColor.GREEN + p.getName() + ChatColor.GRAY + " pulled you in with " + ChatColor.GREEN + getName(level));

                }
                Vector v = target.getLocation().toVector().subtract(x).normalize().multiply(-1);
                LogManager.addLog(target, p, "Cyclone");
                UtilVelocity.velocity(target, v, 0.5D, false, 0.0D, 0.7D, 7.0D, true);


            }
        }
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1F, 0.6F);

    }
}
