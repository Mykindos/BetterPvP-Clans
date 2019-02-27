package net.betterpvp.clans.skills.selector.skills;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.classes.Energy;
import net.betterpvp.clans.classes.Role;
import net.betterpvp.clans.effects.EffectManager;
import net.betterpvp.clans.effects.EffectType;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.skills.ISkill;
import net.betterpvp.clans.skills.Types;
import net.betterpvp.clans.skills.selector.BuildSkill;
import net.betterpvp.clans.skills.selector.RoleBuild;
import net.betterpvp.clans.weapon.EnchantedWeapon;
import net.betterpvp.clans.weapon.Weapon;
import net.betterpvp.clans.weapon.WeaponManager;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilItem;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public abstract class Skill extends BPVPListener<Clans> implements ISkill {

	private String name;
	private Material[] items;
	private Action[] actions;
	private int maxLevel;
	private String classType;
	private boolean showRecharge;
	private boolean useInteract;


	public Skill(Clans i, String name, String classType, Material[] items, Action[] actions, int maxLevel, boolean showRecharge, boolean useInteract) {
		super(i);
		this.name = name;
		this.items = items;
		this.actions = actions;
		this.maxLevel = maxLevel;
		this.classType = classType;
		this.showRecharge = showRecharge;
		this.useInteract = useInteract;


	}

	/**
	 * Name of the skill
	 * E.g. Seismic Slam
	 * @return Returns the name of the Skill
	 */
	public String getName() {
		return name;
	}

	public boolean isCancellable() {
		return false;
	}

	/**
	 * Name of the skill with level
	 * @param level Level of the skill
	 * @return Returns the skills name with its level appended onto the end
	 */
	public String getName(int level) {
		return name + " " + level;
	}




	/**
	 * Class that the skill belongs to
	 * @return Returns the name of the class the skill belongs to
	 */
	public String getClassType(){
		return classType;
	}


	/**
	 * The materials (tools) that can be used to cast this skill
	 * @return Returns an array of Materials that can be used to cast this skill
	 */
	public Material[] getMaterials() {
		return items;
	}

	/**
	 * The actions (Left / Right Click) that can be used to cast this skill
	 * @return Returns an array of actions that can be used to cast this skill
	 */
	public Action[] getActions() {
		return actions;
	}


	/**
	 * That skills max level
	 * @return Returns the max level of this skill
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * Whether or not the recharge information is shown to the caster
	 * @return Returns true if the recharge information will be shown to the caster
	 */
	public boolean showRecharge() {
		return showRecharge;
	}

	/**
	 * Whether or not Left / Right click is needed to cast the skill
	 * @return True if interaction is required to cast this skill
	 */
	public boolean useInteract() {
		return useInteract;
	}


	/**
	 * The description of the skill
	 * The description often changes based on the skills level
	 * @param level The level of the skill
	 * @return Returns the description of the skill as an array
	 */
	public abstract String[] getDescription(int level);


	/**
	 * The type of skill it is
	 * Types: AXE, SWORD, BOW, PASSIVE_A, PASSIVE_B, GLOBAL
	 * @return Returns the skills type
	 */
	public abstract Types getType();

	/**
	 * The recharge time of the skills depending on its level
	 * @param level The level of the skill
	 * @return Returns the recharge time of the skill
	 */
	public abstract double getRecharge(int level);


	/**
	 * The energy usage per cast or second of the skill
	 * @param level The level of the skill
	 * @return Returns the energy usage of the skill
	 */
	public abstract float getEnergy(int level);

	/**
	 * 1.9+ Support
	 * Skills that used sword blocking in 1.8.9 could use a shield in 1.9+ to accomplish
	 * the same result
	 * @return Returns true if a shield is required to cast the skill
	 */
	public abstract boolean requiresShield();

	/**
	 * The method used to perform a skill
	 * @param p The player casting the skill
	 */
	public abstract void activateSkill(Player p);

	/**
	 * Checks if a skill is currently usable
	 * Generally returns false if a player is in a safe zone or no cast zone
	 * @param p The player
	 * @return Returns true if the player can cast an ability
	 */
	public abstract boolean usageCheck(Player p);


	/**
	 * Gets the level of the skill the player has equipped
	 * As the level increases, skills often receive shorter cooldowns
	 * and less energy usage
	 * @param p The player to check
	 * @return Returns the level of the skill in the players build
	 */
	public int getLevel(Player p){
		int level = 1;
		Gamer g = GamerManager.getOnlineGamer(p);

			if(g != null) {
				if(g.getActiveBuild(getClassType()) != null){
					level = g.getActiveBuild(getClassType()).getBuildSkill(getType()).getLevel();
				}else{
					Role r = Role.getRole(p);
					if(r != null){
						RoleBuild rb = g.getActiveBuild(r.getName());
						if(rb != null) {
							level = rb.getBuildSkill(getType()).getLevel();
						}
					}
				}

		}
		if(getType() == Types.AXE || getType() == Types.SWORD){
			if(p.getItemInHand().getType() == Material.DIAMOND_SWORD || p.getItemInHand().getType() == Material.DIAMOND_AXE){
				level++;
			}
		}
		return level;
	}



	public boolean hasSwordInMainHand(Player p){
		if(p.getItemInHand() != null){
			return p.getItemInHand().getType().name().contains("SWORD");
		}
		return false;
	}

	/**
	 * Check if a player has a skill in their active build
	 * @param p Player to check
	 * @param s Skill to check for
	 * @return Returns true if the player has the skill in their active build
	 */
	public boolean hasSkill(Player p, Skill s){
		Gamer g = GamerManager.getOnlineGamer(p);

		if(g != null){
			return hasSkill(g, p, s);
		}

		return false;
	}

	/**
	 * Check if a player has a skill in their active build
	 * @param g The gamer to check
	 * @param p Player to check
	 * @param s Skill to check for
	 * @return Returns true if the player has the skill in their active build
	 */
	public boolean hasSkill(Gamer g, Player p, Skill s){


			if(g != null){
				if(g.getActiveBuild(getClassType()) != null){
					if(g.getActiveBuild(getClassType()).getBuildSkill(getType()) != null){
						Role r = Role.getRole(p);
						if(r != null && r.getName().equals(s.getClassType())){
							return g.getActiveBuild(getClassType()).getBuildSkill(getType()).getSkill().equals(s);
						}
					}
				}
			}


		return false;
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onSkillActivate(PlayerInteractEvent event) {

		Player player = event.getPlayer();
	
		if(player.getItemInHand() == null){
			return;
		}

		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			Block b = event.getClickedBlock();
			if(b.getType() == Material.SPONGE){
				if(b.getLocation().getY() < player.getLocation().getY()){
					return;
				}
			}else if(b.getType() == Material.IRON_DOOR || b.getType() == Material.IRON_DOOR_BLOCK) {
				return;
			}
		}else {
			if(event.getClickedBlock() != null) {
		
			
				if(event.getClickedBlock().getType() == Material.IRON_DOOR 
						|| event.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK) {
					return;
				}
			}
		}


		if (getActions() != null) {
			if (Arrays.asList(getActions()).contains(event.getAction())) {
				if (Arrays.asList(getMaterials()).contains(player.getItemInHand().getType())) {
					Weapon weapon = WeaponManager.getWeapon(player.getItemInHand());

					if(weapon == null || weapon != null && weapon instanceof EnchantedWeapon){
						Role role = Role.getRole(player);

						if (role != null && role.getName().equals(getClassType())) {

							if (useInteract()) {
								if(UtilItem.isBlockDoor(player)){
									
								}
								
								Gamer gamer = GamerManager.getOnlineGamer(player);
								RoleBuild rb = gamer.getActiveBuild(role.getName());
								if(rb == null){
									gamer.setActiveBuild(gamer.getBuild(Role.getRole(player).getName(), 1));
								}

								BuildSkill bs = rb.getBuildSkill(getType());
								if(bs != null){

									Skill s = bs.getSkill();
									if(s != null){
										if(s.getName().equals(getName())){
											if(!EffectManager.hasEffect(player, EffectType.SILENCE)){
												if(ClanUtilities.canCast(player)){
													if (usageCheck(player)) {
														if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK){
															if(UtilBlock.usable(event.getClickedBlock())){
																return;
															}
														}

														if(0.999 * (s.getEnergy(getLevel(player)) / 100) < Energy.getEnergy(player)){
															if(getRecharge(bs.getLevel()) == 0){
																
																s.activateSkill(player);
																return;
															}
															if (RechargeManager.getInstance().add(player, s.getName(), getRecharge(getLevel(player)), s.showRecharge(), true, s.isCancellable())) {
																if (Energy.use(player, s.getName(), getEnergy(getLevel(player)), true)) {
																	s.activateSkill(player);

																}
															}
														}else{
															UtilMessage.message(player, "Energy", "You are too exhausted to use " + ChatColor.GREEN + s.getName(getLevel(player)) + ChatColor.GRAY + ".");
														}
													}
												}
											}else{
												UtilMessage.message(player, "Effect", "You are silenced!");
											}
										}
									}
								}

							}
						}
					}
				}
			}
		}

	}

}
