package hotfies.perfectemojieffects.listeners;

import io.lumine.mythic.bukkit.MythicBukkit;
import hotfies.perfectemojieffects.PerfectEmojiEffects;
import hotfies.perfectemojieffects.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerDeathListener implements Listener {
    private final PerfectEmojiEffects plugin;

    public PlayerDeathListener(PerfectEmojiEffects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String playerUuid = event.getEntity().getUniqueId().toString();

        try (Connection connection = plugin.getDatabase().getConnection()) {
            String query = "SELECT death_id FROM perfect_emojis WHERE player_uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerUuid);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String emojiKey = resultSet.getString("death_id");
                    if (emojiKey != null) {
                        FileConfiguration emojisConfig = ConfigLoader.getEmojisConfig();
                        String path = "animations.death." + emojiKey + ".mm-id";
                        String mmId = emojisConfig.getString(path);
                        if (mmId != null) {
                            // Get player location to spawn the mob
                            Location location = event.getEntity().getLocation();
                            // Spawn the MythicMob
                            MythicBukkit.inst().getMobManager().spawnMob(mmId, location);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Could not retrieve death emoji: " + e.getMessage());
        }
    }
}