package me.skipperguy12.AutoBroadcasterPlus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;

import me.anxuiz.settings.bukkit.PlayerSettings;
import me.skipperguy12.AutoBroadcasterPlus.Settings.AnnouncementOptions;
import me.skipperguy12.AutoBroadcasterPlus.Settings.Settings;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class AutoBroadcasterPlus extends JavaPlugin {
	private int currentLine = 0;
	private int tid = 0;
	private int running = 1;
	public boolean settingsPlugin = false;

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " has been disabled!");
	}

	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("BukkitSettings") != null) {
			settingsPlugin = true;
			Settings.register();
		}

		PluginDescriptionFile pdfFile = getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");

		if (getDataFolder().exists()) {
			getLogger().info("File is there.");
		} else {
			createConfig(new File(getDataFolder(), "config.yml"));
			getLogger().info("File didn't exist");
		}

		final File msgs = new File(getDataFolder() + File.separator + "messages.txt");
		if (!msgs.exists()) {
			try {
				msgs.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		tid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				try {
					AutoBroadcasterPlus.this.broadcastMessages(msgs.getPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0L, getConfig().getLong("interval") * 20L);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
		}
	}

	public void createConfig(File f) {
		InputStream cfgStream = getResource("config.yml");
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		try {
			FileOutputStream fos = new FileOutputStream(f);
			ReadableByteChannel rbc = Channels.newChannel(cfgStream);
			fos.getChannel().transferFrom(rbc, 0L, 16777216L);
			fos.close();
		} catch (Exception e) {
			getLogger().info("There was an error in creating the config. Using bukkit methods to do so.");
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}

	public void broadcastMessages(String filename) throws IOException {
		if (numberOfOnlinePlayers() >= 1) {
			FileInputStream fs = new FileInputStream(filename);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			for (int i = 0; i < currentLine; i++) {
				br.readLine();
			}

			String line = br.readLine().replaceAll("&", "§");
			String ann = getConfig().getString("announcerName").replaceAll("&", "§");

			for (Player p : Bukkit.getOnlinePlayers()) {
				String line2 = line;
				String ann2 = ann;
				line2 = line.replace("%player%", p.getName());
				ann2 = ann.replace("%player%", p.getName());

				if (settingsPlugin) {
					boolean showAnnouncement = PlayerSettings.getManager(p).getValue(Settings.ANNOUNCE, AnnouncementOptions.class) == AnnouncementOptions.ON;

					if ((showAnnouncement)) {
						p.sendMessage(ann2 + "" + ChatColor.WHITE + line2);
					}
				} else {
					p.sendMessage(ann2 + "" + ChatColor.WHITE + line2);
				}

			}
			LineNumberReader lnr = new LineNumberReader(new FileReader(new File(filename)));
			lnr.skip(9223372036854775807L);
			int lastLine = lnr.getLineNumber();
			if (currentLine + 1 == lastLine + 1)
				currentLine = 0;
			else
				currentLine += 1;
			lnr.close();

		}

	}

	public static int numberOfOnlinePlayers() {
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		return onlinePlayers.length;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ab")) {
			if ((sender.isOp()) || (sender.hasPermission("ab.reload"))) {
				reloadConfig();
				sender.sendMessage("" + ChatColor.AQUA + "[AutoBroadcaster]: " + "" + ChatColor.RED + "Auto Broadcaster Configuration files reloaded successfully!");
				return true;
			}

			sender.sendMessage("" + ChatColor.AQUA + "[AutoBroadcaster]: " + "" + ChatColor.RED + "You do not have sufficient permissions! (OP or ab.reload)");
		}

		return false;
	}
}