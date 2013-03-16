package com.github.skipperguy12.autobroadcaster;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

//1.5 comment FTW

public class autobroadcaster extends JavaPlugin{
	public static int currentLine = 0;
	public static int tid =  0;
	public static int running = 1;


    public void onDisable() {
        PluginDescriptionFile pdfFile = getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " has been disabled!" );
    }

    public void onEnable() {
    	try {
    		Metrics metrics = new Metrics(this); metrics.start();
    		} catch (IOException e) { // Failed to submit the stats :-(
    		System.out.println("Error Submitting stats!");
    		}
        PluginDescriptionFile pdfFile = getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
      //  getServer().getPluginManager().registerEvents(Listener, this);
       // Register the Event(s)
       // getServer().getPluginManager().registerEvents(new SanityPlayerListener(this), this);
        
      // File file = new File(getDataFolder() + File.separator + "config.yml");
       /* 
        if(!file.exists()){
        	this.getLogger().info("Generating config files for auto broadcaster");
        	//Announcer's name
        	this.getConfig().addDefault("announcerName", "[Announcer]: ");
        	//interval
        	this.getConfig().addDefault("interval", 60);
 
            //inbounds
      //  	this.getConfig().addDefault("inBound", "You are now in bounds.");
        	//level for OutOfBoundsMsg
        	//this.getConfig().addDefault("yAxis", "40");
        	
        	this.getConfig().options().copyDefaults(true);
        	
        	this.saveConfig();
      // final String announcer = this.getConfig().getString("anouncerName"); 
        	 
        }
        
        */
        
        if (this.getDataFolder().exists())
        {
         this.getLogger().info("File is there.");
        }else
        {
         createConfig(new File(this.getDataFolder(), "config.yml"));
         this.getLogger().info("File didn't exist");
        }
        
        
        
        
        
        
        
        
        final File msgs =new File(getDataFolder() + File.separator + "messages.txt");
		if(!msgs.exists()){
			 try {
				msgs.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		tid = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

			@Override
			public void run() {
				
				try{
				    broadcastMessages(msgs.getPath());
				}catch(IOException e){
				    e.printStackTrace();
				}
			}
			
		}, 0, this.getConfig().getLong("interval") * 20);


}
    
    public void createConfig(File f){
        InputStream cfgStream = this.getResource("config.yml");
        if (!this.getDataFolder().exists()){
            this.getDataFolder().mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ReadableByteChannel rbc = Channels.newChannel(cfgStream);
            fos.getChannel().transferFrom(rbc, 0, 1 << 24);
            fos.close();
         
        } catch (Exception e) {
          //log here is the instance of the logger obtained with getLogger().
            this.getLogger().info("There was an error in creating the config. Using bukkit methods to do so.");
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
        }
    
	public  void broadcastMessages(String filename) throws IOException{
		
		if(numberOfOnlinePlayers() >= 1){
			
		
		FileInputStream fs;
		fs = new FileInputStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		for(int i = 0; i < currentLine; ++i)
			br.readLine();
		/*
		 # &b = Aqua 
# &0 = Black
# &l = Bold
# &3 = Dark Aqua
# &1 = Dark Blue
# &8 = Dark Gray 
# &2  = Dark Green
# &4 = Dark Red
# &6 = Gold
# &7 = Gray
# &a = Green
# &o = Italic
# &k = Random character(s)
# &c = Red
# &r = Reset 	WARNING: If you have &r%player% &resetdied by %killer% Everything after the reset will be back the basic color.
# &m = Strike
# &n = Underline
# &f = White
# &e = Yellow
		 */
		String line = br.readLine();
		String ann = this.getConfig().getString("announcerName");
		line = line.replaceAll("&0", ChatColor.BLACK + "");
		line = line.replaceAll("&l", ChatColor.BOLD + "");
		line = line.replaceAll("&3", ChatColor.DARK_AQUA + "");
		line = line.replaceAll("&1", ChatColor.DARK_BLUE + "");
		line = line.replaceAll("&8", ChatColor.DARK_GRAY + "");
		line = line.replaceAll("&2", ChatColor.DARK_GREEN + "");
		line = line.replaceAll("&4", ChatColor.DARK_RED + "");
		line = line.replaceAll("&6", ChatColor.GOLD + "");
		line = line.replaceAll("&7", ChatColor.GRAY + "");
		line = line.replaceAll("&a", ChatColor.GREEN + "");
		line = line.replaceAll("&o", ChatColor.ITALIC + "");
		line = line.replaceAll("&k", ChatColor.MAGIC + "");
		line = line.replaceAll("&c", ChatColor.RED + "");
		line = line.replaceAll("&r", ChatColor.RESET + "");
		line = line.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		line = line.replaceAll("&n", ChatColor.UNDERLINE + "");
		line = line.replaceAll("&f", ChatColor.WHITE + "");
		line = line.replaceAll("&e", ChatColor.YELLOW + "");		
//
		ann = ann.replaceAll("&0", ChatColor.BLACK + "");
		ann = ann.replaceAll("&l", ChatColor.BOLD + "");
		ann = ann.replaceAll("&3", ChatColor.DARK_AQUA + "");
		ann = ann.replaceAll("&1", ChatColor.DARK_BLUE + "");
		ann = ann.replaceAll("&8", ChatColor.DARK_GRAY + "");
		ann = ann.replaceAll("&2", ChatColor.DARK_GREEN + "");
		ann = ann.replaceAll("&4", ChatColor.DARK_RED + "");
		ann = ann.replaceAll("&6", ChatColor.GOLD + "");
		ann = ann.replaceAll("&7", ChatColor.GRAY + "");
		ann = ann.replaceAll("&a", ChatColor.GREEN + "");
		ann = ann.replaceAll("&o", ChatColor.ITALIC + "");
		ann = ann.replaceAll("&k", ChatColor.MAGIC + "");
		ann = ann.replaceAll("&c", ChatColor.RED + "");
		ann = ann.replaceAll("&r", ChatColor.RESET + "");
		ann = ann.replaceAll("&m", ChatColor.STRIKETHROUGH + "");
		ann = ann.replaceAll("&n", ChatColor.UNDERLINE + "");
		ann = ann.replaceAll("&f", ChatColor.WHITE + "");
		ann = ann.replaceAll("&e", ChatColor.YELLOW + "");	


		Bukkit.getServer().broadcastMessage(ann + ChatColor.WHITE + line);
		LineNumberReader lnr = new LineNumberReader(new FileReader(new File(filename)));
		lnr.skip(Long.MAX_VALUE);
		int lastLine = lnr.getLineNumber();
		if(currentLine + 1 == lastLine + 1){
			currentLine = 0;
		} else {
			currentLine++;
		}
	}
	}
	public static int numberOfOnlinePlayers() {
		final Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		return onlinePlayers.length;
	}
    
    
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equalsIgnoreCase("ab")){ 
    		if(sender.isOp() || sender.hasPermission("ab.reload")){
    			// If the player typed /basic then do the following...
        		this.reloadConfig();
        		sender.sendMessage(ChatColor.AQUA + "[AutoBroadcaster]: " + ChatColor.RED + "Auto Broadcaster Configuration files reloaded successfully!");
        		return true;
        	
    		}else{
    			sender.sendMessage(ChatColor.AQUA + "[AutoBroadcaster]: " + ChatColor.RED + "You do not have sufficient permissions! (OP or ab.reload)");
    		}
    		
    	}
		return false;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
}
