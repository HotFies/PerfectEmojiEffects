package hotfies.perfectemojieffects.commands;

import hotfies.perfectemojieffects.PerfectEmojiEffects;
import hotfies.perfectemojieffects.utils.ConfigLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class PerfectEmojiCommand implements CommandExecutor {
    private final PerfectEmojiEffects plugin;

    public PerfectEmojiCommand(PerfectEmojiEffects plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2) {
            return true;
        }

        String type = args[0];
        String name = args[1];

        FileConfiguration emojisConfig = ConfigLoader.getEmojisConfig();
        String path = "animations." + type + "." + name;

        if (!emojisConfig.contains(path)) {
            return true;
        }

        String permission = emojisConfig.getString(path + ".permission");
        if (permission != null && !player.hasPermission(permission)) {
            return true;
        }

        // Update the database with the selected emoji key (name)
        try (Connection connection = plugin.getDatabase().getConnection()) {
            updatePlayerEmoji(connection, player.getUniqueId(), type, name);
        } catch (SQLException e) {
            // Handle exception silently
        }

        return true;
    }

    private void updatePlayerEmoji(Connection connection, UUID playerUuid, String type, String name) throws SQLException {
        String query = "INSERT INTO perfect_emojis (player_uuid, " + type + "_id) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE " + type + "_id = VALUES(" + type + "_id)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerUuid.toString());
            statement.setString(2, name);
            statement.executeUpdate();
        }
    }
}