package me.skipperguy12.AutoBroadcasterPlus.Settings;

import me.anxuiz.settings.Setting;
import me.anxuiz.settings.SettingBuilder;
import me.anxuiz.settings.SettingCallbackManager;
import me.anxuiz.settings.SettingRegistry;
import me.anxuiz.settings.bukkit.PlayerSettings;
import me.anxuiz.settings.types.BooleanType;
import me.anxuiz.settings.types.EnumType;

public class Settings {
	public static final Setting ANNOUNCE = new SettingBuilder().name("Announcements").alias("a").summary("Get sent announcements").description("Available options:\nON: Recieve announcements\nOFF: Disable announcements").type(new EnumType("Announcement Options", AnnouncementOptions.class)).defaultValue(AnnouncementOptions.ON).get();

	public static void register() {
		SettingRegistry registry = PlayerSettings.getRegistry();
		SettingCallbackManager callbacks = PlayerSettings.getCallbackManager();
		registry.register(ANNOUNCE);
	}
}
