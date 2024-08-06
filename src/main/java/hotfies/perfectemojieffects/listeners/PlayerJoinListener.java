package hotfies.perfectemojieffects.listeners;

import io.lumine.mythic.bukkit.MythicBukkit;
import hotfies.perfectemojieffects.PerfectEmojiEffects;
import hotfies.perfectemojieffects.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerJoinListener implements Listener {
    private final PerfectEmojiEffects plugin;

    public PlayerJoinListener(PerfectEmojiEffects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerUuid = event.getPlayer().getUniqueId().toString();

        try (Connection connection = plugin.getDatabase().getConnection()) {
            // Check if player already exists in the database
            String checkQuery = "SELECT 1 FROM perfect_emojis WHERE player_uuid = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, playerUuid);
                ResultSet resultSet = checkStatement.executeQuery();
                if (!resultSet.next()) {
                    // Player does not exist, insert new record with null values
                    String insertQuery = "INSERT INTO perfect_emojis (player_uuid, death_id, kill_id, respawn_id, join_id) VALUES (?, NULL, NULL, NULL, NULL)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setString(1, playerUuid);
                        insertStatement.executeUpdate();
                    }
                }
            }

            // Existing logic to play join animation if set
            String selectQuery = "SELECT join_id FROM perfect_emojis WHERE player_uuid = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setString(1, playerUuid);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    String emojiKey = resultSet.getString("join_id");
                    if (emojiKey != null) {
                        FileConfiguration emojisConfig = ConfigLoader.getEmojisConfig();
                        String path = "animations.join." + emojiKey + ".mm-id";
                        String mmId = emojisConfig.getString(path);
                        if (mmId != null) {
                            // Get player location to spawn the mob
                            Location location = event.getPlayer().getLocation();
                            // Spawn the MythicMob
                            MythicBukkit.inst().getMobManager().spawnMob(mmId, location);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not process player join: " + e.getMessage());
        }
    }
}