package me.skipperguy12.autobroadcasterplus.commands;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.NestedCommand;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

/**
 * Parent command for AutoBroadcast commands
 */
public class AutoBroadcasterParentCommand {
    /**
     * Parent command for all nested AutoBroadcaster commands
     * @param args
     * @param sender Player who sends the command
     * @throws CommandException thrown if a nested command throws a CommandException
     */
    @Command(aliases = { "autobroadcaster", "ab" }, desc = "Commands for AutoBroadcasterPlus", min = 0, max = -1)
    @NestedCommand(AutoBroadcasterCommands.class) //All commands will get passed on to QuickFillCommands.class
    public static void quickfill(final CommandContext args, CommandSender sender) throws CommandException {
    }
}

