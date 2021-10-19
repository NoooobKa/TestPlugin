package testplugin.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public enum MainConfig {

	MySQL_HOSTNAME("MySQL.Host-Name", "hostname"),
	MySQL_DATABASE("MySQL.Database", "database"),
	MySQL_USERNAME("MySQL.User-Name", "username"),
	MySQL_PASSWORD("MySQL.Password", "password");
	
    private final String path;
    private final Object def;
    private static YamlConfiguration configFile;
    private static File rawFile;
    
    private MainConfig(final String path, final Object def) {
        this.path = path;
        this.def = def;
    }
	
    public static void set(final String path, final Object val) {
        if (MainConfig.configFile != null) {
            MainConfig.configFile.set(path, val);
            save();
        }
    }
    
    public static void save() {
        if (MainConfig.rawFile != null && MainConfig.configFile != null) {
            try {
                MainConfig.configFile.save(MainConfig.rawFile);
            }
            catch (IOException ex) {}
        }
    }
    
    public String getPath() {
        return this.path;
    }
    
    public Object getDefault() {
        return this.def;
    }
    
    public static void setFile(final YamlConfiguration config) {
        MainConfig.configFile = config;
    }
    
    public static void setRawFile(final File f) {
        MainConfig.rawFile = f;
    }
    
    public File toFile() {
        return new File(MainConfig.configFile.getString(this.path));
    }
    
    public int toInt() {
        if (!(this.def instanceof Integer)) {
            return 0;
        }
        return MainConfig.configFile.getInt(this.path, (int)this.def);
    }
    
    public boolean toBoolean() {
        return this.def instanceof Boolean && MainConfig.configFile.getBoolean(this.path, (boolean)this.def);
    }
    
    public double toDouble() {
    	if (this.def instanceof Double) {
    		return MainConfig.configFile.getDouble(this.path, (double) this.def);
    	}
    	return 0.0;
    }
    
    @Override
    public String toString() {
    	if (!(this.def instanceof String)) {
    		return null; 	}
    	return MainConfig.configFile.getString(this.path, (String) this.def);
    }
    
	public static void checkConfig(Plugin p) {
		if (!p.getDataFolder().exists()) {
			p.getDataFolder().mkdirs();
		}
		File file = new File(p.getDataFolder(), "MainConfig.yml");
		boolean exist = true;
		if (!file.exists()) {
			exist = false;
			try {
				file.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
		if (!exist) {
			for (MainConfig c : MainConfig.values()) {
				configFile.set(c.getPath(), c.getDefault());
			}
		}else {
			for (MainConfig object : values()) {
				if (!configFile.contains(object.getPath())) {
					configFile.set(object.path, object.def);
				}
			}
		}
		try {
			configFile.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainConfig.setRawFile(file);
		MainConfig.setFile(configFile);
	}
	
	
}
