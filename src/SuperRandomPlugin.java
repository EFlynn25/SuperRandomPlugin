package com.flynntech;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.StringUtil;

public final class SuperRandomPlugin extends org.bukkit.plugin.java.JavaPlugin
{
	FileConfiguration config = getConfig();
	private static Map<UUID, ItemStack[]> hashMapInv = new HashMap<>();
	private static Map<UUID, ItemStack[]> hashMapArm = new HashMap<>();
	private static UUID[] peopleFilledInv = new UUID[1];
	private static boolean lastPeopleArrayUsed = true;
	
  public SuperRandomPlugin() {}
  
  public void onEnable()
  {
    getLogger().info("Welcome to SuperRandomPlugin v1.4.1!");
    getLogger().info("Checking config...");
    checkConfig();
    getLogger().info("SuperRandomPlugin has been enabled");
    getLogger().info("[ChangeLog] Added autofill for block on /fillinv command (Credit to ShaneBee, Thanks!)");
  }
  
  public void onDisable()
  {
	getLogger().info("Thanks for using SuperRandomPlugin!");
	getLogger().info("SuperRandomPlugin has been disabled");
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
	// Explode command
    if (cmd.getName().equalsIgnoreCase("explode") && args.length == 1 && config.getBoolean("canUseExplode")) {
        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
          sender.sendMessage(args[0] + " is not currently online.");
          return true;
        }
        target.getWorld().createExplosion(target.getLocation(), 0.0F);
        target.setHealth(0.0D);
        sender.sendMessage("You have exploded " + args[0]);
        target.sendMessage("You have been exploded by " + sender.getName());
        return true;
    } else if (cmd.getName().equalsIgnoreCase("explode") && !config.getBoolean("canUseExplode")) {
    	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
        // Sky command
    } else if (cmd.getName().equalsIgnoreCase("sky") && args.length == 1 && config.getBoolean("canUseSky")) {
        Player target = sender.getServer().getPlayer(args[0]);
        if (target == null) {
          sender.sendMessage(args[0] + " is not currently online.");
          return true;
        }
        Location pos = target.getLocation();
        pos.setY(255.0D);
        target.teleport(pos);
        target.sendMessage("You have been skyed by: " + sender.getName());
        return true;
    } else if (cmd.getName().equalsIgnoreCase("sky") && !config.getBoolean("canUseSky")) {
       	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
        // Cmdblock command
    } else if (cmd.getName().equalsIgnoreCase("cmdblock") && config.getBoolean("canUseCmdblock")) {
        if (!(sender instanceof Player)) {
          sender.sendMessage("This command can only be run by a player.");
          return true;
        } else {
          Player player = (Player)sender;
          PlayerInventory inventory = player.getInventory();
          ItemStack itemstack = new ItemStack(org.bukkit.Material.COMMAND_BLOCK);
        
          inventory.addItem(new ItemStack[] { itemstack });
          player.sendMessage("You have been given a command block.");
          return true;
        }
    } else if (cmd.getName().equalsIgnoreCase("cmdblock") && !config.getBoolean("canUseCmdblock")) {
    	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
        // Lightningstr command
    } else if (cmd.getName().equalsIgnoreCase("lightningstr") && args.length == 1 && config.getBoolean("canUseLightningstr")) {
       	Player target = sender.getServer().getPlayer(args[0]);
     	if (target == null) {
    		sender.sendMessage(args[0] + " is currently not online.");
    		return true;
    	}
    	World world = target.getWorld();
    	Location location = target.getLocation();
    	world.strikeLightning(location);
    	sender.sendMessage("You have struck " + args[0] + " with lightning.");
    	target.sendMessage("You have been struck with lightning by " + sender.getName());
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("lightningstr") && !config.getBoolean("canUseLightningstr")) {
    	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("dirt") && args.length == 1 && config.getBoolean("canUseDirt")) {
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
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("dirt") && !config.getBoolean("canUseDirt")) {
    	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("fillinv") && args.length == 2 && config.getBoolean("canUsefillinv")) {
    	Player target = sender.getServer().getPlayer(args[0]);
    	if (target == null) {
    		sender.sendMessage(args[0] + " is currently not online.");
    		return true;
    	}
    	int tempInt = 0;
    	boolean foundIndex = false;
    	while (tempInt < peopleFilledInv.length) {
        	if (peopleFilledInv[tempInt] == target.getUniqueId()) {
    			foundIndex = true;
    		}
    		tempInt++;
    	}
    	if (foundIndex) {
    		sender.sendMessage("You have already used /fillinv on this player!");
    		return true;
    	}
    	PlayerInventory inventory = target.getInventory();
		ItemStack itemstack = new ItemStack(org.bukkit.Material.getMaterial(args[1]), 2304);
		hashMapInv.put(target.getUniqueId(), inventory.getContents());
		hashMapArm.put(target.getUniqueId(), inventory.getArmorContents());
		UUID[] tempArray = new UUID[peopleFilledInv.length];
		if (peopleFilledInv[peopleFilledInv.length - 1] == null) {
			peopleFilledInv[peopleFilledInv.length - 1] = target.getUniqueId();
		} else {
			if (lastPeopleArrayUsed) {
				tempInt = 0;
				while (tempInt != tempArray.length) {
					tempArray[tempInt] = peopleFilledInv[tempInt];
					tempInt++;
				}
				peopleFilledInv = new UUID[tempArray.length + 1];
				peopleFilledInv[peopleFilledInv.length - 1] = target.getUniqueId();
			} else {
				peopleFilledInv[peopleFilledInv.length - 1] = target.getUniqueId();
			}
		}
		
		inventory.clear();
		inventory.addItem(new ItemStack[] { itemstack });
		target.sendMessage("Your inventory has been filled with the item '" + args[1] + "' by " + sender.getName());
		sender.sendMessage("You filled " + args[0] + "'s inventory with the item '" + args[1] + "'");
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("fillinv") && !config.getBoolean("canUsefillinv")) {
    	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("undoinv") && config.getBoolean("canUseundoinv")) {
    	Player player = sender.getServer().getPlayer(sender.getName());
    	int tempInt = 0;
    	int index = 0;
    	boolean foundIndex = false;
    	while (tempInt < peopleFilledInv.length) {
        	if (peopleFilledInv[tempInt] == player.getUniqueId()) {
    			foundIndex = true;
    			index = tempInt;
    		}
    		tempInt++;
    	}
    	if (foundIndex == false) {
    		sender.sendMessage("Nobody has used /fillinv on you!");
    		return true;
    	}
    	tempInt = 0;
    	while (tempInt <= peopleFilledInv.length) {
    		if (index >= tempInt) {
    			if (index == peopleFilledInv.length - 1) {
    				peopleFilledInv[peopleFilledInv.length - 1] = null;
    				tempInt = peopleFilledInv.length + 1;
    			} else {
    				peopleFilledInv[index] = peopleFilledInv[index + 1];
    			}
    		}
    		tempInt++;
    	}
    	PlayerInventory inventory = player.getInventory();
    	inventory.clear();
    	inventory.setContents(hashMapInv.get(player.getUniqueId()));
    	inventory.setArmorContents(hashMapArm.get(player.getUniqueId()));
    	player.sendMessage("You have restored your inventory!");
    	return true;
    } else if (cmd.getName().equalsIgnoreCase("undoinv") && !config.getBoolean("canUseundoinv")) {
    	sender.sendMessage(config.getString("messageIfCannotUse"));
    	return true;
    }
    return false;
  }
  
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
	  if (command.getName().equalsIgnoreCase("fillinv")) {
			if (args.length == 2) {
				List<String> items = new ArrayList<>();
				for (Material material : Material.values()) {
					items.add(material.name());
				}
				return StringUtil.copyPartialMatches(args[1], items, new ArrayList<>());
			}
		}
		return null;
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
			  config.set("pluginVersion", "v1.4.1");
			  config.set("canUseExplode", true);
			  config.set("canUseSky", true);
			  config.set("canUseCmdblock", true);
			  config.set("canUseLightningstr", true);
			  config.set("canUseDirt", true);
			  config.set("canUsefillinv", true);
			  config.set("canUseundoinv", true);
			  config.set("messageIfCannotUse", "That command has been turned off.");
			  saveConfig();
		  } else if (config.getString("pluginVersion") == null) {
			  getLogger().info("config.yml found, outdated! Updating...");
			  config.set("pluginVersion", "v1.4.1");
			  config.set("canUseDirt", true);
			  config.set("canUsefillinv", true);
			  config.set("canUseundoinv", true);
			  saveConfig();
		  } else if (!(config.getString("pluginVersion") == "v1.4") && config.getString("pluginVersion") != null) {
			  getLogger().info("config.yml found, outdated! Updating...");
			  config.set("pluginVersion", "v1.4.1");
			  config.set("canUsefillinv", true);
			  config.set("canUseundoinv", true);
			  saveConfig();
		  } else {
			  getLogger().info("config.yml found! Loading...");
		  }
		  if (!(config.getString("pluginVersion") == "v1.4.1")) {
			  getLogger().info("config.yml found, outdated version! Updating...");
			  config.set("pluginVersion", "v1.4.1");
			  saveConfig();
		  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
  }
}
