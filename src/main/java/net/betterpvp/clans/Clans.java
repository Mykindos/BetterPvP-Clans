package net.betterpvp.clans;


import io.github.bananapuncher714.cartographer.core.Cartographer;
import io.github.bananapuncher714.cartographer.core.api.map.MapCursorProvider;
import io.github.bananapuncher714.cartographer.core.api.map.WorldCursorProvider;
import io.github.bananapuncher714.cartographer.core.map.Minimap;
import me.mykindos.MAH.user.MAHManager;
import net.betterpvp.clans.anticheat.AnticheatManager;
import net.betterpvp.clans.clans.commands.ClanCommand;
import net.betterpvp.clans.clans.commands.ClanReloadCommand;
import net.betterpvp.clans.clans.listeners.*;
import net.betterpvp.clans.clans.map.ClanCursorProvider;
import net.betterpvp.clans.clans.map.ClanMapSettings;
import net.betterpvp.clans.clans.map.ClanPixelProvider;
import net.betterpvp.clans.clans.map.MapListener;
import net.betterpvp.clans.clans.tnt.TNTManager;
import net.betterpvp.clans.classes.DamageManager;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.RoleManager;
import net.betterpvp.clans.classes.menu.KitListener;
import net.betterpvp.clans.combat.CombatManager;
import net.betterpvp.clans.combat.LogManager;
import net.betterpvp.clans.combat.combatlog.CombatLogManager;
import net.betterpvp.clans.combat.combatlog.npc.NPCManager;
import net.betterpvp.clans.combat.ratings.RatingManager;
import net.betterpvp.clans.combat.safelog.SafeLogManager;
import net.betterpvp.clans.combat.throwables.ThrowableManager;
import net.betterpvp.clans.cosmetics.CosmeticManager;
import net.betterpvp.clans.cosmetics.types.wings.*;
import net.betterpvp.clans.crates.CrateListener;
import net.betterpvp.clans.crates.CrateManager;
import net.betterpvp.clans.dailies.QuestManager;
import net.betterpvp.clans.dailies.perks.QuestPerkManager;
import net.betterpvp.clans.economy.shops.ShopCommand;
import net.betterpvp.clans.economy.shops.ShopEntities;
import net.betterpvp.clans.economy.shops.ShopManager;
import net.betterpvp.clans.economy.shops.ShopListener;
import net.betterpvp.clans.economy.shops.ignatius.IgnatiusListener;
import net.betterpvp.clans.economy.shops.nms.ShopSkeleton;
import net.betterpvp.clans.economy.shops.nms.ShopVillager;
import net.betterpvp.clans.economy.shops.nms.ShopZombie;
import net.betterpvp.clans.economy.shops.nms.UtilShop;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.farming.FarmingListener;
import net.betterpvp.clans.farming.bee.BeeListener;
import net.betterpvp.clans.fields.Fields;
import net.betterpvp.clans.fields.management.FieldsManager;
import net.betterpvp.clans.fishing.FishingListener;
import net.betterpvp.clans.fun.BounceListener;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerConnectionListener;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.gamer.mysql.GamerRepository;
import net.betterpvp.clans.gamer.mysql.PlayerStatRepository;
import net.betterpvp.clans.general.ClanSpawnListener;
import net.betterpvp.clans.general.ServerListener;
import net.betterpvp.clans.general.VoteListener;
import net.betterpvp.clans.general.WorldListener;
import net.betterpvp.clans.general.commands.HubCommand;
import net.betterpvp.clans.general.commands.SearchChestsCommand;
import net.betterpvp.clans.general.commands.SendAllToHubCommand;
import net.betterpvp.clans.general.commands.menu.LegendaryMenuListener;
import net.betterpvp.clans.koth.KOTHManager;
import net.betterpvp.clans.mysql.ReflectionsUtil;
import net.betterpvp.clans.networking.QueueCommand;
import net.betterpvp.clans.networking.SlotListener;
import net.betterpvp.clans.recipes.*;
import net.betterpvp.clans.scoreboard.ScoreboardManager;
import net.betterpvp.clans.settings.Options;
import net.betterpvp.clans.skills.selector.SelectorManager;
import net.betterpvp.clans.skills.selector.SkillListener;
import net.betterpvp.clans.skills.selector.skills.data.CustomArmorStand;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.clans.worldevents.WEManager;
import net.betterpvp.clans.worldevents.types.nms.*;
import net.betterpvp.core.command.CommandManager;
import net.betterpvp.core.configs.ConfigManager;
import net.betterpvp.core.database.Connect;
import net.betterpvp.core.database.Repository;
import net.betterpvp.core.donation.DonationManager;
import net.betterpvp.core.framework.CoreLoadedEvent;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilMessage;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import net.minecraft.world.entity.animal.EntityPolarBear;
import net.minecraft.world.entity.animal.EntitySnowman;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.monster.EntityCaveSpider;
import net.minecraft.world.entity.monster.EntitySkeleton;
import net.minecraft.world.entity.monster.EntitySpider;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.npc.EntityVillager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;


public class Clans extends JavaPlugin implements Listener {

    private static Options options = null;
    private List<Repository> repositoryList;
    private ConfigManager config;
    private boolean hasStarted;

    @Override
    public void onEnable() {

        config = new ConfigManager(this);
        options = new Options(this);
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);


        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getOpenInventory().close();
            Gamer c = GamerManager.getOnlineGamer(p);
            GamerRepository.updateGamer(c);
            PlayerStatRepository.updateAllStats(c);
        }

        Connect.disableSQL();

        for (org.bukkit.World w : Bukkit.getWorlds()) {
            for (LivingEntity e : w.getLivingEntities()) {
                if (e instanceof Player || e instanceof ArmorStand || e instanceof ItemFrame) continue;
                e.setHealth(0);
                e.remove();
            }
        }

        net.betterpvp.clans.combat.combatlog.npc.NPC.removeAllNPCs();
    }

    private void load() {

        if (!Clans.getOptions().isHub()) {
            new WorldCreator("bossworld").createWorld();
        }

        new RoleManager(this);

        UtilShop.registerEntity("Zombie", 54, EntityZombie.class, ShopZombie.class);
        UtilShop.registerEntity("Spider", 52, EntitySpider.class, BossSpider.class);
        UtilShop.registerEntity("CaveSpider", 59, EntityCaveSpider.class, BossCaveSpider.class);
        UtilShop.registerEntity("Skeleton", 51, EntitySkeleton.class, ShopSkeleton.class);
        UtilShop.registerEntity("Zombie", 54, EntityZombie.class, BossZombie.class);
        UtilShop.registerEntity("Wither", 64, EntityWither.class, BossWither.class);
        UtilShop.registerEntity("Skeleton", 51, EntitySkeleton.class, BossSkeleton.class);
        UtilShop.registerEntity("PolarBear", 102, EntityPolarBear.class, BossPolarBear.class);
        UtilShop.registerEntity("Snowman", 97, EntitySnowman.class, BossSnowGolem.class);
        UtilShop.registerEntity("Villager", 120, EntityVillager.class, ShopVillager.class);
        UtilShop.registerEntity("ArmorStand", 30, EntityArmorStand.class, CustomArmorStand.class);

        repositoryList = ReflectionsUtil.loadRepositories("net.betterpvp.clans", this);
        ReflectionsUtil.registerCommands("net.betterpvp.clans", this);
        ReflectionsUtil.registerDonations("net.betterpvp.clans", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        loadRecipes();
        startTimers();


        new SkillListener(this);
        new SelectorManager(this);
        new DamageManager(this);

        new CombatManager(this);
        new WeaponManager(this);
        new WEManager(this);
        new GamerConnectionListener(this);

        new EffectManager(this);
        new ThrowableManager(this);
        new ChatListener(this);
        new ClanEventListener(this);
        new ClanMenuListener(this);
        // new ClanScoreboardListener(this);
        new DamageListener(this);
        new EnergyListener(this);
        new ExplosionListener(this);
        new InsuranceListener(this);
        new InteractListener(this);
        new InviteHandler(this);
        new MovementListener(this);
        new PillageListener(this);
        new Energy(this);
        new BounceListener(this);
        new WorldListener(this);
        new ShopEntities(this);
        new ShopListener(this);
        new ShopManager(this);
        new QuestPerkManager();
        new FieldsManager(this);
        new Fields(this);
        new ScoreboardManager(this);
        new KitListener(this);
        new QuestManager(this);
        new FishingListener(this);
        new FarmingListener(this);
        new KOTHManager(this);
        new ClanScoreboardListener(this);
        new CombatLogManager(this);
        new NPCManager(this);
        new SafeLogManager(this);
        new AnticheatManager(this);
        new CrateManager();
        new CrateListener(this);
        new ServerListener(this);
        new VoteListener(this);
        new BeeListener(this);
        new MapListener(this);
        new ClanSpawnListener(this);
        new TNTManager(this);
        new RatingManager(this);
        new IgnatiusListener(this);
        new CosmeticManager(this);
        new LegendaryMenuListener(this);

        CommandManager.addCommand(new ShopCommand(this));
        //CommandManager.addCommand(new FindCommand(this));
        CommandManager.addCommand(new HubCommand(this));
        CommandManager.addCommand(new ClanReloadCommand(this));
        CommandManager.addCommand(new SearchChestsCommand(this));
        CommandManager.addCommand(new SendAllToHubCommand(this));


        if (Clans.getOptions().isHub()) {
            QueueCommand queueCommand = new QueueCommand(this);
            CommandManager.addCommand(queueCommand);
            Bukkit.getPluginManager().registerEvents(queueCommand, this);
        } else {
            new SlotListener(this);
        }

        getCommand("clan").setExecutor(new ClanCommand(this));

        //Cartographer.getInstance().getMapManager().remove(Cartographer.getInstance().getMapManager().getMinimaps().get("Clans"));

        ClanMapSettings clanMapSettings = new ClanMapSettings(YamlConfiguration.loadConfiguration(new File("/plugins/Cartographer2/Clans-Map-Config.yml")));
        if(!Cartographer.getInstance().getMapManager().getMinimaps().containsKey("Clans")) {
            Cartographer.getInstance().getMapManager().constructNewMinimap("Clans");
        }

        for(Map.Entry<String, Minimap> map : Cartographer.getInstance().getMapManager().getMinimaps().entrySet()){
            for (MapCursorProvider prov : map.getValue().getMapCursorProviders()) {
                map.getValue().unregisterProvider(prov);
            }
            for (WorldCursorProvider prov : map.getValue().getWorldCursorProviders()) {
                map.getValue().unregisterProvider(prov);
            }

            map.getValue().registerProvider(new ClanCursorProvider());
            map.getValue().registerProvider(new ClanPixelProvider(this));
            map.getValue().setSettings(clanMapSettings);
        }

    }

    @EventHandler
    public void onLoad(CoreLoadedEvent event) {
        System.out.println("Core loaded, beginning Clans load.");
        for (org.bukkit.World w : Bukkit.getWorlds()) {
            for (LivingEntity e : w.getLivingEntities()) {
                if (e instanceof Player || e instanceof ArmorStand || e instanceof ItemFrame) continue;
                e.setHealth(0);
                e.remove();
            }
        }

        load();
    }

    private static Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");

    public static CoreProtectAPI getCoreProtect() {


        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI CoreProtect = ((CoreProtect) plugin).getAPI();
        if (CoreProtect.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (CoreProtect.APIVersion() < 4) {
            return null;
        }

        return CoreProtect;
    }


    private void startTimers() {
        new BukkitRunnable() {
            @Override
            public void run() {

                LogManager.processLogs();

            }
        }.runTaskTimerAsynchronously(this, 0L, 2L);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Gamer gamer = GamerManager.getOnlineGamer(p);
                    GamerRepository.updateGamer(gamer);

                }
                getLogger().log(Level.INFO, "Updated player data");
            }
        }.runTaskTimerAsynchronously(this, 6000L, 6000L);

        new BukkitRunnable() {


            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    Gamer gamer = GamerManager.getOnlineGamer(p);
                    if (gamer != null) {


                        double add = 0;
                        int frags = 2;

                        if(MAHManager.isAuthenticated(p.getUniqueId())){
                            frags++;
                        }

                        if (gamer.getClient().isDiscordLinked()) {
                            add += 1000;
                        }

                        if(gamer.getClient().hasDonation("VIP")){
                            frags = (int) (frags * 1.25);
                        }

                        gamer.addCoins((getOptions().getOnlineReward() + add));

                        gamer.addFragments(frags);

                        UtilMessage.message(p, "Online Reward", "You received " + ChatColor.YELLOW + "$"
                                + UtilFormat.formatNumber((int) ((getOptions().getOnlineReward() + add))) + ChatColor.GRAY + " coins and " + ChatColor.YELLOW + (frags) + ChatColor.GRAY + " fragments");

                    }
                }

            }
        }.runTaskTimer(this, 72000 / 2, 72000 / 2);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Gamer gamer : GamerManager.getOnlineGamers()) {
                    gamer.setStatValue("Time Played", gamer.getStatValue("Time Played") + 0.25);
                    PlayerStatRepository.updateAllStats(gamer);

                }
            }
        }.runTaskTimerAsynchronously(this, 18000, 18000);

    }

    private void loadRecipes() {
        new RadiantSword(this);
        new PowerSword(this);
        new Chainmail(this);
        new PowerAxe(this);
        new FireAxe(this);
        new Netherite(this);
    }



    public static Options getOptions() {
        return options;
    }

    public ConfigManager getConfigManager() {
        return config;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void setStarted(boolean started) {
        this.hasStarted = started;
    }

    public List<Repository> getRepositoryList() {
        return repositoryList;
    }
}