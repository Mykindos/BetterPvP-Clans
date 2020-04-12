package net.betterpvp.clans.skills.selector.skills.necromancer;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.roles.Necromancer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.minecraft.server.v1_15_R1.EntityVex;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SoulHarvest extends Skill {

    private List<SoulData> souls = new ArrayList<>();

    public SoulHarvest(Clans i) {
        super(i, "Soul Harvest", "Necromancer", noMaterials, noActions, 3, false, false);
    }

    @Override
    public String[] getDescription(int level) {
        return new String[]{
                "Harvest the soul of nearby dead players.",
                "",
                "Collected souls give bursts of speed and regeneration.",
                "Souls are visible by necromancers only"
        };
    }

    @Override
    public Types getType() {
        return Types.PASSIVE_B;
    }

    @Override
    public double getRecharge(int level) {
        return 0;
    }

    @Override
    public float getEnergy(int level) {
        return 0;
    }

    @Override
    public boolean usageCheck(Player p) {
        return false;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        souls.add(new SoulData(e.getEntity().getUniqueId(), e.getEntity().getLocation(), System.currentTimeMillis() + 120_000));
    }

    @EventHandler
    public void displaySouls(UpdateEvent e){

        if(e.getType() == UpdateEvent.UpdateType.FASTER){
            List<Player> active = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> {
                Role role = Role.getRole(p);
                if(role != null && role instanceof Necromancer){
                    if(hasSkill(p, this)){
                        active.add(p);

                        if(!p.isDead() && p.getHealth() > 0) {
                            List<SoulData> remove = new ArrayList<>();
                            souls.forEach(soul -> {
                                if(soul.loc.getWorld().getName().equals(p.getWorld().getName())) {
                                    if (soul.loc.distance(p.getLocation()) <= 1.5 && !soul.uuid.toString().equals(p.getUniqueId().toString())) {
                                        giveEffect(p);
                                        remove.add(soul);
                                    }
                                }
                            });

                            souls.removeIf(s -> remove.contains(s));
                        }
                    }
                }
            });

            souls.removeIf(soul -> soul.expiry - System.currentTimeMillis() <= 0);
            souls.forEach(soul -> {
                List<Player> newActives = new ArrayList<>(active);
                newActives.removeIf(p -> p.getUniqueId().toString().equals(soul.uuid.toString()));
                ParticleEffect.HEART.display(soul.loc.clone().add(0, 1, 0), newActives);
            });

        }
    }

    private void giveEffect(Player p){
        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40 + ((getLevel(p)) * 20), 1));
        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 40 + ((getLevel(p)) * 20), 1));
    }

    private class SoulData {

        public Location loc;
        public long expiry;
        public UUID uuid;

        public SoulData(UUID uuid, Location loc, long expiry){
            this.uuid = uuid;
            this.loc = loc;
            this.expiry = expiry;
        }

    }
}
