package me.odium.newspaper;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.odium.newspaper.commands.news;
import me.odium.newspaper.listener.PListener;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class newspaper extends JavaPlugin {
  public Logger log = Logger.getLogger("Minecraft");

  //Custom Config  
  private FileConfiguration StorageConfig = null;
  private File StorageConfigFile = null;

  public void reloadStorageConfig() {
    if (StorageConfigFile == null) {
      StorageConfigFile = new File(getDataFolder(), "StorageConfig.yml");
    }
    StorageConfig = YamlConfiguration.loadConfiguration(StorageConfigFile);

    // Look for defaults in the jar
    InputStream defConfigStream = getResource("StorageConfig.yml");
    if (defConfigStream != null) {
      YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
      StorageConfig.setDefaults(defConfig);
    }
  }
  public FileConfiguration getStorageConfig() {
    if (StorageConfig == null) {
      reloadStorageConfig();
    }
    return StorageConfig;
  }
  public void saveStorageConfig() {
    if (StorageConfig == null || StorageConfigFile == null) {
      return;
    }
    try {
      StorageConfig.save(StorageConfigFile);
    } catch (IOException ex) {
      Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + StorageConfigFile, ex);
    }
  }
  // End Custom Config

  public Map<Player, Integer> commandStatus = new HashMap<Player, Integer>();

  public void onEnable(){    
    log.info("[" + getDescription().getName() + "] " + getDescription().getVersion() + " enabled.");    
    // Load Config.yml
    FileConfiguration cfg = getConfig();
    FileConfigurationOptions cfgOptions = cfg.options();
    cfgOptions.copyDefaults(true);
    cfgOptions.copyHeader(true);
    saveConfig();
    // Load Custom Config
    FileConfiguration ccfg = getStorageConfig();
    FileConfigurationOptions ccfgOptions = ccfg.options();
    ccfgOptions.copyDefaults(true);
    ccfgOptions.copyHeader(true);
    saveStorageConfig();
    // declare new listener
    new PListener(this);
    // Declare Command Executor
    this.getCommand("news").setExecutor(new news(this));   
  }

  public void onDisable(){ 
    log.info("[" + getDescription().getName() + "] " + getDescription().getVersion() + " disabled.");    
  } 

  public String getCurrentDate() {
    Calendar currentDate = Calendar.getInstance();
    SimpleDateFormat dtgFormat = null;    
    dtgFormat = new SimpleDateFormat ("yyyy/MM/dd");   
    return dtgFormat.format (currentDate.getTime());
  }  

  // plugin Menu
  public void displayMenu(Player player) {
    player.sendMessage(ChatColor.GOLD+"[ "+ChatColor.WHITE+"News Stand - "+getDescription().getVersion()+ChatColor.GOLD+" ]");
    player.sendMessage(ChatColor.YELLOW+" /news help "+ChatColor.WHITE+"- Display this menu");
    player.sendMessage(ChatColor.YELLOW+" /news set "+ChatColor.WHITE+"- Set the News Stand");
    player.sendMessage(ChatColor.YELLOW+" /news clear"+ChatColor.WHITE+"- Clear NewsStand location");
    player.sendMessage(ChatColor.YELLOW+" /news reset"+ChatColor.WHITE+"- Clear today's recipients");
    player.sendMessage(ChatColor.YELLOW+" /news "+ChatColor.WHITE+"- Retrieve today's Newspaper");
    
    player.sendMessage(ChatColor.YELLOW+" /news reload "+ChatColor.WHITE+"- Reload the news config");
  }

  // Plugin Responses
  public void cannotBreakStand(Player player) {
    player.sendMessage(ChatColor.RED+" You do not have permission to break the news stand.");
  }  
  public void standSetSuccess(Player player) {
    player.sendMessage(ChatColor.GREEN+" You have successfully defined the News Stand.");
  }    
  public void standSetHowTo(Player player) {
    player.sendMessage(ChatColor.YELLOW+" Click a chest to make it the News Stand");
  }
  public void standMissing(Player player) {
    player.sendMessage(ChatColor.RED+" The news stand is missing.");
  }
  public void standBroken(Player player) {
    player.sendMessage(ChatColor.RED+" You have broken the news stand!.");
  }
  public void standReset(Player player) {
    player.sendMessage(ChatColor.GREEN+" News stand reset");
  }
  public void newsReceived(Player player) {
    player.sendMessage(ChatColor.GREEN+" Received today's news, no more until after tomorrow!");
  }  
  public void newsAlreadyReceived(Player player) {
    player.sendMessage(ChatColor.RED+" You have already receieved today's news");
  }

  public void noPermission(Player player) {
    player.sendMessage(ChatColor.RED+" You do not have permission.");
  }
  public void pluginReloaded(Player player) {
    player.sendMessage(ChatColor.GREEN+" NewsPaper Successfully reloaded!");
  }
  public void NewsReset(Player player) {
    player.sendMessage(ChatColor.GREEN+" NewsPaper Successfully reloaded!");
  }
  public void newsstandCleared(Player player) {
    player.sendMessage(ChatColor.GREEN+" NewsStand Successfully Cleared!");
  }
  


}
