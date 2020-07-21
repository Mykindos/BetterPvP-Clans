package net.betterpvp.clans.clans.map;

import io.github.bananapuncher714.cartographer.core.api.MapPixel;
import io.github.bananapuncher714.cartographer.core.api.map.MapPixelProvider;
import io.github.bananapuncher714.cartographer.core.map.Minimap;
import io.github.bananapuncher714.cartographer.core.renderer.PlayerSetting;
import io.github.bananapuncher714.cartographer.core.util.MapUtil;
import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.clans.clans.ClanUtilities;
import net.betterpvp.core.utility.UtilBlock;
import net.betterpvp.core.utility.UtilFormat;
import net.betterpvp.core.utility.UtilLocation;
import net.betterpvp.core.utility.restoration.BlockRestoreData;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ClanPixelProvider implements MapPixelProvider {

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
                        Chunk cChunk = UtilFormat.stringToChunk(clan.getTerritory().get(0));
                        if (cChunk != null) {
                            Block cBlock = cChunk.getBlock(0, playerSetting.getLocation().getBlockY(), 0);
                            if (cBlock.getLocation().distance(playerSetting.getLocation()) > Clans.getOptions().getAdvancedMapDistance()) continue;
                        }
                    }
                }

                List<Location> outline = clan.getChunkOutlines();
                if (clan.getChunkOutlines().isEmpty()) {
                    for (String s : clan.getTerritory()) {
                        Chunk chunk = UtilFormat.stringToChunk(s);

                        List<Location> temp = getChunkOutline(chunk);
                        outline.addAll(temp);
                    }
                }


                for (Location loc : outline) {
                    if(!loc.getWorld().equals(player.getWorld())) continue;
                    if (loc.distance(playerSetting.getLocation()) > Clans.getOptions().getAdvancedMapDistance()) continue;
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
                            color = Color.ORANGE;
                        }
                    }

                    MapPixel pixel = new MapPixel(pixelLoc[0], pixelLoc[1], color);
                    pixels.add(pixel);
                }


            }

        }

        return pixels;
    }

    public List<Location> getChunkOutline(Chunk chunk) {
        List<Location> temp = new ArrayList<>();

        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                if (z == 0 || z == 15 || x == 0 || x == 15) {
                    temp.add(chunk.getBlock(x, 0, z).getLocation());
                }
            }
        }

        return temp;


    }
}
