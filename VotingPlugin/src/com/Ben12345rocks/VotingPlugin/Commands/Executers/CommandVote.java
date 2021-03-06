package com.Ben12345rocks.VotingPlugin.Commands.Executers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Objects.CommandHandler;
import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Commands.Commands;
import com.Ben12345rocks.VotingPlugin.Config.ConfigFormat;
import com.Ben12345rocks.VotingPlugin.Objects.User;
import com.Ben12345rocks.VotingPlugin.TopVoter.TopVoter;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandVote.
 */
public class CommandVote implements CommandExecutor {

	/** The instance. */
	private static CommandVote instance = new CommandVote();

	/** The plugin. */
	private static Main plugin;

	/**
	 * Gets the single instance of CommandVote.
	 *
	 * @return single instance of CommandVote
	 */
	public static CommandVote getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new command vote.
	 */
	private CommandVote() {
	}

	/**
	 * Instantiates a new command vote.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public CommandVote(Main plugin) {
		CommandVote.plugin = plugin;
	}

	/**
	 * Help.
	 *
	 * @param sender
	 *            the sender
	 */
	public void help(CommandSender sender) {
		if (sender instanceof Player) {
			User user = new User((Player) sender);
			user.sendJson(Commands.getInstance().voteHelpText(sender));
		} else {
			sender.sendMessage(Utils.getInstance().convertArray(
					Utils.getInstance().comptoString(
							Commands.getInstance().voteHelpText(sender))));
		}

	}

	/**
	 * Info other.
	 *
	 * @param sender
	 *            the sender
	 * @param playerName
	 *            the player name
	 */
	public void infoOther(CommandSender sender, String playerName) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if (sender instanceof Player) {
					User user = new User((Player) sender);
					user.sendMessage(Utils.getInstance().colorize(
							"&cGetting player info..."));
					user.sendMessage(Commands.getInstance().playerInfo(
							new User(playerName)));
				} else {
					sender.sendMessage(Utils.getInstance().colorize(
							Commands.getInstance().playerInfo(
									new User(playerName))));
				}
			}
		});

	}

	/**
	 * Info self.
	 *
	 * @param sender
	 *            the sender
	 */
	public void infoSelf(CommandSender sender) {
		if (sender instanceof Player) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

				@Override
				public void run() {
					User user = new User((Player) sender);
					user.sendMessage(Utils.getInstance().colorize(
							"&cGetting info..."));
					user.sendMessage(Commands.getInstance().playerInfo(
							new User(sender.getName())));
				}
			});

		} else {
			sender.sendMessage("You must be a player to do this!");
		}
	}

	/**
	 * Last other.
	 *
	 * @param sender
	 *            the sender
	 * @param playerName
	 *            the player name
	 */
	public void lastOther(CommandSender sender, String playerName) {

		if (sender instanceof Player) {
			User user = new User((Player) sender);
			user.sendMessage(Commands.getInstance().voteCommandLast(
					new User(playerName)));
		} else {
			sender.sendMessage(Utils.getInstance().colorize(
					Commands.getInstance()
							.voteCommandLast(new User(playerName))));
		}

	}

	/**
	 * Last self.
	 *
	 * @param sender
	 *            the sender
	 */
	public void lastSelf(CommandSender sender) {
		if (sender instanceof Player) {
			User user = new User((Player) sender);
			user.sendMessage(Commands.getInstance().voteCommandLast(user));
		} else {
			sender.sendMessage("You must be a player to do this!");
		}
	}

	/**
	 * Next other.
	 *
	 * @param sender
	 *            the sender
	 * @param playerName
	 *            the player name
	 */
	public void nextOther(CommandSender sender, String playerName) {
		if (sender instanceof Player) {
			User user = new User((Player) sender);
			user.sendMessage(Commands.getInstance().voteCommandNext(
					new User(playerName)));

		} else {
			sender.sendMessage(Utils.getInstance().colorize(
					Commands.getInstance()
							.voteCommandNext(new User(playerName))));
		}

	}

	/**
	 * Next self.
	 *
	 * @param sender
	 *            the sender
	 */
	public void nextSelf(CommandSender sender) {
		if (sender instanceof Player) {
			String playerName = sender.getName();
			User user = new User(playerName);
			user.sendMessage(Commands.getInstance().voteCommandNext(user));
		} else {
			sender.sendMessage("You must be a player to do this!");
		}
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

		for (CommandHandler commandHandler : plugin.voteCommand) {
			if (commandHandler.runCommand(sender, args)) {
				return true;
			}
		}

		// invalid command
		sender.sendMessage(ChatColor.RED
				+ "No valid arguments, see /vote help!");
		return true;
	}

	/**
	 * Points other.
	 *
	 * @param sender
	 *            the sender
	 * @param user
	 *            the user
	 */
	public void pointsOther(CommandSender sender, User user) {
		String msg = ConfigFormat.getInstance().getCommandVotePoints()
				.replace("%Player%", user.getPlayerName())
				.replace("%Points%", "" + user.getPoints());
		if (sender instanceof Player) {
			new User(sender.getName()).sendMessage(msg);
		} else {
			sender.sendMessage(Utils.getInstance().colorize(msg));
		}

	}

	/**
	 * Points self.
	 *
	 * @param user
	 *            the user
	 */
	public void pointsSelf(User user) {
		String msg = ConfigFormat.getInstance().getCommandVotePoints()
				.replace("%Player%", user.getPlayerName())
				.replace("%Points%", "" + user.getPoints());
		user.sendMessage(msg);
	}

	/**
	 * Today.
	 *
	 * @param sender
	 *            the sender
	 * @param page
	 *            the page
	 */
	public void today(CommandSender sender, int page) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			@Override
			public void run() {
				if (sender instanceof Player) {
					User user = new User((Player) sender);
					user.sendMessage(Commands.getInstance().commandVoteToday(
							page));
					Bukkit.getScheduler().runTask(plugin, new Runnable() {

						@Override
						public void run() {
							Commands.getInstance().sendVoteTodayScoreBoard(
									(Player) sender, page);
						}
					});
				} else {
					sender.sendMessage(Commands.getInstance().commandVoteToday(
							page));
				}

			}
		});

	}

	/**
	 * Top voter daily.
	 *
	 * @param sender
	 *            the sender
	 * @param page
	 *            the page
	 */
	public void topVoterDaily(CommandSender sender, int page) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if (sender instanceof Player) {
					User user = new User((Player) sender);
					user.sendMessage(TopVoter.getInstance().topVoterDaily(page));
					Bukkit.getScheduler().runTask(plugin, new Runnable() {

						@Override
						public void run() {
							Commands.getInstance().sendTopVoterDailyScoreBoard(
									(Player) sender, page);
						}
					});
				} else {
					sender.sendMessage(TopVoter.getInstance().topVoterDaily(
							page));
				}

			}
		});

	}

	/**
	 * Top voter monthly.
	 *
	 * @param sender
	 *            the sender
	 * @param page
	 *            the page
	 */
	public void topVoterMonthly(CommandSender sender, int page) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if (sender instanceof Player) {
					User user = new User((Player) sender);
					user.sendMessage(TopVoter.getInstance().topVoterMonthly(
							page));
					Bukkit.getScheduler().runTask(plugin, new Runnable() {

						@Override
						public void run() {
							Commands.getInstance()
									.sendTopVoterMonthlyScoreBoard(
											(Player) sender, page);
						}
					});
				} else {
					sender.sendMessage(TopVoter.getInstance().topVoterMonthly(
							page));
				}

			}
		});

	}

	/**
	 * Top voter weekly.
	 *
	 * @param sender
	 *            the sender
	 * @param page
	 *            the page
	 */
	public void topVoterWeekly(CommandSender sender, int page) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if (sender instanceof Player) {
					User user = new User((Player) sender);
					user.sendMessage(TopVoter.getInstance()
							.topVoterWeekly(page));
					Bukkit.getScheduler().runTask(plugin, new Runnable() {

						@Override
						public void run() {
							Commands.getInstance()
									.sendTopVoterWeeklyScoreBoard(
											(Player) sender, page);
						}
					});
				} else {
					sender.sendMessage(TopVoter.getInstance().topVoterWeekly(
							page));
				}

			}
		});

	}

	/**
	 * Total all.
	 *
	 * @param sender
	 *            the sender
	 */
	public void totalAll(CommandSender sender) {

		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if (sender instanceof Player) {
					User user = new User((Player) sender);
					user.sendMessage(Commands.getInstance()
							.voteCommandTotalAll());

				} else {
					sender.sendMessage(Commands.getInstance()
							.voteCommandTotalAll());
				}

			}
		});

	}

	/**
	 * Total other.
	 *
	 * @param sender
	 *            the sender
	 * @param playerName
	 *            the player name
	 */
	public void totalOther(CommandSender sender, String playerName) {
		if (sender instanceof Player) {
			User user = new User((Player) sender);
			user.sendMessage(Commands.getInstance().voteCommandTotal(
					new User(playerName)));
		} else {
			sender.sendMessage(Utils.getInstance().colorize(
					Commands.getInstance().voteCommandTotal(
							new User(playerName))));
		}

	}

	/**
	 * Total self.
	 *
	 * @param sender
	 *            the sender
	 */
	public void totalSelf(CommandSender sender) {
		if (sender instanceof Player) {
			String playerName = sender.getName();
			User user = new User(playerName);
			user.sendMessage(Commands.getInstance().voteCommandTotal(user));
		} else {
			sender.sendMessage("You must be a player to do this!");
		}
	}

	/**
	 * Vote GUI.
	 *
	 * @param sender
	 *            the sender
	 */
	public void voteGUI(CommandSender sender) {
		if (sender instanceof Player) {
			Commands.getInstance().openVoteGUI((Player) sender);
		} else {
			sender.sendMessage("Must be a player to do this!");
		}
	}

	/**
	 * Vote reward.
	 *
	 * @param sender
	 *            the sender
	 * @param siteName
	 *            the site name
	 */
	public void voteReward(CommandSender sender, String siteName) {
		if (sender instanceof Player) {
			Commands.getInstance().voteReward((Player) sender, siteName);
		} else {
			sender.sendMessage("Must be a player to do this!");
		}
	}

	/**
	 * Vote URL.
	 *
	 * @param sender
	 *            the sender
	 */
	public void voteURL(CommandSender sender) {
		if (sender instanceof Player) {
			Commands.getInstance().voteURL((Player) sender);
		} else {
			sender.sendMessage("Must be a player to do this!");
		}
	}

	/**
	 * Vote UR ls.
	 *
	 * @param sender
	 *            the sender
	 */
	public void voteURLs(CommandSender sender) {
		if (sender instanceof Player) {
			User user = new User((Player) sender);
			user.sendMessage(Commands.getInstance().voteURLs());
		} else {
			sender.sendMessage(Commands.getInstance().voteURLs());
		}
	}

}
