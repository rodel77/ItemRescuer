package mx.com.rodel.itemrescuer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FastInventorySave
{
  private boolean keepInventory;
  private ItemStack[] contents;
  private ItemStack helmet;
  private ItemStack chesplate;
  private ItemStack leggings;
  private ItemStack boots;
  
  public FastInventorySave(Player player, boolean keep)
  {
    this.contents = player.getInventory().getContents();
    this.helmet = player.getEquipment().getHelmet();
    this.chesplate = player.getEquipment().getChestplate();
    this.leggings = player.getEquipment().getLeggings();
    this.boots = player.getEquipment().getBoots();
    this.keepInventory = keep;
  }
  
  public ItemStack[] getContents()
  {
    return this.contents;
  }
  
  public void setContents(ItemStack[] contents)
  {
    this.contents = contents;
  }
  
  public ItemStack getHelmet()
  {
    return this.helmet;
  }
  
  public void setHelmet(ItemStack helmet)
  {
    this.helmet = helmet;
  }
  
  public ItemStack getChesplate()
  {
    return this.chesplate;
  }
  
  public void setChesplate(ItemStack chesplate)
  {
    this.chesplate = chesplate;
  }
  
  public ItemStack getLeggings()
  {
    return this.leggings;
  }
  
  public void setLeggings(ItemStack leggings)
  {
    this.leggings = leggings;
  }
  
  public ItemStack getBoots()
  {
    return this.boots;
  }
  
  public void setBoots(ItemStack boots)
  {
    this.boots = boots;
  }
  
  public boolean canKeepInventory()
  {
    return this.keepInventory;
  }
  
  public void setKeepInventory(boolean keepInventory)
  {
    this.keepInventory = keepInventory;
  }
}
