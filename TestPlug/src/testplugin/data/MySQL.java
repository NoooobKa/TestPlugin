package testplugin.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import testplugin.TestPlug;

public class MySQL {
	
	private String Host;
    private String Database;
    private String Username;
    private String Password;
    private Connection connection;
    
    public MySQL(final String host, final String database, final String username, final String password) {
        this.Host = host;
        this.Database = database;
        this.Username = username;
        this.Password = password;
    }
    
    public String getDatabase() {
    	return Database;
    }
    
    public void Connect() {
    	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §aConnecting§f...");
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e1) {
            Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cThe required classes are missing§f!");
            e1.printStackTrace();
        }
    	final String url = "jdbc:mysql://" + this.Host + ":3306/" + this.Database + "?autoReconnect=true&enabledTLSProtocols=TLSv1.2";//"?useUnicode=true&characterEncoding=utf-8";//UTF connection
        //final String url = "jdbc:mysql://" + this.Host + ":3306/" + this.Database;
        try {
            this.connection = DriverManager.getConnection(url, this.Username, this.Password);

            //c = DriverManager.getConnection(url, this.Username, this.Password);
            Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §aConnected§f!");
        }
        catch (SQLException e2) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cThere was a connection error§f!");
            e2.printStackTrace();
        }
    }
    
    public void Disconnect() {
        try {
            if (!this.connection.isClosed() && this.connection != null) {
                this.connection.close();
                Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §eThe connection to the MySQL server was successfully disconnected§f!");
            }
            else {
            	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cThe connection is already terminated!");
            }
        }
        catch (SQLException e3) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cAn error occurred while disconnecting§f!");
            e3.printStackTrace();
        }
    }
    
    public void ExecuteCommand(final String command) {
        try {
            if (this.connection.isClosed()) {
                this.Connect();
            }
            final Statement st = this.connection.createStatement();
            st.executeUpdate(command);
        }
        catch (SQLException e5) {
        	Bukkit.getServer().getConsoleSender().sendMessage("§7§l[§2MySQL§7§l] §cAn error occurred while executing the command§f!");
            e5.printStackTrace();
        }
    }
    
	public Connection getConnection() {
		return this.connection;
	}
	
	public void checkPlayer(UUID id) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			boolean exist = true;
			c = getConnection();
			ps = c.prepareStatement("SELECT * FROM test_bd WHERE UUID = ?");
			ps.setString(1, id.toString());
			rs = ps.executeQuery();
			if (!rs.next())exist = false;
			
			if (!exist) {
				PreparedStatement insert = getConnection().prepareStatement("INSERT INTO test_bd(Name,UUID,Mute,Kick,Jail) VALUE(?,?,?,?,?)");
				insert.setString(1, Bukkit.getPlayer(id).getName());
				insert.setString(2, id.toString());
				insert.setInt(3, 0);
				insert.setInt(4, 0);
				insert.setInt(5, 0);
				insert.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addMute(UUID id) {
		setAsyncronously(id, "Mute", getValue(id, "Mute") + 1);
	}
	
	public void addKick(UUID id) {
		setAsyncronously(id, "Kick", getValue(id, "Kick") + 1);
	}
	
	public void addJail(UUID id) {
		setAsyncronously(id, "Jail", getValue(id, "Jail") + 1);
	}
	
	private int getValue(UUID id, String field) {
		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			c = getConnection();
			ps = c.prepareStatement("SELECT " + field + " FROM test_bd WHERE UUID = ?");
			ps.setString(1, id.toString());
			rs = ps.executeQuery();
			rs.next();
			return rs.getInt(field);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	private void setAsyncronously(UUID id, String field, Object o) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Connection c = null;
				PreparedStatement ps = null;
				try {
					c = getConnection();
					ps = c.prepareStatement("UPDATE test_bd SET " + field + " = ? WHERE UUID = ?");
					ps.setObject(1, o);
					ps.setString(2, id.toString());
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(TestPlug.INSTANCE);
	}
}
