package me.odium.newspaper.listener;

import me.odium.newspaper.newspaper;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class PListener implements Listener {

  public newspaper plugin;
  public PListener(newspaper plugin) {    
    this.plugin = plugin;    
    plugin.getServer().getPluginManager().registerEvents(this, plugin);  
  }
  
  @EventHandler(priority = EventPriority.LOW)
  public void onBlockBreak(final BlockBreakEvent event) {
    Block b = event.getBlock();
    Player p = event.getPlayer();
    if (b.getType().equals(Material.CHEST)) {
      // IF PLAYER DOES NOT HAVE PERMISSION
      if (!p.hasPermission("newspaper.admin")) {  
        // COMPILE BLOCKBREAK LOCATION
        String worldName = b.getLocation().getWorld().getName();
        double x = b.getLocation().getX();
        double y = b.getLocation().getY();
        double z = b.getLocation().getZ();
        String standLocation = worldName+"_"+x+"_"+y+"_"+z;
        // COMPARE DEFINED LOCATION WITH BLOCLBREAK LOCATION, IF THE SAME CANCEL EVENT.
        if (plugin.getConfig().get("NewsStand").equals(standLocation)) {
          event.setCancelled(true);
          plugin.cannotBreakStand(p);
          return;
        }
      } else {
        
      }
    }
    return;
  }


  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Block b = event.getClickedBlock();

    //make sure we are dealing with a block and not clicking on air or an entity
    if (!(event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
      return;
    }
    // make sure block click is a chest
    if (!b.getType().equals(Material.CHEST)) {
      return;
    }

    if (plugin.commandStatus.containsKey(player) && plugin.commandStatus.get(player) == 1) {
      String worldName = b.getLocation().getWorld().getName();
      double x = b.getLocation().getX();
      double y = b.getLocation().getY();
      double z = b.getLocation().getZ();

      plugin.getConfig().set("NewsStand", worldName+"_"+x+"_"+y+"_"+z);
      plugin.saveConfig();
      plugin.commandStatus.remove(player);
      plugin.standSetSuccess(player);
      event.setCancelled(true);
    }

  }



}

