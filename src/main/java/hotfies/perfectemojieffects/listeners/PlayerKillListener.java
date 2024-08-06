package hotfies.perfectemojieffects.listeners;

import io.lumine.mythic.bukkit.MythicBukkit;
import hotfies.perfectemojieffects.PerfectEmojiEffects;
import hotfies.perfectemojieffects.utils.ConfigLoader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerKillListener implements Listener {
    private final PerfectEmojiEffects plugin;

    public PlayerKillListener(PerfectEmojiEffects plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            String playerUuid = killer.getUniqueId().toString();

            try (Connection connection = plugin.getDatabase().getConnection()) {
                String query = "SELECT kill_id FROM perfect_emojis WHERE player_uuid = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, playerUuid);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        String emojiKey = resultSet.getString("kill_id");
                        if (emojiKey != null) {
                            FileConfiguration emojisConfig = ConfigLoader.getEmojisConfig();
                            String path = "animations.kill." + emojiKey + ".mm-id";
                            String mmId = emojisConfig.getString(path);
                            if (mmId != null) {
                                // Get player location to spawn the mob
                                Location location = killer.getLocation();
                                // Spawn the MythicMob
                                MythicBukkit.inst().getMobManager().spawnMob(mmId, location);
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Could not retrieve kill emoji: " + e.getMessage());
            }
        }
    }
}