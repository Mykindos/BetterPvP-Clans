package net.betterpvp.clans.classes;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.classes.events.CustomKnockbackEvent;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;

import net.betterpvp.clans.utilities.UtilGamer;
import net.betterpvp.clans.weapon.ILegendary;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.framework.UpdateEvent.UpdateType;
import net.betterpvp.core.utility.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class DamageManager extends BPVPListener<Clans> {

    public DamageManager(Clans i) {
        super(i);

    }

    private static List<DamageData> delays = new ArrayList<>();


    @EventHandler(priority = EventPriority.HIGHEST)
    public void startCustomDamageEvent(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof LivingEntity)) {
            return;
        }

        if ((e instanceof EntityDamageByEntityEvent)) {
            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
            if (ev.getDamager() instanceof FishHook) {
                FishHook fh = (FishHook) ev.getDamager();
                if (fh.getShooter() instanceof Player) {
                    return;
                }

            }
        }


        if (hasDamageData(e.getEntity().getUniqueId().toString(), e.getCause())) {
            e.setCancelled(true);

        }

        if (e.getCause() == DamageCause.LIGHTNING) {
            e.setCancelled(true);
        }

        if (e.getCause() == DamageCause.WITHER) {
            e.setCancelled(true);
        }


        Clan c = ClanUtilities.getClan(e.getEntity().getLocation());
        if (c != null) {
            if (c instanceof AdminClan) {
                AdminClan adminClan = (AdminClan) c;
                if (adminClan.isSafe()) {
                    if (e.getEntity() instanceof Player) {
                        if (UtilTime.elapsed(GamerManager.getOnlineGamer((Player) e.getEntity()).getLastDamaged(), 15000)) {
                            e.setCancelled(true);
                        }
                    } else {
                        e.setCancelled(true);
                    }

                }
            }
        }

        if (ShopManager.isShop((LivingEntity) e.getEntity())) {
            e.setCancelled(true);
        }


        if (e.getEntity() instanceof ArmorStand || e.getEntity() instanceof ItemFrame) {
            if (e.getEntity().getWorld() == Bukkit.getWorld("tutorial")) {
                if ((e instanceof EntityDamageByEntityEvent)) {
                    EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;
                    if (ev.getDamager() instanceof Player) {
                        if (ClientUtilities.getOnlineClient((Player) ev.getDamager()).isAdministrating()) {
                            return;
                        }
                    }
                }

                e.setCancelled(true);
            }
            return;
        }

        if (e.isCancelled()) {
            return;
        }

        LivingEntity damagee = getDamageeEntity(e);

        LivingEntity damager = getDamagerEntity(e);
        Projectile proj = getProjectile(e);


        CustomDamageEvent cde = new CustomDamageEvent(damagee, damager, proj, e.getCause(), e.getDamage(), true);
        Bukkit.getPluginManager().callEvent(cde);
        e.setCancelled(true);


    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void handleCauseTimers(CustomDamageEvent e) {

        if (e.getCause() == DamageCause.ENTITY_ATTACK || e.getCause() == DamageCause.PROJECTILE || e.getCause() == DamageCause.CUSTOM) {
            e.setDamageDelay(400);
        }

        if (e.getCause() == DamageCause.POISON) {
            e.setDamageDelay(1000);
        }

        if (e.getCause() == DamageCause.LAVA) {
            e.setDamageDelay(400);
        }

        if (e.getDamagee().getLocation().getBlock().isLiquid()) {
            if (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK) {
                e.setCancelled("Already in lava / liquid");
            }
        }


    }


    @EventHandler
    public void ignoreArmours(CustomDamageEvent e) {
        if (e.getCause() == DamageCause.DROWNING) {
            e.setIgnoreArmour(true);
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        String ip = e.getRealAddress().getHostAddress();
        String pport = e.getHostname();

        System.out.println(ip + " and " + pport);
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void damageEvent(CustomDamageEvent e) {

        if(e.getDamagee() instanceof ArmorStand){
            return;
        }

        if (e.isCancelled()) return;
        if (!(e.getDamager() instanceof Player) && e.getDamagee() instanceof Player) {
            Gamer gamer = GamerManager.getOnlineGamer((Player) e.getDamagee());
            if (gamer != null) {
                gamer.setLastDamaged(System.currentTimeMillis());
            }
        }
        if (e.getDamagee() instanceof Player && e.getDamager() instanceof Player) {
            if (((Player) e.getDamagee()).getGameMode() == GameMode.CREATIVE) {
                return;
            }
            if (ClanUtilities.canHurt((Player) e.getDamager(), (Player) e.getDamagee())) {
                Gamer gamer = GamerManager.getOnlineGamer((Player) e.getDamager());
                if (gamer != null) {
                    gamer.setLastDamaged(System.currentTimeMillis());

                }


                Gamer xGamer = GamerManager.getOnlineGamer((Player) e.getDamagee());
                if (xGamer != null) {
                    xGamer.setLastDamaged(System.currentTimeMillis());
                }


            } else {
                return;
            }


        }


        damage(e);
    }

    @EventHandler
    public void onArmorStand(EntityDamageEvent e) {
        if (e.getEntity() instanceof ArmorStand) {
            if (e.getEntity().getWorld().getName().equals("tutorial")) {
                if (e instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent ed = (EntityDamageByEntityEvent) e;
                    if (ed.getDamager() instanceof Player) {
                        if (!ClientUtilities.getOnlineClient((Player) ed.getDamager()).isAdministrating()) {
                            e.setCancelled(true);
                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }


    private void damage(CustomDamageEvent e) {
        if (e.getDamagee() != null) {


            if (hasDamageData(e.getDamagee().getUniqueId().toString(), e.getCause())) {
                return;

            }

            if (e.getCause() == DamageCause.ENTITY_ATTACK) {
                if(e.getDamager() != null) {
                    if (e.getDamager().getHealth() <= 0) {
                        return;
                    }
                }
            }

            if (e.getDamagee().getHealth() > 0) {
                if (e.getDamage() >= 0) {

                    delays.add(new DamageData(e.getDamagee().getUniqueId().toString(), e.getCause(), e.getDamageDelay()));

                    if (e.getKnockback()) {
                        if (e.getDamager() != null) {
                            CustomKnockbackEvent cke = new CustomKnockbackEvent(e.getDamagee(), e.getDamager(), e.getDamage(), e);
                            Bukkit.getPluginManager().callEvent(cke);
                        }
                    }

                    double damage = e.isIgnoreArmour() ? e.getDamage() : UtilGamer.getDamageReduced(e.getDamage(), e.getDamagee());
                    if (e.getCause() == DamageCause.POISON) {
                        if (e.getDamagee().getHealth() < 2) {
                            return;
                        }
                    }

                    playDamageEffect(e);
                    updateDurability(e);

                    if (!e.getDamagee().isDead()) {


                        if (e.getDamagee().getHealth() - damage < 1.0) {
                            if (Clans.getOptions().isFNG()) {
                                return;
                            }


                            e.getDamagee().setHealth(0);
                            // Fixed in spigot version
                            if (e.getDamagee().isDead()) {
                                if (!(e.getDamagee() instanceof Player)) {

                                    /*
                                     * EntityDeathEvent not called if entity is killed with setHealth(0);
                                     */
                                    List<ItemStack> drops = new ArrayList<>();
                                    Bukkit.getPluginManager().callEvent(new EntityDeathEvent(e.getDamagee(), drops));

                                }
                                return;
                            }

                        } else {

                            e.getDamagee().setHealth(e.getDamagee().getHealth() - damage);
                        }


                    }

                }

                if (e.getDamagee() instanceof Player) {
                    Player p = (Player) e.getDamagee();
                    if (p.getInventory().getItemInMainHand().getType() == Material.BOOK) {
                        p.sendMessage("");
                        p.sendMessage("Damage: " + e.getDamage());
                        p.sendMessage("Damage Reduced: " + UtilGamer.getDamageReduced(e.getDamage(), e.getDamagee()));
                        p.sendMessage("Delay: " + e.getDamageDelay());
                        p.sendMessage("Cause: " + e.getCause().name());

                    }
                }
            }
        }


    }

    @EventHandler
    public void onKB(CustomKnockbackEvent e) {
        double knockback = e.getDamage();
        if (e.getDamager() instanceof Player) {
            Player player = (Player) e.getDamager();
            if (player.isSprinting()) {
                if (e.d.getCause() == DamageCause.ENTITY_ATTACK) {
                    knockback += 3;
                }
            }
        }
        if (knockback < 2.0D) knockback = 2.0D;
        knockback = Math.log10(knockback);

        e.setDamage(knockback);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFinalKB(CustomKnockbackEvent e) {
        Vector trajectory = UtilVelocity.getTrajectory2d(e.getDamager(), e.getDamagee());
        trajectory.multiply(0.8D * e.getDamage());
        trajectory.setY(Math.abs(trajectory.getY()));

        UtilVelocity.velocity(e.getDamagee(),
                trajectory, 0.3D + trajectory.length() * 0.8D, false, 0.0D, Math.abs(0.2D * e.getDamage()), 0.4D + 0.04D * e.getDamage(), true);
    }

    private void playDamageEffect(CustomDamageEvent e) {
        e.getDamagee().playEffect(EntityEffect.HURT);
        if (e.getProjectile() instanceof Arrow) {
            if (e.getDamager() instanceof Player) {
                Player p = (Player) e.getDamager();

                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.5f, 0.7f);
                e.getDamager().getWorld().playSound(e.getDamagee().getLocation(), Sound.ENTITY_ARROW_HIT, 0.5f, 1.0f);

            }
        } else {

            e.getDamagee().getWorld().playSound(e.getDamagee().getLocation(),
                    getDamageSound(e.getDamagee().getType()), 1.0F, 1.0F);

        }
    }

    private void updateDurability(CustomDamageEvent e) {


        if (e.getDamagee() instanceof Player) {
            if (!ClanUtilities.isInTerritory((Player) e.getDamagee(), "Fields")) {

                for (ItemStack armour : e.getDamagee().getEquipment().getArmorContents()) {
                    if (armour != null) {
                        if (UtilItem.isArmour(armour.getType())) {

                            boolean takeDura = false;

                            if (armour.getType().name().contains("LEATHER")) {
                                double rand = UtilMath.randomInt(1, 7);
                                if (rand == 6) {
                                    takeDura = true;
                                }
                            } else if (armour.getType().name().contains("CHAINMAIL") || armour.getType().name().contains("IRON")) {
                                if (UtilMath.randDouble(0, 10) >= 5) {
                                    takeDura = true;
                                }
                            } else if (armour.getType().name().contains("GOLD")) {
                                if (UtilMath.randomInt(1, 6) == 5) {

                                    takeDura = true;
                                }
                            } else {
                                takeDura = true;
                            }

                            if (takeDura) {
                                armour.setDurability((short) (armour.getDurability() + 1));
                                if (armour.getDurability() > armour.getType().getMaxDurability()) {

                                    if (armour.getType().name().contains("HELMET")) {
                                        e.getDamagee().getEquipment().setHelmet(null);
                                    }
                                    if (armour.getType().name().contains("CHESTPLATE")) {
                                        e.getDamagee().getEquipment().setChestplate(null);
                                    }
                                    if (armour.getType().name().contains("LEGGINGS")) {
                                        e.getDamagee().getEquipment().setLeggings(null);
                                    }
                                    if (armour.getType().name().contains("BOOTS")) {
                                        e.getDamagee().getEquipment().setBoots(null);
                                    }
                                }
                            }
                        }
                    }
                }
                ((Player) e.getDamagee()).updateInventory();
            }

        }


        if (e.getDamager() instanceof Player) {
            if (e.getCause() == DamageCause.ENTITY_ATTACK) {
                Player p = (Player) e.getDamager();
                ItemStack hand = p.getInventory().getItemInMainHand();
                if (hand != null) {
                    if (UtilItem.isAxe(hand.getType()) || UtilItem.isSword(hand.getType())
                            || UtilItem.isPickAxe(hand.getType()) || UtilItem.isHoe(hand.getType())
                            || UtilItem.isShovel(hand.getType())) {



                        if (p.getInventory().getItemInMainHand().getType() == Material.GOLDEN_SWORD) {
                            if (UtilMath.randomInt(10) > 6) {
                                hand.setDurability((short) (hand.getDurability() + 1));

                            }
                            p.updateInventory();
                        } else {

                            Weapon w = WeaponManager.getWeapon(hand);
                            if (w != null && w instanceof ILegendary) {
                                return;
                            }

                            hand.setDurability((short) (hand.getDurability() + 1));
                        }
                        if (hand.getDurability() > hand.getType().getMaxDurability()) {
                            p.getInventory().setItemInMainHand(null);
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 1.0F);

                        }
                    }
                }


            }
        }
    }


    @EventHandler
    public synchronized void delayUpdater(UpdateEvent e) {
        if (e.getType() == UpdateType.TICK) {
            ListIterator<DamageData> it = delays.listIterator();
            while (it.hasNext()) {
                DamageData next = it.next();

                if (UtilTime.elapsed(next.getTimeOfDamage(), next.getDamageDelay())) {
                    it.remove();
                }
            }
        }
    }

    public static boolean hasDamageData(String u, DamageCause c) {
        for (DamageData dd : delays) {
            if (dd.getUUID().equalsIgnoreCase(u)) {
                if (dd.getCause() == c) {
                    return true;
                }
            }
        }
        return false;
    }

    public DamageData getDamageData(String u, DamageCause c) {
        for (DamageData dd : delays) {
            if (dd.getUUID().equalsIgnoreCase(u) && dd.getCause() == c) {
                return dd;
            }
        }
        return null;
    }


    private Projectile getProjectile(EntityDamageEvent e) {
        if (!(e instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;

        if ((ev.getDamager() instanceof Projectile)) {
            return (Projectile) ev.getDamager();
        }
        return null;
    }

    private LivingEntity getDamageeEntity(EntityDamageEvent e) {
        if ((e.getEntity() instanceof LivingEntity)) {
            return (LivingEntity) e.getEntity();
        }
        return null;
    }

    public static LivingEntity getDamagerEntity(EntityDamageEvent e) {
        if (!(e instanceof EntityDamageByEntityEvent)) {
            return null;
        }
        EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) e;


        if ((ev.getDamager() instanceof LivingEntity)) {
            return (LivingEntity) ev.getDamager();
        }

        if (!(ev.getDamager() instanceof Projectile)) {
            return null;
        }
        Projectile projectile = (Projectile) ev.getDamager();

        if (projectile.getShooter() == null) {
            return null;
        }
        if (!(projectile.getShooter() instanceof LivingEntity)) {
            return null;
        }
        return (LivingEntity) projectile.getShooter();
    }

    public Sound getDamageSound(EntityType ent) {

        switch (ent) {
            case PIG:
                return Sound.ENTITY_PIG_HURT;
            case PIG_ZOMBIE:
                return Sound.ENTITY_ZOMBIE_PIGMAN_HURT;
            case COW:
                return Sound.ENTITY_COW_HURT;
            case SHEEP:
                return Sound.ENTITY_SHEEP_HURT;

            case CHICKEN:
                return Sound.ENTITY_CHICKEN_HURT;
            case ZOMBIE:
                return Sound.ENTITY_ZOMBIE_HURT;
            case VILLAGER:
                return Sound.ENTITY_VILLAGER_HURT;
            case SKELETON:
                return Sound.ENTITY_SKELETON_HURT;
            case SQUID:
                return Sound.ENTITY_SQUID_HURT;
            case SPIDER:
                return Sound.ENTITY_SPIDER_HURT;
            case CAVE_SPIDER:
                return Sound.ENTITY_SPIDER_HURT;
            case BAT:
                return Sound.ENTITY_BAT_HURT;
            case RABBIT:
                return Sound.ENTITY_RABBIT_HURT;
            case ENDERMAN:
                return Sound.ENTITY_ENDERMAN_HURT;
            case BLAZE:
                return Sound.ENTITY_BLAZE_HURT;
            case CREEPER:
                return Sound.ENTITY_CREEPER_HURT;
            case GUARDIAN:
                return Sound.ENTITY_GUARDIAN_HURT;
            case WOLF:
                return Sound.ENTITY_WOLF_HURT;
            case ENDERMITE:
                return Sound.ENTITY_ENDERMITE_HURT;
            case GHAST:
                return Sound.ENTITY_GHAST_HURT;
            case MAGMA_CUBE:
                return Sound.ENTITY_MAGMA_CUBE_HURT;
            case SLIME:
                return Sound.ENTITY_SLIME_HURT;
            case HORSE:
                return Sound.ENTITY_HORSE_HURT;
            case IRON_GOLEM:
                return Sound.ENTITY_IRON_GOLEM_HURT;
            case SNOWMAN:
                return Sound.ENTITY_SNOW_GOLEM_HURT;
            case ENDER_DRAGON:
                return Sound.ENTITY_ENDER_DRAGON_HURT;
            case WITHER:
                return Sound.ENTITY_WITHER_HURT;
            default:
                return Sound.ENTITY_PLAYER_HURT;
        }
    }


}
