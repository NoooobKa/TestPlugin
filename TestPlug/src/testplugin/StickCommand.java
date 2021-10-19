package testplugin;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.sun.istack.internal.NotNull;

public class StickCommand implements CommandExecutor{

	public StickCommand(JavaPlugin p) {
		p.getCommand("Stick").setExecutor(this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
		if (!(s instanceof Player)) {
			return true;
		}
		Player p = ((Player)s);
		if (args.length == 0) {
			Random r = new Random();
			ItemBuilder ib = new ItemBuilder(Material.STICK);
			for (int i = 0; i < r.nextInt(Enchantment.values().length) + 1; i++) {
				ib.unsafeEnchant(Enchantment.values()[r.nextInt(Enchantment.values().length)], 1);
			}
			p.getInventory().addItem(ib.build());
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("effect")) {
				p.addPotionEffect(PotionEffectType.values()[new Random().nextInt(PotionEffectType.values().length)].createEffect(100, 0));
				return true;
			}
		}
		return false;
	}
	
}
