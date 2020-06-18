package net.betterpvp.clans.donations;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.classes.roles.Assassin;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;

import net.betterpvp.core.client.mysql.SettingsRepository;
import net.betterpvp.core.donation.IDonation;
import net.betterpvp.core.donation.events.DonationStatusEvent;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMath;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RaveArmour implements IDonation, Listener {

    private List<UUID> actives = new ArrayList<>();

    @EventHandler
    public void updateActives(UpdateEvent e) {

        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Client client = ClientUtilities.getOnlineClient(player);
                if (client != null) {
                    if (client.hasDonation(getName())) {
                        if (client.getSettingAsBoolean("Cosmetics.Rave Armour")) {
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

    @EventHandler
    public void onDonation(DonationStatusEvent e) {
        if (e.getPerk().getName().equalsIgnoreCase(getName())) {
            if (e.getStatus() == DonationStatusEvent.Status.ADDED) {
                if (!e.getClient().getSettingAsBoolean("Cosmetics.Rave Armour")) {
                    e.getClient().getSettings().put("Cosmetics.Rave Armour", 1);
                    SettingsRepository.updateSetting(e.getClient().getUUID(), "Cosmetics.Rave Armour", 1);
                }
                if (!actives.contains(e.getClient().getUUID())) {
                    actives.add(e.getClient().getUUID());
                }
            } else if (e.getStatus() == DonationStatusEvent.Status.REMOVED) {
                if (actives.contains(e.getClient().getUUID())) {
                    actives.remove(e.getClient().getUUID());
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (actives.contains(e.getPlayer().getUniqueId())) {
            actives.remove(e.getPlayer().getUniqueId());
        }
    }


    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FASTEST) {
            for (UUID active : actives) {
                Player player = Bukkit.getPlayer(active);
                if (player != null) {
                    Role role = Role.getRole(player);
                    if (role != null && role instanceof Assassin) {
                        Color color = Colours.values()[UtilMath.randomInt(Colours.values().length - 1)].getColor();
                        for (ItemStack i : player.getEquipment().getArmorContents()) {
                            if (i == null) continue;
                            short dura = i.getDurability();
                            if (i.getType() == Material.LEATHER_HELMET) {
                                player.getInventory().setHelmet(getColouredArmor(i.getType(), color, dura));
                            } else if (i.getType() == Material.LEATHER_CHESTPLATE) {
                                player.getInventory().setChestplate(getColouredArmor(i.getType(), color, dura));
                            } else if (i.getType() == Material.LEATHER_LEGGINGS) {
                                player.getInventory().setLeggings(getColouredArmor(i.getType(), color, dura));
                            } else if (i.getType() == Material.LEATHER_BOOTS) {
                                player.getInventory().setBoots(getColouredArmor(i.getType(), color, dura));
                            }

                        }
                    }
                }
            }
        }
    }

    private ItemStack getColouredArmor(Material m, Color c, short dura) {
        ItemStack i = new ItemStack(m, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
        meta.setColor(c);
        i.setDurability(dura);
        i.setItemMeta(meta);
        return i;
    }



    @Override
    public String getName() {
        return "RaveArmour";
    }

    @Override
    public String getDisplayName() {
        return "Rave Armour";
    }

    @Override
    public long getExpiryTime() {
        return 0;
    }

    private enum Colours {


        BLACK(Color.BLACK),
        WHITE(Color.WHITE),
        RED(Color.RED),
        GREEN(Color.GREEN),
        AQUA(Color.AQUA),
        ORANGE(Color.ORANGE),
        YELLOW(Color.YELLOW),
        SILVER(Color.SILVER),
        PURPLE(Color.PURPLE),
        PINK(Color.fromRGB(255, 0, 255));

        private Color c;

        Colours(Color c) {
            this.c = c;
        }

        public Color getColor() {
            return c;
        }

    }
}
