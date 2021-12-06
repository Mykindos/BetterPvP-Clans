package net.betterpvp.clans.fishing;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.fishing.mysql.FishRepository;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.utilities.UtilClans;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.*;
import net.minecraft.world.entity.projectile.FishingHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FishingListener extends BPVPListener<Clans> {

    public FishingListener(Clans i) {
        super(i);
    }

    private String[] fish = {"Gummy Shark", "Snapper", "Swordfish", "Trout", "Tuna",
            "Barracuda", "Salmon", "Lobster", "Catfish", "Great White Shark", "Jellyfish",
            "Octopus", "Marlin", "Dolphin", "Angler Fish", "Carp", "Bream",
            "Stingray", "Mackerel", "Flathead", "Bass"};

    private static final EntityType[] types = new EntityType[]{EntityType.CREEPER, EntityType.ZOMBIE,
            EntityType.WITCH, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.CAVE_SPIDER, EntityType.ILLUSIONER};

    private static final EntityType[] frenzyTypes = new EntityType[]{EntityType.CREEPER, EntityType.ZOMBIE,
            EntityType.WITCH, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.CAVE_SPIDER, EntityType.GHAST, EntityType.VEX, EntityType.ILLUSIONER};

    List<String> lore = new ArrayList<String>();

    private String randomFish() {
        int name = UtilMath.randomInt(fish.length);
        String randomName = (fish[name]);
        return randomName;
    }

    @EventHandler
    public void handleFishingEvent(PlayerFishEvent event) {
        System.out.println(event.getState().name());


        if (event.getCaught() != null && event.getCaught().getType() == EntityType.ARMOR_STAND) {
            event.setCancelled(true);
            return;
        }


        if(event.getCaught() != null && event.getCaught() instanceof LivingEntity){
            if(ShopManager.isShop((LivingEntity) event.getCaught())){
                event.setCancelled(true);
                return;
            }
        }

        if (event.getCaught() != null && event.getCaught().equals(event.getPlayer())) {
            return;
        }


        Player player = event.getPlayer();

        if (UtilBlock.isInLiquid(event.getHook())) {
            if (event.getCaught() != null) {
                Clan aClan = ClanUtilities.getClan(event.getCaught().getLocation());
                if (aClan != null) {
                    if (aClan instanceof AdminClan) {
                        AdminClan adminClan = (AdminClan) aClan;
                        if (adminClan.isSafe()) {
                            UtilMessage.message(event.getPlayer(), "Fishing", "You cannot fish here!");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        if (Energy.use(player, "Hook", 25D, true)) {


            Gamer gamer = GamerManager.getOnlineGamer(player);
            Client c = gamer.getClient();
            if (c.hasDonation("MasterFisher")) {

                setBiteTime(event.getHook(), UtilMath.randomInt(100, 650));
                System.out.println(player.getName() + " threw hook using MasterFisher");
            }

            if (event.getCaught() != null && event.getCaught().getType() == EntityType.PLAYER) {
                Player caught = (Player) event.getCaught();

                if (caught != null) {
                    if (ClanUtilities.getClan(caught.getLocation()) != null) {
                        if (ClanUtilities.getClan(caught.getLocation()) instanceof AdminClan) {
                            AdminClan a = (AdminClan) ClanUtilities.getClan(caught.getLocation());
                            if (a.isSafe()) {
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }

                    caught.setVelocity(player.getLocation().getDirection().multiply(-1).normalize());

                }
                return;
            }


            if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
            if (event.getCaught() != null && event.getCaught().getType() == EntityType.DROPPED_ITEM) {

                event.getCaught().remove();
                event.setExpToDrop(0);
                Clan clan = ClanUtilities.getClan(player.getLocation());
                if (clan != null || QuestPerkManager.hasPerk(player, "Base Fishing")) {
                    if ((clan == null && QuestPerkManager.hasPerk(player, "Base Fishing"))
                            || clan.getName().equals("Lake") || QuestPerkManager.hasPerk(player, "Base Fishing")
                            || clan.getName().equals("Fields")) {


                        String name = randomFish();
                        int value = UtilMath.randomInt(10, 500);
                        ItemStack item = new ItemStack(Material.COD, value);
                        ItemMeta im = item.getItemMeta();
                        im.setDisplayName(name);
                        item.setItemMeta(im);

                        if(clan != null && (c.getName().equals("Lake") || c.getName().equals("Fields"))) {
                            int legValue = UtilMath.randomInt(3000);
                            if (legValue >= 2998) {
                                List<ItemStack> weapons = new ArrayList<>();
                                for (Weapon w : WeaponManager.weapons) {
                                    if (w instanceof ILegendary) {
                                        weapons.add(w.createWeapon(true));
                                    }

                                }
                                Item items = player.getWorld().dropItem(event.getHook().getLocation(), weapons.get(UtilMath.randomInt(weapons.size())));
                                ItemStack l = items.getItemStack();
                                UtilVelocity.velocity(items, UtilVelocity.getTrajectory(items, player), 1.1D, false, 0.0D, 0.4D, 10.0D, false);
                                UtilMessage.broadcast("Fishing", player.getName() + " caught a " + ChatColor.GREEN + l.getItemMeta().getDisplayName() + ChatColor.GRAY + ".");
                                Log.write("Fishing", player.getName() + " caught a " + ChatColor.GREEN + l.getItemMeta().getDisplayName() + ChatColor.GRAY + ".");
                                for (Player d : Bukkit.getOnlinePlayers()) {
                                    d.playSound(d.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1F, 1F);
                                }

                                return;
                            }
                        }

                        int mobValue = UtilMath.randomInt(100);
                        if (mobValue <= 10) {

                            if(!WEManager.isEventActive("FishingFrenzy")) {
                                Entity entity = player.getWorld().spawnEntity(event.getHook().getLocation(), types[UtilMath.randomInt(types.length - 1)]);
                                UtilVelocity.velocity(entity, UtilVelocity.getTrajectory(entity, player), 1.1D, false, 0.0D, 0.4D, 10.0D, false);
                                UtilMessage.message(player, "Fishing", "You caught a " + ChatColor.GREEN + UtilFormat.cleanString(entity.getType().toString()) + ChatColor.GRAY + ".");
                                return;
                            }else{
                                Entity entity = player.getWorld().spawnEntity(event.getHook().getLocation(), frenzyTypes[UtilMath.randomInt(frenzyTypes.length - 1)]);
                                UtilVelocity.velocity(entity, UtilVelocity.getTrajectory(entity, player), 1.1D, false, 0.0D, 0.4D, 10.0D, false);
                                UtilMessage.message(player, "Fishing", "You caught a " + ChatColor.GREEN + UtilFormat.cleanString(entity.getType().toString()) + ChatColor.GRAY + ".");
                                return;
                            }
                        }

                        int itemValue = UtilMath.randomInt(100);
                        if (itemValue <= 15) {

                            ItemStack[] types = new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.DIAMOND, 3), new ItemStack(Material.NETHERITE_INGOT, 3),
                                    new ItemStack(Material.IRON_INGOT, 3), new ItemStack(Material.GOLD_INGOT, 3), new ItemStack(Material.LEATHER, 3),
                                    new ItemStack(Material.DIAMOND_AXE), new ItemStack(Material.SPONGE), new ItemStack(Material.LAPIS_BLOCK),
                                    new ItemStack(Material.FISHING_ROD)};
                            Item items = player.getWorld().dropItem(event.getHook().getLocation(), UtilClans.updateNames(types[UtilMath.randomInt(types.length - 1)]));
                            UtilVelocity.velocity(items, UtilVelocity.getTrajectory(items, player), 1.1D, false, 0.0D, 0.4D, 10.0D, false);
                            UtilMessage.message(player, "Fishing", "You caught a " + ChatColor.GREEN + UtilFormat.cleanString(items.getItemStack().getType().toString()) + ChatColor.GRAY + ".");
                            return;
                        }

                        if (value < 450) {


                            UtilMessage.message(player, "Fishing", "You caught " + ChatColor.GREEN + value + " Pound " + name + ChatColor.GRAY + ".");
                        } else {

                            value = UtilMath.randomInt(500);
                            if (value < 450) {

                                value = value + 1000;


                                UtilMessage.broadcast("Big Catch", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " caught a "
                                        + ChatColor.GREEN + value + " Pound " + name + ChatColor.GRAY + "!");
                            } else {

                                value = UtilMath.randomInt(1, 500);
                                if (value < 450) {
                                    value = UtilMath.randomInt(1, 500);
                                } else {
                                    value = UtilMath.randomInt(1, 1000);
                                }
                                value = value + 1500;


                                UtilMessage.broadcast("Huge Catch", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " caught a "
                                        + ChatColor.GREEN + value + " Pound " + name + ChatColor.GRAY + "!");
                            }
                        }

                        gamer.setStatValue("Fish Weight", gamer.getStatValue("Fish Weight") + value);
                        gamer.setStatValue("Fish Reeled", gamer.getStatValue("Fish Reeled") + 1);
                        boolean tripled = false;
                        if (clan != null && (clan.getName().equalsIgnoreCase("Fields") || clan.getName().equalsIgnoreCase("Lake"))) {
                            if (WEManager.isEventActive("FishingFrenzy")) {
                                value *= 3;
                                tripled = true;
                            }
                        }

                        if(!(clan != null && (clan.getName().equals("Fields") || clan.getName().equals("Lake")))){
                            value = (int) (value * 0.8);
                        }
                        item.setAmount(value / 3);
                        UtilItem.insert(player, UtilClans.updateNames(item));

                        Fish fish = new Fish(player.getName(), tripled ? value / 3 : value, name, System.currentTimeMillis());
                        FishManager.addFish(fish);
                        FishRepository.saveFish(fish);

                        if (FishManager.isTop(fish)) {
                            UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.RED + " caught the biggest fish this map!");
                        } else if (FishManager.isTopWeek(fish)) {
                            UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.RED + " caught the biggest fish this week!");
                        } else if (FishManager.isTopDay(fish)) {
                            UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.RED + " caught the biggest fish today!");
                        }
                    } else {
                        UtilMessage.message(event.getPlayer(), "Fishing", "You can only catch fish at the Lake!");
                    }
                } else {
                    UtilMessage.message(event.getPlayer(), "Fishing", "You can only catch fish at the Lake!");
                }


            }
        } else {
            event.setCancelled(true);
        }
    }

    private void setBiteTime(FishHook hook, int time) {
        FishingHook hookCopy = (FishingHook) ((CraftEntity) hook).getHandle();


        try {

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        Field tmp = FishingHook.class.getDeclaredField("timeUntilLured");
                        tmp.setAccessible(true);
                        tmp.setInt(hookCopy, (int) (tmp.getInt(hookCopy) / 2));
                        System.out.println(tmp.getInt(hookCopy));
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }.runTaskLater(getInstance(), 20);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }
}