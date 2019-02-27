package net.betterpvp.clans.clans.map;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ChunkClaimEvent;
import net.betterpvp.clans.clans.map.NMS.MaterialMapColorInterface;
import net.betterpvp.clans.clans.map.commands.MapCommand;
import net.betterpvp.clans.clans.map.events.MinimapExtraCursorEvent;
import net.betterpvp.clans.clans.map.events.MinimapPlayerCursorEvent;
import net.betterpvp.core.command.CommandManager;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.map.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MinimapRenderer
extends MapRenderer
implements Listener
{
	private Map<String, Map<Integer, Map<Integer, MapChunk>>> worldCacheMap = new TreeMap<>();
	public static WeakHashMap<Player, PlayerMapData> usedMaps = new WeakHashMap<>();
	public List<Short> used = new ArrayList<>();
	protected Queue<Coords> queue = new LinkedList<>();
	private RenderTask cacheTask = new RenderTask(this);
	private SendTask sendTask = new SendTask();
	private int globalScale = 0;
	private int cpr = 0;
	private int colorlimit;
	private Clans plugin;

	public MinimapRenderer(int scale, int cpr, Clans plugin)
	{
		super(true);
		CommandManager.addCommand(new MapCommand(this));
		this.plugin = plugin;
		this.cpr = cpr;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		this.globalScale = (scale < 1 ? 1 : scale);
		this.colorlimit = (this.globalScale * this.globalScale / 2);

		this.cacheTask.runTaskTimer(plugin, 10, 10);
		this.sendTask.runTaskTimer(plugin, 20, 40);
	}

	public int getDefaultScale()
	{
		return this.globalScale;
	}

	public int getChunksPerRun()
	{
		return this.cpr;
	}

	public Queue<Coords> getQueue()
	{
		return this.queue;
	}

	@SuppressWarnings("deprecation")
	public void render(final MapView map, final MapCanvas canvas, final Player player) {
		if (player.getWorld().hasMetadata("minimap.disabled")) {
			return;
		}
		if (!this.worldCacheMap.containsKey(player.getWorld().getName())) {
			this.worldCacheMap.put(player.getWorld().getName(), new TreeMap<Integer, Map<Integer, MapChunk>>());
		}
		final Map<Integer, Map<Integer, MapChunk>> cacheMap = this.worldCacheMap.get(player.getWorld().getName());
		final int scale = (player.getWorld().hasMetadata("minimap.scale")
				&& !player.getWorld().getMetadata("minimap.scale").isEmpty()) 
				? player.getWorld().getMetadata("minimap.scale").get(0).asInt() : this.getDefaultScale();
				final int locX = player.getLocation().getBlockX() / scale - 64;
				final int locZ = player.getLocation().getBlockZ() / scale - 64;

				for (int i = 0; i < 128; ++i) {
					for (int j = 0; j < 128; ++j) {
						int x = (int)((locX + i) / 16.0);
						if (locX + i < 0 && (locX + i) % 16 != 0) {
							--x;
						}
						int z = (int)((locZ + j) / 16.0);
						if (locZ + j < 0 && (locZ + j) % 16 != 0) {
							--z;
						}


						if (cacheMap.containsKey(x) && cacheMap.get(x).containsKey(z)) {

							final MaterialMapColorInterface color = cacheMap.get(x).get(z).get(Math.abs(locX + i + 16 * Math.abs(x)) % 16, Math.abs(locZ + j + 16 * Math.abs(z)) % 16);
							final short avgY = cacheMap.get(x).get(z).getY(Math.abs(locX + i + 16 * Math.abs(x)) % 16, Math.abs(locZ + j + 16 * Math.abs(z)) % 16);
							final short prevY = this.getPrevY(x, z, Math.abs(locX + i + 16 * Math.abs(x)) % 16, Math.abs(locZ + j + 16 * Math.abs(z)) % 16, player.getWorld().getName());
							final double d2 = (avgY - prevY) * 4.0 / (scale + 4) + ((i + j & 0x1) - 0.5) * 0.4;

							byte b0 = 1;
							if (d2 > 0.6) {
								b0 = 2;
							}
							if (d2 < -0.6) {
								b0 = 0;
							}


							canvas.setPixel(i, j, (byte)(color.getM() * 4 + b0));






						}else {
							canvas.setPixel(i, j, (byte)0);
							if (this.queue.size() >= 200) {
								break;
							}
							this.addToQueue(x, z, true, player.getWorld().getName());
						}
					}
				}

				final MapCursorCollection cursors = canvas.getCursors();
				
				while (cursors.size() > 0) {
					cursors.removeCursor(cursors.getCursor(0));
				}
				
				MinimapExtraCursorEvent e = new MinimapExtraCursorEvent(player, cursors, scale);
				plugin.getServer().getPluginManager().callEvent(e);
				
	}


	@EventHandler(priority=EventPriority.LOWEST)
	public void onCursor(MinimapExtraCursorEvent e)
	{
		Player player = e.getPlayer();
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(player.getWorld()))
			{


				float yaw = p.getLocation().getYaw();
				if (yaw < 0.0F) {
					yaw += 360.0F;
				}
				byte direction = (byte)(int)((Math.abs(yaw) + 11.25D) / 22.5D);
				if (direction > 15) {
					direction = 0;
				}
				int x = p.getLocation().getBlockX();
				int z = p.getLocation().getBlockZ();

				Clan aClan = ClanUtilities.getClan(player);
				Clan bClan = ClanUtilities.getClan(p);
				MinimapPlayerCursorEvent event = null;
				if(aClan == null) {
					if(player == p) {
						event = new MinimapPlayerCursorEvent(player, p, true, MapCursor.Type.WHITE_POINTER);
					}
				}else {
					if(bClan != null) {
						if(aClan == bClan) {
							event = new MinimapPlayerCursorEvent(player, p, true, MapCursor.Type.BLUE_POINTER);
						}else if(aClan.isAllied(bClan)) {
							event = new MinimapPlayerCursorEvent(player, p, true, MapCursor.Type.GREEN_POINTER);
						}
					}
				}

				if(event != null) {
					Bukkit.getPluginManager().callEvent(event);
					e.getCursors().add(new ExtraCursor(x, z, (player == p) || (event.isCursorShown()), event.getType(), direction, p.getWorld().getName(), false));
				}



			}
		}
		
		for (final ExtraCursor c : e.getCursors()) {
			if (!c.getWorld().equalsIgnoreCase(player.getWorld().getName())) {
				continue;
			}
			int x2 = (c.getX() - player.getLocation().getBlockX()) / e.getScale() * 2;
			int z2 = (c.getZ() - player.getLocation().getBlockZ()) / e.getScale() * 2;
			if (Math.abs(x2) > 127) {
				if (!c.isShownOutside()) {
					continue;
				}
				x2 = ((c.getX() > player.getLocation().getBlockX()) ? 127 : -128);
			}
			if (Math.abs(z2) > 127) {
				if (!c.isShownOutside()) {
					continue;
				}
				z2 = ((c.getZ() > player.getLocation().getBlockZ()) ? 127 : -128);
			}
			e.getCursorCollection().addCursor(x2, z2, c.getDirection(), c.getType().getValue(), c.isVisible());
		}
	}

	public void addToQueue(int x, int y, boolean chunk, String world)
	{
		Coords c = new Coords(x, y, chunk, world);
		if (!this.queue.contains(c)) {
			this.queue.offer(c);
		}
	}

	@SuppressWarnings("rawtypes")
	public void loadData(int x, int z, String world)
	{
		if (!this.worldCacheMap.containsKey(world)) {
			this.worldCacheMap.put(world, new TreeMap<>());
		}
		World w = this.plugin.getServer().getWorld(world);
		int scale = w.hasMetadata("minimap.scale") ? w.getMetadata("minimap.scale").get(0).asInt() : getDefaultScale();

		Map<Integer, Map<Integer, MapChunk>> cacheMap = this.worldCacheMap.get(world);
		if (!cacheMap.containsKey(Integer.valueOf(x))) {
			cacheMap.put(Integer.valueOf(x), new TreeMap<>());
		}
		if (!cacheMap.get(Integer.valueOf(x)).containsKey(Integer.valueOf(z))) {
			((Map)cacheMap.get(Integer.valueOf(x))).put(Integer.valueOf(z), new MapChunk(this.plugin));
		}
		MapChunk map = (MapChunk) ((Map) cacheMap.get(Integer.valueOf(x))).get(Integer.valueOf(z));

		int initX = x * scale * 16;
		int initZ = z * scale * 16;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				map.set(i, j, renderBlock(initX + i * scale, initZ + j * scale, world));
			}
		}
	}

	private short getPrevY(int x, int z, int i, int j, String world)
	{
		Map<Integer, Map<Integer, MapChunk>> cacheMap = this.worldCacheMap.get(world);

		j--;
		if (j < 0)
		{
			z--;
			j = 15;
		}
		if ((cacheMap.containsKey(Integer.valueOf(x))) && (cacheMap.get(Integer.valueOf(x)).containsKey(Integer.valueOf(z)))) {
			return ((MapChunk)((Map)cacheMap.get(Integer.valueOf(x))).get(Integer.valueOf(z))).getY(i, j);
		}
		return 0;
	}

	public void loadBlock(int initX, int initZ, String world)
	{
		if (!this.worldCacheMap.containsKey(world)) {
			this.worldCacheMap.put(world, new TreeMap());
		}
		Map<Integer, Map<Integer, MapChunk>> cacheMap = this.worldCacheMap.get(world);

		World w = this.plugin.getServer().getWorld(world);
		int scale = (w.hasMetadata("minimap.scale")) && (!w.getMetadata("minimap.scale").isEmpty()) ? w.getMetadata("minimap.scale").get(0).asInt() : getDefaultScale();

		int locX = initX / scale;
		int locZ = initZ / scale;

		int x = (int)(locX / 16.0D);
		if ((locX < 0) && (locX % 16 != 0)) {
			x--;
		}
		int z = (int)(locZ / 16.0D);
		if ((locZ < 0) && (locZ % 16 != 0)) {
			z--;
		}
		int sx = Math.abs(locX + 16 * Math.abs(x)) % 16;
		int sz = Math.abs(locZ + 16 * Math.abs(z)) % 16;
		if (!cacheMap.containsKey(Integer.valueOf(x))) {
			return;
		}
		if (!cacheMap.get(Integer.valueOf(x)).containsKey(Integer.valueOf(z))) {
			return;
		}
		MapChunk map = (MapChunk)((Map)cacheMap.get(Integer.valueOf(x))).get(Integer.valueOf(z));
		map.set(sx, sz, renderBlock((x * 16 + sx) * scale, (z * 16 + sz) * scale, world));
	}

	public RenderResult renderBlock(int baseX, int baseZ, String strworld)
	{
		Map<MaterialMapColorInterface, Integer> colors = new HashMap();
		short avgY = 0;
		MaterialMapColorInterface mainColor = null;
		World world = this.plugin.getServer().getWorld(strworld);
		int scale = (world.hasMetadata("minimap.scale")) && (!world.getMetadata("minimap.scale").isEmpty()) 
				? world.getMetadata("minimap.scale").get(0).asInt() : getDefaultScale();

		boolean changedHeight = (world.hasMetadata("minimap.drawheight")) && (!world.getMetadata("minimap.drawheight").isEmpty());
		int y = changedHeight ? world.getMetadata("minimap.drawheight").get(0).asInt() : 0;
		Block b;
		for (int k = 0; k < scale; k++) {
			for (int l = 0; l < scale; l++)
			{
				if (!changedHeight) {
					y = world.getHighestBlockYAt(baseX + k, baseZ + l) + 1;
				}
				b = world.getBlockAt(baseX + k, y, baseZ + l);
				if (!b.getChunk().isLoaded()) {
					b.getChunk().load();
				}
				b = world.getHighestBlockAt(b.getX(), b.getZ());
				
				Clan c = ClanUtilities.getClan(b.getChunk());
				boolean outline = false;
				if(c != null) {
					outline = UtilLocation.chunkOutline(b.getChunk(), b.getLocation());
				}

				while ((b.getY() > 0) && (this.plugin.getNMSHandler().getBlockColor(c, outline, b) 
						== this.plugin.getNMSHandler().getColorNeutral())) {
					b = world.getBlockAt(b.getX(), b.getY() - 1, b.getZ());
				}
				avgY = (short)(avgY + b.getY());
				if (mainColor == null) 
				{
					MaterialMapColorInterface color = this.plugin.getNMSHandler().getBlockColor(c, outline, b);

					int value = colors.containsKey(color) ? colors.get(color).intValue() + 1 : 1;
					colors.put(color, Integer.valueOf(value));
					if (colors.get(color).intValue() >= this.colorlimit) {
						mainColor = color;
					}
				}
			}
		}
		avgY = (short)(avgY / scale);
		if (mainColor == null)
		{
			int max = 0;
			for (Map.Entry<MaterialMapColorInterface, Integer> c : colors.entrySet()) {
				if (c.getValue().intValue() > max)
				{
					max = c.getValue().intValue();
					mainColor = c.getKey();
				}
			}
		}
		return new RenderResult(mainColor, avgY);
	}

	private void handleBlockEvent(Location loc)
	{

		if (loc.getBlockY() >= loc.getWorld().getHighestBlockYAt(loc) - 1) {
			addToQueue(loc.getBlockX(), loc.getBlockZ(), false, loc.getWorld().getName());
		}
	}

	private void handleBlockEvent(Block e)
	{
		Location loc = e.getLocation();
		if (loc.getBlockY() >= loc.getWorld().getHighestBlockYAt(loc) - 1) {
			addToQueue(loc.getBlockX(), loc.getBlockZ(), false, e.getWorld().getName());
		}
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockPlaceEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockFromToEvent e)
	{
		handleBlockEvent(e.getBlock());
		handleBlockEvent(e.getToBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockPhysicsEvent e)
	{
		switch (e.getChangedType())
		{
		case APPLE: 
		case ARMOR_STAND: 
		case ARROW: 
		case BAKED_POTATO: 
			handleBlockEvent(e.getBlock());
			break;
		}
	}

	@EventHandler
	public void onClaim(ChunkClaimEvent e) {
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				final int x1 = x;
				final int z1 = z;
				new BukkitRunnable() {

					@Override
					public void run() {
						for(int i = -2; i < 2; i++) {
							Location loc = e.getChunk().getWorld().getHighestBlockAt(e.getChunk().getBlock(x1, 0, z1).getLocation()).getLocation();
							handleBlockEvent(loc.add(x1 + i, loc.getY(), z1 + i));
						}

					}
				}.runTaskLater(plugin, (x + z) * 5);
			}
		}


	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockBreakEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockBurnEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockFadeEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockFormEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockGrowEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(BlockSpreadEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onBlockEvent(EntityBlockFormEvent e)
	{
		handleBlockEvent(e.getBlock());
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		usedMaps.put(e.getPlayer(), new PlayerMapData());
		usedMaps.get(e.getPlayer()).used.add((short) 0);
	}

	@EventHandler
	public void onUpdate(UpdateEvent e) {
		if(e.getType() == UpdateEvent.UpdateType.SEC) {
			for(PlayerMapData d : usedMaps.values()) {
				MapView w = Bukkit.getMap(d.current);
				if(w != null) {
					if(!w.getRenderers().contains(MinimapRenderer.this)) {
						w.addRenderer( MinimapRenderer.this);
					}
				}
			}
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				PlayerMapData data = usedMaps.get(e.getPlayer());
				for(short s : used) {

					if(!data.used.contains(s)) {
						data.current = s;
						data.used.add(s);
						break;
					}
				}


				MapView map2 = Bukkit.getMap((short) (data.current +1));
				if(map2 == null) {
					MapView newMap = Bukkit.createMap(e.getPlayer().getWorld());
					if(!data.used.contains(newMap.getId())) {
						newMap.setScale(Bukkit.getMap((short) 0).getScale());
						if (!(newMap.getRenderers().get(0) instanceof MinimapRenderer)) {
							for (final MapRenderer r :newMap.getRenderers()) {
								newMap.removeRenderer(r);
							}

							newMap.addRenderer( MinimapRenderer.this);

						}

						e.getPlayer().sendMap(newMap);
						if(!used.contains(newMap.getId())) {
							used.add(newMap.getId());
						}

						data.current = newMap.getId();
						data.used.add(newMap.getId());
					}
				}else {
					data.current = map2.getId();

					if(!used.contains(map2.getId())) {
						used.add(map2.getId());
					}
					if(!data.used.contains(map2.getId())) {
						data.used.add(map2.getId());
					}
					if(!map2.getRenderers().contains(MinimapRenderer.this)) {
						map2.addRenderer( MinimapRenderer.this);
					}
				}


			}
		}.runTaskLater(plugin, 20);
	}


	@EventHandler
	public void onTeleport(PlayerChangedWorldEvent e) {

		new BukkitRunnable() {
			@Override
			public void run() {
				PlayerMapData data = usedMaps.get(e.getPlayer());
				for(short s : used) {

					if(!data.used.contains(s)) {
						data.current = s;
						data.used.add(s);
						break;
					}
				}


				MapView map2 = Bukkit.getMap((short) (data.current +1));
				if(map2 == null) {
					MapView newMap = Bukkit.createMap(e.getPlayer().getWorld());
					if(!data.used.contains(newMap.getId())) {
						newMap.setScale(Bukkit.getMap((short) 0).getScale());
						if (!(newMap.getRenderers().get(0) instanceof MinimapRenderer)) {
							for (final MapRenderer r :newMap.getRenderers()) {
								newMap.removeRenderer(r);
							}

							newMap.addRenderer( MinimapRenderer.this);

						}

						e.getPlayer().sendMap(newMap);
						if(!used.contains(newMap.getId())) {
							used.add(newMap.getId());
						}

						data.current = newMap.getId();
						data.used.add(newMap.getId());
					}
				}else {
					data.current = map2.getId();

					if(!used.contains(map2.getId())) {
						used.add(map2.getId());
					}
					if(!data.used.contains(map2.getId())) {
						data.used.add(map2.getId());
					}
					if(!map2.getRenderers().contains(MinimapRenderer.this)) {
						map2.addRenderer( MinimapRenderer.this);
					}
				}


			}
		}.runTaskLater(plugin, 20);

	}



	@EventHandler
	public void onJoinFix(PlayerJoinEvent e) {

		new BukkitRunnable() {
			@Override
			public void run() {
				if(e.getPlayer().getItemInHand().getType() == Material.MAP) {
					PlayerMapData data = usedMaps.get(e.getPlayer());
					for(short s : used) {

						if(!data.used.contains(s)) {
							data.current = s;
							data.used.add(s);
							break;
						}
					}


					MapView map2 = Bukkit.getMap((short) (data.current +1));
					if(map2 == null) {
						MapView newMap = Bukkit.createMap(e.getPlayer().getWorld());
						if(!data.used.contains(newMap.getId())) {
							newMap.setScale(Bukkit.getMap((short) 0).getScale());
							if (!(newMap.getRenderers().get(0) instanceof MinimapRenderer)) {
								for (final MapRenderer r :newMap.getRenderers()) {
									newMap.removeRenderer(r);
								}

								newMap.addRenderer( MinimapRenderer.this);

							}

							e.getPlayer().sendMap(newMap);
							if(!used.contains(newMap.getId())) {
								used.add(newMap.getId());
							}

							data.current = newMap.getId();
							data.used.add(newMap.getId());
						}
					}else {
						data.current = map2.getId();

						if(!used.contains(map2.getId())) {
							used.add(map2.getId());
						}
						if(!data.used.contains(map2.getId())) {
							data.used.add(map2.getId());
						}
						if(!map2.getRenderers().contains(MinimapRenderer.this)) {
							map2.addRenderer( MinimapRenderer.this);
						}
					}

				}

			}
		}.runTaskLater(plugin, 100);

	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.getPlayer().getInventory().remove(Material.MAP);
	}



	public class PlayerMapData {

		public List<Short> used = new ArrayList<>();
		public short current = (short) 0;
	}
}