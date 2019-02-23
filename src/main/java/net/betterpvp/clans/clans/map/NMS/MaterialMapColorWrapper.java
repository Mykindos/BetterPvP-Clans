package net.betterpvp.clans.clans.map.NMS;

import net.minecraft.server.v1_8_R3.MaterialMapColor;

public class MaterialMapColorWrapper
  implements MaterialMapColorInterface
{
  private MaterialMapColor color;
  
  public MaterialMapColorWrapper(MaterialMapColor color)
  {
    this.color = color;
  }
  
  public int getM()
  {
    return this.color.M;
  }
}
