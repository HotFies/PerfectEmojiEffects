package hotfies.perfectemojieffects;

import hotfies.perfectemojieffects.commands.PerfectEmojiCommand;
import hotfies.perfectemojieffects.listeners.PlayerJoinListener;
import hotfies.perfectemojieffects.listeners.PlayerDeathListener;
import hotfies.perfectemojieffects.listeners.PlayerKillListener;
import hotfies.perfectemojieffects.listeners.PlayerRespawnListener;
import hotfies.perfectemojieffects.utils.ConfigLoader;
import hotfies.perfectemojieffects.utils.Database;
import org.bukkit.plugin.java.JavaPlugin;

public final class PerfectEmojiEffects extends JavaPlugin {
    private Database database;

    @Override
    public void onEnable() {
        // Load configurations
        ConfigLoader.loadConfig(this);
        ConfigLoader.loadEmojis(this);

        // Setup database
        database = new Database(this);
        database.setupDatabase();

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);

        // Register command
        this.getCommand("perfectemoji").setExecutor(new PerfectEmojiCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public Database getDatabase() {
        return database;
    }
}