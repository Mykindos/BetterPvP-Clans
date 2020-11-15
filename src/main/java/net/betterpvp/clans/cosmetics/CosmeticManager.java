package net.betterpvp.clans.cosmetics;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.roles.Paladin;
import net.betterpvp.clans.cosmetics.menu.CosmeticMenuListener;
import net.betterpvp.clans.cosmetics.types.death.Scream;
import net.betterpvp.clans.cosmetics.types.death.TNTSplatter;
import net.betterpvp.clans.cosmetics.types.kill.EvilLaugh;
import net.betterpvp.clans.cosmetics.types.kill.Roar;
import net.betterpvp.clans.cosmetics.types.particles.Flame;
import net.betterpvp.clans.cosmetics.types.particles.Hearts;
import net.betterpvp.clans.cosmetics.types.particles.Spiral;
import net.betterpvp.clans.cosmetics.types.particles.Wake;
import net.betterpvp.clans.cosmetics.types.wings.*;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.donation.DonationManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CosmeticManager {

    private static List<Cosmetic> cosmetics = new ArrayList<>();

    public CosmeticManager(Clans instance){
        new CosmeticMenuListener(instance);

        // Kill Effects
        addCosmetic(new Roar(instance));
        addCosmetic(new EvilLaugh(instance));

        // Death Effects
        addCosmetic(new TNTSplatter(instance));
        addCosmetic(new Scream(instance));

        // Particle Effects
        addCosmetic(new Wake(instance));
        addCosmetic(new Flame(instance));
        addCosmetic(new Hearts(instance));
        addCosmetic(new Spiral(instance));

        // Wing Effects
        addCosmetic(new AssassinWings(instance));
        addCosmetic(new BoosterWings(instance));
        addCosmetic(new KnightWings(instance));
        addCosmetic(new GladiatorWings(instance));
        addCosmetic(new WarlockWings(instance));
        addCosmetic(new PaladinWings(instance));
        addCosmetic(new RangerWings(instance));

    }

    private static void addCosmetic(Cosmetic cosmetic){
        cosmetics.add(cosmetic);
        DonationManager.addDonation(cosmetic);
    }

    /**
     *
     * @param name Name of the cosmetic
     * @return the cosmetic
     */
    public static Cosmetic getCosmetic(String name){
        return cosmetics.stream().filter(cosmetic -> cosmetic.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static List<Cosmetic> getCosmeticsByType(CosmeticType type){
        return cosmetics.stream().filter(cosmetic -> cosmetic.getCosmeticType() == type).collect(Collectors.toList());
    }

    public static boolean hasActiveCosmetic(Player player, CosmeticType type){
        return cosmetics.stream().anyMatch(cosmetic -> cosmetic.getActive().contains(player.getUniqueId()) && cosmetic.getCosmeticType() == type);
    }

    /**
     * Activate a cosmetic for a certain player
     * @param player The player
     * @param cosmetic The cosmetic
     */
    public static void activateCosmetic(Player player, Cosmetic cosmetic){

        Gamer gamer = GamerManager.getOnlineGamer(player);
        if(gamer != null){
            if(!gamer.getClient().hasDonation(cosmetic.getName())){
                return;
            }
        }

        cosmetics.stream().forEach(c -> {
            if(c.getCosmeticType() == cosmetic.getCosmeticType()){
                if(c.getActive().contains(player.getUniqueId())){
                    c.getActive().remove(player.getUniqueId());
                }
            }
        });

        cosmetic.getActive().add(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 1);

    }
}
