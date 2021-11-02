package net.galaxacraft;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class InteractChest
implements Listener
{
	Main plugin;

	public InteractChest(Main instance) {
		this.plugin = instance;
	}



	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
		{


			if (e.getClickedBlock().getType() == Material.getMaterial(54)) {

				String worldN = e.getClickedBlock().getLocation().getWorld().getName();
				double bX = e.getClickedBlock().getLocation().getBlockX();
				double bY = e.getClickedBlock().getLocation().getBlockY();
				double bZ = e.getClickedBlock().getLocation().getBlockZ();

				String fLoc = String.valueOf(worldN) + " " + bX + " " + bY + " " + bZ;

				for (String locs : this.plugin.getConfig().getStringList("ChestLocations")) {

					if (fLoc.equalsIgnoreCase(locs)) {

						Inventory inv = ((Chest)e.getClickedBlock().getState()).getInventory();
						Main.c.populateChest(inv);
						e.getClickedBlock().setType(Material.AIR);
					} 
				} 
			} 
		}
	}
}