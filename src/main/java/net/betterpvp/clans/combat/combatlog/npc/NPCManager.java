package net.betterpvp.clans.combat.combatlog.npc;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.combat.combatlog.npc.events.PlayerInteractNPCEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Iterator;

public class NPCManager extends BPVPListener<Clans> {

    public NPCManager(Clans i) {
        super(i);
    }

    @EventHandler
    public void onPlayerDamageEntity(EntityDamageEvent event) {
        if (NPC.npcs.isEmpty()) return;
        for (NPC npc : NPC.npcs) {
            if (event.getEntity().equals(npc.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        if (NPC.npcs.isEmpty()) return;
        for (NPC npc : NPC.npcs) {
            if (event.getEntity() == npc.getEntity()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onNPCTarget(EntityTargetEvent event) {
        if (NPC.npcs.isEmpty()) return;
        for (NPC npc : NPC.npcs) {
            if (event.getEntity() == npc.getEntity()) {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onNPCInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.OFF_HAND) {
            if (NPC.npcs.isEmpty()) return;
            Player player = event.getPlayer();

            Iterator<NPC> iterator = NPC.npcs.iterator();
            while (iterator.hasNext()) {
                NPC npc = iterator.next();

                if (event.getRightClicked() == npc.getEntity()) {
                    event.setCancelled(true);
                    Bukkit.getServer().getPluginManager().callEvent(new PlayerInteractNPCEvent(player, npc));
                }
            }
        }


    }

    @EventHandler
    public void updateNPC(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SEC) {
            if (NPC.npcs.isEmpty()) return;
            Iterator<NPC> iterator = NPC.npcs.iterator();
            while (iterator.hasNext()) {
                NPC npc = iterator.next();
                if (npc.getLocation().getChunk().isLoaded()) {
                    npc.returnToPost();
                }

                if (npc.getEntity().isDead() || !npc.getEntity().isValid()) {
                    iterator.remove();
                }
            }
        }
    }
}