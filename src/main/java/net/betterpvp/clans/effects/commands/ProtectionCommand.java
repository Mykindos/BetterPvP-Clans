package net.betterpvp.clans.effects.commands;

import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import org.bukkit.entity.Player;

public class ProtectionCommand extends Command {

	public ProtectionCommand() {
		super("protection", new String[]{"prot"}, Rank.PLAYER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Player p, String[] args) {
		if(EffectManager.hasEffect(p, EffectType.PROTECTION)){
			EffectManager.removeEffect(p, EffectType.PROTECTION);
			UtilMessage.message(p, "Protected", "You no longer have protection, be careful!");
		}else{
			UtilMessage.message(p, "Protected", "You do not have protection.");
		}
		
	}

	@Override
	public void help(Player player) {
		// TODO Auto-generated method stub
		
	}

}
