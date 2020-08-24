package net.betterpvp.clans.worldevents.types;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ScoreboardUpdateEvent;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.WEType;
import net.betterpvp.clans.worldevents.WorldEvent;
import net.betterpvp.clans.worldevents.types.bosses.SlimeKing;
import net.betterpvp.clans.worldevents.types.bosses.ads.SlimeBase;
import net.betterpvp.core.database.Log;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Boss extends WorldEvent {

    private List<WorldEventMinion> minions;

    public Boss(Clans i, String name, WEType type) {
        super(i, name, type);

        minions = new ArrayList<>();
    }

    private ItemStack loot;


    public abstract double getBaseDamage();

    public abstract String getBossName();

    public abstract EntityType getEntityType();

    public abstract double getMaxHealth();

    public abstract LivingEntity getBoss();

    public abstract void removeBoss();

    public abstract boolean isBoss(LivingEntity ent);

    public long lastDamaged;

    public List<WorldEventMinion> getMinions() {
        return minions;
    }

    public WorldEventMinion getMinion(LivingEntity ent) {
        for (WorldEventMinion bm : getMinions()) {
            if (bm.getEntity() == ent) {
                return bm;
            }
        }
        return null;
    }

    @EventHandler
    public void minionKiller(EntityDeathEvent e) {
        if (isMinion(e.getEntity())) {
            WorldEventMinion remove = null;
            for (WorldEventMinion bm : getMinions()) {
                if (bm.getEntity().equals(e.getEntity())) {
                    remove = bm;
                }
            }

            if (remove != null) {
                getMinions().remove(remove);
            }
        }
    }


    @EventHandler
    public void onDamageTeleport(CustomDamageEvent e) {
        if (isActive()) {
            if (isBoss(e.getDamagee())) {
                if (e.getDamager() instanceof Player) {
                    lastDamaged = System.currentTimeMillis();
                }

            }

        }
    }

    @EventHandler
    public void onLavaDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (isBoss(e.getDamagee())) {
                if (e.getCause() == DamageCause.LAVA || e.getCause() == DamageCause.FIRE) {
                    e.setCancelled("Boss cannot take damage from lava");
                }
            }
        }
    }

    @EventHandler
    public void checkLava(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.FASTER) {
            if (isActive()) {
                if (getBoss() != null && !getBoss().isDead()) {
                    Block b = getBoss().getLocation().getBlock();


                    if (b.getType() == Material.LAVA) {
                        if (b.getRelative(BlockFace.UP).getType() != Material.LAVA) {
                            new BlockRestoreData(b, b.getType(), (byte) 0, 180000);
                            b.setType(Material.OBSIDIAN);


                            getBoss().teleport(getBoss().getLocation().add(0, 1, 0));
                        }
                    }
                }
            }
        }
    }


    public void announceDeath(EntityDeathEvent e) {
        for (WorldEventMinion bm : getMinions()) {
            bm.getEntity().remove();
        }
        getMinions().clear();

        loot = WEManager.getRandomItem();


        LivingEntity killer = LogManager.getKiller(e.getEntity()).getDamager();
        if (killer != null) {
            UtilMessage.broadcast("World Event", ChatColor.YELLOW + getBossName() + ChatColor.GRAY
                    + " has been killed by " + ChatColor.YELLOW + killer.getName());
            Log.write("World Event", killer.getName() + " killed " + getBossName());
            if (killer instanceof Player) {
                Player p = (Player) killer;
				
				/*
				Clan c = ClanUtilities.getClan(p);
				if(c != null) {
					c.setPoints(c.getPoints() + 2);
					ClanRepository.updatePoints(c);
					UtilMessage.broadcast("World Event", ChatColor.YELLOW + c.getName() + ChatColor.GRAY + " received " + ChatColor.GREEN + "2" + ChatColor.GRAY + " clan points.");
				}
				*/

                Gamer killerGamer = GamerManager.getOnlineGamer(p);
                if (killerGamer != null) {

                    double fragments = 10;


                /*
                if (killerClient.hasDonationRank(DonationRank.SAVIOR)) {
                    fragments *= 2;
                } else if (killerClient.hasDonationRank(DonationRank.LEGACY)) {
                    fragments *= 1.5;
                }

                if (killerClient.getGamer().hasPerk(Perk.getPerk("FragmentBuff"))) {
                    fragments *= 1.5;
                }

                 */

                    if(killerGamer.getClient().hasDonation("VIP")){
                        fragments = fragments * 1.25;
                    }

                    killerGamer.addCoins(50000);
                    killerGamer.addFragments(fragments);

                    giveBonus(killerGamer, getBossName());

                    UtilMessage.message(p, "World Event", "You received " + ChatColor.GREEN + "$50,000 " + ChatColor.GRAY + "and "
                            + ChatColor.GREEN + fragments + " fragments");

                }
            }
        }

        Log.write("World Event", org.bukkit.ChatColor.stripColor(loot.getItemMeta().getDisplayName()) + " dropped");
        Weapon wep = WeaponManager.getWeapon(loot);
        if (wep != null
                && !(wep instanceof EnchantedWeapon)) {
            UtilMessage.broadcast("Legendary Loot", ChatColor.YELLOW + "A " + wep.getName() + ChatColor.YELLOW + " was dropped at the world event!");
            Log.write("Legendary", wep.getName() + " dropped from world event");
        }

        e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), loot);


        setActive(false);
        for (Player p : Bukkit.getOnlinePlayers()) {
            Bukkit.getPluginManager().callEvent(new ScoreboardUpdateEvent(p));
        }
    }

   /* private void giveBonus(Client client, String boss) {
        PlayerStat stat = client.getStats();
        switch (ChatColor.stripColor(boss)) {
            case "Slime King":
                stat.slimeKing = stat.slimeKing + 1;
                break;
            case "Skeleton King":
                stat.skeletonKing = stat.skeletonKing + 1;
                break;
            case "Broodmother":
                stat.broodmother = stat.broodmother + 1;
                break;
            case "Charles Witherton":
                stat.witherton = stat.witherton + 1;
                break;
        }

        PlayerStatRepository.updateStats(client);

    }*/

    protected void heal(double amount) {

        getBoss().setHealth(Math.min(getBoss().getMaxHealth(), getBoss().getHealth() + amount));

        getBoss().setCustomName(getBossName() + "  " + ChatColor.GREEN + (int) getBoss().getHealth() + ChatColor.YELLOW + "/" + ChatColor.GREEN + (int) getBoss().getMaxHealth());

    }

    public boolean isMinion(LivingEntity ent) {
        if (ent == null) return false;
        if (ent.isDead()) return false;

        for (WorldEventMinion b : getMinions()) {
            if (b == null) continue;
            if (b.getEntity() == null) continue;
            if (b.getEntity().isDead()) continue;
            if (b.getEntity().getUniqueId().toString().equalsIgnoreCase(ent.getUniqueId().toString())) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSuffocate(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() != null) {
                if (isBoss(e.getDamagee())) {
                    if (e.getCause() == DamageCause.SUFFOCATION || e.getCause() == DamageCause.DROWNING) {
                        e.setCancelled("Bosses cant suffocate or drown");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExpire(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC) {
            if (isActive()) {
                if (UtilTime.elapsed(lastDamaged, 60000 * 15)) {
                    setActive(false);

                    removeBoss();

                    for (WorldEventMinion wem : getMinions()) {
                        wem.getEntity().remove();
                    }

                    getMinions().clear();

                    if (this instanceof SlimeKing) {
                        SlimeKing slimeKing = (SlimeKing) this;
                        for (SlimeBase bem : slimeKing.allSlimes) {
                            bem.getEntity().remove();
                        }

                        slimeKing.allSlimes.clear();
                    }
                }
            }
        }
    }

    private void giveBonus(Gamer gamer, String boss) {

        String bossName = ChatColor.stripColor(boss);

        gamer.setStatValue(bossName, gamer.getStatValue(bossName) + 1);

        Player player = Bukkit.getPlayer(gamer.getUUID());
        if (player != null) {
            int kc = (int) gamer.getStatValue(bossName);
            UtilMessage.message(player, "World Event", "Your kill count for " + getBossName() + ChatColor.GRAY + " is now "
                    + ChatColor.GREEN + kc + ChatColor.GRAY + " (" + ChatColor.YELLOW + "+" + (kc * 2) + "% bonus damage versus this boss" + ChatColor.GRAY + ").");
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void bonusDamage(CustomDamageEvent e) {
        if (isActive()) {
            if (e.getDamagee() == null || e.getDamager() == null) return;
            if (e.getDamager() != null) {
                if (e.getDamager() instanceof Player) {
                    Player p = (Player) e.getDamager();

                    if (isBoss(e.getDamagee()) || isMinion(e.getDamagee())) {
                        Gamer gamer = GamerManager.getOnlineGamer(p);

                        int kills = (int) gamer.getStatValue(ChatColor.stripColor(getBossName()));
                        double modifier = kills * 2;
                        double modifier2 = modifier >= 10 ? 0.01 : 0.1;

                        e.setDamage(e.getDamage() * (1 + (modifier * modifier2)));
                    }
                }
            }
        }
    }

}
