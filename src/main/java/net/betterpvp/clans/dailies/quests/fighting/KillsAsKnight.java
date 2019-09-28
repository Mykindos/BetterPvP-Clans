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

public class KillsAsKnight extends General{

	public KillsAsKnight(Clans i) {
		super(i, "Kills As Knight", new String[]{
				ChatColor.GRAY + "Kill a total of " + ChatColor.GREEN + "20" + ChatColor.GRAY + " players",
				ChatColor.GRAY + "using the" + ChatColor.GREEN + " Knight " + ChatColor.GRAY + "class."
		});

	}

	@Override
	public int getRequiredAmount() {
		// TODO Auto-generated method stub
		return 20;
	}


	@EventHandler
	public void onKill(PlayerDeathEvent e){
		if(isActive()){
			CombatLogs cl = LogManager.getKiller(e.getEntity());
			if(cl != null){
				if(cl.getDamager() instanceof Player){
					Player d = (Player) cl.getDamager();
					if(Role.getRole(d) != null && Role.getRole(d).getName().equals("Knight")){
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
