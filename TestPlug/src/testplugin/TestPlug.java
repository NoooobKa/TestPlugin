package testplugin;

import org.bukkit.plugin.java.JavaPlugin;

import testplugin.data.MainConfig;
import testplugin.data.MySQL;

public class TestPlug extends JavaPlugin{
	
	public static TestPlug INSTANCE;
	private MySQL sql;

	public void onEnable() {
		INSTANCE = this;
		MainConfig.checkConfig(this);
		this.sql = new MySQL(MainConfig.MySQL_HOSTNAME.toString(), MainConfig.MySQL_DATABASE.toString(), MainConfig.MySQL_USERNAME.toString(), MainConfig.MySQL_PASSWORD.toString());
		sql.Connect();
		checkTable(sql);
		GUI.init(this);
		new StickCommand(this);
		new GameListener(this);
	}
	
	private void checkTable(MySQL sql) {
		sql.ExecuteCommand("CREATE TABLE IF NOT EXISTS `" + MainConfig.MySQL_DATABASE.toString() + "`.`test_bd` (\r\n"
				+ "  `id` INT NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `Name` VARCHAR(45) NOT NULL,\r\n"
				+ "  `UUID` VARCHAR(45) NOT NULL,\r\n"
				+ "  `Mute` INT NOT NULL,\r\n"
				+ "  `Kick` INT NOT NULL,\r\n"
				+ "  `Jail` INT NOT NULL,\r\n"
				+ "  PRIMARY KEY (`id`))\r\n"
				+ "ENGINE = InnoDB\r\n"
				+ "DEFAULT CHARACTER SET = utf8\r\n"
				+ "COLLATE = utf8_unicode_ci;");
	}
	
	public MySQL getMySQL() {
		return sql;
	}
	
}
