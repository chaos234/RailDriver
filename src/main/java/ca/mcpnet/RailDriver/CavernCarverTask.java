package ca.mcpnet.RailDriver;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class CavernCarverTask implements Runnable {
	RailDriver plugin;
	private CommandSender sender;
	private Player player;
	private Location location;
	private int radius;
	private int taskid;

	private int r;
	private int x;
	private int y;
	private int z;

	public CavernCarverTask(RailDriver instance, CommandSender sndr, Player pl, Location loc, int rad) {
		plugin = instance;
		sender = sndr;
		player = pl;
		location = loc;
		radius = rad;
		r = 1;
		x = 0;
		y = 0;
		z = 0;
	}
	
	public void setTaskid(int id) {
		taskid = id;
	}

	public void run() {
		for (; r <= radius; r++) {
			for (; x <= r; x++) {
				for (; z <= r; z++) {
					for (int y = 0; y <= r; y++) {
						Location curPPloc = new Location(location.getWorld(),
								location.getX() + x, location.getY() + y,
								location.getZ() + z);
						Location curPNloc = new Location(location.getWorld(),
								location.getX() + x, location.getY() + y,
								location.getZ() - z);
						Location curNPloc = new Location(location.getWorld(),
								location.getX() - x, location.getY() + y,
								location.getZ() + z);
						Location curNNloc = new Location(location.getWorld(),
								location.getX() - x, location.getY() + y,
								location.getZ() - z);

						if (location.distance(curPPloc) < r) {
							boolean brokeblock = false;
							brokeblock = breakBlock(curPPloc.getBlock()) ? true : brokeblock;
							brokeblock = breakBlock(curPNloc.getBlock()) ? true : brokeblock;
							brokeblock = breakBlock(curNPloc.getBlock()) ? true : brokeblock;
							brokeblock = breakBlock(curNNloc.getBlock()) ? true : brokeblock;
							if (brokeblock) {
								return;
							}
						}
					} // for y
					y = 0;
				} // for z 
				z = 0;
			} // for x
			x = 0;
		} // for r
		plugin.getServer().getScheduler().cancelTask(taskid);
		sender.sendMessage("Completed cavern carver job "+ taskid);
	}

	boolean breakBlock(Block block) {
		if (block.isLiquid() || block.isEmpty() || block.getType() == Material.TORCH) {
			return false;
		}
		block.getWorld().playEffect(block.getLocation(),Effect.STEP_SOUND, block.getTypeId());
		block.setType(Material.AIR);
		player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
		return true;
	}

	public int getTaskid() {
		return taskid;
	}
}
