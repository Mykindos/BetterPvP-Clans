package net.betterpvp.clans.weapon;

import net.betterpvp.clans.Clans;
import net.betterpvp.core.utility.UtilItem;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Weapon implements Listener {


    private Material material;
    private byte data;
    private String name;
    private String[] lore;
    private boolean legendary;
    private Clans i;
    private double chance;

    public Weapon(Clans i, Material material, byte data, String name, String[] lore, boolean legendary, double chance) {
        Bukkit.getPluginManager().registerEvents(this, i);
        this.material = material;
        this.data = data;
        this.name = name;
        this.lore = lore;
        this.legendary = legendary;
        this.i = i;
        this.chance = chance;

    }

    public List<String> getLoreWithPrice(int price) {
        List<String> temp = new ArrayList<String>();

        temp.add(ChatColor.GRAY + "Buy Price: " + ChatColor.YELLOW + price);
        if (getLore() != null) {
            temp.addAll(Arrays.asList(getLore()));
        }
        return temp;

    }

    public Clans getInstance() {
        return i;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public byte getData() {
        return data;
    }

    public Double getChance() {
        return chance;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getLore() {
        return lore;
    }

    public void setLore(String[] lore) {
        this.lore = lore;
    }

    public ItemStack createWeaponNoGlow() {

        ItemStack item = new ItemStack(getMaterial(), 1);
        return UtilItem.setItemNameAndLore(item, getName(), getLore());
    }

    public ItemStack createWeapon() {
       return createWeapon(false);
    }

    public ItemStack createWeapon(boolean showUUID){
        ItemStack item;

        List<String> loreTemp = new ArrayList<>(Arrays.asList(getLore()));
        if (this instanceof ILegendary) {
            ILegendary iLegendary = (ILegendary) this;
            if(iLegendary.isTextured()){
                item = new ItemStack(getMaterial(), 1);
            }else{
                item = UtilItem.addGlow(new ItemStack(getMaterial(), 1));
            }

            if(showUUID) {
                loreTemp.add(ChatColor.GRAY.toString() + "UUID: " + ChatColor.YELLOW.toString() + UUID.randomUUID().toString());
            }
        }else{
            item = UtilItem.addGlow(new ItemStack(getMaterial(), 1));
        }

        return UtilItem.setItemNameAndLore(item, getName(), loreTemp);
    }


    protected boolean isThisWeapon(Player p) {
        return WeaponManager.isWeapon(p.getInventory().getItemInMainHand()) && WeaponManager.getWeapon(p.getInventory().getItemInMainHand()).equals(this);
    }

}
