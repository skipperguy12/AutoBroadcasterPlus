package me.skipperguy12.autobroadcasterplus;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

/**
 * Configuration handler
 */
public class Config {

    protected static FileConfiguration config;
    protected static File dataFolder;


    /**
     * Gets a casted object from a path in config, returns def if null
     *
     * @param world   World to get setting from
     * @param setting setting value to get
     * @param def     def object to return if null
     * @param <T>     object type
     * @return casted object from path, def if null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getSettingFromWorld(World world, String setting, Object def) {
        if (world != null)
            return (T) config.get("broadcaster." + world.getName() + "." + setting, def);
        else
            return (T) config.get("broadcaster.global." + setting, def);
    }


    /**
     * Gets a casted object from a path in config
     *
     * @param path path to object
     * @param <T>  object type
     * @return casted object from path
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String path) {
        return (T) config.get(path);
    }

    /**
     * Gets a casted object from a path in config, returns def if null
     *
     * @param path path to object
     * @param def  def object to return if null
     * @param <T>  object type
     * @return casted object from path, def if null
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String path, Object def) {
        return (T) config.get(path, def);
    }


    /**
     * Sets the value of an object at specified path
     *
     * @param path  path to set object
     * @param value value to set at path
     */
    public static void set(String path, Object value) {
        config.set(path, value);
        try {
            config.save(new File(dataFolder, config.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads this class and sets all variables that require the plugin instance
     *
     * @param plugin instance of plugin
     */
    public static void load(Plugin plugin, String fileName) {
        dataFolder = plugin.getDataFolder();
        config = YamlConfiguration.loadConfiguration(new File(dataFolder, fileName));
    }


    /**
     * Config section; broadcaster section
     */
    public static class Broadcaster {
        public static class Global {

            /**
             * Config variable; who is the announcer?
             */
            public static String announcerName = get("broadcaster.global.announcerName", "[&4AutoBroadcaster&f]: ");
            /**
             * Config variable; how often, in seconds, should the message be broadcasted?
             */
            public static Integer interval = get("broadcaster.global.interval", 60);
            /**
             * Config variable; is debugging enabled?
             */
            public static boolean debugging = get("broadcaster.global.debugging", false);
            /**
             * Config variable; delimeter to split messages by
             */
            public static String delimeter = get("broadcaster.global.delimeter", "\n");
            /**
             * Config variable; should messages be broadcasted to console?
             */
            public static boolean broadcast_to_console = get("broadcaster.global.broadcast-to-console", false);
            /**
             * Config variable; min number of people before messages will be broadcasted
             */
            public static Integer min_players = get("broadcaster.global.min-players", 1);
        }


    }
} 