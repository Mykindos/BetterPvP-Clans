package net.betterpvp.clans.dailies.menus;

import net.betterpvp.clans.dailies.Quest;
import net.betterpvp.clans.dailies.QuestManager;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.core.interfaces.Button;
import net.betterpvp.core.interfaces.Menu;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class QuestMenu extends Menu {

	public QuestMenu(Player player) {
		super(player, 9, ChatColor.RED.toString() + ChatColor.BOLD + "Daily Quest Tracker", new Button[]{});
		
		int count = 0;
		for(Quest q : QuestManager.getQuests()){
			if(q.isActive()){
				Progress p = q.getQuestProgression(player.getUniqueId(), q.getName());
				List<String> desc = new ArrayList<>();
				desc.add(ChatColor.BLUE + "Name: " + ChatColor.YELLOW + q.getName());
				for(String s : q.getDescription()){
					desc.add(s);
				}
				
				
				if(p instanceof GeneralProgression){
					GeneralProgression gp = (GeneralProgression) p;
					desc.add(ChatColor.BLUE + "Progress: " + ChatColor.YELLOW + gp.getCurrentAmount() + ChatColor.GRAY + " / " + ChatColor.YELLOW + gp.getRequiredAmount());
					desc.add(ChatColor.BLUE + "Completed: " + (gp.isComplete() ? ChatColor.GREEN.toString() 
							+ ChatColor.BOLD + "YES" : ChatColor.RED.toString() + ChatColor.BOLD + "NO"));
				}
				
				addButton(new Button(count, p.isComplete() ? new ItemStack(Material.GREEN_WOOL, 1) : new ItemStack(Material.RED_WOOL,  1),
						p.isComplete() ? ChatColor.GREEN + q.getName() : ChatColor.RED + q.getName(),
								desc
								));
				
				count+=2;
			
			}
		}
		construct();
	}

}
