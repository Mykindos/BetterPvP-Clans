package net.betterpvp.clans.mysql;

import net.betterpvp.clans.recipes.CustomRecipe;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.command.CommandManager;
import net.betterpvp.core.database.QueryFactory;
import net.betterpvp.core.database.Repository;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Duplicate code due to a bug with the Reflections library & spigot.
 *
 */
public class ReflectionsUtil {

    /**

     * @param instance
     * Loads all Repository objects in order of priority. Data that requires other data to be loaded first should be on high
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void loadRepositories(String packageName, JavaPlugin instance) {

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends Repository>> classes = reflections.getSubTypesOf(Repository.class);
        System.out.println("Repositories : " + classes.size());
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

    public static void loadRecipes(String packageName) {

        Reflections reflections = new Reflections(packageName);

        Set<Class<? extends CustomRecipe>> classes = reflections.getSubTypesOf(CustomRecipe.class);

        for (Class<? extends CustomRecipe> r : classes) {
            try {
                r.newInstance();

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public static void registerCommands(String packageName, Plugin instance) {
        int count = 0;
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        for (Class<? extends Command> c : classes) {
            try {

                Bukkit.broadcastMessage(c.getName());
                if(c.getConstructors()[0].getParameterCount() > 0){
                    System.out.println("Skipped Command (Requires arguments): " + c.getName());
                    continue;
                }
                if (Listener.class.isAssignableFrom(c)) {
                    Command command = c.newInstance();
                    Bukkit.getPluginManager().registerEvents((Listener) command, instance);
                    CommandManager.addCommand(command);
                    System.out.println("Registered command + event listener");

                } else {
                    CommandManager.addCommand(c.newInstance());
                }
                count++;

            } catch (InstantiationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Registered " + count + " commands for " + packageName);

    }
}