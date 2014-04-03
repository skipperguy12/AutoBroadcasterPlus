package me.skipperguy12.autobroadcasterplus.commands;


import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import me.skipperguy12.autobroadcasterplus.AutoBroadcasterPlus;
import me.skipperguy12.autobroadcasterplus.Messenger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

/**
 * Class to store the nested commands of the AutoBroadcaster command
 */
public class AutoBroadcasterCommands {
    /**
     * Reloads the plugin configuration
     *
     * @param args   parameters
     * @param sender CommandSender who sends the command
     * @throws CommandException
     */
    @Command(aliases = {"reload", "r"}, desc = "Reloads the configuration file", min = 0, max = 0)
    @CommandPermissions({"ab.reload"})
    public static void reload(final CommandContext args, CommandSender sender) throws CommandException {
        AutoBroadcasterPlus.getInstance().reloadConfig();
        AutoBroadcasterPlus.getInstance().messanger = new Messenger(AutoBroadcasterPlus.getInstance().getDataFolder());
        AutoBroadcasterPlus.getInstance().messanger.reloadSchedulers();

        sender.sendMessage(ChatColor.AQUA + "[AutoBroadcaster]: " + ChatColor.RED + "Auto Broadcaster Configuration files reloaded successfully!");
    }
}
