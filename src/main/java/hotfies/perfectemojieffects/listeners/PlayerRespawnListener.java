package hotfies.perfectemojieffects.listeners;

import io.lumine.mythic.bukkit.MythicBukkit;
import hotfies.perfectemojieffects.PerfectEmojiEffects;
import hotfies.perfectemojieffects.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRespawnListener implements Listener {
    private final PerfectEmojiEffects plugin;

    public PlayerRespawnListener(PerfectEmojiEffects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        String playerUuid = event.getPlayer().getUniqueId().toString();

        try (Connection connection = plugin.getDatabase().getConnection()) {
            String query = "SELECT respawn_id FROM perfect_emojis WHERE player_uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUuid);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String emojiKey = resultSet.getString("respawn_id");
                    if (emojiKey != null) {
                        FileConfiguration emojisConfig = ConfigLoader.getEmojisConfig();
                        String path = "animations.respawn." + emojiKey + ".mm-id";
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
            plugin.getLogger().severe("Could not retrieve respawn emoji: " + e.getMessage());
        }
    }
}