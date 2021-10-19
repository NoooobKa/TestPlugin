package testplugin;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sun.istack.internal.NotNull;

public class GUI {

	private Inventory inv;
	
	public GUI() {
		this.inv = Bukkit.createInventory(null, 9, "GUI");
		inv.setItem(0, new ItemBuilder(Material.RED_DYE).setName("§aHeal").build());
		inv.setItem(1, new ItemBuilder(Material.COOKED_CHICKEN).setName("§eFood").build());
		inv.setItem(2, new ItemBuilder(Material.NETHER_STAR).setName("§6Heal + Food").build());
		inv.setItem(3, new ItemBuilder(Material.SKELETON_SKULL).setName("§cSuicide").build());
		inv.setItem(4, new ItemBuilder(Material.POTION).setName("§dRandom effect").build());
		inv.setItem(5, new ItemBuilder(Material.MILK_BUCKET).setName("§aClear effects").build());
	}
	
	public void open(Player p) {
		p.openInventory(inv);
	}
	
	public static void init(JavaPlugin p) {
		new GUICommand(p);
		Bukkit.getPluginManager().registerEvents(new GUIListener(), p);
	}
	
	private static class GUIListener implements Listener {
		
		@EventHandler
		public void onInventory(InventoryClickEvent e) {
			if (e.getClickedInventory() != null) {
				if (e.getView().getTitle() != null && e.getView().getTitle().equals("GUI")) {
					if (e.getClickedInventory().equals(e.getView().getTopInventory())) {
						e.setCancelled(true);
						Player p = ((Player)e.getWhoClicked());
						switch (e.getSlot()) {
						case 0:
							p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
							break;
						case 1:
							p.setFoodLevel(20);
							break;
						case 2:
							p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
							p.setFoodLevel(20);
							break;
						case 3:
							p.setHealth(0.0D);
							break;
						case 4:
							p.addPotionEffect(PotionEffectType.values()[new Random().nextInt(PotionEffectType.values().length)].createEffect(100, 0));
							break;
						case 5:
							clearEffects(p);
							break;
						default:
							break;
						}
					}
				}
			}
		}
		
		private void clearEffects(Player p) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					for (PotionEffect ef : p.getActivePotionEffects()) {
						p.removePotionEffect(ef.getType());
					}
				}
			}.runTaskLater(TestPlug.INSTANCE, 40);
		}
	}
	
	private static class GUICommand implements CommandExecutor {
		
		public GUICommand(JavaPlugin p) {
			p.getCommand("GUI").setExecutor(this);
		}

		@Override
		public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
			if (!(s instanceof Player)) {
				return true;
			}
			new GUI().open(((Player)s));
			return true;
		}
		
	}
	
}
