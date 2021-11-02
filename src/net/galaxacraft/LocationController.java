package net.galaxacraft;

import com.google.common.collect.Lists;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;






public class LocationController
{
	Main plugin;
	public final List<Location> locationList;

	public LocationController(Main instance) {
		this.locationList = Lists.newArrayList();
		this.plugin = instance;
		loadL();
	} public void loadL() {
		this.locationList.clear();
		if (this.plugin.getConfig().contains("ChestLocations"))
		{
			for (String locs : this.plugin.getConfig().getStringList("ChestLocations")) {



				String[] itemData = locs.split(" ");




				String world = itemData[0];
				double x = Double.parseDouble(itemData[1]);
				double y = Double.parseDouble(itemData[2]);
				double z = Double.parseDouble(itemData[3]);

				Location loc = new Location(Bukkit.getWorld(world), x, y, z);
				this.locationList.add(loc);
			} 
		}
	}
}