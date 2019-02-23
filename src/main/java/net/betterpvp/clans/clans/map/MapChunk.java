package net.betterpvp.clans.clans.map;

import net.betterpvp.clans.Clans;
import net.betterpvp.clans.clans.map.NMS.MaterialMapColorInterface;

public class MapChunk
{
  private MaterialMapColorInterface[][] cache = new MaterialMapColorInterface[16][16];
  private short[][] avgY = new short[16][16];
  
  public MapChunk(Clans plugin)
  {
    for (int i = 0; i < 16; i++) {
      for (int j = 0; j < 16; j++)
      {
        this.cache[i][j] = plugin.getNMSHandler().getColorNeutral();
        this.avgY[i][j] = 64;
      }
    }
  }
  
  public void set(int i, int j, RenderResult value)
  {
    this.cache[i][j] = value.getColor();
    this.avgY[i][j] = value.getAverageY();
  }
  
  public MaterialMapColorInterface get(int i, int j)
  {
    return this.cache[i][j];
  }
  
  public short getY(int i, int j)
  {
    return this.avgY[i][j];
  }
}
