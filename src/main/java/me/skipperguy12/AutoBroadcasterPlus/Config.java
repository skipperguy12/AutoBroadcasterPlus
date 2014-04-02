package me.skipperguy12.autobroadcasterplus;

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
        /**
         * Config variable; who is the announcer?
         */
        public static String announcerName = get("broadcaster.announcerName", "[&4AutoBroadcaster&f]: ");


        /**
         * Config variable; how often, in seconds, should the message be broadcasted?
         */
        public static Integer interval = get("broadcaster.interval", 60);

        /**
         * Config variable; is debugging enabled?
         */
        public static boolean debugging = get("broadcaster.debugging", false);

        /**
         * Config variable; delimeter to split messages by
         */
        public static String delimeter = get("broadcaster.delimeter", "\n");

        /**
         * Config variable; should messages be broadcasted to console?
         */
        public static boolean broadcast_to_console = get("broadcaster.broadcast-to-console", false);

        /**
         * Config variable; min number of people before messages will be broadcasted
         */
        public static Integer min_players = get("broadcaster.min-players", 1);


    }
} 