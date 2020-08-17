package net.betterpvp.clans.donations.cosmetics;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.donation.IDonation;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.particles.data.color.RegularColor;
import net.betterpvp.core.settings.SettingChangedEvent;
import net.betterpvp.core.utility.UtilTime;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public abstract class CosmeticWings implements IDonation, Listener {

    private List<UUID> actives = new ArrayList<>();

    public CosmeticWings(Clans clans){
        Bukkit.getPluginManager().registerEvents(this, clans);
    }

    abstract String getSetting();

    @EventHandler
    public void onChange(SettingChangedEvent e) {
        if (e.getSetting().contains("Cosmetic")) {
            if (e.getValue()) {
                if (e.getSetting().equals(getSetting())) {
                    if (!actives.contains(e.getPlayer().getUniqueId())) {
                        actives.add(e.getPlayer().getUniqueId());
                    }
                }
            } else {
                if (actives.contains(e.getPlayer().getUniqueId())) {
                    actives.remove(e.getPlayer().getUniqueId());
                }
            }
        }
    }

    @EventHandler
    public void updateActives(UpdateEvent e) {

        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Client client = ClientUtilities.getOnlineClient(player);
                if (client != null) {

                    if (client.hasDonation(getName())) {
                        if (client.getSettingAsBoolean(getSetting())) {
                            if (!actives.contains(player.getUniqueId())) {
                                actives.add(player.getUniqueId());
                            }
                        } else {
                            if (actives.contains(player.getUniqueId())) {
                                actives.remove(player.getUniqueId());
                            }
                        }
                    } else {
                        if (actives.contains(player.getUniqueId())) {
                            actives.remove(player.getUniqueId());
                        }
                    }
                }

            }
        }
    }

    public void display(Location loc, Location loc2, RegularColor c1, RegularColor c2, RegularColor c3) {
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c1);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c1);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c1);

        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c2);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.4D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.4D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.4D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        // ParticleEffect.REDSTONE.display(c2, loc2.clone().add(loc2.getDirection().multiply(-1.6D)), 30.0D);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.4D)), c1);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c3);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c3);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.4D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c3);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.8D)), c3);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-2.0D)), c3);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.6000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-0.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c3);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.8D)), c3);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.0D)), c3);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.2000000000000002D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-2.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-2.2D)), c3);
        loc.add(0.0D, 0.2D, 0.0D);
        loc2 = loc.clone();
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.4000000000000001D)), c3);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.6D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-1.8D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-2.0D)), c2);
        ParticleEffect.REDSTONE.display(loc2.clone().add(loc2.getDirection().multiply(-2.2D)), c3);
    }

    protected boolean canShow(Player player) {

        if(player.isDead()) {
            return false;
        }

        Gamer gamer = GamerManager.getOnlineGamer(player);
        if (gamer != null) {
            if (!UtilTime.elapsed(gamer.getLastDamaged(), 15000)) {
                return false;
            }

            if (EffectManager.hasEffect(player, EffectType.INVISIBILITY)) {
                return false;
            }
        }

        return true;
    }

    abstract void run(Player player);

    @EventHandler
    public void update(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.TICK_2) {
            for (UUID uuid : actives) {
                Player p = Bukkit.getPlayer(uuid);
                if (p != null) {
                    run(p);
                }
            }
        }
    }
}
