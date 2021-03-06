package me.skipperguy12.autobroadcasterplus.runnables;

import me.skipperguy12.autobroadcasterplus.AutoBroadcasterPlus;
import me.skipperguy12.autobroadcasterplus.Config;
import me.skipperguy12.autobroadcasterplus.MessageFile;
import me.skipperguy12.autobroadcasterplus.settings.AnnouncementOptions;
import me.skipperguy12.autobroadcasterplus.settings.Settings;
import me.skipperguy12.autobroadcasterplus.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

import static me.anxuiz.settings.bukkit.PlayerSettings.getManager;

public class MessagesRunnable implements Runnable {

    // Instance of plugin
    private AutoBroadcasterPlus instance;

    // File storing messages
    private MessageFile messageFile;

    // List of messages
    private List<String> messages;

    // Position in list
    private int current = -1;

    public MessagesRunnable(AutoBroadcasterPlus instance, MessageFile messageFile) {
        this.instance = instance;
        this.messageFile = messageFile;
        Log.debug("Runnable for " + messageFile.getFile().getName() + " has been instantiated.");
        this.messages = messageFile.getMessages();
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

    private void sendMessage(Player p, String message) {
        String line = ChatColor.translateAlternateColorCodes('&', message).replace("%player%", p.getName());
        String announcerName = ChatColor.translateAlternateColorCodes('&', (String) Config.getSettingFromWorld(messageFile.getWorld(), "announcerName", Config.Broadcaster.Global.announcerName)).replace("%player%", p.getName());

        if (instance.settingsPlugin) {
            boolean showAnnouncement = getManager(p).getValue(Settings.ANNOUNCE, AnnouncementOptions.class) == AnnouncementOptions.ON;

            if ((showAnnouncement)) {
                p.sendMessage(announcerName + ChatColor.WHITE + line);
                if (Config.Broadcaster.Global.broadcast_to_console)
                    Bukkit.getConsoleSender().sendMessage(announcerName + ChatColor.WHITE + line);

            }
        } else {
            p.sendMessage(announcerName + ChatColor.WHITE + line);
            if (Config.getSettingFromWorld(messageFile.getWorld(), "broadcast-to-console", Config.Broadcaster.Global.broadcast_to_console))
                Bukkit.getConsoleSender().sendMessage(announcerName + ChatColor.WHITE + line);

        }
    }

    // Broadcasts message to players who have the setting enabled

    private void broadcastMessage(String message) {
        if (messageFile.isGlobal()) {
            Log.debug("Broadcasting: " + message + " to everyone.");
            for (Player p : Bukkit.getOnlinePlayers()) {
                sendMessage(p, message);
            }
        } else {
            Log.debug("Broadcasting: " + message + " to players in " + messageFile.getWorld().getName());
            for (Player p : messageFile.getWorld().getPlayers()) {
                sendMessage(p, message);
            }
        }
    }


    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().length >= ((Integer) Config.getSettingFromWorld(messageFile.getWorld(), "min-players", Config.Broadcaster.Global.min_players)))
            broadcastMessage(getNextMessageAndIncrement());
    }
}
