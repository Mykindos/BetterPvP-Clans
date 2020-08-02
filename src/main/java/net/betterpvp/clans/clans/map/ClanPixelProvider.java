package net.betterpvp.clans.clans.map;

import io.github.bananapuncher714.cartographer.core.api.MapPixel;
import io.github.bananapuncher714.cartographer.core.api.events.chunk.ChunkLoadedEvent;
import io.github.bananapuncher714.cartographer.core.api.map.MapPixelProvider;
import io.github.bananapuncher714.cartographer.core.map.Minimap;
import io.github.bananapuncher714.cartographer.core.renderer.PlayerSetting;
import io.github.bananapuncher714.cartographer.core.util.MapUtil;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.clans.clans.events.ChunkClaimEvent;
import net.betterpvp.core.framework.BPVPListener;
import net.betterpvp.core.framework.UpdateEvent;
import net.betterpvp.core.utility.UtilFormat;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.Color;
import java.util.*;
import java.util.List;

public class ClanPixelProvider extends BPVPListener<Clans> implements MapPixelProvider {

    public ClanPixelProvider(Clans instance) {
        super(instance);
    }

    @Override
    public Collection<MapPixel> getMapPixels(Player player, Minimap minimap, PlayerSetting playerSetting) {
        Set<MapPixel> pixels = new HashSet<>();
        if (Clans.getOptions().isAdvancedMap()) {
            Clan pClan = ClanUtilities.getClan(player);
            for (Clan clan : ClanUtilities.getClans()) {
                if (clan.getTerritory().isEmpty()) continue;
                if (clan.getHome() != null) {
                    if (clan.getHome().getWorld().equals(player.getWorld())) {
                        if (clan.getHome().distance(player.getLocation()) > Clans.getOptions().getAdvancedMapDistance()) {
                            continue;
                        }
                    }
                } else {
                    if (!(clan instanceof AdminClan)) {
                        Chunk cChunk = stringToChunk(clan.getTerritory().get(0));
                        if (cChunk != null) {
                            Block cBlock = cChunk.getBlock(0, player.getLocation().getBlockY(), 0);
                            if (cBlock.getWorld().equals(player.getWorld())) {
                                if (cBlock.getLocation().distance(player.getLocation()) > Clans.getOptions().getAdvancedMapDistance())
                                    continue;
                            }
                        }
                    }
                }

                List<Location> outline = new ArrayList<>(clan.getChunkOutlines());
                if (clan.getChunkOutlines().isEmpty()) continue;


                for (Location loc : outline) {
                    if (!loc.getWorld().equals(player.getWorld())) continue;
                    Location locCompare = loc.clone();
                    locCompare.setY(player.getLocation().getY());
                    if (locCompare.distance(player.getLocation()) > Clans.getOptions().getAdvancedMapDistance())
                        continue;
                    int[] pixelLoc = MapUtil.getLocationToPixel(playerSetting.getLocation(), loc, playerSetting.getScale(),
                            Math.toRadians(playerSetting.isRotating() ? (player.getLocation().getYaw() + 180) : 0));
                    Color color = Color.YELLOW;
                    if (pClan != null) {
                        ClanUtilities.ClanRelation relation = ClanUtilities.getRelation(pClan, clan);
                        if (relation == ClanUtilities.ClanRelation.SELF) {
                            color = Color.CYAN;
                        } else if (relation == ClanUtilities.ClanRelation.ALLY) {
                            color = Color.GREEN;
                        } else if (relation == ClanUtilities.ClanRelation.ALLY_TRUST) {
                            color = Color.GREEN;
                        } else if (relation == ClanUtilities.ClanRelation.ENEMY) {
                            color = Color.RED;
                        } else if (relation == ClanUtilities.ClanRelation.PILLAGE) {
                            color = Color.MAGENTA;
                        }
                    }

                    if (clan instanceof AdminClan) {
                        if (((AdminClan) clan).isSafe()) {
                            color = Color.WHITE;
                        } else {
                            if (clan.getName().equals("Fields") || clan.getName().equals("Lake")) {
                                color = Color.RED;
                            } else {
                                color = Color.PINK;
                            }
                        }
                    }

                    MapPixel pixel = new MapPixel(pixelLoc[0], pixelLoc[1], color);
                    if(!clan.getName().equals("Outskirts")){
                        pixel.setPriority(999);
                    }
                    pixels.add(pixel);
                }


            }

        }

        return pixels;
    }

    private static final BlockFace[] BLOCK_FACES = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    public static List<Location> getChunkOutline(Clan clan, Chunk chunk) {
        List<Location> temp = new ArrayList<>();

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                if (z == 0 || z == 15 || x == 0 || x == 15) {
                    Block block = chunk.getBlock(x, 0, z);
                    boolean add = false;
                    try {
                        for (BlockFace f : BLOCK_FACES) {
                            if (add) continue;

                            if (block.getWorld().isChunkLoaded((block.getLocation().getBlockX() + f.getModX()) / 16, (block.getLocation().getBlockZ() + f.getModZ()) / 16)) {


                                Location relLoc = block.getLocation().add(f.getModX(), f.getModY(), f.getModZ());

                                Clan relClan = ClanUtilities.getClanByChunkString(relLoc.getWorld().getName() + "/ "
                                        + ((int) (Math.floor(relLoc.getX() / 16))) + "/ " + ((int) (Math.floor(relLoc.getZ() / 16))));
                                if (relClan == null) {
                                    add = true;
                                } else {
                                    if (!relClan.equals(clan)) {
                                        add = true;
                                    }
                                }


                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (add) {
                        temp.add(block.getLocation());
                    }
                }
            }
        }

        return temp;

    }

    @EventHandler
    public void onChunkClaim(ChunkClaimEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                List<Location> locs = getChunkOutline(e.getClan(), e.getChunk());
                for (Location loc : locs) {
                    if (!e.getClan().getChunkOutlines().contains(loc)) {
                        e.getClan().getChunkOutlines().add(loc);
                    }
                }
            }
        }.runTaskAsynchronously(getInstance());

    }


    public Chunk stringToChunk(String string) {
        try {
            String[] tokens = string.split("/ ");
            World world = Bukkit.getWorld(tokens[0]);
            if (world != null) {
                if (world.isChunkLoaded(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]))) {
                    Chunk chunk = world.getChunkAt(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                    return chunk;
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @EventHandler
    public void onUpdate(UpdateEvent e) {
        if (e.getType() == UpdateEvent.UpdateType.SEC_30) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Clan clan : ClanUtilities.getClans()) {

                        for (String s : clan.getTerritory()) {
                            try {
                                Chunk chunk = stringToChunk(s);
                                if (chunk != null) {
                                    List<Location> temp = getChunkOutline(clan, chunk);
                                    for (Location loc : temp) {
                                        if (!clan.getChunkOutlines().contains(loc)) {
                                            clan.getChunkOutlines().add(loc);
                                        }
                                    }

                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }.runTaskAsynchronously(getInstance());

        }
    }

}
