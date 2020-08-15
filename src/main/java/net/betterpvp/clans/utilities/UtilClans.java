package net.betterpvp.clans.utilities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilClans {

    /**
     * General method that updates the name of almost every item that is picked up by players
     * E.g. Names leather armour after assassins
     * E.g. Turns the colour of the items name to yellow from white
     *
     * @param abc ItemStack to update
     * @return An ItemStack with an updated name
     */
    public static ItemStack updateNames(ItemStack abc) {
        if (WeaponManager.getWeapon(abc) != null) {
            return abc;
        }

        if (abc.hasItemMeta()) {
            //if(Perk.getPerk(abc.getItemMeta().getDisplayName()) != null){
            //    return abc;
            // }
        }
        List<String> lore = new ArrayList<>();
        Material m = abc.getType();
        ItemMeta a = abc.getItemMeta();

        if (a != null) {
            a.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        if (m == Material.LEATHER_HELMET) {
            a.setDisplayName("Assassin Helmet");
        } else if (m == Material.LEATHER_CHESTPLATE) {
            a.setDisplayName("Assassin Vest");
        } else if (m == Material.LEATHER_LEGGINGS) {
            a.setDisplayName("Assassin Leggings");
        } else if (m == Material.LEATHER_BOOTS) {
            a.setDisplayName("Assassin Boots");
        } else if (m == Material.IRON_HELMET) {
            a.setDisplayName("Knight Helmet");
        } else if (m == Material.IRON_CHESTPLATE) {
            a.setDisplayName("Knight Chestplate");
        } else if (m == Material.IRON_LEGGINGS) {
            a.setDisplayName("Knight Leggings");
        } else if (m == Material.IRON_BOOTS) {
            a.setDisplayName("Knight Boots");
        } else if (m == Material.DIAMOND_HELMET) {
            a.setDisplayName("Gladiator Helmet");
        } else if (m == Material.DIAMOND_CHESTPLATE) {
            a.setDisplayName("Gladiator Chestplate");
        } else if (m == Material.DIAMOND_LEGGINGS) {
            a.setDisplayName("Gladiator Leggings");
        } else if (m == Material.DIAMOND_BOOTS) {
            a.setDisplayName("Gladiator Boots");
        } else if (m == Material.GOLDEN_HELMET) {
            a.setDisplayName("Paladin Helmet");
        } else if (m == Material.GOLDEN_CHESTPLATE) {
            a.setDisplayName("Paladin Vest");
        } else if (m == Material.GOLDEN_LEGGINGS) {
            a.setDisplayName("Paladin Leggings");
        } else if (m == Material.GOLDEN_BOOTS) {
            a.setDisplayName("Paladin Boots");
        } else if (m == Material.NETHERITE_HELMET) {
            a.setDisplayName("Warlock Helmet");
        } else if (m == Material.NETHERITE_CHESTPLATE) {
            a.setDisplayName("Warlock Vest");
        } else if (m == Material.NETHERITE_LEGGINGS) {
            a.setDisplayName("Warlock Leggings");
        } else if (m == Material.NETHERITE_BOOTS) {
            a.setDisplayName("Warlock Boots");
        } else if (m == Material.CHAINMAIL_HELMET) {
            a.setDisplayName("Ranger Helmet");
        } else if (m == Material.CHAINMAIL_CHESTPLATE) {
            a.setDisplayName("Ranger Vest");
        } else if (m == Material.CHAINMAIL_LEGGINGS) {
            a.setDisplayName("Ranger Leggings");
        } else if (m == Material.CHAINMAIL_BOOTS) {
            a.setDisplayName("Ranger Boots");
        } else if (m == Material.GOLDEN_AXE) {
            a.setDisplayName("Radiant axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "5");

        } else if (m == Material.MUSIC_DISC_WAIT) {
            a.setDisplayName("$100,000");
        } else if (m == Material.MUSIC_DISC_13) {
            a.setDisplayName("$50,000");
        } else if (m == Material.MUSIC_DISC_PIGSTEP) {
            a.setDisplayName("$1,000,000");
        } else if (m == Material.CARROT) {
            a.setDisplayName("Carrot");
        } else if (m == Material.POTATO) {
            a.setDisplayName("Potato");
        } else if (m == Material.IRON_SWORD) {

            a.setDisplayName("Standard Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "4");

        } else if (m == Material.GOLDEN_SWORD) {
            a.setDisplayName("Radiant Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "6");
        } else if (m == Material.DIAMOND_SWORD) {
            a.setDisplayName("Power Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "5");
            lore.add(ChatColor.GRAY + "Bonus 1 Level to Sword Skills");
        } else if (m == Material.NETHERITE_SWORD) {
            a.setDisplayName("Ancient Sword");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "6");
            lore.add(ChatColor.GRAY + "Bonus 1 Level to Sword Skills");
        } else if (m == Material.IRON_AXE) {
            a.setDisplayName("Standard Axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "3");

        } else if (m == Material.DIAMOND_AXE) {
            a.setDisplayName("Power Axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "4");
            lore.add(ChatColor.GRAY + "Bonus 1 Level to Axe Skills");
        } else if (m == Material.NETHERITE_AXE) {
            a.setDisplayName("Ancient Axe");
            lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GREEN + "5");
            lore.add(ChatColor.GRAY + "Bonus 1 Level to Axe Skills");
        } else if (m == Material.TURTLE_HELMET) {
            a.setDisplayName("Agility Helmet");
        }

        if (a.hasDisplayName()) {
            if (a.getDisplayName().equalsIgnoreCase(ChatColor.stripColor("Base Fishing"))) {
                lore.add(ChatColor.WHITE + "Allows a player to fish inside their base");
            }
            if (!a.getDisplayName().contains("Crate")) {
                a.setDisplayName(ChatColor.YELLOW + ChatColor.stripColor(a.getDisplayName()));
            }
        } else {
            a.setDisplayName(ChatColor.YELLOW + UtilFormat.cleanString(abc.getType().name()));
        }
        a.setLore(lore);
        abc.setItemMeta(a);
        return abc;
    }

    /**
     * Check if a player has any valuable items in their inventory
     *
     * @param p Players inventory to check
     * @return Returns true if a player has any items of value in their inventory
     */
    public static boolean hasValuables(Player p) {


        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null) {
                Weapon w = WeaponManager.getWeapon(i);
                if (w != null) {
                    if (w instanceof ILegendary) {
                        return true;
                    }
                }

                if (i.getType() == Material.MUSIC_DISC_WAIT || i.getType() == Material.MUSIC_DISC_PIGSTEP
                        || i.getType() == Material.MUSIC_DISC_13 || i.getType() == Material.TNT) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isUsableWithShield(ItemStack item) {

        if (item.getType().name().contains("_SWORD")) {
            return true;
        }

        //Windblade
        if (item.getType() == Material.MUSIC_DISC_MELLOHI || item.getType() == Material.MUSIC_DISC_STRAD
                || item.getType() == Material.MUSIC_DISC_CAT) {
            return true;
        }

        return false;
    }

    public static Location closestWilderness(Player p) {
        List<Location> locs = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            Location loc = p.getLocation().clone().add(i, 0, 0);
            Clan aClan = ClanUtilities.getClan(loc);
            if (aClan == null) {
                locs.add(loc);
                break;
            }
        }

        for (int i = 0; i > -64; i--) {
            Location loc = p.getLocation().clone().add(i, 0, 0);
            Clan bClan = ClanUtilities.getClan(loc);
            if (bClan == null) {
                locs.add(loc);
                break;
            }
        }

        for (int i = 0; i < 64; i++) {
            Location loc = p.getLocation().clone().add(0, 0, i);
            Clan cClan = ClanUtilities.getClan(loc);
            if (cClan == null) {
                locs.add(loc);
                break;
            }
        }

        for (int i = 0; i > -64; i--) {
            Location loc = p.getLocation().clone().add(0, 0, i);
            Clan dClan = ClanUtilities.getClan(loc);
            if (dClan == null) {
                locs.add(loc);
                break;
            }
        }

        if (locs.size() > 0) {

            Collections.sort(locs, new Comparator<Location>() {

                @Override
                public int compare(Location a, Location b) {
                    // TODO Auto-generated method stub
                    return (int) p.getLocation().distance(a) - (int) p.getLocation().distance(b);
                }

            });


            return locs.get(0);


        }
        return null;


    }

    public static Location closestWildernessBackwards(Player p) {
        List<Location> locs = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            Location loc = p.getLocation().add(p.getLocation().getDirection().multiply(i * -1));
            Clan aClan = ClanUtilities.getClan(loc);
            if (aClan == null) {
                locs.add(loc);
                break;
            }
        }


        if (locs.size() > 0) {

            Collections.sort(locs, new Comparator<Location>() {

                @Override
                public int compare(Location a, Location b) {
                    // TODO Auto-generated method stub
                    return (int) p.getLocation().distance(a) - (int) p.getLocation().distance(b);
                }

            });


            return locs.get(0);


        }
        return null;


    }

    public static void setGlowing(Player player, Player target, boolean glowing) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, target.getEntityId()); //Set packet's entity id
        WrappedDataWatcher watcher = new WrappedDataWatcher(); //Create data watcher, the Entity Metadata packet requires this
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class); //Found this through google, needed for some stupid reason
        watcher.setEntity(target); //Set the new data watcher's target
        byte entityByte = 0x00;
        if (glowing) {
            entityByte = (byte) (entityByte | 0x40);
        } else {
            entityByte = (byte) (entityByte & ~0x40);
        }
        watcher.setObject(0, serializer, entityByte); //Set status to glowing, found on protocol page
        packet.getWatchableCollectionModifier().write(0, watcher.getWatchableObjects()); //Make the packet's datawatcher the one we created
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
