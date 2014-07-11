package me.odium.newspaper.commands;

import java.util.Set;

import me.odium.newspaper.newspaper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class news implements CommandExecutor {   

  public newspaper plugin;
  public news(newspaper plugin)  {
    this.plugin = plugin;
  }

  public void sendNews(Player player, Block b) {
    //Get first item in chest and add to player inventory 
    Chest chest = (Chest) b.getState();
    ItemStack[] inv = chest.getBlockInventory().getContents();    
    for (ItemStack item  : inv) {
      if (item != null) {
        player.getInventory().addItem(item);  
      }
    }
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)  {
    Player player = (Player) sender;
    // * IF ARGUMENT HELP
    if (args.length == 1 && args[0].equals("help")) {
      plugin.displayMenu(player);
      return true;
    // * IF ARGUMENT SET
    } else if (args.length == 1 && args[0].equals("set")) {
      // CHECK PLAYER'S PERMISSIONS FOR THIS COMMAND
      if (!player.hasPermission("news.setstand") || !player.hasPermission("news.admin")) {
        plugin.noPermission(player);
        return true;
      }

      plugin.commandStatus.put(player, 1);
      plugin.standSetHowTo(player);
      return true;
    // * IF ARGUMENT RELOAD
    } else if (args.length == 1 && args[0].equals("reload")) {

      // CHECK PLAYER'S PERMISSIONS FOR THIS COMMAND
      if (!player.hasPermission("news.reload") || !player.hasPermission("news.admin") ) {
        plugin.noPermission(player);
        return true;
      }

      plugin.reloadStorageConfig();
      plugin.reloadConfig();
      plugin.pluginReloaded(player);
      return true;
      // * IF ARGUMENT CLEAR
    } else if (args.length == 1 && args[0].equals("clear")) {
      // CHECK PLAYER'S PERMISSIONS FOR THIS COMMAND
      if (!player.hasPermission("news.setstand") || !player.hasPermission("news.admin")) {
        plugin.noPermission(player);
        return true;
      }
      plugin.getConfig().set("NewsStand", null);
      plugin.saveConfig();
      plugin.newsstandCleared(player);
      return true;
      // * IF ARGUMENT RESET
    } else if (args.length == 1 && args[0].equals("reset")) {
      // CHECK PLAYER'S PERMISSIONS FOR THIS COMMAND
      if (!player.hasPermission("news.admin")) {
        plugin.noPermission(player);
        return true;
      }
      Set<String> storageKeys = plugin.getStorageConfig().getKeys(false);
      for (String key : storageKeys) {
        plugin.getStorageConfig().set(key, null);
      }
      plugin.saveStorageConfig();
      plugin.NewsReset(player);
      return true;
      // * IF NO ARGS
    } else if(args.length == 0) {
      if (plugin.getConfig().getString("NewsStand") != null) { 

        // Get NewsStand Location
        String[] newsLoc = plugin.getConfig().getString("NewsStand").split("_");

        // Compile Location     
        String worldName = newsLoc[0]; 
        double x = Double.parseDouble(newsLoc[1]);
        double y = Double.parseDouble(newsLoc[2]);
        double z = Double.parseDouble(newsLoc[3]);                
        Location chestloc = new Location(Bukkit.getWorld(worldName), x, y, z,0,0);

        // Get NewsStand
        Block b = chestloc.getBlock();    
        if (b.getType() == Material.CHEST) {

          String currentDate = plugin.getCurrentDate();
          String playerName = player.getName().toLowerCase();
          // IF PLAYER EXISTS IN STORAGE
          if (plugin.getStorageConfig().get(playerName) != null) {           
            // IF PLAYER RECIEVED THE NEWS TODAY
            if (plugin.getStorageConfig().get(playerName).equals(currentDate)) { 
              plugin.newsAlreadyReceived(player);
              return true;
            } else { // IF PLAYER HAS NOT RECEIVED THE NEWS TODAY              
              plugin.getStorageConfig().set(playerName, currentDate);
              plugin.saveStorageConfig();
              this.sendNews(player, b);
              plugin.newsReceived(player);
              return true;
            }
          } // IF PLAYER DOESN'T EXIST IN STORAGE (NO RECORD OF PLAYER)          
          plugin.getStorageConfig().set(playerName, currentDate);
          plugin.saveStorageConfig();
          this.sendNews(player, b);
          plugin.newsReceived(player);
          return true;
        } else {
          plugin.standMissing(player);
          return true;
        }
      }

    }
    return true;
  }

}