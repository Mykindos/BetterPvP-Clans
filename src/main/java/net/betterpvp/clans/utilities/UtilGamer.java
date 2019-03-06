package net.betterpvp.clans.utilities;

import net.betterpvp.clans.combat.armour.ArmourRepository;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class UtilGamer {

    public static double getDamageReduced(double base, LivingEntity p)
    {
        if(p.getEquipment() == null) return base;
        ItemStack helmet = p.getEquipment().getHelmet();
        ItemStack chest =  p.getEquipment().getChestplate();
        ItemStack pants =  p.getEquipment().getLeggings();
        ItemStack boots =  p.getEquipment().getBoots();
        double red = 0.0;

        if (helmet != null)
        {
            red += ArmourRepository.getArmour(helmet);
        }

        if (boots != null)
        {
            red += ArmourRepository.getArmour(boots);
        }

        if (pants != null)
        {
            red += ArmourRepository.getArmour(pants);
        }

        if (chest != null)
        {
            red += ArmourRepository.getArmour(chest);
        }
        if(red == 0) return base;

        return base * (100 - red) / 100;
    }

}
