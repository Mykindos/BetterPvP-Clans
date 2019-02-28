package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HolyLight extends Skill {

    public HolyLight(Clans i) {
        super(i, "Holy Light", "Paladin", noMaterials, noActions, 5, true, true);
    }

    @Override
    public String[] getDescription(int level) {
        // TODO Auto-generated method stub
        return new String[]{
                "Create an aura that gives", "yourself and all allies within",
                ChatColor.GREEN.toString() + (8 + level) + ChatColor.GRAY + " blocks extra regeneration"};
    }

    @Override
    public void activateSkill(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 140, 0));
        int level = getLevel(p);
        for (Player cur : UtilPlayer.getNearby(p.getLocation(), (8 + level))) {
            if (!ClanUtilities.canHurt(p, cur)) {
                cur.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40, 0));
            }
        }
    }

    @Override
    public boolean usageCheck(Player player) {
        return true;
    }

    @EventHandler
    public void updateHolyLight(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.FAST) {
            for (Player player : Bukkit.getOnlinePlayers()) {


                if (hasSkill(player, this)) {
                    activateSkill(player);
                }


            }
        }
    }


    @Override
    public Types getType() {
        // TODO Auto-generated method stub
        return Types.PASSIVE_A;
    }

    @Override
    public double getRecharge(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean requiresShield() {
        // TODO Auto-generated method stub
        return false;
    }

}
