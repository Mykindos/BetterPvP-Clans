package net.betterpvp.clans.clans.map;

import org.bukkit.map.MapCursor;

public class ExtraCursor
{
  private int x;
  private int z;
  private boolean visible;
  private MapCursor.Type type;
  private byte direction;
  private String world;
  private boolean outside;
  
  public ExtraCursor(int x, int z, boolean visible, MapCursor.Type type, byte direction, String world, boolean outside)
  {
    setX(x);
    setZ(z);
    setVisible(visible);
    setType(type);
    setDirection(direction);
    setWorld(world);
    setShowOutside(outside);
  }
  
  public int getX()
  {
    return this.x;
  }
  
  public void setX(int x)
  {
    this.x = x;
  }
  
  public int getZ()
  {
    return this.z;
  }
  
  public void setZ(int z)
  {
    this.z = z;
  }
  
  public boolean isVisible()
  {
    return this.visible;
  }
  
  public void setVisible(boolean visible)
  {
    this.visible = visible;
  }
  
  public MapCursor.Type getType()
  {
    return this.type;
  }
  
  public void setType(MapCursor.Type type)
  {
    this.type = type;
  }
  
  public byte getDirection()
  {
    return this.direction;
  }
  
  public void setDirection(byte direction)
  {
    this.direction = ((byte)(direction % 16));
  }
  
  public String getWorld()
  {
    return this.world;
  }
  
  public void setWorld(String world)
  {
    this.world = world;
  }
  
  public String toString()
  {
    return this.x + " " + this.z + " " + this.world + " " + this.visible;
  }
  
  public boolean isShownOutside()
  {
    return this.outside;
  }
  
  public void setShowOutside(boolean bool)
  {
    this.outside = bool;
  }
}
