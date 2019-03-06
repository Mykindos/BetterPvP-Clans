package net.betterpvp.clans.clans.map.commands;

import net.betterpvp.clans.clans.map.MinimapRenderer;
import net.betterpvp.core.client.Rank;
import net.betterpvp.core.command.Command;
import net.betterpvp.core.utility.UtilMessage;
import net.betterpvp.core.utility.recharge.RechargeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapCommand extends Command {

    /**
     *
     */
    private MinimapRenderer renderer;

    public MapCommand(MinimapRenderer renderer) {
        super("map", new String[]{}, Rank.PLAYER);

        this.renderer = renderer;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args == null) {
            if (!player.getInventory().contains(Material.MAP)) {
                if (player.getInventory().firstEmpty() != -1) {
                    UtilMessage.message(player, "Map", "A map was added to your inventory");
                    UtilMessage.message(player, "Map", "If your map is stuck, you can fix it with '" + ChatColor.WHITE + "/map fix" + ChatColor.GRAY + "'");
					/*
				for(int i = 36; i > 27; i--) {
					if(player.getInventory().getItem(i) != null) {
						player.getInventory().setItem(i, new ItemStack(Material.MAP));
						return;
					}
				}
					 */
                    player.getInventory().addItem(new ItemStack(Material.MAP));
                }
            } else {
                UtilMessage.message(player, "Map", "You already have a map");
            }
        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("fix")) {
                    if (RechargeManager.getInstance().add(player, "MapFix", 120, false)) {
                        MinimapRenderer.PlayerMapData data = renderer.usedMaps.get(player);
                        for (short s : renderer.used) {

                            if (!data.used.contains(s)) {
                                data.current = s;
                                data.used.add(s);
                                break;
                            }
                        }


                        MapView map2 = Bukkit.getMap((short) (data.current + 1));
                        if (map2 == null) {
                            MapView newMap = Bukkit.createMap(player.getWorld());
                            if (!data.used.contains(newMap.getId())) {
                                newMap.setScale(Bukkit.getMap((short) 0).getScale());
                                if (!(newMap.getRenderers().get(0) instanceof MinimapRenderer)) {
                                    for (final MapRenderer r : newMap.getRenderers()) {
                                        newMap.removeRenderer(r);
                                    }

                                    newMap.addRenderer(renderer);

                                }

                                player.sendMap(newMap);
                                if (!renderer.used.contains(newMap.getId())) {
                                    renderer.used.add(newMap.getId());
                                }

                                data.current = newMap.getId();
                                data.used.add(newMap.getId());
                            }
                        } else {
                            data.current = map2.getId();

                            if (!renderer.used.contains(map2.getId())) {
                                renderer.used.add(map2.getId());
                            }
                            if (!data.used.contains(map2.getId())) {
                                data.used.add(map2.getId());
                            }
                            if (!map2.getRenderers().contains(renderer)) {
                                map2.addRenderer(renderer);
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void help(Player player) {
    }

}
