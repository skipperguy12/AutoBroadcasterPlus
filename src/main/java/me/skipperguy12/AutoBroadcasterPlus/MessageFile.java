package me.skipperguy12.autobroadcasterplus;

import me.skipperguy12.autobroadcasterplus.utils.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class MessageFile {
    // Messages file
    protected final File file;

    // The world the messages are meant for (is nullable if this is not a global messages file)
    protected
    @Nullable
    World world;


    // Should the messages in this file go across to all worlds?
    private final boolean global;

    // The messages parsed from messagesFile
    private final List<String> messages;


    public MessageFile(File file) {
        this.file = file;

        // Check if exists, create if not
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Check if global or per world
        String fileName = FilenameUtils.getBaseName(file.getName());
        if (fileName.contains("messages-")) { // If this is a global messages file...
            this.global = true;
            String worldName = StringUtils.substringAfter(fileName, "-");
            if (Bukkit.getWorld(worldName) != null)
                this.world = Bukkit.getWorld(worldName);
            else
                Log.log(Level.WARNING, "Unable to load message file: " + file.getName() + " because the world specified, " + worldName + ", does not exist.");
        } else
            this.global = false;


        // Parse messages into list
        String contents = "";
        try {
            contents = FileUtils.readFileToString(file);
            Log.debug(contents);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot read file at location "
                    + file.getAbsolutePath());
        }
        messages = Arrays.asList(contents.split(Config.Broadcaster.delimeter));

    }

    public File getFile() {
        return this.file;
    }

    public @Nullable World getWorld() {
        return this.world;
    }

    public boolean isGlobal() {
        return this.global;
    }

    public List<String> getMessages() {
        return this.messages;
    }

}
