package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.commands.ClanCommand;
import net.betterpvp.clans.clans.commands.IClanCommand;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.clans.menus.ClanMenu;
import net.betterpvp.clans.clans.menus.ClanShopMenu;
import net.betterpvp.clans.clans.menus.buttons.*;
import net.betterpvp.clans.clans.menus.buttons.energybuttons.Buy1KEnergy;
import net.betterpvp.clans.clans.menus.buttons.energybuttons.BuyOneDayEnergy;
import net.betterpvp.clans.clans.menus.buttons.energybuttons.BuyOneHourEnergy;
import net.betterpvp.clans.clans.mysql.ClanRepository;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.interfaces.events.ButtonClickEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;

public class ClanMenuListener extends BPVPListener<Clans> {

    public ClanMenuListener(Clans instance) {
        super(instance);

    }

    @EventHandler
    public void onEnergyMenuClick(ButtonClickEvent e) {
        if (e.getMenu() instanceof ClanMenu) {

            if (e.getButton() instanceof EnergyMenuButton) {

                e.getPlayer().openInventory(new ClanShopMenu(e.getPlayer()).getInventory());
            }


        }
    }

    @EventHandler
    public void onEnergyBuy(ButtonClickEvent e) {
        if (e.getMenu() instanceof ClanShopMenu) {
            int cost = 0;
            int energy = 0;
            Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());

            Clan c = ClanUtilities.getClan(e.getPlayer());
            if (c.getEnergy() >= c.getTerritory().size() * 5000) {
                UtilMessage.message(e.getPlayer(), "Clans", "You cannot purchase any more energy. (5000 per claim)");
                return;
            }

            if (e.getButton() instanceof Buy1KEnergy) {

                Buy1KEnergy buy1kEnergy = (Buy1KEnergy) e.getButton();
                cost = 1000 * Clans.getOptions().getCostPerEnergy();
                if (gamer.hasCoins(cost)) {
                    gamer.removeCoins(cost);
                    energy = 1000;
                    buy1kEnergy.getClan().setEnergy(buy1kEnergy.getClan().getEnergy() + energy);
                    UtilMessage.message(e.getPlayer(), "Clans", "You purchased " + ChatColor.GREEN + energy
                            + ChatColor.GRAY + " energy for " + ChatColor.GREEN + "$" + cost);
                    ClanRepository.updateEnergy(buy1kEnergy.getClan());
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 2.0F);
                    return;
                }
            } else if (e.getButton() instanceof BuyOneDayEnergy) {
                BuyOneDayEnergy buyOneDayEnergy = (BuyOneDayEnergy) e.getButton();
                cost = (((buyOneDayEnergy.getClan().getTerritory().size() * 25)) * Clans.getOptions().getCostPerEnergy() * 24);
                if (gamer.hasCoins(cost)) {
                    gamer.removeCoins(cost);
                    energy = ((buyOneDayEnergy.getClan().getTerritory().size() * 25) * 24);
                    buyOneDayEnergy.getClan().setEnergy(buyOneDayEnergy.getClan().getEnergy()
                            + energy);
                    ClanRepository.updateEnergy(buyOneDayEnergy.getClan());
                    UtilMessage.message(e.getPlayer(), "Clans", "You purchased " + ChatColor.GREEN + energy
                            + ChatColor.GRAY + " energy for " + ChatColor.GREEN + "$" + cost);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 2.0F);
                    return;
                }
            } else if (e.getButton() instanceof BuyOneHourEnergy) {
                BuyOneHourEnergy buyOneHourEnergy = (BuyOneHourEnergy) e.getButton();
                cost = ((buyOneHourEnergy.getClan().getTerritory().size() * 25) * Clans.getOptions().getCostPerEnergy());
                if (gamer.hasCoins(cost)) {
                    gamer.removeCoins(cost);
                    energy = ((buyOneHourEnergy.getClan().getTerritory().size() * 25));
                    buyOneHourEnergy.getClan().setEnergy(buyOneHourEnergy.getClan().getEnergy()
                            + energy);
                    ClanRepository.updateEnergy(buyOneHourEnergy.getClan());
                    UtilMessage.message(e.getPlayer(), "Clans", "You purchased " + ChatColor.GREEN + energy
                            + ChatColor.GRAY + " energy for " + ChatColor.GREEN + "$" + cost);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 2.0F);
                    return;
                }
            }else if(e.getButton() instanceof BuyTNTProtection){
                BuyTNTProtection buyTNTProtection = (BuyTNTProtection) e.getButton();

                if(!buyTNTProtection.getClan().isInstantTntProtection()) {
                    if (gamer.hasCoins(250_000)) {
                        gamer.removeCoins(250_000);

                        UtilMessage.message(e.getPlayer(), "Clans", "The next time everybody in your clan logs out, you will have instant TNT protection.");
                        buyTNTProtection.getClan().setInstantTntProtection(true);
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 2.0F);
                    }
                }else{
                    UtilMessage.message(e.getPlayer(), "Clans", "Your clan has already purchased this!");
                }
            }

            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.6F);

            Clan clan = ClanUtilities.getClan(e.getPlayer());
            if (clan != null) {
                for (ClanMember m : clan.getMembers()) {
                    Player mP = Bukkit.getPlayer(m.getUUID());
                    if (mP != null) {
                        Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(mP));
                    }
                }
            }

        }
    }

    @EventHandler
    public void onMemberButton(ButtonClickEvent e) {
        if (e.getMenu() instanceof ClanMenu) {

            ClanMenu clanMenu = (ClanMenu) e.getMenu();
            if (e.getButton() instanceof MemberButton) {
                MemberButton memberButton = (MemberButton) e.getButton();

                if (e.getClickType() == ClickType.SHIFT_LEFT) {
                    IClanCommand com = ClanCommand.getCommand("Promote");
                    if (com != null) {
                        com.run(e.getPlayer(), new String[]{"", ChatColor.stripColor(memberButton.getName())});
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
                        clanMenu.fillPage();
                        clanMenu.construct();
                    }


                } else if (e.getClickType() == ClickType.RIGHT) {

                    IClanCommand com = ClanCommand.getCommand("Demote");
                    if (com != null) {
                        com.run(e.getPlayer(), new String[]{"", ChatColor.stripColor(memberButton.getName())});
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
                        clanMenu.fillPage();
                        clanMenu.construct();
                    }
                } else if (e.getClickType() == ClickType.SHIFT_RIGHT) {
                    IClanCommand com = ClanCommand.getCommand("Kick");
                    if (com != null) {
                        com.run(e.getPlayer(), new String[]{"", ChatColor.stripColor(memberButton.getName())});
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
                        clanMenu.fillPage();
                        clanMenu.construct();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeaveButton(ButtonClickEvent e) {
        if (e.getMenu() instanceof ClanMenu) {
            if (e.getButton() instanceof LeaveButton) {
                if (e.getClickType() == ClickType.SHIFT_LEFT) {
                    IClanCommand com = ClanCommand.getCommand("Leave");
                    if (com != null) {
                        com.run(e.getPlayer(), new String[]{""});
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1f, 1f);
                        e.getPlayer().closeInventory();
                    }

                } else if (e.getClickType() == ClickType.SHIFT_RIGHT) {
                    IClanCommand com = ClanCommand.getCommand("Disband");
                    if (com != null) {
                        com.run(e.getPlayer(), new String[]{""});
                        e.getPlayer().closeInventory();
                    }


                }
            }
        }
    }

    @EventHandler
    public void onClaimButton(ButtonClickEvent e) {
        if (e.getMenu() instanceof ClanMenu) {

            ClanMenu clanMenu = (ClanMenu) e.getMenu();
            if (e.getButton() instanceof ClaimButton) {
                ClaimButton claimButton = (ClaimButton) e.getButton();
                ClanMember member = claimButton.getClan().getMember(e.getPlayer().getUniqueId());
                if (member != null) {
                    if (member.hasRole(Role.ADMIN)) {
                        if (e.getClickType() == ClickType.SHIFT_LEFT) {
                            IClanCommand com = ClanCommand.getCommand("Claim");
                            if (com != null) {
                                com.run(e.getPlayer(), new String[]{""});
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 2.0F);
                                clanMenu.fillPage();
                                clanMenu.construct();
                            }


                        } else if (e.getClickType() == ClickType.SHIFT_RIGHT) {
                            IClanCommand com = ClanCommand.getCommand("Unclaim");
                            if (com != null) {
                                com.run(e.getPlayer(), new String[]{""});
                                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0F, 2.0F);
                                clanMenu.fillPage();
                                clanMenu.construct();
                            }
                        }

                    }

                }
            }
        }
    }

}
