package me.skipperguy12.autobroadcasterplus;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import me.skipperguy12.autobroadcasterplus.runnables.MessagesRunnable;
import me.skipperguy12.autobroadcasterplus.settings.Settings;
import me.skipperguy12.autobroadcasterplus.Config;
import me.skipperguy12.autobroadcasterplus.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin main class
 */
public class AutoBroadcasterPlus extends JavaPlugin {
    /**
     * Holds instance for plugin
     */
    private static AutoBroadcasterPlus instance;

    /**
     * sk89q's command framework CommandsManager
     */
    private CommandsManager<CommandSender> commands;

    /**
     * has the settings plugin been found?
     */
    public boolean settingsPlugin = false;

    /**
     * handles the messages
     */
    public Messages messages;

    /**
     * Called when the plugin gets enabled
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Config.load(this, "config.yml");

        // Set singleton instance
        instance = this;

        messages = new Messages();
        messages.load(this, "messages.txt");

        Log.load(this);

        // Scan plugins to try and find the settings plugin
        if (Bukkit.getPluginManager().getPlugin("BukkitSettings") != null) {
            settingsPlugin = true;
            Settings.register();
        }

        // Start the task
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MessagesRunnable(this), 0L, Config.Broadcaster.interval * 20L);

        // Set up commands and register listeners
        this.setupCommands();
        this.registerListeners();

    }

    /**
     * Called when the plugin gets disabled
     */
    public void onDisable() {
        instance = null;
    }

    // Registers Events for a listener
    private void registerEvents(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    // Registers the Listeners that will be on while the plugin is enabled
    private void registerListeners() {
        // TODO: Register listeners
    }

    /**
     * sk89q's command framework method to setup commands from onEnable
     */
    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);
        // TODO: Register commands here
    }

    /**
     * Gets instance of plugin
     *
     * @return main plugin instance
     */
    public static AutoBroadcasterPlus getInstance() {
        return instance;
    }

    // Passes commands from Bukkit to sk89q
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }

        return true;
    }
}