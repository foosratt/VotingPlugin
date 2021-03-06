package com.Ben12345rocks.VotingPlugin.Commands.Executers;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.VotingPlugin.Main;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandAliases.
 */
public class CommandAliases implements CommandExecutor {

	/** The plugin. */
	private Main plugin = Main.plugin;

	/** The cmd handle. */
	private CommandHandler cmdHandle;

	/**
	 * Instantiates a new command aliases.
	 *
	 * @param cmdHandle
	 *            the cmd handle
	 */
	public CommandAliases(CommandHandler cmdHandle) {
		this.cmdHandle = cmdHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		ArrayList<String> argsNew = new ArrayList<String>();
		argsNew.add(cmdHandle.getArgs()[0]);
		for (String arg : args) {
			argsNew.add(arg);
		}
		plugin.debug("Attempting cmd...");
		plugin.debug("Inputed args: "
				+ Utils.getInstance().makeStringList(argsNew));

		ArrayList<CommandHandler> cmdHandlers = new ArrayList<CommandHandler>();
		cmdHandlers.addAll(plugin.voteCommand);
		cmdHandlers.addAll(plugin.adminVoteCommand);
		for (CommandHandler cmdHandle : cmdHandlers) {
			if (cmdHandle.getArgs().length > args.length) {
				for (String arg : cmdHandle.getArgs()[0].split("&")) {
					if (cmd.getName().equalsIgnoreCase("vote" + arg)
							|| cmd.getName()
									.equalsIgnoreCase("adminvote" + arg)) {
						argsNew.set(0, arg);

						boolean argsMatch = true;
						for (int i = 0; i < argsNew.size(); i++) {
							if (i < cmdHandle.getArgs().length) {
								if (!cmdHandle.argsMatch(argsNew.get(i), i)) {
									argsMatch = false;
								}
							}

						}

						if (argsMatch) {
							if (cmdHandle.runCommand(sender, Utils
									.getInstance().convertArray(argsNew))) {
								plugin.debug("cmd found, ran cmd");
								return true;
							}
						}
					}
				}
			}
		}

		/*
		 * for (String arg : cmdHandle.getArgs()[0].split("&")) { argsNew.set(0,
		 * arg); if (cmdHandle.runCommand(sender,
		 * Utils.getInstance().convertArray(argsNew))) {
		 * plugin.debug("cmd found, ran cmd"); return true; } }
		 */

		// invalid command
		sender.sendMessage(ChatColor.RED
				+ "No valid arguments, see /vote help!");
		return true;
	}
}
