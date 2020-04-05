package net.betterpvp.clans.skills.selector.page;

import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.skills.selector.SelectorManager;
import net.betterpvp.clans.skills.selector.button.SkillButton;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SkillPage extends Menu {

    private final RoleBuild build;
    private final Gamer gamer;

    public SkillPage(Gamer gamer, RoleBuild build) {
        super(Bukkit.getPlayer(gamer.getUUID()), 54, "Skill Page", new Button[]{});
        this.gamer = gamer;
        this.build = build;
        buildPage(build.getRole());
        construct();
    }

    public Gamer getGamer() {
        return gamer;
    }

    public RoleBuild getBuild() {
        return build;
    }

    public boolean isSkillActive(Skill s) {
        for (Skill z : getBuild().getActiveSkills()) {
            if (z == null) continue;
            if (z.equals(s)) {
                return true;
            }
        }

        return false;
    }

    public void buildPage(String role) {
        int slotNumber = 0;
        int swordSlotNumber = 1;
        int axeSlotNumber = 10;
        int bowSlotNumber = 19;
        int passiveASlotNumber = 28;
        int passiveBSlotNumber = 37;
        int globalSlotNumber = 46;

        addButton(new Button(0, new ItemStack(Material.IRON_SWORD), ChatColor.GREEN.toString() + ChatColor.BOLD + "Sword Skills"));
        addButton(new Button(9, new ItemStack(Material.IRON_AXE), ChatColor.GREEN.toString() + ChatColor.BOLD + "Axe Skills"));
        addButton(new Button(18, new ItemStack(Material.BOW), ChatColor.GREEN.toString() + ChatColor.BOLD + "Bow Skills"));
        addButton(new Button(27, new ItemStack(Material.INK_SAC, 1), ChatColor.GREEN.toString() + ChatColor.BOLD + "Class Passive A Skills"));
        addButton(new Button(36, new ItemStack(Material.INK_SAC, 1), ChatColor.GREEN.toString() + ChatColor.BOLD + "Class Passive B Skills"));
        addButton(new Button(45, new ItemStack(Material.INK_SAC, 1), ChatColor.GREEN.toString() + ChatColor.BOLD + "Global Passive Skills"));
        addButton(new Button(8, new ItemStack(Material.EMERALD, getBuild().getPoints()), ChatColor.GREEN.toString() + ChatColor.BOLD + "Skill Points"));
        for (Skill skill : SelectorManager.getSkillsFor(role)) {
            if (skill == null) continue;
            if (skill.getType() == null) continue;
            if (skill.getType() == Types.SWORD) {
                slotNumber = swordSlotNumber;
                swordSlotNumber++;
            } else if (skill.getType() == Types.AXE) {
                slotNumber = axeSlotNumber;
                axeSlotNumber++;
            } else if (skill.getType() == Types.BOW) {
                slotNumber = bowSlotNumber;
                bowSlotNumber++;
            } else if (skill.getType() == Types.PASSIVE_A) {
                slotNumber = passiveASlotNumber;
                passiveASlotNumber++;
            } else if (skill.getType() == Types.PASSIVE_B) {
                slotNumber = passiveBSlotNumber;
                passiveBSlotNumber++;
            } else if (skill.getType() == Types.GLOBAL) {
                slotNumber = globalSlotNumber;
                globalSlotNumber++;
            }
            if (getBuild().getBuildSkill(skill.getType()) != null) {
                addButton(buildButton(skill, slotNumber, getBuild().getBuildSkill(skill.getType()).getLevel()));
            } else {
                addButton(buildButton(skill, slotNumber, 1));
            }
        }
    }


    public SkillButton buildButton(Skill s, int slot, int level) {
        String[] lore;
        lore = s.getDescription(level);

        List<String> tempLore = new ArrayList<>();
        for (String str : lore) {
            tempLore.add(ChatColor.GRAY + str);
        }
        ItemStack book = null;
        String name = "";
        if (isSkillActive(s)) {
            book = new ItemStack(Material.ENCHANTED_BOOK);
            name = ChatColor.GREEN.toString() + ChatColor.BOLD + s.getName() + " (" + level + " / " + s.getMaxLevel() + ")";
        } else {
            book = new ItemStack(Material.BOOK);
            name = ChatColor.RED + s.getName();
        }

        Button button = new Button(slot, book, name, tempLore);

        return new SkillButton(button, s);
    }

    public SkillButton buildButton(Skill s, int slot) {
        String[] lore = s.getDescription(0);
        List<String> tempLore = new ArrayList<>();
        for (String str : lore) {
            tempLore.add(ChatColor.GRAY + str);
        }
        ItemStack book = null;
        String name = "";
        if (isSkillActive(s)) {
            book = new ItemStack(Material.ENCHANTED_BOOK);
            name = ChatColor.GREEN.toString() + ChatColor.BOLD + s.getName();
        } else {
            book = new ItemStack(Material.BOOK);
            name = ChatColor.RED + s.getName();
        }


        Button button = new Button(slot, book, name, tempLore);

        return new SkillButton(button, s);
    }

}