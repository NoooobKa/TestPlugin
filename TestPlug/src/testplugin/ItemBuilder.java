package testplugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

public class ItemBuilder {

	private ItemStack is;
	private BukkitTask bt;
	
	public ItemBuilder(ItemStack is) {
		this.is = is == null ? new ItemStack(Material.AIR) : is;
		
	}
	
	public ItemBuilder(Material m) {
		this.is = new ItemStack(m);
	}
	
	public ItemStack build() {
		return is;
	}
	
	public ItemBuilder setName(String name) {
		ItemMeta m = getMeta();
		m.setDisplayName(name);
		setMeta(m);
		return this;
		
	}
	
	public String getName() {
		return getMeta().getDisplayName() == null ? "" : getMeta().getDisplayName();
	}
	
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta m = getMeta();
		m.setLore(lore);
		is.setItemMeta(m);
		return this;
	}
	
	public ItemBuilder addStringInLoreAbove(String string) {
		List<String> lore = getLore();
		lore.add(0, string);
		setLore(lore);
		return this;
	}
	
	public ItemBuilder addStringInLoreAfter(String target, String string) {
		List<String> lore = getLore();
		int slot = -1;
		for (String l : lore) {
			slot++;
			if (l.equals(target)) {
				break;
			}
		}
		if (slot != -1) {
			lore.add(slot + 1, string);
		}
		setLore(lore);
		return this;
	}
	
	public ItemBuilder addStringInLore(String string) {
		List<String> lore = getLore();
		lore.add(string);
		setLore(lore);
		return this;
	}
	
	public List<String> getLore() {
		return getMeta().getLore() == null ? new ArrayList<>() : getMeta().getLore();
	}
	
	public ItemMeta getMeta() {
		return is.getItemMeta();
	}
	
	private void setMeta(ItemMeta meta) {
		is.setItemMeta(meta);
	}
	
	public ItemBuilder setDurablity(short durablity) {
		is.setDurability(durablity);
		return this;
	}
	
	public Material getType() {
		return is.getType();
	}
	
	public ItemBuilder setAmount(int amount) {
		is.setAmount(amount);
		return this;
	}
	
	public int getAmount() {
		return is.getAmount();
	}
	
	public boolean loreCountains(String s) {
		for (String lore : getLore()) {
			if (lore.contains(s))return true;
		}
		return false;
	}
	
	public String getStringInLoreWhereContains(String s) {
		for (String lore : getLore()) {
			if (lore.contains(s))return lore;
		}
		return null;
	}
	
	public ItemBuilder enchant(Enchantment ench, int level) {
		is.addEnchantment(ench, level);
		return this;
	}
	
	public ItemBuilder unsafeEnchant(Enchantment ench, int level) {
		is.addUnsafeEnchantment(ench, level);
		return this;
	}
	
	public boolean isMaxEnchant(Enchantment ench) {
		return is.getEnchantmentLevel(ench) >= ench.getMaxLevel();
	}
	
	public ItemBuilder setRunnuble(BukkitTask runnuble) {
		this.bt = runnuble;
		return this;
	}
	
	public void cancelRunnuble() {
		if (this.bt != null) {
			this.bt.cancel();
		}
	}
	
}
