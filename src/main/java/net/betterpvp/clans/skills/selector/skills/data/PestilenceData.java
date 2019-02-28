package net.betterpvp.clans.skills.selector.skills.data;

import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PestilenceData {

    private UUID damager;
    private List<UUID> oldInfected;
    private List<PestilenceDamageData> currentInfected;

    public PestilenceData(UUID damager) {
        this.damager = damager;
        oldInfected = new ArrayList<>();
        currentInfected = new ArrayList<>();
    }

    public UUID getDamager() {
        return damager;
    }

    public List<UUID> getOldInfected() {
        return oldInfected;
    }

    public List<PestilenceDamageData> getCurrentInfected() {
        return currentInfected;
    }

    public void addInfected(Player p) {


        Player damager = Bukkit.getPlayer(getDamager());
        if (damager != null) {
            UtilMessage.message(p, "Pestilence", ChatColor.GREEN + damager.getName() + ChatColor.GRAY + " infected you with pestilence!");
            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
            getCurrentInfected().add(new PestilenceDamageData(p.getUniqueId()));
        }
    }

    public void addOldInfected(UUID uuid) {
        if (!getOldInfected().contains(uuid)) {
            getOldInfected().add(uuid);
        }
    }


    public class PestilenceDamageData {

        private UUID damagee;
        private long startTime;
        private final long length = 5000;

        public PestilenceDamageData(UUID damagee) {
            this.damagee = damagee;
            startTime = System.currentTimeMillis();
        }

        public UUID getDamagee() {
            return damagee;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getLength() {
            return length;
        }

    }
}


