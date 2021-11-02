package net.galaxacraft;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main
extends JavaPlugin
{
	private static Main instance;

	public static Main getInstance() {
		return instance;
	}

	public static ArrayList<String> envoyEditer = new ArrayList<>();
	public static ArrayList<String> envoyList = new ArrayList<>();

	public static ChestController c;

	public static LocationController l;

	private Firework f;
	private FireworkMeta fm;
	private Location fLoc;
	private Byte blockData;
	private Material fallB;
	private FallingBlock fallingBlock;
	public static String TAG = "§6§l[Envoy] §e";
	public int time = 0;


	private boolean editing = false;


	public void onEnable() {
		instance = this;

		c = new ChestController(this);
		l = new LocationController(this);

		getServer().getPluginManager().registerEvents(new InteractChest(instance), (Plugin)this);
		getServer().getPluginManager().registerEvents(new EnvoyEditer(instance), (Plugin)this);

		System.out.println("Envoys has been enabled!");

		saveDefaultConfig();
		spawnEnvoys();
		timer();
	}






	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {

			Player p = (Player)sender;

			if (cmd.getName().equalsIgnoreCase("envoy") || cmd.getName().equalsIgnoreCase("ev"))
			{
				if (args.length == 1) {

					if (args[0].equalsIgnoreCase("edit")) {

						if (sender.hasPermission("envoy.admin"))
						{
							if (envoyEditer.contains(p.getName()))
							{
								this.editing = false;
								envoyEditer.remove(p.getName());
								p.sendMessage(String.valueOf(TAG) + "Envoy spawn locations saved!");
								envoyList.clear();
								reloadConfig();
								l.locationList.clear();
								l.loadL();
								for (Location locs : l.locationList)
								{
									locs.getWorld().getBlockAt(locs).setType(Material.AIR);
								}
							}
							else
							{
								this.editing = true;
								ItemStack a = new ItemStack(Material.BEDROCK);
								p.getInventory().addItem(new ItemStack[] { a });
								envoyEditer.add(p.getName());
								p.sendMessage(String.valueOf(TAG) + "Place the bedrock where you want the envoy to spawn or remove the bedrock to remove envoy spawn location!");

								for (Location locs : l.locationList)
								{
									locs.getWorld().getBlockAt(locs).setType(Material.BEDROCK);
								}
								for (String locs : getConfig().getStringList("ChestLocations"))
								{
									envoyList.add(locs);
								}
							}

						}
					} else if (args[0].equalsIgnoreCase("reload")) {

						if (sender.hasPermission("envoy.admin"))
						{
							reloadConfig();

							c.loadC();
							p.sendMessage(String.valueOf(TAG) + "Reloaded config!");
						}

					} else if (args[0].equalsIgnoreCase("time")) {

						p.sendMessage(String.valueOf(TAG) + "Envoy drop is in §c" + this.time + " §eseconds!");
					}
					else if (args[0].equalsIgnoreCase("help")) {

						p.sendMessage(String.valueOf(TAG) + "-------- Commands --------");
						if (sender.hasPermission("envoy.admin")) {

							p.sendMessage("§6/envoy edit §c- §eEdit/Save Envoy spawn locations");
							p.sendMessage("§6/envoy reload §c- §eReload Envoy config");
						} 
						p.sendMessage("§6/envoy time §c- §eTells you how long till next drop");
					}

				} else if (args.length == 2) {

					if (args[0].equalsIgnoreCase("additem"))
					{
						if (sender.hasPermission("envoy.admin"))
						{
							int itemid = p.getItemInHand().getTypeId();
							int amount = p.getItemInHand().getAmount();
							int itemda = p.getItemInHand().getDurability();

							ArrayList<String> list = (ArrayList<String>)getConfig().getStringList("ChestItems");

							list.add(String.valueOf(args[1]) + " " + itemid + ":" + itemda + " " + amount);

							getConfig().set("ChestItems", list);
							saveConfig();

							p.sendMessage(ChatColor.GOLD + "Successfully added item!");
						}

					}
				} else {

					p.sendMessage(String.valueOf(TAG) + "-------- Commands --------");
					if (sender.hasPermission("envoy.admin")) {

						p.sendMessage("§6/envoy edit §c- §eEdit/Save Envoy spawn locations");
						p.sendMessage("§6/envoy reload §c- §eReload Envoy config");
					} 
					p.sendMessage("§6/envoy time §c- §eTells you how long till next drop");
				} 
			}
		} 
		return true;
	}










	public void spawnEnvoys() {
		int min = getConfig().getInt("ChestSpawn");
		int a = 20 * min * 60;

		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable()
		{
			public void run()
			{
				if (!Main.this.editing && 
						!Main.this.editing)
				{
					Main.this.spawnChests();
				}
			}
		}, 
		a, a);
	}




	public void spawnChests() {
		(new BukkitRunnable()
		{
			int c = 6;


			public void run() {
				this.c--;
				if (this.c < 6 && this.c > 0) Bukkit.broadcastMessage(String.valueOf(Main.TAG) + "Envoys will drop in§c " + this.c + " §eseconds"); 
				if (this.c == 4)
				{
					for (Location locs : Main.l.locationList) {

						Main.this.f = (Firework)locs.getWorld().spawn(locs, Firework.class);
						Main.this.fm = Main.this.f.getFireworkMeta();
						Main.this.fm.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(FireworkEffect.Type.BALL).withColor(Color.GREEN).withFade(Color.RED).build());
						Main.this.fm.setPower(1);
						Main.this.f.setFireworkMeta(Main.this.fm);
					} 
				}
				if (this.c == 2)
				{
					for (Location locs : Main.l.locationList) {

						Main.this.fLoc = new Location(Bukkit.getWorld(locs.getWorld().getName()), locs.getX(), locs.getY() + 17.0D, locs.getZ());
						Main.this.blockData = Byte.valueOf((byte)0);
						Main.this.fallB = Material.getMaterial(Main.this.getConfig().getInt("FallingBlock"));
						Main.this.fallingBlock = locs.getWorld().spawnFallingBlock(Main.this.fLoc, Main.this.fallB, Main.this.blockData.byteValue());
						Main.this.fallingBlock.setDropItem(false);
					} 
				}
				if (this.c == 0) {

					for (Location locs : Main.l.locationList)
					{

						locs.getWorld().getBlockAt(locs).setType(Material.getMaterial(54));
					}

					Bukkit.broadcastMessage(String.valueOf(Main.TAG) + "Envoys have dropped!");
					Main.this.timer();
				} 
			}
		}).runTaskTimer((Plugin)this, 0L, 20L);
	}



	public void timer() {
		int min = getConfig().getInt("ChestSpawn");
		final int a = min * 60;

		(new BukkitRunnable()
		{
			int c;


			public void run() {
				Main.this.time = --this.c;
				if (this.c == 0)
				{
					Main.this.time = a;
					this.c = a;
				}

			}
		}).runTaskTimer((Plugin)this, 0L, 20L);
	}
}