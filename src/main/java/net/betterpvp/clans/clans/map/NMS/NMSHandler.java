package net.betterpvp.clans.clans.map.NMS;

import net.betterpvp.clans.clans.AdminClan;
import net.betterpvp.clans.clans.Clan;
import net.betterpvp.core.utility.UtilLocation;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.MaterialMapColor;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;

import java.util.HashMap;

public class NMSHandler
        implements INMSHandler {
    private HashMap<MaterialMapColor, MaterialMapColorWrapper> colors;

    public NMSHandler() {
        this.colors = new HashMap<>();

        this.colors.put(MaterialMapColor.b, new MaterialMapColorWrapper(MaterialMapColor.b));
        this.colors.put(MaterialMapColor.c, new MaterialMapColorWrapper(MaterialMapColor.c));
        this.colors.put(MaterialMapColor.d, new MaterialMapColorWrapper(MaterialMapColor.d));
        this.colors.put(MaterialMapColor.e, new MaterialMapColorWrapper(MaterialMapColor.e));
        this.colors.put(MaterialMapColor.f, new MaterialMapColorWrapper(MaterialMapColor.f));
        this.colors.put(MaterialMapColor.g, new MaterialMapColorWrapper(MaterialMapColor.g));
        this.colors.put(MaterialMapColor.h, new MaterialMapColorWrapper(MaterialMapColor.h));
        this.colors.put(MaterialMapColor.i, new MaterialMapColorWrapper(MaterialMapColor.i));
        this.colors.put(MaterialMapColor.j, new MaterialMapColorWrapper(MaterialMapColor.j));
        this.colors.put(MaterialMapColor.k, new MaterialMapColorWrapper(MaterialMapColor.k));
        this.colors.put(MaterialMapColor.l, new MaterialMapColorWrapper(MaterialMapColor.l));
        this.colors.put(MaterialMapColor.m, new MaterialMapColorWrapper(MaterialMapColor.m));
        this.colors.put(MaterialMapColor.n, new MaterialMapColorWrapper(MaterialMapColor.n));
        this.colors.put(MaterialMapColor.o, new MaterialMapColorWrapper(MaterialMapColor.o));
        this.colors.put(MaterialMapColor.p, new MaterialMapColorWrapper(MaterialMapColor.p));
        this.colors.put(MaterialMapColor.q, new MaterialMapColorWrapper(MaterialMapColor.q));
        this.colors.put(MaterialMapColor.r, new MaterialMapColorWrapper(MaterialMapColor.r));
        this.colors.put(MaterialMapColor.s, new MaterialMapColorWrapper(MaterialMapColor.s));
        this.colors.put(MaterialMapColor.t, new MaterialMapColorWrapper(MaterialMapColor.t));
        this.colors.put(MaterialMapColor.u, new MaterialMapColorWrapper(MaterialMapColor.u));
        this.colors.put(MaterialMapColor.v, new MaterialMapColorWrapper(MaterialMapColor.v));
        this.colors.put(MaterialMapColor.w, new MaterialMapColorWrapper(MaterialMapColor.w));
        this.colors.put(MaterialMapColor.x, new MaterialMapColorWrapper(MaterialMapColor.x));
        this.colors.put(MaterialMapColor.y, new MaterialMapColorWrapper(MaterialMapColor.y));
        this.colors.put(MaterialMapColor.z, new MaterialMapColorWrapper(MaterialMapColor.z));
        this.colors.put(MaterialMapColor.A, new MaterialMapColorWrapper(MaterialMapColor.A));
        this.colors.put(MaterialMapColor.B, new MaterialMapColorWrapper(MaterialMapColor.B));
        this.colors.put(MaterialMapColor.C, new MaterialMapColorWrapper(MaterialMapColor.C));
        this.colors.put(MaterialMapColor.D, new MaterialMapColorWrapper(MaterialMapColor.D));
        this.colors.put(MaterialMapColor.E, new MaterialMapColorWrapper(MaterialMapColor.E));
        this.colors.put(MaterialMapColor.F, new MaterialMapColorWrapper(MaterialMapColor.F));
        this.colors.put(MaterialMapColor.G, new MaterialMapColorWrapper(MaterialMapColor.G));
        this.colors.put(MaterialMapColor.H, new MaterialMapColorWrapper(MaterialMapColor.H));
        this.colors.put(MaterialMapColor.I, new MaterialMapColorWrapper(MaterialMapColor.I));
        this.colors.put(MaterialMapColor.J, new MaterialMapColorWrapper(MaterialMapColor.J));
        this.colors.put(MaterialMapColor.K, new MaterialMapColorWrapper(MaterialMapColor.K));
    }

    public MaterialMapColorInterface getColorNeutral() {
        return this.colors.get(MaterialMapColor.b);
    }

    public MaterialMapColorInterface getBlockColor(Clan c, boolean outline, org.bukkit.block.Block block) {
        net.minecraft.server.v1_8_R3.Block nmsblock = CraftMagicNumbers.getBlock(block);
        CraftChunk w = (CraftChunk) block.getChunk();


        if (c != null) {


            if (UtilLocation.chunkOutline(block.getChunk(), block.getLocation())) {

                if (c instanceof AdminClan) {
                    AdminClan adminClan = (AdminClan) c;
                    if (adminClan.isSafe()) {
                        return this.colors.get(MaterialMapColor.s);
                    } else {
                        if (adminClan.getName().equalsIgnoreCase("Outskirts")) {
                            // MaterialMapColor.G = Cyan
                            return this.colors.get(MaterialMapColor.j);
                        } else {
                            return this.colors.get(MaterialMapColor.f);
                        }
                    }
                }
                return this.colors.get(MaterialMapColor.t);

            }
        }
        MaterialMapColor nms = nmsblock.g(w.getHandle().getBlockData(new BlockPosition(block.getX(), block.getY(), block.getZ())));
        if (!this.colors.containsKey(nms)) {
            Bukkit.getLogger().severe("[ServerMinimap] unknown color, error in NMSHandler - please report to author!");
        }
        return this.colors.get(nms);
    }


}