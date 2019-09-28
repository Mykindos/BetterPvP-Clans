package net.betterpvp.clans.classes;

import net.betterpvp.clans.classes.events.RoleChangeEvent;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.weapon.weapons.legendaries.FireWalkers;
import net.betterpvp.core.client.ClientUtilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class Role {

    public static Set<Role> roles = new HashSet<>();
    public static HashMap<UUID, Role> playerRole = new HashMap<>();

    private String name;


    public Role(String name) {
        this.name = name;

        roles.add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public abstract Material getHelmet();

    public abstract Material getChestplate();

    public abstract Material getLeggings();

    public abstract Material getBoots();

    public String[] equipMessage(Player p) {
        Gamer g = GamerManager.getOnlineGamer(p);

        if (g == null) return new String[]{};

        RoleBuild build = g.getActiveBuild(getName());
        if (build != null) {


            String sword = build.getSword() == null ? "" : build.getSword().getString();
            String axe = build.getAxe() == null ? "" : build.getAxe().getString();
            String bow = build.getBow() == null ? "" : build.getBow().getString();
            String passivea = build.getPassiveA() == null ? "" : build.getPassiveA().getString();
            String passiveb = build.getPassiveB() == null ? "" : build.getPassiveB().getString();
            String global = build.getGlobal() == null ? "" : build.getGlobal().getString();
            return new String[]{
                    ChatColor.GREEN + "Sword: " + ChatColor.WHITE + sword,
                    ChatColor.GREEN + "Axe: " + ChatColor.WHITE + axe,
                    ChatColor.GREEN + "Bow: " + ChatColor.WHITE + bow,
                    ChatColor.GREEN + "Passive A: " + ChatColor.WHITE + passivea,
                    ChatColor.GREEN + "Passive B: " + ChatColor.WHITE + passiveb,
                    ChatColor.GREEN + "Global: " + ChatColor.WHITE + global
            };
        } else {
            return new String[]{};
        }

    }


    public static boolean hasRole(Player player, Role kit) {
        if (player.isDead()) {
            return false;
        }
        if (getRole(player) == null) {
            return false;
        }

        return getRole(player).equals(kit);
    }

    public static Role getRole(Player player) {
        if (playerRole.containsKey(player.getUniqueId())) {
            return playerRole.get(player.getUniqueId());
        }
        return null;
    }

    public static Role getRole(UUID uuid) {
        if (playerRole.containsKey(uuid)) {
            return playerRole.get(uuid);
        }
        return null;
    }

    public static boolean playerHasRole(Player player) {
        if (playerRole.containsKey(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static Role getRole(String role) {
        for (Role r : roles) {
            if (r.getName().equalsIgnoreCase(role)) {
                return r;
            }
        }
        return null;
    }

    public static void setRole(Player player, Role role) {
        if (role == null) {
            if (playerRole.containsKey(player.getUniqueId()) && playerRole.get(player.getUniqueId()) == null) {
                return;
            }

            playerRole.put(player.getUniqueId(), null);
            Bukkit.getServer().getPluginManager().callEvent(new RoleChangeEvent(player, role));
            return;
        }

        if (playerRole.containsKey(player.getUniqueId()) && playerRole.get(player.getUniqueId()) == role) {
            return;
        }

        playerRole.put(player.getUniqueId(), role);
        // ClientUtilities.getClient(player).getGamer().setRole(role);
        Bukkit.getServer().getPluginManager().callEvent(new RoleChangeEvent(player, role));
    }

    public static void doEquip(Player player) {
        for (Role role : roles) {
            for (ItemStack armor : player.getEquipment().getArmorContents()) {

                if (armor == null || armor.getType() == Material.AIR) {
                    setRole(player, null);
                    return;
                }

            }

            Weapon fireWalkers = WeaponManager.getWeapon(player.getEquipment().getBoots());

            if (player.getEquipment().getHelmet().getType() == role.getHelmet()
                    && (player.getEquipment().getChestplate().getType() == role.getChestplate())
                    && player.getEquipment().getLeggings().getType() == role.getLeggings()
                    && (player.getEquipment().getBoots().getType() == role.getBoots()

                    // FireWalkers complete any set
                    || (fireWalkers != null && fireWalkers instanceof FireWalkers))) {
                setRole(player, role);
                return;
            }
        }


        setRole(player, null);
    }

    public static String getPrefix(Player player, boolean colour) {
        Role role = getRole(player);
        if (role != null) {
            if (colour) {
                return ChatColor.GREEN + role.getName().substring(0, 1) + ".";
            } else {
                return role.getName().substring(0, 1);
            }
        }
        return "";
    }

}
