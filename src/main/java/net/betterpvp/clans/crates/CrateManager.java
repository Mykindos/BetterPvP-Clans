package net.betterpvp.clans.crates;


import net.betterpvp.clans.Clans;
import net.betterpvp.clans.crates.crates.VotingCrate;
import net.betterpvp.clans.crates.menu.CrateMenu;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CrateManager {

    public CrateManager() {
        addCrate(new VotingCrate());

    }

    private static List<Crate> crates = new ArrayList<>();

    public static List<Crate> getCrates() {
        return crates;
    }

    public static void addCrate(Crate c) {
        getCrates().add(c);
    }

    public static boolean isCrate(ItemStack i) {
        if (!i.hasItemMeta()) {
            return false;
        }
        for (Crate c : getCrates()) {
            if (i.getItemMeta().getDisplayName() == null) {
                return false;
            }


            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(c.getCrate().getItemMeta().getDisplayName())) {
                return true;
            }
        }
        return false;
    }

    public static Crate getCrate(String name) {
        for (Crate c : getCrates()) {
            if (ChatColor.stripColor(c.getName()).equalsIgnoreCase(ChatColor.stripColor(name).replace("_", " "))) {
                return c;
            }
        }

        return null;
    }

    public static void openPreview(Player p, Crate c) {
        CrateMenu menu = new CrateMenu(p, c.getSize(), c.getName());
        int slot = 0;
        for (ItemStack i : c.getLoot().keySet()) {

            menu.addButton(new Button(slot, i, i.getItemMeta().getDisplayName(), i.getItemMeta().getLore()));
            slot++;
        }

        menu.construct();
        p.openInventory(menu.getInventory());
    }

    public static void openCrate(Clans i, final Player p, final Crate c) {
        final CrateMenu menu = new CrateMenu(p, 9, c.getName());
        menu.construct();
        p.openInventory(menu.getInventory());

        new BukkitRunnable() {
            int counter = 0;

            @Override
            public void run() {

                ItemStack item = getRandomItem(c);
                if (item != null) {

                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
                    menu.addButton(new Button(4, item, item.getItemMeta().getDisplayName(), item.getItemMeta().getLore()));
                    menu.construct();
                    counter++;
                    if (counter >= 10) {
                        reward(p, item, c);
                        cancel();
                    }

                }
            }

        }.runTaskTimer(i, 0, 10);
    }

    public static void reward(Player p, ItemStack item, Crate c) {


        ItemStack newItem = UtilClans.updateNames(item);
        if (newItem.hasItemMeta()) {
            if (ChatColor.stripColor(newItem.getItemMeta().getDisplayName()).startsWith("$")) {
                int amount = Integer.valueOf(ChatColor.stripColor(newItem.getItemMeta().getDisplayName()).substring(1));
                GamerManager.getOnlineGamer(p).addCoins(amount);
                UtilMessage.message(p, c.getName(), "You received $" + ChatColor.YELLOW + amount);
                return;
            }
            p.getInventory().addItem(newItem.clone());
            UtilMessage.message(p, c.getName(), "You received " + ChatColor.GREEN + newItem.getAmount() + " " + newItem.getItemMeta().getDisplayName());

            Weapon w = WeaponManager.getWeapon(newItem);

            if (w != null) {
                if (w instanceof ILegendary) {
                    UtilMessage.broadcast("Vote", ChatColor.GREEN + p.getName() + ChatColor.GRAY + " received a " + newItem.getItemMeta().getDisplayName() + ChatColor.GRAY + " from voting!");
                    for (Player dd : Bukkit.getOnlinePlayers()) {
                        dd.playSound(dd.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1f, 1f);
                    }
                }
            }


        }
    }

    public static ItemStack getRandomItem(Crate c) {

        double sumWeights = 0; // total sum of the weights of items

        // loop through all items adding each weighted value
        // NB: the values DO NOT have to equal 1
        for (Map.Entry<ItemStack, Double> map : c.getLoot().entrySet()) {
            sumWeights += map.getValue();
        }

        double randNum = UtilMath.randDouble(0, sumWeights);
        double sum = 0;
        for (Map.Entry<ItemStack, Double> map : c.getLoot().entrySet()) {
            sum += map.getValue();
            if (randNum <= sum) {
                return map.getKey();
            }
        }

        return null;

    }
}