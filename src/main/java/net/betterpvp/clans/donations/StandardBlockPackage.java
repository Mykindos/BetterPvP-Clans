package net.betterpvp.clans.donations;

import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.core.donation.DonationExpiryTimes;
import net.betterpvp.core.donation.IClaimable;
import net.betterpvp.core.donation.IDonation;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StandardBlockPackage implements IDonation, IClaimable {

    @Override
    public String getName() {
        return "StandardBlockPackage";
    }

    @Override
    public String getDisplayName() {
        return "Standard Block Package";
    }

    @Override
    public long getExpiryTime() {
        return DonationExpiryTimes.NONE;
    }

    @Override
    public void claim(Player player) {
        for(PackageContents content : PackageContents.values()){
            UtilItem.insert(player, UtilClans.updateNames(new ItemStack(content.getMaterial(), content.getCount())));
        }
        UtilMessage.message(player, "Donation", "You claimed " + ChatColor.GREEN + getDisplayName() + ChatColor.GRAY + ".");
    }

    @Override
    public String getClaimFailedReason() {
        return ChatColor.RED + "You can only claim this perk in your own or an allied clans territory!";
    }

    @Override
    public boolean canClaim(Player player) {
        Clan clan = ClanUtilities.getClan(player);
        if(clan != null){
           Clan tClan = ClanUtilities.getClan(player.getLocation());
           if(tClan != null){
               if(clan.equals(tClan) || clan.isAllied(tClan)){
                   return true;
               }
           }
        }
        return false;
    }

    private enum PackageContents {


        STONEBRICK(Material.STONE_BRICKS, 448),
        DIAMONDPICKAXE(Material.DIAMOND_PICKAXE, 3),
        DIAMONDSHOVEL(Material.DIAMOND_SHOVEL, 3),
        OAKPLANK(Material.OAK_PLANKS, 256),
        SPONGE(Material.SPONGE, 12),
        GLASS(Material.GLASS, 128),
        GLOWSTONE(Material.GLOWSTONE, 32),
        REDCONCRETE(Material.RED_CONCRETE, 64),
        BLUECONCRETE(Material.BLUE_CONCRETE, 64),
        YELLOWCONCRETE(Material.YELLOW_CONCRETE, 64),
        PURPLECONCRETE(Material.PURPLE_CONCRETE, 64),
        PINKCONCRETE(Material.PINK_CONCRETE, 64),
        SEALANTERN(Material.SEA_LANTERN, 64);

        private Material material;
        private int count;

        PackageContents(Material material, int count) {
            this.material = material;
            this.count = count;
        }

        public Material getMaterial() {
            return material;
        }

        public int getCount(){
            return count;
        }

    }
}
