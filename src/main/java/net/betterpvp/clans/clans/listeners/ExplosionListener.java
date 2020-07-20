package net.betterpvp.clans.clans.listeners;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.*;
import net.betterpvp.clans.clans.Clan.DataType;
import net.betterpvp.clans.clans.InsuranceType;
import net.betterpvp.clans.clans.insurance.Insurance;
import net.betterpvp.clans.clans.mysql.InsuranceRepository;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ListIterator;

public class ExplosionListener extends BPVPListener<Clans> {

    public ExplosionListener(Clans i) {
        super(i);
    }

    @EventHandler
    public void updateClanTNTProtection(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            ListIterator<Clan> it = ClanUtilities.getClans().listIterator();
            while (it.hasNext()) {
                Clan clan = it.next();
                for (ClanMember member : clan.getMembers()) {

                    if (Bukkit.getPlayer(member.getUUID()) != null) {
                        int bonus = 0;
                        if (clan.getMembers().size() > Clans.getOptions().getTntBonusMemberThreshold()) {
                            // 5 minutes per member
                            bonus = (clan.getMembers().size() - Clans.getOptions().getTntBonusMemberThreshold())
                                    * (Clans.getOptions().getBonusTimeUntilTNTProtection() * 60000);
                        }

                        clan.getData().put(DataType.PROTECTION, System.currentTimeMillis() +
                                (Clans.getOptions().getTimeUntilTNTProtection() * 60000) + bonus);
                        clan.setVulnerable(true);
                        break;
                    }
                }

                if (clan.getData().containsKey(DataType.PROTECTION)) {
                    Long time = (Long) clan.getData().get(DataType.PROTECTION);
                    if (time - System.currentTimeMillis() <= 0) {
                        clan.setVulnerable(false);
                        clan.getData().remove(DataType.PROTECTION);
                    }
                }
            }

        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Clan c = ClanUtilities.getClan(e.getPlayer());
        Clan d = ClanUtilities.getClan(e.getBlock().getLocation());
        if (c != null && d != null) {
            if (c == d) {
                if (System.currentTimeMillis() < c.getLastTnted()) {
                    UtilMessage.message(e.getPlayer(), "Clans", "You cannot place blocks for "
                            + ChatColor.GREEN + UtilTime.getTime(c.getLastTnted() - System.currentTimeMillis(), UtilTime.TimeUnit.BEST, 1));
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTNTExplode(EntityExplodeEvent e) {
        if (e.getEntity() != null && e.getEntity().getType() == EntityType.PRIMED_TNT) {
            e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 1.0f);

            boolean clear = false;
            for (Block block : e.blockList()) {
                Clan clan = ClanUtilities.getClan(block.getLocation());

                if (clan != null) {
                    if(clan instanceof AdminClan){
                        clear = true;
                        break;
                    }
                    if (!clan.isVulnerable()) {
                        if (!Clans.getOptions().isLastDay()) {
                            clear = true;

                        } else {
                            clan.setLastTnted(System.currentTimeMillis() + 300000);
                        }
                    } else {
                        clan.setLastTnted(System.currentTimeMillis() + 300000);


                        if (clan.isOnline()) {
                            clan.messageClan("YOUR TERRITORY IS UNDER ATTACK!", null, true);

                            for (ClanMember member : clan.getMembers()) {
                                if (Bukkit.getPlayer(member.getUUID()) != null) {
                                    Player d = Bukkit.getPlayer(member.getUUID());
                                    d.playSound(d.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0F, 1.0F);
                                    return;
                                }
                            }
                        }

                    }
                }

            }

            if (clear) {
                e.blockList().clear();
                e.setCancelled(true);
            }
        }
    }


    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
        if (e.getEntity() != null && e.getEntity().getType() == EntityType.PRIMED_TNT) {

            for (Block block : UtilBlock.getInRadius(e.getLocation().add(0, 1, 0), 6).keySet()) {
                if (block.getType() == Material.BEDROCK) {
                    continue;
                }

                if (block.getType() == Material.BEACON) {
                    continue;
                }

                if (block.getType() == Material.IRON_BLOCK) {
                    if (BlockRestoreData.isRestoredBlock(block)) {
                        continue;
                    }
                }


                Clan c = ClanUtilities.getClan(block.getLocation());
                if (c != null) {
                    if(c instanceof AdminClan){

                        continue;
                    }
                    if (block.getType() == Material.AIR) {
                        continue;
                    }
                    if (BlockRestoreData.isRestoredBlock(block)) {
                        continue;
                    }
                    if (!Clans.getOptions().isFNG()) {
                        Insurance i = new Insurance(block.getLocation(), block.getType(), block.getBlockData().getAsString(), InsuranceType.BREAK, System.currentTimeMillis());
                        InsuranceRepository.saveInsurance(c, i);
                        c.getInsurance().add(i);
                    }
                } else {
                    if (block.getType() == Material.DIRT || block.getType() == Material.GRASS_BLOCK || block.getType() == Material.STONE || block.getType() == Material.GRAVEL
                            || block.getType() == Material.SAND || block.getType() == Material.COBBLESTONE) {
                        //new BlockRestoreData(block, (int) block.getData(), (byte) 0, 60000 * 5L);
                    }
                }

                if (block.isLiquid()) {
                    Clans.getCoreProtect().logRemoval("TNT", block.getLocation(), block.getType(), block.getData());
                    block.setType(Material.AIR);

                }
                if (block.getType() == Material.ENCHANTING_TABLE) {
                    Clans.getCoreProtect().logRemoval("TNT", block.getLocation(), block.getType(), block.getData());
                    block.breakNaturally();


                }

                if (block.getType() == Material.TNT && block.getLocation().distance(e.getEntity().getLocation()) < 4) {
                    Clans.getCoreProtect().logRemoval("TNT", block.getLocation(), block.getType(), block.getData());
                    block.breakNaturally();
                }


            }

            for (Block x : UtilBlock.getInRadius(e.getLocation(), 4).keySet()) {


                if (x.getType() == Material.BEDROCK) {
                    continue;
                }


                if (x.getType() != Material.CHEST && x.getType() != Material.TRAPPED_CHEST && !x.getType().name().contains("DOOR")) {
                    if (!e.blockList().contains(x)) {
                        e.blockList().add(x);
                    }
                }
            }


            for (Block b : e.blockList()) {
                Clans.getCoreProtect().logRemoval("TNT", b.getLocation(), b.getType(), b.getData());
                Clan c = ClanUtilities.getClan(b.getLocation());
                if (c != null) {
                    if(c instanceof AdminClan){
                        continue;
                    }
                    if (!c.isVulnerable()) {
                        return;
                    }

                    Insurance i = new Insurance(b.getLocation(), b.getType(), b.getBlockData().getAsString(), InsuranceType.BREAK, System.currentTimeMillis());
                    if (i.getMaterial() != Material.AIR) {
                        if (BlockRestoreData.isRestoredBlock(b)) {
                            continue;
                        }
                        if (!Clans.getOptions().isFNG()) {
                            InsuranceRepository.saveInsurance(c, i);
                            c.getInsurance().add(i);
                        }
                    }
                } else {
                    if (b.getType() == Material.DIRT || b.getType() == Material.GRASS_BLOCK || b.getType() == Material.STONE || b.getType() == Material.GRAVEL
                            || b.getType() == Material.SAND || b.getType() == Material.COBBLESTONE) {
                        //	new BlockRestoreData(b, (int) b.getData(), (byte) 0, 60000 * 5L);
                    }
                }


                if (b.getType() == Material.BEACON) {
                    continue;
                }

                if (b.getType() == Material.IRON_BLOCK) {
                    if (BlockRestoreData.isRestoredBlock(b)) {
                        continue;
                    }
                }


                boolean broken = false;
                for (TNTBlocks tntBlock : TNTBlocks.values()) {
                    if (b.getType() == tntBlock.normal) {
                        b.setType(tntBlock.damaged);
                        broken = true;
                        break;
                    } else if (b.getType() == tntBlock.damaged) {
                        b.breakNaturally();
                        broken = true;
                        break;
                    }
                }

                if(broken){
                    continue;
                }

                if (b.getType() == Material.DARK_PRISMARINE) {
                    b.setType(Material.PRISMARINE_BRICKS);
                    continue;
                } else if (b.getType() == Material.PRISMARINE_BRICKS) {
                    b.setType(Material.PRISMARINE);
                    continue;
                } else if (b.getType() == Material.PRISMARINE) {
                    b.breakNaturally();
                    continue;
                }

                if (b.getType() == Material.TRAPPED_CHEST || b.getType() == Material.CHEST
                        || b.getType() == Material.ENDER_CHEST
                        || b.getType() == Material.ANVIL) {
                    b.breakNaturally();

                } else if (b.isLiquid()) {
                    b.setType(Material.AIR);
                }

                b.breakNaturally();

            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void ExplosionBlocks(EntityExplodeEvent e) {
        if (e.getEntity() == null) {
            e.blockList().clear();
        }
    }

    private enum TNTBlocks {

        STONEBRICK(Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS),
        NETHERBRICKS(Material.NETHER_BRICKS, Material.NETHERRACK),
        SANDSTONE(Material.SMOOTH_SANDSTONE, Material.SANDSTONE),
        REDSANDSTONE(Material.SMOOTH_RED_SANDSTONE, Material.RED_SANDSTONE),
        BLACKSTONE(Material.POLISHED_BLACKSTONE_BRICKS, Material.CRACKED_POLISHED_BLACKSTONE_BRICKS),
        QUARTZ(Material.QUARTZ_BRICKS, Material.CHISELED_QUARTZ_BLOCK),
        PURPUR(Material.PURPUR_BLOCK, Material.PURPUR_PILLAR);





        private Material normal, damaged;

        TNTBlocks(Material normal, Material damaged) {
            this.normal = normal;
            this.damaged = damaged;
        }

        public Material getNormal() {
            return normal;

        }

        public Material getDamaged() {
            return damaged;
        }
        }

}
