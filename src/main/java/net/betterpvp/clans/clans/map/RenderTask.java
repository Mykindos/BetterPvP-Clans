package net.betterpvp.clans.clans.map;


import net.betterpvp.clans.Clans;
import org.bukkit.scheduler.BukkitRunnable;

public class RenderTask
        extends BukkitRunnable {
    private MinimapRenderer renderer;

    public RenderTask(MinimapRenderer renderer) {
        this.renderer = renderer;
    }

    public void run() {
        if (Clans.getOptions().isMapEnabled()) {
            int chunks = 0;
            int blocks = 0;
            while (chunks < this.renderer.getChunksPerRun()) {
                Coords c = this.renderer.getQueue().poll();
                if (c == null) {
                    break;
                }
                if (c.isChunk()) {
                    this.renderer.loadData(c.getX(), c.getZ(), c.getWorld());
                    chunks++;
                } else {
                    this.renderer.loadBlock(c.getX(), c.getZ(), c.getWorld());
                    blocks++;
                    if (blocks >= 256 * this.renderer.getDefaultScale() * this.renderer.getDefaultScale()) {
                        blocks = 0;
                        chunks++;
                    }
                }
            }
        }
    }
}
