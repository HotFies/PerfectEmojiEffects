package hotfies.perfectemojieffects.utils;

import hotfies.perfectemojieffects.PerfectEmojiEffects;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final PerfectEmojiEffects plugin;
    private final String url;
    private final String username;
    private final String password;

    public Database(PerfectEmojiEffects plugin) {
        this.plugin = plugin;
        FileConfiguration config = plugin.getConfig();
        String host = config.getString("database.host");
        int port = config.getInt("database.port");
        String database = config.getString("database.name");
        this.username = config.getString("database.username");
        this.password = config.getString("database.password");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void setupDatabase() {
        try (Connection connection = getConnection()) {
            createTable(connection);
            plugin.getLogger().info("Database connected and setup");
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not connect to the database: " + e.getMessage());
        }
    }

    private void createTable(Connection connection) throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS perfect_emojis (" +
                "player_uuid VARCHAR(36) NOT NULL," +
                "death_id VARCHAR(255)," +
                "kill_id VARCHAR(255)," +
                "respawn_id VARCHAR(255)," +
                "join_id VARCHAR(255)," +
                "PRIMARY KEY (player_uuid)" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }
}