package net.betterpvp.clans.skills.selector.skills.paladin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.InteractSkill;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.PestilenceData;
import net.betterpvp.clans.skills.selector.skills.data.PestilenceData.PestilenceDamageData;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilPlayer;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class Pestilence extends Skill implements InteractSkill {

    private List<PestilenceData> data;
    private List<UUID> active;

    public Pestilence(Clans i) {
        super(i, "Pestilence", "Paladin", getSwords, rightClick, 3, true, true);
        data = new ArrayList<>();
        active = new ArrayList<>();
    }

    @Override
    public void activate(Player p, Gamer gamer) {
        UtilMessage.message(p, getClassType(), "You prepared " + ChatColor.GREEN + getName(getLevel(p)));
        active.add(p.getUniqueId());
    }

    private class TempData {

        private Player damager;
        private Player p;
        private PestilenceData d;

        public TempData(PestilenceData d, Player p, Player damager) {
            this.d = d;
            this.p = p;
            this.damager = damager;
        }

        public Player getPlayer() {
            return p;
        }

        public Player getDamager() {
            return damager;
        }

        public PestilenceData getPestilence() {
            return d;
        }
    }

    @EventHandler
    public void spread(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            List<TempData> temp = new ArrayList<>();
            for (PestilenceData d : data) {
                Player damager = Bukkit.getPlayer(d.getDamager());
                if (damager != null) {
                    for (PestilenceDamageData pdd : d.getCurrentInfected()) {
                        Player infected = Bukkit.getPlayer(pdd.getDamagee());
                        if (infected != null) {
                            for (Player p : UtilPlayer.getInRadius(infected.getLocation(), 5)) {
                                if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE)
                                    continue;
                                if (ClanUtilities.canHurt(damager, p)) {
                                    if (d.getOldInfected().contains(p.getUniqueId())) {
                                        continue;
                                    }
                                    if (pdd.getDamagee() == p.getUniqueId()) {
                                        continue;
                                    }
                                    temp.add(new TempData(d, p, damager));
                                }
                            }
                        }
                    }
                }
            }
            for (TempData d : temp) {
                if (isInfected(d.getPlayer(), d.getDamager())) {
                    continue;
                }
                d.getPestilence().addInfected(d.getPlayer());
                LogManager.addLog(d.getPlayer(), d.getDamager(), "Pestilence");
            }
        }
    }

    public PestilenceData getPestilence(Player p) {
        for (PestilenceData d : data) {
            if (d.getDamager().equals(p.getUniqueId())) {
                return d;
            }
        }
        return null;
    }

    @EventHandler
    public void updatePestilence(UpdateEvent e) {
        if (e.getType() == UpdateType.FAST) {
            ListIterator<PestilenceData> i = data.listIterator();
            while (i.hasNext()) {
                PestilenceData next = i.next();
                ListIterator<PestilenceDamageData> it = next.getCurrentInfected().listIterator();
                while (it.hasNext()) {
                    PestilenceDamageData pdd = it.next();
                    if (UtilTime.elapsed(pdd.getStartTime(), pdd.getLength())) {
                        next.addOldInfected(pdd.getDamagee());
                        it.remove();
                    }
                }

                if (next.getCurrentInfected().isEmpty()) {
                    next.getOldInfected().clear();
                }
            }
        }
    }


    public boolean isInfected(Player p) {
        for (PestilenceData pd : data) {
            for (PestilenceDamageData pdd : pd.getCurrentInfected()) {
                if (pdd.getDamagee() == p.getUniqueId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInfected(Player p, Player damager) {
        for (PestilenceData pd : data) {
            if (pd.getDamager() == damager.getUniqueId()) {
                for (PestilenceDamageData pdd : pd.getCurrentInfected()) {
                    if (pdd.getDamagee() == p.getUniqueId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void displayPestilence(UpdateEvent e) {
        if (e.getType() == UpdateType.SEC) {
            for (PestilenceData pd : data) {
                for (PestilenceDamageData pdd : pd.getCurrentInfected()) {
                    final Player p = Bukkit.getPlayer(pdd.getDamagee());
                    if (p != null) {
                        for (int q = 0; q <= 10; q++) {
                            final float x = (float) (1 * Math.cos(q));
                            final float z = (float) (1 * Math.sin(q));

                            Bukkit.getScheduler().scheduleSyncDelayedTask(getInstance(), new Runnable() {

                                @Override
                                public void run() {

                                    ParticleEffect.VILLAGER_HAPPY.display(p.getLocation().add(x, 1, z));
                                }


                            }, q * 5L);

                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void damageReduction(CustomDamageEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK) {
            return;
        }
        if (e.getDamager() instanceof Player) {
            if (e.getDamagee() instanceof Player) {
                Player damager = (Player) e.getDamager();
                if (hasSkill(damager, this)) {
                    if (active.contains(damager.getUniqueId())) {

                        active.remove(damager.getUniqueId());
                        if (getPestilence(damager) == null) {
                            PestilenceData pd = new PestilenceData(damager.getUniqueId());
                            pd.addInfected((Player) e.getDamagee());
                            data.add(pd);
                        } else {
                            getPestilence(damager).addInfected((Player) e.getDamagee());
                        }
                    }
                }

            }
            Player player = (Player) e.getDamager();
            if (isInfected(player)) {
                e.setDamage(e.getDamage() * 0.80);
            }
        }
    }

    @Override
    public String[] getDescription(int level) {

        return new String[]{
                "Right click with Sword to Activate.",
                "",
                "Your next hit will apply Pestilence to the target.",
                "Pestilence poisons the target, and spreads to",
                "nearby enemies. While enemies are infectetd,",
                "they deal 20% reduced damage",
                "",
                "Cooldown: " + ChatColor.GREEN + getRecharge(level)
        };
    }

    @Override
    public Types getType() {

        return Types.SWORD;
    }

    @Override
    public double getRecharge(int level) {

        return 20 - ((level - 1) * 3);
    }

    @Override
    public float getEnergy(int level) {

        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        if (active.contains(p.getUniqueId())) {
            UtilMessage.message(p, getClassType(), ChatColor.GREEN + getName() + ChatColor.GRAY + " is already active.");
            return false;
        }

        return true;
    }

}
