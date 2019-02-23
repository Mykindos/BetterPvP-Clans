package net.betterpvp.clans.skills.selector.skills.assassin;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.events.CustomDamageEvent;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.events.UpdateEvent;
import net.betterpvp.clans.events.UpdateEvent.UpdateType;
import net.betterpvp.clans.gamer.combat.LogManager;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.skills.Skill;
import net.betterpvp.clans.skills.selector.skills.data.SilencingStrikesData;
import net.betterpvp.clans.skills.selector.skills.paladin.Polymorph;
import net.betterpvp.clans.utility.UtilMessage;
import net.betterpvp.clans.utility.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SilencingStrikes extends Skill{

	public List<SilencingStrikesData> data = new ArrayList<>();

	public SilencingStrikes(Clans i) {
		super(i, "Silencing Strikes", "Assassin", noMaterials, noActions, 3, false, false);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String[] getDescription(int level) {
		// TODO Auto-generated method stub
		return new String[]{
				"Hit a player 3 times within 2 seconds",
				"to silence them for " + ChatColor.GREEN + (level) + ChatColor.GRAY + " seconds."
		};
	}


	@EventHandler
	public void onDamage(CustomDamageEvent e){
		if(e.getCause() == DamageCause.ENTITY_ATTACK){
			if(e.getDamager() instanceof Player && e.getDamagee() instanceof Player){
				Player p = (Player) e.getDamager();
				Player ent = (Player) e.getDamagee();
				if(ClanUtilities.canHurt(p, ent)){
					if(hasSkill(p, this)){
						if(getSilencingStrikesData(p, ent) == null){
							data.add(new SilencingStrikesData(p.getUniqueId(), ent.getUniqueId(), 0));
						}

						if(Polymorph.polymorphed.containsKey(p)){
							return;
						}

						SilencingStrikesData d = getSilencingStrikesData(p, ent);
						d.addCount();
						d.setLastHit(System.currentTimeMillis());
						LogManager.addLog(ent, p, "Silencing Strikes");
						if(d.getCount() == 3){
							if(!EffectManager.hasEffect(ent, EffectType.INVULNERABILITY)) {
								EffectManager.addEffect(ent, EffectType.SILENCE, (long) ((getLevel(p) * 1000) * 0.75));
							}else {
								
								UtilMessage.message(p, getClassType(), ChatColor.GREEN + ent.getName() + ChatColor.GRAY + " is immune to your silence!");
							}
							data.remove(d);
						}
					}
				}
			}
		}
	}


	@EventHandler
	public void onUpdate(UpdateEvent e){
		if(e.getType() == UpdateType.TICK){
			ListIterator<SilencingStrikesData> it = data.listIterator();
			while(it.hasNext()){
				SilencingStrikesData d = it.next();
				if(UtilTime.elapsed(d.getLastHit(), 800)){
					it.remove();
				}
			}
		}
	}

	public SilencingStrikesData getSilencingStrikesData(Player p, Player d){
		for(SilencingStrikesData x : data){
			if(x.getPlayer().equals(p.getUniqueId())){
				if(x.getEntity().equals(d.getUniqueId())){
					return x;
				}
			}
		}
		return null;
	}

	@Override
	public Types getType() {
		// TODO Auto-generated method stub
		return Types.PASSIVE_A;
	}

	@Override
	public double getRecharge(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getEnergy(int level) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean requiresShield() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void activateSkill(Player p) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean usageCheck(Player p) {
		// TODO Auto-generated method stub
		return false;
	}

}
