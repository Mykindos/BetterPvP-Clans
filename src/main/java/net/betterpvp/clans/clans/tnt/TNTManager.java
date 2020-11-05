package net.betterpvp.clans.clans.tnt;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class TNTManager extends BPVPListener<Clans> {

    private static WeakHashMap<Block, Clan> tntMap = new WeakHashMap<>();
    private static WeakHashMap<Entity, Clan> primedTntMap = new WeakHashMap<>();

    public TNTManager(Clans instance) {
        super(instance);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(instance, ListenerPriority.HIGHEST, PacketType.Play.Server.SPAWN_ENTITY) {
            @Override
            public void onPacketSending(PacketEvent event) {

                try {
                    // TNT spawn packet
                    if (event.getPacketType() != PacketType.Play.Server.SPAWN_ENTITY) return;

                    Player player = event.getPlayer();
                    Entity entity = event.getPacket().getEntityModifier(player.getWorld()).read(0);

                    if (!(entity instanceof TNTPrimed))
                        return;

                    Optional<Map.Entry<Block, Clan>> tntOptional = getTntMap().entrySet().stream().filter(t -> {
                        Location entLoc = entity.getLocation();
                        Location blockLoc = t.getKey().getLocation();
                        return entLoc.getBlockX() == blockLoc.getBlockX() && entLoc.getBlockY() == blockLoc.getBlockY()
                                && entLoc.getBlockZ() == blockLoc.getBlockZ();
                    }).findFirst();

                    if (tntOptional.isPresent()) {
                        Map.Entry<Block, Clan> tnt = tntOptional.get();
                        getPrimedTntMap().put(entity, tnt.getValue());
                    }
                } catch (Exception ex) {

                }
            }
        });
    }

    public static WeakHashMap<Block, Clan> getTntMap() {
        return tntMap;
    }

    public static WeakHashMap<Entity, Clan> getPrimedTntMap() {
        return primedTntMap;
    }

    @EventHandler
    public void onPlaceTnt(BlockPlaceEvent e) {
        if (e.getBlock().getType() != Material.TNT) return;
        if(Clans.getOptions().isLastDay()){
            return;
        }
        Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());
        if (gamer != null) {
            Clan clan = ClanUtilities.getClan(e.getPlayer());
            if (clan == null && !gamer.getClient().isAdministrating()) {
                UtilMessage.message(e.getPlayer(), "Clans", "You must be in a clan to place TNT.");
                e.setCancelled(true);
            }

            if (clan != null) {
                getTntMap().put(e.getBlock(), clan);
            }

        }
    }

    @EventHandler
    public void onRemoveTnt(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SLOW) {
            getTntMap().entrySet().removeIf(m -> m.getKey().getWorld().getBlockAt(m.getKey().getLocation()).getType() != Material.TNT);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExplode(EntityExplodeEvent e) {
        if(Clans.getOptions().isLastDay()){
            return;
        }
        if (getPrimedTntMap().containsKey(e.getEntity())) {
            Clan attacker = getPrimedTntMap().get(e.getEntity());

            boolean success = true;
            for (Block block : e.blockList()) {
                Clan target = ClanUtilities.getClan(block.getLocation());
                if (target != null) {
                    if (!attacker.isEnemy(target)) {
                        success = false;
                        continue;
                    }

                    if (target.getDominance(attacker).getPoints() < Clans.getOptions().getDominanceBeforeTnt()) {
                        success = false;
                        continue;
                    }
                }
            }

            if (!success) {
                e.setCancelled(true);
                attacker.messageClan("Your clan must be " + ChatColor.GREEN + "+"
                        + Clans.getOptions().getDominanceBeforeTnt() + ChatColor.GRAY + " dominance before using TNT.", null, true);
            }
        }
    }

}
