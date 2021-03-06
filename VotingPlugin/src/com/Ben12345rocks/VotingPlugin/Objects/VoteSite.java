package com.Ben12345rocks.VotingPlugin.Objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import com.Ben12345rocks.AdvancedCore.Utils;
import com.Ben12345rocks.AdvancedCore.Configs.ConfigRewards;
import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Config.Config;
import com.Ben12345rocks.VotingPlugin.Config.ConfigFormat;
import com.Ben12345rocks.VotingPlugin.Config.ConfigVoteSites;

// TODO: Auto-generated Javadoc
/**
 * The Class VoteSite.
 */
public class VoteSite {

	/** The config. */
	static Config config = Config.getInstance();

	/** The config vote sites. */
	static ConfigVoteSites configVoteSites = ConfigVoteSites.getInstance();

	/** The format. */
	static ConfigFormat format = ConfigFormat.getInstance();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/** The vote URL. */
	private String voteURL;

	/** The service site. */
	private String serviceSite;

	/** The site name. */
	private String siteName;

	/** The vote delay. */
	private int voteDelay;

	/** The enabled. */
	private boolean enabled;

	/** The rewards. */
	private ArrayList<String> rewards;

	/** The cumulative votes. */
	private int cumulativeVotes;

	/** The cumulative rewards. */
	private ArrayList<String> cumulativeRewards;

	/** The priority. */
	private int priority;

	/**
	 * Instantiates a new vote site.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public VoteSite(Main plugin) {
		VoteSite.plugin = plugin;
	}

	/**
	 * Instantiates a new vote site.
	 *
	 * @param siteName
	 *            the site name
	 */
	public VoteSite(String siteName) {
		setSiteName(siteName);
		if (!configVoteSites.getVoteSiteFile(siteName).exists()) {
			if (Config.getInstance().getAutoCreateVoteSites()) {
				configVoteSites.generateVoteSite(siteName);
			}
			init();
			plugin.loadVoteSites();
		} else {
			init();
		}
	}

	/**
	 * Broadcast vote.
	 *
	 * @param user
	 *            the user
	 */
	public void broadcastVote(User user) {
		String playerName = user.getPlayerName();
		String bc = Utils.getInstance().colorize(format.getBroadCastMsg());
		bc = bc.replace("%player%", playerName).replace("%SiteName%", siteName);
		final String str = bc;
		Bukkit.getScheduler().runTask(plugin, new Runnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage(str);
			}
		});

	}

	/**
	 * Gets the cumulative rewards.
	 *
	 * @return the cumulative rewards
	 */
	public ArrayList<String> getCumulativeRewards() {
		return cumulativeRewards;
	}

	/**
	 * Gets the cumulative votes.
	 *
	 * @return the cumulative votes
	 */
	public int getCumulativeVotes() {
		return cumulativeVotes;
	}

	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the rewards.
	 *
	 * @return the rewards
	 */
	public ArrayList<String> getRewards() {
		return rewards;
	}

	/**
	 * Gets the service site.
	 *
	 * @return the service site
	 */
	public String getServiceSite() {
		return serviceSite;
	}

	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * Gets the vote delay.
	 *
	 * @return the vote delay
	 */
	public int getVoteDelay() {
		return voteDelay;
	}

	/**
	 * Gets the vote URL.
	 *
	 * @return the vote URL
	 */
	public String getVoteURL() {
		return voteURL;
	}

	/**
	 * Give culumative rewards.
	 *
	 * @param user
	 *            the user
	 * @param online
	 *            the online
	 */
	public void giveCulumativeRewards(User user, boolean online) {
		for (String reward : getCumulativeRewards()) {
			ConfigRewards.getInstance().getReward(reward)
					.giveReward(user, online);
		}
	}

	/**
	 * Give rewards.
	 *
	 * @param user
	 *            the user
	 * @param online
	 *            the online
	 */
	public void giveRewards(User user, boolean online) {
		for (String reward : getRewards()) {
			if (reward != "") {
				ConfigRewards.getInstance().getReward(reward)
						.giveReward(user, online);
			}
		}
	}

	/**
	 * Give site reward.
	 *
	 * @param user
	 *            the user
	 * @param online
	 *            the online
	 */
	public void giveSiteReward(User user, boolean online) {
		giveRewards(user, online);

		try {

			user.addCumulativeReward(this);

			if (configVoteSites.getCumulativeRewardVotesAmount(siteName) != 0) {
				user.addCumulativeReward(this);
				if ((user.getCumulativeReward(this) >= configVoteSites
						.getCumulativeRewardVotesAmount(siteName))) {

					giveCulumativeRewards(user, online);

					user.setCumulativeReward(this, 0);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Inits the.
	 */
	public void init() {
		setVoteURL(configVoteSites.getVoteURL(siteName));
		setServiceSite(configVoteSites.getServiceSite(siteName));
		setVoteDelay(configVoteSites.getVoteDelay(siteName));
		setEnabled(configVoteSites.getVoteSiteEnabled(siteName));
		setRewards(configVoteSites.getRewards(siteName));
		setCumulativeVotes(configVoteSites
				.getCumulativeRewardVotesAmount(siteName));
		setCumulativeRewards(configVoteSites.getCumulativeRewards(siteName));
		setPriority(configVoteSites.getPriority(siteName));
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets the cumulative rewards.
	 *
	 * @param cumulativeRewards
	 *            the new cumulative rewards
	 */
	public void setCumulativeRewards(ArrayList<String> cumulativeRewards) {
		this.cumulativeRewards = cumulativeRewards;
	}

	/**
	 * Sets the cumulative votes.
	 *
	 * @param cumulativeVotes
	 *            the new cumulative votes
	 */
	public void setCumulativeVotes(int cumulativeVotes) {
		this.cumulativeVotes = cumulativeVotes;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Sets the priority.
	 *
	 * @param priority
	 *            the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Sets the rewards.
	 *
	 * @param rewards
	 *            the new rewards
	 */
	public void setRewards(ArrayList<String> rewards) {
		this.rewards = rewards;
	}

	/**
	 * Sets the service site.
	 *
	 * @param serviceSite
	 *            the new service site
	 */
	public void setServiceSite(String serviceSite) {
		this.serviceSite = serviceSite;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName
	 *            the new site name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Sets the vote delay.
	 *
	 * @param voteDelay
	 *            the new vote delay
	 */
	public void setVoteDelay(int voteDelay) {
		this.voteDelay = voteDelay;
	}

	/**
	 * Sets the vote URL.
	 *
	 * @param voteURL
	 *            the new vote URL
	 */
	public void setVoteURL(String voteURL) {
		this.voteURL = voteURL;
	}

}
