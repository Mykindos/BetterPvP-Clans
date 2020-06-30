package net.betterpvp.clans.skills.selector.page;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.mysql.BuildRepository;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuildPage extends Menu {

    private String role;

    public BuildPage(Clans x, Player p, String role) {
        super(p, 54, ChatColor.GREEN.toString() + ChatColor.BOLD + role + " builds", new Button[]{});
        this.role = role;
        fillPage(x, p);
        construct();
    }

    public String getRole() {
        return this.role;
    }

    private void fillPage(Clans x, Player p) {
        int slot = 9;
        Role r = Role.getRole(role);
        addButton(new Button(0, new ItemStack(Material.EMERALD_BLOCK), ChatColor.GREEN.toString() + ChatColor.BOLD + "Back"));
        addButton(new Button(18, new ItemStack(r.getHelmet()), ChatColor.GREEN.toString() + ChatColor.BOLD + role + " Helmet"));
        addButton(new Button(27, new ItemStack(r.getChestplate()), ChatColor.GREEN.toString() + ChatColor.BOLD + role + " Chestplate"));
        addButton(new Button(36, new ItemStack(r.getLeggings()), ChatColor.GREEN.toString() + ChatColor.BOLD + role + " Leggings"));
        addButton(new Button(45, new ItemStack(r.getBoots()), ChatColor.GREEN.toString() + ChatColor.BOLD + role + " Boots"));
        Gamer g = GamerManager.getOnlineGamer(p);
        if (g != null) {
            for (int i = 0; i < 4; i++) {

                if (g.getActiveBuild(role) == null) {
                    try {
                        g.getBuild(role, 1).setActive(true);
                        g.setActiveBuild(g.getBuild(role, 1));
                        BuildRepository.updateBuild(g.getUUID(), g.getBuild(role, 1));
                    } catch (Exception e) {
                        UtilMessage.message(p, "Build", "Your builds appear to be bugged! Try reopening the window");
                        BuildRepository.loadBuilds(x, p.getUniqueId());
                        return;
                    }
                }
                if (g.getActiveBuild(role).getID() == i + 1) {
                    addButton(new Button(slot + 11, UtilItem.addGlow(getApplyBuildItem(i + 1)), ChatColor.GREEN.toString() + ChatColor.BOLD + "Apply Build - " + (i + 1)));
                } else {
                    addButton(new Button(slot + 11, getApplyBuildItem(i + 1), ChatColor.GREEN.toString() + ChatColor.BOLD + "Apply Build - " + (i + 1)));
                }
                addButton(new Button(slot + 20, new ItemStack(Material.ANVIL), ChatColor.GREEN.toString() + ChatColor.BOLD + "Edit & Save Build - " + (i + 1)));
                addButton(new Button(slot + 38, new ItemStack(Material.TNT), ChatColor.GREEN.toString() + ChatColor.BOLD + "Delete Build - " + (i + 1)));

                slot += 2;
            }
        }
    }

    private ItemStack getApplyBuildItem(int id){

        switch(id){
            case 1:
                return new ItemStack(Material.INK_SAC, 1);
            case 2:
                return new ItemStack(Material.RED_DYE, 1);
            case 3:
                return new ItemStack(Material.GREEN_DYE, 1);
            case 4:
                return new ItemStack(Material.CYAN_DYE, 1);
        }

        return null;
    }
}
