package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanMember.Role;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.ClanUtilities.ClanRelation;
import net.betterpvp.clans.clans.Pillage;
import net.betterpvp.clans.farming.FarmBlocks;
import net.betterpvp.clans.koth.KOTHManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.punish.PunishManager;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.PacketPlayOutAnimation;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;


public class InteractListener extends BPVPListener<Clans> {

    public InteractListener(Clans i) {
        super(i);
    }

    /**
     * Prevent players from placing redstone on the outer edges of peoples base that arent in a claim
     * @param e
     */
    @EventHandler
    public void onPlaceRedstoneStuff(BlockPlaceEvent e) {
        Clan c = ClanUtilities.getClan(e.getBlock().getLocation());
        if (c == null) {
            if (e.getBlock().getType().name().contains("_BUTTON") && e.getBlock().getType() != Material.STONE_BUTTON) {
                e.getBlock().setType(Material.STONE_BUTTON);
            } else if (e.getBlock().getType().name().contains("PRESSURE_PLATE") && e.getBlock().getType() != Material.STONE_PRESSURE_PLATE) {
                e.getBlock().setType(Material.STONE_PRESSURE_PLATE);
            } else if (e.getBlock().getType() == Material.REDSTONE_WIRE || e.getBlock().getType() == Material.REPEATER
            || e.getBlock().getType().name().contains("REDSTONE") || e.getBlock().getType() == Material.OBSERVER
            || e.getBlock().getType().name().contains("BUTTON") || e.getBlock().getType() == Material.DAYLIGHT_DETECTOR
            || e.getBlock().getType() == Material.TARGET) {
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        Clan d = ClanUtilities.getClan(e.getBlock().getLocation().add(x, 0, z));
                        if (d != null) {

                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * Prevent players from placing redstone on the outer edges of peoples base that arent in a claim
     * @param e
     */
    @EventHandler
    public void onOuterRedstone(PlayerInteractEvent e) {
        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() == Action.PHYSICAL) {


            if (e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE || e.getClickedBlock().getType() == Material.OAK_PRESSURE_PLATE) {
                Clan c = ClanUtilities.getClan(e.getClickedBlock().getLocation());
                if (c == null) {

                    for (int x = -1; x <= 1; x++) {
                        for (int z = -1; z <= 1; z++) {
                            Clan d = ClanUtilities.getClan(e.getClickedBlock().getLocation().add(x, 0, z));
                            if (d != null) {

                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        } else {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (e.getClickedBlock() != null) {
                    if (e.getClickedBlock().getType().name().contains("_BUTTON")
                            || e.getClickedBlock().getType() == Material.LEVER) {
                        Clan c = ClanUtilities.getClan(e.getClickedBlock().getLocation());
                        if (c == null) {
                            for (int x = -1; x <= 1; x++) {
                                for (int z = -1; z <= 1; z++) {
                                    Clan d = ClanUtilities.getClan(e.getClickedBlock().getLocation().add(x, 0, z));
                                    if (d != null) {
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player p = e.getPlayer();
        Clan clan = ClanUtilities.getClan(p);

        if (UtilBlock.isTutorial(block.getLocation())) {
            return;
        }

        Clan bClan = ClanUtilities.getClan(block.getLocation());
        if (bClan != null) {
            if (!bClan.equals(clan)) {


                if (bClan instanceof AdminClan) {
                    if (bClan.getName().contains("Fields")) {
                        return;
                    }
                }

                if (ClientUtilities.getOnlineClient(p).isAdministrating()) {
                    return;
                }


                if (!(bClan instanceof AdminClan)) {
                    if (FarmBlocks.isCultivation(block.getType())) {
                        return;
                    }
                }


                if (Pillage.isPillaging(clan, bClan)) {
                    return;
                }


                UtilMessage.message(p, "Clans", "You cannot break " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().toString())
                        + ChatColor.GRAY + " in " + ChatColor.YELLOW + ClanUtilities.getRelation(clan, bClan).getPrimary()
                        + "Clan " + bClan.getName() + ChatColor.GRAY + ".");
                e.setCancelled(true);


            } else if (bClan == clan) {
                if (clan.getMember(p.getUniqueId()).getRole() == Role.RECRUIT) {
                    UtilMessage.message(p, "Clans", "Clan Recruits cannot break blocks" + ChatColor.GRAY + ".");
                    e.setCancelled(true);

                }
            }
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {

        if (e.isCancelled()) {
            return;
        }


        Block block = e.getBlock();
        Player p = e.getPlayer();
        Clan clan = ClanUtilities.getClan(p);
        Clan bClan = ClanUtilities.getClan(block.getLocation());

        if (ClientUtilities.getOnlineClient(p).isAdministrating()) {
            return;
        }


        if (UtilBlock.isTutorial(block.getLocation())) {
            return;
        }

        if (bClan != null) {


            if (block.getType() == Material.SAND || block.getType() == Material.GRAVEL
            || block.getType().name().contains("POWDER")) {
                UtilMessage.message(p, "Clans", "You cannot place " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().toString())
                        + ChatColor.GRAY + " in " + ChatColor.YELLOW + ClanUtilities.getRelation(clan, bClan).getPrimary()
                        + "Clan " + bClan.getName() + ChatColor.GRAY + ".");
                e.setCancelled(true);
            }
            if (bClan != clan) {

                if (Pillage.isPillaging(clan, bClan)) {
                    return;
                }


                UtilMessage.message(p, "Clans", "You cannot place " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().toString())
                        + ChatColor.GRAY + " in " + ChatColor.YELLOW + ClanUtilities.getRelation(clan, bClan).getPrimary()
                        + "Clan " + bClan.getName() + ChatColor.GRAY + ".");
                e.setCancelled(true);
            } else if (bClan == ClanUtilities.getClan(p)) {
                if (clan.getMember(p.getUniqueId()).getRole() == Role.RECRUIT) {
                    UtilMessage.message(p, "Clans", "Clan Recruits cannot break blocks" + ChatColor.GRAY + ".");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        Player p = e.getPlayer();
        Clan clan = ClanUtilities.getClan(p);

        Block block = e.getClickedBlock();


        if (block != null) {
            if (UtilBlock.isTutorial(block.getLocation())) {
                return;
            }

            Clan bClan = ClanUtilities.getClan(block.getLocation());
            if (bClan != null) {
                if (bClan != clan) {

                    if (bClan instanceof AdminClan && block.getType() == Material.ENCHANTING_TABLE) {
                        return;
                    }

                    if (ClanUtilities.getRelation(clan, bClan) == ClanRelation.ALLY_TRUST && ( block.getType() == Material.IRON_DOOR
                            || block.getType() == Material.IRON_TRAPDOOR
                            || block.getType().name().contains("GATE")
                            || block.getType().name().contains("DOOR")
                            || block.getType().name().contains("_BUTTON")
                            || block.getType() == Material.LEVER)) {
                        return;

                    }

                    if (Pillage.isPillaging(clan, bClan)) {
                        return;
                    }

                    if (ClientUtilities.getOnlineClient(p).isAdministrating()) {
                        return;
                    }

                    if (block.getType() == Material.REDSTONE_ORE) {
                        return;
                    }

                    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.LEVER
                            || block.getType().name().contains("_BUTTON") || block.getType() == Material.FURNACE
                            || block.getType() == Material.OAK_FENCE_GATE || block.getType() == Material.CRAFTING_TABLE || UtilBlock.usable(block)) {

                        if(e.getAction() == Action.LEFT_CLICK_BLOCK){
                            if(block.getType() == Material.ENDER_CHEST){
                                return;
                            }
                        }

						if(KOTHManager.koth != null){
							if(KOTHManager.koth.getLocation().getBlockX() == block.getLocation().getBlockX() 
									&& KOTHManager.koth.getLocation().getBlockZ()  == block.getLocation().getBlockZ()){
								return;
							}
						}


                        UtilMessage.message(p, "Clans", "You cannot use " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().toString())
                                + ChatColor.GRAY + " in " + ChatColor.YELLOW + ClanUtilities.getRelation(clan, bClan).getPrimary()
                                + "Clan " + bClan.getName() + ChatColor.GRAY + ".");
                        e.setCancelled(true);
                    }
                } else if (clan.getMember(p.getUniqueId()).getRole() == Role.RECRUIT) {
                    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
                        UtilMessage.message(p, "Clans", "Clan Recruits cannot access " + ChatColor.GREEN + UtilFormat.cleanString(block.getType().toString())
                                + ChatColor.GRAY + ".");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onDoorInteract(PlayerInteractEvent e) {

        if(e.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (ClanUtilities.hasAccess(e.getPlayer(), e.getClickedBlock().getLocation())
                    || ClientUtilities.getOnlineClient(e.getPlayer()).isAdministrating()) {
                Block block = e.getClickedBlock();

                if (block.getType() == Material.IRON_DOOR || block.getType() == Material.LEGACY_IRON_DOOR_BLOCK) {

                    BlockData doorData = block.getBlockData();
                    Openable door = (Openable) doorData;

                    if(door.isOpen()){
                        door.setOpen(false);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1f, 1f);
                    }else{
                        door.setOpen(true);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1f, 1f);
                    }

                    block.setBlockData(doorData);
                    block.getState().update();


                    EntityPlayer ep = ((CraftPlayer) e.getPlayer()).getHandle();
                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                    e.setCancelled(true);
                } else if (block.getType() == Material.IRON_TRAPDOOR) {


                    BlockData doorData = block.getBlockData();
                    Openable door = (Openable) doorData;

                    if(door.isOpen()){
                        door.setOpen(false);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1f, 1f);
                    }else{
                        door.setOpen(true);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 1f, 1f);
                    }

                    block.setBlockData(doorData);
                    block.getState().update(true);
                    block.getState().update();

                    //Bukkit.broadcastMessage(block.getData() + "");
                    EntityPlayer ep = ((CraftPlayer) e.getPlayer()).getHandle();
                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                    ((CraftPlayer) e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
                    e.setCancelled(true);

                }
            }
        }
    }


    @EventHandler
    public static void doorKnock(PlayerInteractEvent e) {

        if(e.getHand() == EquipmentSlot.OFF_HAND) return;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.getClickedBlock().getType() == Material.IRON_DOOR
                    || e.getClickedBlock().getType() == Material.LEGACY_IRON_DOOR_BLOCK
                    || e.getClickedBlock().getType().name().contains("TRA_DOOR")) {

                Block block = e.getClickedBlock();
                if (!ClanUtilities.hasAccess(e.getPlayer(), block.getLocation())) {
                    if (RechargeManager.getInstance().add(e.getPlayer(), "Door Knock", 0.25, false)) {
                        if (PunishManager.isPvPLocked(e.getPlayer().getUniqueId())) return;
                        block.getWorld().playEffect(block.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0);
                    }
                }
            }

        }
    }

}

