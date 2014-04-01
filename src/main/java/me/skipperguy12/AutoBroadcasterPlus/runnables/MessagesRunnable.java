package me.skipperguy12.autobroadcasterplus.runnables;

import me.anxuiz.settings.bukkit.PlayerSettings;
import me.skipperguy12.autobroadcasterplus.AutoBroadcasterPlus;
import me.skipperguy12.autobroadcasterplus.Messages;
import me.skipperguy12.autobroadcasterplus.settings.AnnouncementOptions;
import me.skipperguy12.autobroadcasterplus.settings.Settings;
import me.skipperguy12.autobroadcasterplus.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static me.anxuiz.settings.bukkit.PlayerSettings.*;

public class MessagesRunnable implements Runnable {

    // Instance of plugin
    private AutoBroadcasterPlus instance;

    // List of messages
    private List<String> messages;

    // Position in list
    private int current = -1;

    public MessagesRunnable(AutoBroadcasterPlus instance) {
        this.instance = instance;
        this.messages = instance.messages.getMessages();
    }

    /**
     * Gets the next message that will be displayed
     *
     * @return the next message that will be displayed
     */
    public String getNextMessage() {
        int newI = current + 1;
        if (newI == messages.size()) newI = 0;
        return messages.get(newI);
    }

    /**
     * Gets the next message that will be displayed and shift the position in the List
     *
     * @return next message that will be displayed
     */
    public String getNextMessageAndIncrement() {
        current++;
        if (current == messages.size()) current = 0;
        return messages.get(current);
    }

    // Broadcasts message to players who have the setting enabled
    private void broadcastMessage(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            message.replaceAll("&", "§").replace("%player%", p.getName());
            String announcerName = Config.Broadcaster.announcerName.replaceAll("&", "§").replace("%player%", p.getName());

            if (instance.settingsPlugin) {
                boolean showAnnouncement = getManager(p).getValue(Settings.ANNOUNCE, AnnouncementOptions.class) == AnnouncementOptions.ON;

                if ((showAnnouncement)) {
                    p.sendMessage(announcerName + ChatColor.WHITE + message);
                }
            } else {
                p.sendMessage(announcerName + ChatColor.WHITE + message);
            }

        }
    }

    @Override
    public void run() {
        broadcastMessage(getNextMessageAndIncrement());
    }
}
