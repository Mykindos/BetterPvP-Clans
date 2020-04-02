package net.betterpvp.clans.fishing;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;
import net.betterpvp.clans.fishing.mysql.FishRepository;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.*;
import net.minecraft.server.v1_8_R3.EntityFishingHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FishingListener extends BPVPListener<Clans> {

    public FishingListener(Clans i){
        super(i);
    }

    private String[] fish = {"Gummy Shark", "Snapper", "Swordfish", "Trout", "Tuna",
            "Barracuda", "Salmon", "Lobster", "Catfish", "Great White Shark", "Jellyfish",
            "Octopus", "Marlin", "Dolphin", "Angler Fish", "Carp", "Bream",
            "Stingray", "Mackerel", "Flathead", "Bass"};

    List<String> lore = new ArrayList<String>();

    private String randomFish() {
        int name = UtilMath.randomInt(fish.length);
        String randomName = (fish[name]);
        return randomName;
    }

    @EventHandler
    public void handleFishingEvent(PlayerFishEvent event) {
        System.out.println(event.getState().name());


        if(event.getCaught() != null && event.getCaught().getType() == EntityType.ARMOR_STAND){
            event.setCancelled(true);
            return;
        }


        Player player = event.getPlayer();

        if(event.getCaught() != null) {
            Clan aClan = ClanUtilities.getClan(event.getCaught().getLocation());
            if(aClan != null) {
                if(aClan instanceof AdminClan) {
                    AdminClan adminClan = (AdminClan) aClan;
                    if(adminClan.isSafe()) {
                        UtilMessage.message(event.getPlayer(), "Fishing", "You cannot fish here!");
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        if(Energy.use(player, "Hook", 25D, true)){


            /**Client c = ClientUtilities.getOnlineClient(player);
            if(c.getGamer().hasPerk(Perk.getPerk("MasterFisher")) || c.hasDonationRank(DonationRank.GUARDIAN)){

                setBiteTime(event.getHook(), UtilMath.randomInt(100, 650));
                System.out.println(player.getName() + " threw hook using MasterFisher");
            }*/

            if(event.getCaught() != null && event.getCaught().getType() == EntityType.PLAYER){
                Player caught = (Player) event.getCaught();

                if(caught != null){
                    if(ClanUtilities.getClan(caught.getLocation()) != null){
                        if(ClanUtilities.getClan(caught.getLocation()) instanceof AdminClan){
                            AdminClan a = (AdminClan) ClanUtilities.getClan(caught.getLocation());
                            if(a.isSafe()){
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }

                    caught.setVelocity(player.getLocation().getDirection().multiply(-1).normalize());

                }
                return;
            }



            if (event.getCaught() != null && event.getCaught().getType() == EntityType.DROPPED_ITEM) {

                event.getCaught().remove();
                event.setExpToDrop(0);
                Clan clan = ClanUtilities.getClan(player.getLocation());
                if(clan != null || QuestPerkManager.hasPerk(player, "Base Fishing")){
                    if((clan == null && QuestPerkManager.hasPerk(player, "Base Fishing"))
                            || clan.getName().equals("Lake") || QuestPerkManager.hasPerk(player, "Base Fishing")
                            || clan.getName().equals("Fields") ){


                        String name = randomFish();
                        int value = UtilMath.randomInt(10, 500);
                        ItemStack item = new ItemStack(Material.RAW_FISH, value, (byte) 0);
                        ItemMeta im = item.getItemMeta();
                        im.setDisplayName(name);
                        item.setItemMeta(im);

                        int legValue = UtilMath.randomInt(7500);
                        if(legValue >= 7499){
                            List<ItemStack> weapons = new ArrayList<>();
                            for(Weapon w : WeaponManager.weapons){
                                if(w.isLegendary()){
                                    weapons.add(w.createWeapon());
                                }

                            }
                            Item items = player.getWorld().dropItem(event.getHook().getLocation(), weapons.get(UtilMath.randomInt(weapons.size())));
                            ItemStack l = items.getItemStack();
                            UtilVelocity.velocity(items, UtilVelocity.getTrajectory(items, player), 1.2D, false, 0.0D, 0.4D, 10.0D, false);
                            UtilMessage.broadcast("Fishing", player.getName() + " caught a " + ChatColor.GREEN +  l.getItemMeta().getDisplayName() + ChatColor.GRAY + ".");
                            for(Player d : Bukkit.getOnlinePlayers()){
                                d.playSound(d.getLocation(), Sound.FIREWORK_LAUNCH, 1F, 1F);
                            }

                            return;
                        }

                        int mobValue = UtilMath.randomInt(100);
                        if (mobValue <= 10) {

                            EntityType[] types = new EntityType[]{EntityType.CREEPER , EntityType.ZOMBIE,
                                    EntityType.WITCH, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.CAVE_SPIDER};
                            Entity entity = player.getWorld().spawnEntity(event.getHook().getLocation(), types[UtilMath.randomInt(types.length- 1) ]);
                            UtilVelocity.velocity(entity, UtilVelocity.getTrajectory(entity, player), 1.2D, false, 0.0D, 0.4D, 10.0D, false);
                            UtilMessage.message(player, "Fishing", "You caught a " + ChatColor.GREEN + UtilFormat.cleanString(entity.getType().toString()) + ChatColor.GRAY + ".");
                            return;
                        }

                        int itemValue = UtilMath.randomInt(100);
                        if (itemValue <= 15) {

                            ItemStack[] types = new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD), new ItemStack(Material.DIAMOND),
                                    new ItemStack(Material.IRON_INGOT, 3), new ItemStack(Material.GOLD_INGOT, 3), new ItemStack(Material.LEATHER, 3),
                                    new ItemStack(Material.DIAMOND_AXE), new ItemStack(Material.SPONGE), new ItemStack(Material.LAPIS_BLOCK),
                                    new ItemStack(Material.FISHING_ROD)};
                            Item items = player.getWorld().dropItem(event.getHook().getLocation(), UtilItem.updateNames(types[UtilMath.randomInt(types.length- 1) ]));
                            UtilVelocity.velocity(items, UtilVelocity.getTrajectory(items, player), 1.2D, false, 0.0D, 0.4D, 10.0D, false);
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
                                if(value < 450){
                                    value = UtilMath.randomInt(1, 500);
                                }else{
                                    value = UtilMath.randomInt(1, 1000);
                                }
                                value = value + 1500;


                                UtilMessage.broadcast("Huge Catch", ChatColor.YELLOW + player.getName() + ChatColor.GRAY + " caught a "
                                        + ChatColor.GREEN + value + " Pound " + name + ChatColor.GRAY + "!");
                            }
                        }

                        boolean tripled = false;
                        if(clan != null && (clan.getName().equalsIgnoreCase("Fields") || clan.getName().equalsIgnoreCase("Lake"))) {
                            if(WEManager.isEventActive("FishingFrenzy")) {
                                value *= 3;
                                tripled = true;
                            }
                        }
                        item.setAmount(value / 3);
                        UtilItem.insert(player, UtilItem.updateNames(item));

                        Fish fish = new Fish(player.getName(), tripled ? value / 3: value, name, System.currentTimeMillis());
                        FishManager.addFish(fish);
                        FishRepository.saveFish(fish);

                        if(FishManager.isTop(fish)){
                            UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.RED + " caught the biggest fish this map!" );
                        }else if(FishManager.isTopWeek(fish)){
                            UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.RED + " caught the biggest fish this week!" );
                        }else if(FishManager.isTopDay(fish)){
                            UtilMessage.broadcast("Fishing", ChatColor.YELLOW + player.getName() + ChatColor.RED + " caught the biggest fish today!" );
                        }
                    }else{
                        UtilMessage.message(event.getPlayer(), "Fishing", "You can only catch fish at the Lake!");
                    }
                }else{
                    UtilMessage.message(event.getPlayer(), "Fishing", "You can only catch fish at the Lake!");
                }


            }
        }else{
            event.setCancelled(true);
        }
    }

    private void setBiteTime(FishHook hook, int time) {
        net.minecraft.server.v1_8_R3.EntityFishingHook hookCopy = (EntityFishingHook) ((CraftEntity) hook).getHandle();

        Field fishCatchTime = null;

        try {
            fishCatchTime = net.minecraft.server.v1_8_R3.EntityFishingHook.class.getDeclaredField("ax");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }

        fishCatchTime.setAccessible(true);

        try {
            fishCatchTime.setInt(hookCopy, time);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        fishCatchTime.setAccessible(false);
    }
}