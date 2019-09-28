package net.betterpvp.clans;


import net.betterpvp.clans.clans.map.MinimapRenderer;
import net.betterpvp.clans.clans.map.NMS.INMSHandler;
import net.betterpvp.clans.clans.map.NMS.NMSHandler;
import net.betterpvp.clans.clans.map.OneHandedRenderer;
import net.betterpvp.clans.clans.mysql.TestRepository;
import net.betterpvp.clans.mysql.ReflectionsUtil;
import net.betterpvp.clans.settings.Options;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.command.CommandManager;
import net.betterpvp.core.configs.ConfigManager;
import net.betterpvp.core.database.QueryFactory;
import net.betterpvp.core.database.Repository;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Clans extends JavaPlugin implements Listener {

    private static Options options = null;
    private ConfigManager config;
    private INMSHandler nms = new NMSHandler();

    @Override
    public void onEnable() {

        config = new ConfigManager(this);
        options = new Options(this);
        Bukkit.getPluginManager().registerEvents(this, this);


        load();
    }

    private void load() {

        ReflectionsUtil.loadRepositories("net.betterpvp.clans", this);
        ReflectionsUtil.registerCommands("net.betterpvp.clans", this);

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


    public void loadMap() {
        try {
            deleteFolder(new File("./world/data"));
            File file = new File("./world/data/map_0.dat");
            file.getParentFile().mkdirs();

            file.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MapView map = Bukkit.getMap((short) 0);
        if (map == null) {
            map = Bukkit.createMap(Bukkit.getWorld("world"));
        }

        if (!(map.getRenderers().get(0) instanceof MinimapRenderer)) {
            for (final MapRenderer r : map.getRenderers()) {
                map.removeRenderer(r);
            }
            final MinimapRenderer renderer = new OneHandedRenderer(3, 4, this);

            map.addRenderer(renderer);
        }
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public static void loadRepositories(String packageName, JavaPlugin instance) {

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends Repository>> classes = reflections.getSubTypesOf(Repository.class);
        System.out.println("Repositories: " + classes.size());
        List<Repository> temp = new ArrayList<>();
        for (Class<? extends Repository> r : classes) {
            try {
                Repository repo = r.newInstance();
                QueryFactory.addRepository(repo);
                temp.add(repo);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        temp.sort(Comparator.comparingInt(r2 -> r2.getLoadPriority().getPriority()));

        temp.forEach(r -> {

            r.initialize();
            r.load(instance);

        });






    }


    public INMSHandler getNMSHandler() {
        return this.nms;
    }

    public static Options getOptions() {
        return options;
    }

    public ConfigManager getConfigManager() {
        return config;
    }
}
