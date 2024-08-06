package hotfies.perfectemojieffects.utils;

import hotfies.perfectemojieffects.PerfectEmojiEffects;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    private static FileConfiguration emojisConfig;

    public static void loadConfig(PerfectEmojiEffects plugin) {
        plugin.saveDefaultConfig();
    }

    public static void loadEmojis(PerfectEmojiEffects plugin) {
        File emojisFile = new File(plugin.getDataFolder(), "Emojis.yml");
        if (!emojisFile.exists()) {
            plugin.saveResource("Emojis.yml", false);
        }
        emojisConfig = YamlConfiguration.loadConfiguration(emojisFile);
        plugin.getLogger().info("Emojis.yml loaded");
    }

    public static FileConfiguration getEmojisConfig() {
        return emojisConfig;
    }
}