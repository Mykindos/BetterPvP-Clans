package net.betterpvp.clans.dailies.quests.fighting;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.dailies.progression.Progress;
import net.betterpvp.clans.dailies.progression.types.GeneralProgression;
import net.betterpvp.clans.dailies.quests.General;
import net.betterpvp.clans.combat.CombatLogs;
import net.betterpvp.clans.combat.LogManager;
import net.md_5.bungee.api.ChatColor;

public class KillsAsRanger extends General{

	public KillsAsRanger(Clans i) {
		super(i, "Kills As Ranger", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "10" + ChatColor.GRAY + " players",
				ChatColor.GRAY + "using the" + ChatColor.GREEN + " Ranger " + ChatColor.GRAY + "class."
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 10;
	}


	@EventHandler
	public void onKill(PlayerDeathEvent e){
		if(isActive()){
			CombatLogs cl = LogManager.getKiller(e.getEntity());
			if(cl != null){
				if(cl.getDamager() instanceof Player){
				Player d = (Player) cl.getDamager();
				if(Role.getRole(d) != null && Role.getRole(d).getName().equals("Ranger")){
					Progress p = getQuestProgression(d.getUniqueId(), getName());

					if(!p.isComplete()){
						if(p instanceof GeneralProgression){
							GeneralProgression gp = (GeneralProgression) p;
							gp.addCurrentAmount();

							if(gp.getCurrentAmount() >= gp.getRequiredAmount()){
								gp.onComplete(d.getUniqueId());
							}
						}
					}
				}
				}
			}
		}
	}

}
