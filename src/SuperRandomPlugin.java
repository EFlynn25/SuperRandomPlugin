package com.flynntech;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public final class SuperRandomPlugin extends org.bukkit.plugin.java.JavaPlugin
{
  FileConfiguration config = getConfig();
  
  public SuperRandomPlugin() {}
  
  public void onEnable()
  {
    getLogger().info("Welcome to SuperRandomPlugin v1.3.2!");
    getLogger().info("Checking config...");
    checkConfig();
    getLogger().info("SuperRandomPlugin has been enabled");
    getLogger().info("[ChangeLog] Fixed a small issue in the last version");
  }
  
  public void onDisable()
  {
    getLogger().info("Thanks for using SuperRandomPlugin!");
    getLogger().info("SuperRandomPlugin has been disabled");
  }
  

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if ((cmd.getName().equalsIgnoreCase("explode")) && (args.length == 1) && (config.getBoolean("canUseExplode"))) {
      Player target = sender.getServer().getPlayer(args[0]);
      if (target == null) {
        sender.sendMessage(args[0] + " is not currently online.");
        return true;
      }
      target.getWorld().createExplosion(target.getLocation(), 0.0F);
      target.setHealth(0.0D);
      target.sendMessage("You have been exploded by: " + sender.getName());
    } else { if ((cmd.getName().equalsIgnoreCase("explode")) && (!config.getBoolean("canUseExplode"))) {
        sender.sendMessage(config.getString("messageIfCannotUse"));
        return true;
      }
      if ((cmd.getName().equalsIgnoreCase("sky")) && (args.length == 1) && (config.getBoolean("canUseSky"))) {
        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
          sender.sendMessage(args[0] + " is not currently online.");
          return true;
        }
        Location pos = target.getLocation();
        pos.setY(255.0D);
        target.teleport(pos);
        target.sendMessage("You have been skyed by: " + sender.getName());
      } else { if ((cmd.getName().equalsIgnoreCase("sky")) && (!config.getBoolean("canUseSky"))) {
          sender.sendMessage(config.getString("messageIfCannotUse"));
          return true;
        }
        if ((cmd.getName().equalsIgnoreCase("cmdblock")) && (config.getBoolean("canUseCmdblock"))) {
          if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
          }
          Player player = (Player)sender;
          PlayerInventory inventory = player.getInventory();
          ItemStack itemstack = new ItemStack(org.bukkit.Material.COMMAND_BLOCK);
          
          inventory.addItem(new ItemStack[] { itemstack });
          player.sendMessage("You have been given a command block.");
          return true;
        }
        if ((cmd.getName().equalsIgnoreCase("cmdblock")) && (!config.getBoolean("canUseCmdblock"))) {
          sender.sendMessage(config.getString("messageIfCannotUse"));
          return true;
        }
        if ((cmd.getName().equalsIgnoreCase("lightningstr")) && (args.length == 1) && (config.getBoolean("canUseLightningstr"))) {
          Player target = sender.getServer().getPlayer(args[0]);
          if (target == null) {
            sender.sendMessage(args[0] + " is currently not online.");
            return true;
          }
          org.bukkit.World world = target.getWorld();
          Location location = target.getLocation();
          world.strikeLightning(location);
          return true; }
        if ((cmd.getName().equalsIgnoreCase("lightningstr")) && (!config.getBoolean("canUseLightningstr"))) {
          sender.sendMessage(config.getString("messageIfCannotUse"));
          return true; }
        if ((cmd.getName().equalsIgnoreCase("dirt")) && (args.length == 1) && (config.getBoolean("canUseDirt"))) {
          Player target = sender.getServer().getPlayer(args[0]);
          if (target == null) {
            sender.sendMessage(args[0] + " is currently not online.");
            return true;
          }
          PlayerInventory inventory = target.getInventory();
          ItemStack itemstack = new ItemStack(org.bukkit.Material.DIRT, 64);
          
          inventory.addItem(new ItemStack[] { itemstack });
          target.sendMessage("HAVE SOME DIRT! (Given by " + sender.getName() + ")");
          sender.sendMessage("You gave " + target.getName() + " dirt.");
          return true; }
        if ((cmd.getName().equalsIgnoreCase("dirt")) && (!config.getBoolean("canUseDirt"))) {
          sender.sendMessage(config.getString("messageIfCannotUse"));
          return true;
        } } }
    return false;
  }
  
  private void checkConfig() {
    try {
      if (!getDataFolder().exists()) {
        getDataFolder().mkdirs();
      }
      File file = new File(getDataFolder(), "config.yml");
      if (!file.exists()) {
        getLogger().info("config.yml not found! Creating...");
        saveDefaultConfig();
        config.set("pluginVersion", "v1.3.2");
        config.set("canUseExplode", Boolean.valueOf(true));
        config.set("canUseSky", Boolean.valueOf(true));
        config.set("canUseCmdblock", Boolean.valueOf(true));
        config.set("canUseLightningstr", Boolean.valueOf(true));
        config.set("canUseDirt", Boolean.valueOf(true));
        config.set("messageIfCannotUse", "That command has been turned off.");
        saveConfig();
      } else if ((config.getString("pluginVersion") != "v1.3.2") || (config.getString("pluginVersion") == null)) {
        getLogger().info("config.yml found, outdated! Updating...");
        config.set("pluginVersion", "v1.3.2");
        config.set("canUseDirt", Boolean.valueOf(true));
        saveConfig();
      } else {
        getLogger().info("config.yml found! Loading...");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
