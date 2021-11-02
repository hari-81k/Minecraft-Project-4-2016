package net.galaxacraft;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;


public class EnvoyEditer
implements Listener
{
	Main plugin;

	public EnvoyEditer(Main instance) {
		this.plugin = instance;
	}




	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();

		if (e.getBlock().getType() == Material.BEDROCK)
		{
			if (Main.envoyEditer.contains(p.getName())) {

				Bukkit.broadcastMessage("1.");

				String worldN = e.getBlock().getLocation().getWorld().getName();
				double bX = e.getBlock().getLocation().getBlockX();
				double bY = e.getBlock().getLocation().getBlockY();
				double bZ = e.getBlock().getLocation().getBlockZ();

				String fLoc = String.valueOf(worldN) + " " + bX + " " + bY + " " + bZ;
				if (!Main.envoyList.contains(fLoc)) {

					ArrayList<String> list = (ArrayList<String>)this.plugin.getConfig().getStringList("ChestLocations");
					list.add(fLoc);
					this.plugin.getConfig().set("ChestLocations", list);
					this.plugin.saveConfig();
					p.sendMessage(String.valueOf(Main.TAG) + "Added envoy spawn location!");
				} 
			} 
		}
	}


	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();

		if (e.getBlock().getType() == Material.BEDROCK)
		{
			if (Main.envoyEditer.contains(p.getName())) {

				String worldN = e.getBlock().getLocation().getWorld().getName();
				double bX = e.getBlock().getLocation().getBlockX();
				double bY = e.getBlock().getLocation().getBlockY();
				double bZ = e.getBlock().getLocation().getBlockZ();

				String fLoc = String.valueOf(worldN) + " " + bX + " " + bY + " " + bZ;

				if (Main.envoyList.contains(fLoc)) {

					ArrayList<String> list = (ArrayList<String>)this.plugin.getConfig().getStringList("ChestLocations");
					list.remove(fLoc);
					this.plugin.getConfig().set("ChestLocations", list);
					this.plugin.saveConfig();
					p.sendMessage(String.valueOf(Main.TAG) + "Removed envoy spawn location!");
				} 
			} 
		}
	}
}