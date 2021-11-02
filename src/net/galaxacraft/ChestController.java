package net.galaxacraft;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;








public class ChestController
{
	Main plugin;
	private List<Integer> randomLoc;
	private final List<ChestItem> chestItemList;
	private final Random random;

	public ChestController(Main instance) {
		this.randomLoc = new ArrayList<>();
		this.chestItemList = Lists.newArrayList();
		this.random = new Random();
		this.plugin = instance;
		loadC();
		for (int i = 0; i < 27; i++)
			this.randomLoc.add(Integer.valueOf(i)); 
	} public void loadC() {
		this.chestItemList.clear();

		if (this.plugin.getConfig().contains("ChestItems"))
		{
			for (String item : this.plugin.getConfig().getStringList("ChestItems")) {
				ItemStack itemStack;
				String[] itemData = item.split(" ");

				int chance = Integer.parseInt(itemData[0]);
				int amount = Integer.parseInt(itemData[2]);



				if (itemData[1].contains(":")) {

					String[] a = itemData[1].split(":");
					int itd = Integer.parseInt(a[0]);
					int ita = Integer.parseInt(a[1]);
					itemStack = new ItemStack(Material.getMaterial(itd), amount, (short)ita);
				}
				else {

					int itd = Integer.parseInt(itemData[1]);
					itemStack = new ItemStack(Material.getMaterial(itd), amount);
				} 

				if (itemData.length == 5) {

					int enchan = Integer.parseInt(itemData[3]);
					int enchanLvl = Integer.parseInt(itemData[4]);

					itemStack.addEnchantment(Enchantment.getById(enchan), enchanLvl);
				} 

				if (itemStack != null)
				{
					this.chestItemList.add(new ChestItem(itemStack, chance));
				}
			} 
		}
	}


	public void populateChest(Inventory inventory) {
		inventory.clear();
		int added = 0;
		for (int i = 0; i < 27; i++)
			this.randomLoc.add(Integer.valueOf(i)); 
		Collections.shuffle(this.randomLoc);
		Collections.shuffle(this.chestItemList);

		for (ChestItem chestItem : this.chestItemList) {

			if (this.random.nextInt(100) + 1 <= chestItem.getChance()) {

				inventory.setItem(((Integer)this.randomLoc.get(added)).intValue(), chestItem.getItem());

				if (added++ >= this.plugin.getConfig().getInt("ItemsInChest")) {
					break;
				}
			} 
		} 
	}


	public class ChestItem
	{
		private ItemStack item;

		private int chance;

		public ChestItem(ItemStack item, int chance) {
			this.item = item;
			this.chance = chance;
		}


		public ItemStack getItem() {
			return this.item;
		}


		public int getChance() {
			return this.chance;
		}
	}
}

