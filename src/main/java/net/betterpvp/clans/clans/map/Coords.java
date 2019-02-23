package net.betterpvp.clans.clans.map;

public class Coords
{
  private int x;
  private int z;
  private boolean chunk;
  private String world;
  
  public Coords(int x, int z, boolean chunk, String world)
  {
    this.x = x;
    this.z = z;
    this.chunk = chunk;
    this.world = world;
  }
  
  public boolean isChunk()
  {
    return this.chunk;
  }
  
  public int getX()
  {
    return this.x;
  }
  
  public int getZ()
  {
    return this.z;
  }
  
  public String getWorld()
  {
    return this.world;
  }
  
  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (this.chunk ? 1231 : 1237);
    result = 31 * result + (this.world == null ? 0 : this.world.hashCode());
    result = 31 * result + this.x;
    result = 31 * result + this.z;
    return result;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Coords)) {
      return false;
    }
    Coords other = (Coords)obj;
    if (this.chunk != other.chunk) {
      return false;
    }
    if (this.world == null)
    {
      if (other.world != null) {
        return false;
      }
    }
    else if (!this.world.equals(other.world)) {
      return false;
    }
    if (this.x != other.x) {
      return false;
    }
      return this.z == other.z;
  }
}