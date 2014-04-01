package me.skipperguy12.autobroadcasterplus;

import me.skipperguy12.autobroadcasterplus.Config;
import me.skipperguy12.autobroadcasterplus.utils.Log;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Class to handle Messages
 */
public class Messages {
    // File representing messagesFile.txt
    private File messagesFile;

    // The plugin's dataFolder
    private File dataFolder;

    // The messages parsed from messagesFile
    private List<String> messages;


    /**
     * Loads this class and sets all variables that require the plugin instance
     *
     * @param plugin instance of plugin
     * @param fileName the name of the messages file
     */
    public void load(Plugin plugin, String fileName) {
        dataFolder = plugin.getDataFolder();
        messagesFile = new File(dataFolder + File.separator + fileName);
        if (!messagesFile.exists()) {
            try {
                messagesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String contents = "";
        try {
            contents = FileUtils.readFileToString(messagesFile);
            Log.debug(contents);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot read file at location "
                    + messagesFile.getAbsolutePath());
        }
        messages = Arrays.asList(contents.split(Config.Broadcaster.delimeter));
    }

    /**
     * Gets the File storing the messages
     * @return File storing the messages
     */
    public File getMessagesFile() {
        return messagesFile;
    }

    /**
     * Gets the messages in an ordered list
     * @return List of messages
     */
    public List<String> getMessages() {
        return messages;
    }
}
