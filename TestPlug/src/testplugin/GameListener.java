package testplugin;

import java.math.BigDecimal;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import net.ess3.api.MaxMoneyException;
import net.ess3.api.events.JailStatusChangeEvent;
import net.ess3.api.events.MuteStatusChangeEvent;
import net.essentialsx.api.v2.events.UserKickEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import testplugin.data.MySQL;

public class GameListener implements Listener{

	private MySQL sql;
	
	public GameListener(TestPlug plug) {
		Bukkit.getPluginManager().registerEvents(this, plug);
		this.sql = plug.getMySQL();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.sendTitle("§a§lПривет §6§l" + e.getPlayer().getName(), "", 20, 20, 20);
		TextComponent text = new TextComponent("Кликабельное сообщение");
		text.setClickEvent(new ClickEvent(Action.OPEN_URL, "https://www.google.com/"));
		text.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new TextComponent[] {new TextComponent("Перейти на сайт гугла")}));
		p.spigot().sendMessage(text);
		sql.checkPlayer(p.getUniqueId());
		new BukkitRunnable() {
			int i = 0;
			User user = getUser(p.getUniqueId());
			@Override
			public void run() {
				if (!p.isOnline()) {
					this.cancel();
					return;
				}
				i++;
				p.giveExp(10);
				if (i%4 == 0) {
					try {
						user.giveMoney(new BigDecimal(50));
					} catch (MaxMoneyException e) {
						e.printStackTrace();
					}
				}
			}
		}.runTaskTimer(TestPlug.INSTANCE, 0, 600);
	}
	
	@EventHandler
	public void onMute(MuteStatusChangeEvent e) {
		if (e.getValue()) {
			sql.addMute(e.getAffected().getBase().getUniqueId());
		}
	}
	
	@EventHandler
	public void onKick(UserKickEvent e) {
		sql.addKick(e.getKicked().getBase().getUniqueId());
	}
	
	@EventHandler
	public void onJail(JailStatusChangeEvent e) {
		if (e.getValue()) {
			sql.addJail(e.getAffected().getBase().getUniqueId());
		}
	}
	
	private User getUser(UUID id) {
		Essentials ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
		if (ess != null) {
			return ess.getUser(id);
		}
		return null;
	}
	
}
