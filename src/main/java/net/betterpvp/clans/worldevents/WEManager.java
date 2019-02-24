package net.betterpvp.clans.worldevents;


import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.gamer.Gamer;
import net.betterpvp.clans.gamer.GamerManager;
import net.betterpvp.clans.worldevents.types.Boss;
import net.betterpvp.clans.worldevents.types.TimedEvents.UndeadCamp;
import net.betterpvp.clans.worldevents.types.bosses.Broodmother;
import net.betterpvp.clans.worldevents.types.bosses.SkeletonKing;
import net.betterpvp.clans.worldevents.types.bosses.SlimeKing;
import net.betterpvp.clans.worldevents.types.bosses.Witherton;
import net.betterpvp.clans.worldevents.types.environmental.FishingFrenzy;
import net.betterpvp.clans.worldevents.types.environmental.MiningMadness;
import net.betterpvp.core.client.Client;
import net.betterpvp.core.client.ClientUtilities;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.utility.UtilMath;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.UtilTime;

import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class WEManager extends BPVPListener<Clans> {

	private static List<WorldEvent> worldEvents = new ArrayList<>();

	private static Location[] returnLocs;
	private static Location[] bossLocs;

	public WEManager( Clans i){
		super(i);

		loadItems();
		addWorldEvent(new SlimeKing(i));
		addWorldEvent(new Broodmother(i));
		addWorldEvent(new SkeletonKing(i));
		addWorldEvent(new Witherton(i));
		
		addWorldEvent(new FishingFrenzy(i));
		addWorldEvent(new MiningMadness(i));

		World world = Bukkit.getWorld("world");
		returnLocs = new Location[] {new Location(world, -17, 68, -96),
				new Location(world, 3, 58, -98),
				new Location(world, -6, 44, -75),
				new Location(world, 25, 43, -24)};
		bossLocs = new Location[] {new Location(world, -44.5, 14, -105.5),
				new Location(world, -44.5, 14, -11.5),
				new Location(world, 35.5, 14, -11.5),
				new Location(world, 35.5, 14, -105.5)

		};
		addWorldEvent(new UndeadCamp(i));





		new BukkitRunnable(){

			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() >= Clans.getOptions().MinPlayersForWorldEvent()){
					if(!isWorldEventActive()){
						WorldEvent we = getWorldEvents().get(new Random().nextInt(getWorldEvents().size()));
						if(!we.getSpawn().getChunk().isLoaded()){
							we.getSpawn().getChunk().load();
						}
						we.spawn();
						we.setActive(true);
						announce();
					}
				}
			}

		}.runTaskTimer(i, 1200L, 72000L);


		new BukkitRunnable(){
			@Override
			public void run(){
				if(isWorldEventActive()){
					WorldEvent we = getActiveWorldEvent();
					if(we.getType() == WEType.BOSS){
						Boss b = (Boss) we;
						if(b.getBoss() != null && !b.getBoss().isDead()){
						//	UtilMessage.broadcast("World Event", ChatColor.YELLOW + ChatColor.stripColor(b.getBossName()) + ChatColor.GRAY
									///+ " is near " + ChatColor.YELLOW + UtilLocation.locationToString(b.getBoss().getLocation()));
						}
					}
				}
			}
		}.runTaskTimer(i, 0, 6000);
	}

	public static void teleportToBoss(Player p) {
		Gamer c = GamerManager.getOnlineGamer(p);
		if(c != null) {

			if(UtilTime.elapsed(c.getGamer().getLastDamaged(), 15000)) {


				p.teleport(bossLocs[UtilMath.randomInt(bossLocs.length)]);
				UtilMessage.message(p, "World Event", "You teleported to the Boss Arena");
			}
		}
	}
	
	public static void teleportOutOfBoss(Player p) {
		Client c = ClientUtilities.getOnlineClient(p);
		if(c != null) {

			if(UtilTime.elapsed(c.getGamer().getLastDamaged(), 15000)) {


				p.teleport(returnLocs[UtilMath.randomInt(returnLocs.length)]);
				UtilMessage.message(p, "World Event", "You teleported back to Fields.");
			}
			
		}
	}

	@EventHandler
	public void onSignInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.WALL_SIGN || e.getClickedBlock().getType() == Material.SIGN_POST) {
				Clan c = ClanUtilities.getClan(e.getClickedBlock().getLocation());
				if(c != null) {
					if(c instanceof AdminClan) {
						Sign s = (Sign) e.getClickedBlock().getState();

						Gamer gamer = GamerManager.getOnlineGamer(e.getPlayer());

						if(UtilTime.elapsed(gamer.getLastDamaged(), 15000)) {
							if(s.getLine(1).equalsIgnoreCase("[Boss]")) {

								e.getPlayer().teleport(bossLocs[UtilMath.randomInt(bossLocs.length)]);
								UtilMessage.message(e.getPlayer(), "World Event", "You teleported to the Boss Arena");

							}else if(s.getLine(1).equalsIgnoreCase("[Exit]")) {
								e.getPlayer().teleport(returnLocs[UtilMath.randomInt(returnLocs.length)]);
								UtilMessage.message(e.getPlayer(), "World Event", "You teleported back to Fields.");
							}
						}else {
							UtilMessage.message(e.getPlayer(), "World Event", "You cannot teleport while in combat.");
						}

					}
				}


			}
		}
	}

	@EventHandler
	public void onUnload(ChunkUnloadEvent e){
		for(Entity ent : e.getChunk().getEntities()){
			if(ent instanceof LivingEntity){
				if(ent.getCustomName() != null && !ent.getCustomName().equals("")){
					e.setCancelled(true);
				}
			}
		}
	}

	public static List<WorldEvent> getWorldEvents(){
		return worldEvents;
	}

	public void addWorldEvent(WorldEvent we){
		if(!getWorldEvents().contains(we)){
			worldEvents.add(we);
		}
	}
	
	public static boolean isEventActive(String event) {
		for(WorldEvent we : getWorldEvents()) {
			if(we.getName().equalsIgnoreCase(event)) {
				if(we.isActive()) {
					return true;
				}
			}
		}
		return false;
		
	}

	public static WorldEvent getActiveWorldEvent(){
		for(WorldEvent we : getWorldEvents()){
			if(we.isActive()){
				return we;
			}
		}

		return null;
	}

	public static boolean isWorldEventActive(){
		return getActiveWorldEvent() != null;
	}

	public static WorldEvent getWorldEvent(String name){
		for(WorldEvent we : getWorldEvents()){
			if(we.getName().equalsIgnoreCase(name)){
				return we;
			}
		}

		return null;
	}



	public static void announce(){
		if(isWorldEventActive()){
			if(getActiveWorldEvent() instanceof Boss){
				Boss b = (Boss) getActiveWorldEvent();
				b.lastDamaged = System.currentTimeMillis();
				Location loc = b.getSpawn();
				if(loc != null && b.getBoss() != null){
					UtilMessage.broadcast("World Event", ChatColor.YELLOW + ChatColor.stripColor(b.getBossName()) + ChatColor.GRAY + " has entered the world. ");
					for (Player p : Bukkit.getOnlinePlayers()) {
						Titles.sendTitle(p, 20, 20, 20, ChatColor.YELLOW + ChatColor.stripColor(b.getBossName()), ChatColor.GRAY + "Has entered the world.");
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 0.1F, 1F);
					}
				}
			}else if(getActiveWorldEvent() instanceof Environmental) {
				Environmental e = (Environmental) getActiveWorldEvent();
				UtilMessage.broadcast("World Event", ChatColor.YELLOW + ChatColor.stripColor(e.getDisplayName()) + ChatColor.GRAY + " has started! ");
				e.subAnnounce();
				for (Player p : Bukkit.getOnlinePlayers()) {
					Titles.sendTitle(p, 20, 20, 20, ChatColor.YELLOW + ChatColor.stripColor(e.getDisplayName()), ChatColor.GRAY + "has started!");
					p.playSound(p.getLocation(), Sound.WOLF_HOWL, 1.5F, 1F);
				}
			}
		}
	}



	public static List<Drop> dropList = new ArrayList<>();
	//private static HashMap<ItemStack, Double> dropList = new HashMap<>();
	private void loadItems() {
		for (Weapon weapons : Weapon.weapons) {
			if (weapons.isLegendary()) {
				dropList.add(new Drop(weapons.createWeapon(), weapons.getChance()));
			}

			if(weapons instanceof EnchantedWeapon){
				dropList.add(new Drop(weapons.createWeapon(), weapons.getChance()));
			}
		}

		/*
		dropList.put(UtilPlayer.createItem(Material.DIAMOND_SWORD, 1, ChatColor.YELLOW + "Power Sword"), 12.5);
		dropList.put(UtilPlayer.createItem(Material.DIAMOND_AXE, 1, ChatColor.YELLOW + "Power Axe"), 12.5);
		 */
	}

	public static ItemStack getRandomItem() {

		double sumWeights = 0; // total sum of the weights of items

		Collections.shuffle(dropList);
		// loop through all items adding each weighted value
		// NB: the values DO NOT have to equal 1
		for (Drop map : dropList) {
			sumWeights += map.getChance();
		}


		double randNum = UtilMath.randDouble(0, sumWeights);
		double sum = 0;
		for (Drop map : dropList) {
			sum += map.getChance();
			if (randNum <= sum) {

				return map.getItemStack();
			}
		}

		return null;
	}

}
