package net.betterpvp.clans.worldevents.types.bosses.ads;

import net.betterpvp.clans.worldevents.types.WorldEventMinion;
import net.betterpvp.clans.worldevents.types.nms.BossSkeleton;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WitherMinion extends WorldEventMinion {


    public WitherMinion(WitherSkeleton ent) {
        super(ent);


        ent.getEquipment().setItemInMainHand(new ItemStack(Material.MUSIC_DISC_MELLOHI));
        ent.getEquipment().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
        ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(getMaxHealth());
        ent.setHealth(getMaxHealth());
        ent.setCustomName(getDisplayName());
        ent.setCustomNameVisible(true);
        ent.setRemoveWhenFarAway(false);
        ent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

    }

    @Override
    public String getDisplayName() {

        return ChatColor.BLUE.toString() + ChatColor.BOLD + "Witherson";
    }

    @Override
    public double getMaxHealth() {

        return 75;
    }

}
