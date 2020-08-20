package net.betterpvp.clans.weapon.weapons;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.combat.throwables.Throwables;
import net.betterpvp.clans.combat.throwables.events.ThrowableHitGroundEvent;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.particles.ParticleEffect;
import net.betterpvp.core.utility.*;
import net.betterpvp.core.utility.recharge.RechargeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.Map.Entry;

public class EMPGrenade extends Weapon {

    private WeakHashMap<Chunk, Long> chunks = new WeakHashMap<>();
    private WeakHashMap<Chunk, List<Location>> redstoneLocs = new WeakHashMap<>();

    public EMPGrenade(Clans i) {
        super(i, Material.NETHER_STAR, (byte) 15, ChatColor.YELLOW + "EMP Grenade", new String[]{
                ChatColor.GRAY + "Left-Click: " + ChatColor.YELLOW + "Throw",
                ChatColor.GRAY + "  " + "Disable redstone in the target chunk for 60 seconds."}, false, 10.0);

    }


    @EventHandler
    public void onGrenadeUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        if (player.getInventory().getItemInMainHand() == null) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.NETHER_STAR) return;


        if (isThisWeapon(player)) {
            if (ClanUtilities.canCast(player)) {
                if (event.getAction() == Action.LEFT_CLICK_AIR) {
                    if (RechargeManager.getInstance().add(player, "EMP Grenade", 15, true)) {

                        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.NETHER_STAR));
                        UtilItem.remove(player, Material.NETHER_STAR, 1);
                        item.setPickupDelay(Integer.MAX_VALUE);
                        item.setVelocity(player.getLocation().getDirection().multiply(1.3));
                        Throwables throwables = new Throwables(item, player, "EMP Grenade", 60_000);
                        ThrowableManager.getThrowables().add(throwables);

                        Gamer gamer = GamerManager.getOnlineGamer(player);
                        if(gamer != null){
                            gamer.setStatValue(ChatColor.stripColor(getName()), gamer.getStatValue(ChatColor.stripColor(getName())) + 1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHitGround(ThrowableHitGroundEvent e) {
        if (e.getThrowable().getSkillName().equals("EMP Grenade")) {
            chunks.put(e.getThrowable().getItem().getLocation().getChunk(), System.currentTimeMillis() + 60_000);

            Clan clan = ClanUtilities.getClan(e.getThrowable().getItem().getLocation());
            if(clan != null){
                clan.messageClan(ChatColor.RED + "Chunk " + ChatColor.YELLOW + UtilLocation.chunkToString(e.getThrowable().getItem().getLocation().getChunk())
                        + ChatColor.RED + " was hit with an EMP grenade! Redstone is disabled for 60 seconds.", null, false);
            }

            if (redstoneLocs.containsKey(e.getThrowable().getItem().getLocation().getChunk())) {
                for (Location loc : redstoneLocs.get(e.getThrowable().getItem().getLocation().getChunk())) {
                    Block block = loc.getBlock();
                    BlockData data = block.getBlockData();
                    if (data instanceof AnaloguePowerable) {
                        AnaloguePowerable analoguePowerable = (AnaloguePowerable) data;
                        analoguePowerable.setPower(0);
                        block.setBlockData(analoguePowerable);
                    }
                }
            }
            e.getThrowable().getItem().remove();
            ThrowableManager.getThrowables().remove(e.getThrowable());
        }
    }

    @EventHandler
    public void onRedstone(BlockRedstoneEvent e) {

        if (chunks.containsKey(e.getBlock().getChunk())) {
            if (chunks.get(e.getBlock().getChunk()) - System.currentTimeMillis() > 0) {
                e.setNewCurrent(0);
            } else {
                chunks.remove(e.getBlock().getChunk());
            }
        }

        if (!redstoneLocs.containsKey(e.getBlock().getChunk())) {
            redstoneLocs.put(e.getBlock().getChunk(), new ArrayList<>());
        }


        if (!redstoneLocs.get(e.getBlock().getChunk()).contains(e.getBlock().getLocation())) {
            redstoneLocs.get(e.getBlock().getChunk()).add(e.getBlock().getLocation());
        }

    }
}
