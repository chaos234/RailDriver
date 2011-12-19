package ca.mcpnet.RailDriver;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RailDriver extends JavaPlugin {

	Logger log = Logger.getLogger("Minecraft");
	PluginManager pm;
	private final RailDriverPlayerListener playerListener = new RailDriverPlayerListener(
			this);
	private final RailDriverBlockListener blockListener = new RailDriverBlockListener(
			this);

	private final ItemStack[] devkit = {
			new ItemStack(Material.DIAMOND_PICKAXE, 1),
			new ItemStack(Material.DIAMOND_SWORD, 1),
			new ItemStack(Material.DIAMOND_SPADE, 1),
			new ItemStack(Material.DIAMOND_AXE, 1),
			new ItemStack(Material.DIAMOND_HOE, 1),
			new ItemStack(Material.TORCH,64)};

	private final ItemStack[] devarmor = {
			new ItemStack(Material.DIAMOND_BOOTS, 1),
			new ItemStack(Material.DIAMOND_LEGGINGS, 1),
			new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
			new ItemStack(Material.DIAMOND_HELMET, 1),
	};

	public void onEnable() {
		log.info("RailDriver Plugin Enabled!");
		pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
	}

	public void onDisable() {
		log.info("RailDriver Plugin Disabled!");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		// carve out a cavern from the players location outwards
		if (cmd.getName().equals("rd_cavern")) {
			if (args.length > 2) {
				sender.sendMessage("Too many arguments!");
				return false;
			}
			if (args.length == 2) {
				player = getServer().getPlayer(args[1]);
				if (player == null) {
					sender.sendMessage("Player not found!");
					return false;
				}
			}
			if (player == null) {
				sender.sendMessage("Must specify player when calling from server console!");
				return false;
			}
			int radius = 0;
			try {
				radius = Integer.decode(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage("Must specify the size of the cavern");
				return false;
			}
			CavernCarverTask task = new CavernCarverTask(this, sender, player, player.getLocation(), radius);
			task.setTaskid(getServer().getScheduler().scheduleSyncRepeatingTask(this, task, 10L, 2L));
			sender.sendMessage("Scheduled cavern carver job "+task.getTaskid()+", radius "+ radius);
			return true;
		}
		
		// Stock the player with things useful for a developer of this plugin
		if (cmd.getName().equals("rd_devkit")) {
			if (args.length > 1) {
				sender.sendMessage("Too many arguments!");
				return false;
			}
			if (args.length == 1) {
				player = getServer().getPlayer(args[0]);
				if (player == null) {
					sender.sendMessage("Player not found!");
					return false;
				}
			}
			if (player == null) {
				sender.sendMessage("Must specify player when calling from server console!");
				return false;
			}
			sender.sendMessage("DevKit applied!");
			if (sender != player) {
				player.sendMessage("DevKit applied!");
			}
			PlayerInventory inventory = player.getInventory();
			for (int i = 0; i < devkit.length; i++) {
				if (!inventory.contains(devkit[i].getType())) {
					inventory.addItem(devkit[i]);
				}
			}
			inventory.setArmorContents(devarmor);
			return true;
		}
		
		// Stock the player with materials required to build a raildriver
		if (cmd.getName().equalsIgnoreCase("rd_stock")) {
			// FIXME Should be able to stock a specific player
			if (player == null) {
				sender.sendMessage("Not a server console command!");
				return false;
			} else {
				sender.sendMessage("Yeah! Stock that fucker up!");
				// 29 sticky piston
				// 42 iron block
				// 76 redstone torch
				// 331 redstone
				// 69 lever
				// 61 furnace
				// 23 dispencer
				// 57 block of diamond
				// 
			}
			return true;
		}
		return false;
	}
}
