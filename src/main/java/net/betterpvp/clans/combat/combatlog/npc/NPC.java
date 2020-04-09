package net.betterpvp.clans.combat.combatlog.npc;

import net.minecraft.server.v1_15_R1.EntityAgeable;
import net.minecraft.server.v1_15_R1.EntityCreature;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftAgeable;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftLivingEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NPC {

    public static List<NPC> npcs = new ArrayList<NPC>();

    private String name;
    private Location location;
    private EntityType entityType;
    private LivingEntity entity;
    private double range;
    private ArmorStand stand;

    public NPC(String name, Location location, EntityType entityType, double range, boolean visable) {
        if (!location.getChunk().isLoaded()) {
            location.getChunk().load();
        }

        this.name = name;
        this.location = location;
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);

        this.entityType = entityType;
        this.entity = entity;
        this.range = range;
        entity.setCustomNameVisible(false);
        entity.setFireTicks(0);
        entity.setCanPickupItems(false);
        entity.setRemoveWhenFarAway(false);
        entity.setCustomName(name);
        entity.setCustomNameVisible(true);

        if ((((CraftLivingEntity) entity).getHandle() instanceof EntityAgeable)) {
            ((CraftAgeable) entity).getHandle().ageLocked = true;
        }

        npcs.add(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public double getRange() {
        return this.range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
    }

    public ArmorStand getArmorStand() {
        return stand;
    }

    public void setArmorStand(ArmorStand stand) {
        this.stand = stand;
    }

    public void remove() {
        getEntity().setHealth(0);
        getArmorStand().setHealth(0);
    }

    public boolean inRadius() {
        Location entityLocation = getEntity().getLocation();
        return Math.abs(entityLocation.getBlockX() - getLocation().getBlockX()) + Math.abs(entityLocation.getBlockY() - getLocation().getBlockY())
                + Math.abs(entityLocation.getBlockZ() - getLocation().getBlockZ()) <= getRange();
    }

    public void returnToPost() {
        if (!inRadius()) {
            getEntity().teleport(getLocation());
        }

        EntityCreature ec = ((CraftCreature) getEntity()).getHandle();
        ec.getNavigation().a(getLocation().getX(), getLocation().getY(), getLocation().getZ(), 0.8D);
    }

    public static NPC getNPC(String name) {
        if (!npcs.isEmpty()) {
            for (NPC npc : npcs) {
                if (npc.getName().equalsIgnoreCase(name)) {
                    return npc;
                }
            }
        }
        return null;
    }

    public static void removeAllNPCs() {
        Iterator<NPC> iterator = npcs.iterator();
        while (iterator.hasNext()) {
            NPC npc = iterator.next();
            if (!npc.getEntity().isDead()) {
                npc.getEntity().remove();
                iterator.remove();
            }
        }
    }
}