package net.betterpvp.clans;

import net.betterpvp.clans.clans.map.MinimapRenderer;
import net.betterpvp.clans.clans.map.NMS.INMSHandler;
import net.betterpvp.clans.clans.map.NMS.NMSHandler;
import net.betterpvp.clans.clans.map.OneHandedRenderer;
import net.betterpvp.clans.scoreboard.ScoreboardManager;
import net.betterpvp.clans.settings.Options;
import net.betterpvp.core.command.CommandManager;
import net.betterpvp.core.configs.ConfigManager;
import net.betterpvp.core.database.QueryFactory;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Clans extends JavaPlugin implements Listener {

    private static Options options = null;
    private ConfigManager config;
    private INMSHandler nms = new NMSHandler();

    @Override
    public void onEnable() {
        options = new Options(this);
        config = new ConfigManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);


        load();
    }

    private void load() {
        QueryFactory.loadRepositories("net.betterpvp.clans", this);
        CommandManager.registerCommands("net.betterpvp.clans", this);

        new ScoreboardManager();
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
