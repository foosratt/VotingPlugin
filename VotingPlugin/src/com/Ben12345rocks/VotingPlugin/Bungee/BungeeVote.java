package com.Ben12345rocks.VotingPlugin.Bungee;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.xml.bind.DatatypeConverter;

import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Config.ConfigBungeeVoting;
import com.vexsoftware.votifier.crypto.RSA;
import com.vexsoftware.votifier.model.Vote;

// TODO: Auto-generated Javadoc
/**
 * The Class BungeeVote.
 */
public class BungeeVote {

	/** The instance. */
	static BungeeVote instance = new BungeeVote();

	/** The plugin. */
	static Main plugin = Main.plugin;

	/** The sock. */
	static ServerSocket sock;

	/**
	 * Gets the single instance of BungeeVote.
	 *
	 * @return single instance of BungeeVote
	 */
	public static BungeeVote getInstance() {
		return instance;
	}

	/**
	 * Instantiates a new bungee vote.
	 */
	private BungeeVote() {
	}

	/**
	 * Instantiates a new bungee vote.
	 *
	 * @param plugin
	 *            the plugin
	 */
	public BungeeVote(Main plugin) {
		BungeeVote.plugin = plugin;
	}

	/**
	 * Check offline bungee votes.
	 */
	public void checkOfflineBungeeVotes() {
		for (String server : plugin.offlineBungee.keySet()) {
			ArrayList<Vote> votes = plugin.offlineBungee.get(server);
			if (votes != null) {
				for (Vote vote : votes) {
					try {
						sendBungeeVoteServer(server, vote);
					} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
						e.printStackTrace();
					}
				}
			}
			plugin.offlineBungee.put(server, new ArrayList<Vote>());
		}
	}

	/**
	 * Send bungee vote server.
	 *
	 * @param server
	 *            the server
	 * @param vote
	 *            the vote
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws InvalidKeySpecException
	 *             the invalid key spec exception
	 */
	public void sendBungeeVoteServer(String server, Vote vote)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] encodedPublicKey = DatatypeConverter
				.parseBase64Binary(ConfigBungeeVoting.getInstance()
						.getServerKey(server));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
				encodedPublicKey);
		PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		String serverIP = ConfigBungeeVoting.getInstance().getServerIP(server);
		int serverPort = ConfigBungeeVoting.getInstance().getServerPort(server);
		String serviceSite = ConfigBungeeVoting.getInstance()
				.getServerServiceSite(serverIP);
		if (serverIP.length() != 0) {
			try {
				if (serviceSite != null) {
					if (serviceSite.length() > 0) {
						vote.setServiceName(serviceSite);
					}
				}
				String VoteString = "VOTE\n" + vote.getServiceName() + "\n"
						+ vote.getUsername() + "\n" + vote.getAddress() + "\n"
						+ vote.getTimeStamp() + "\n";

				SocketAddress sockAddr = new InetSocketAddress(serverIP,
						serverPort);
				Socket socket = new Socket();
				socket.connect(sockAddr, 1000);
				OutputStream socketOutputStream = socket.getOutputStream();
				socketOutputStream.write(RSA.encrypt(VoteString.getBytes(),
						publicKey));
				socketOutputStream.close();
				socket.close();

				plugin.debug("Sending vote to " + server + "("
						+ sockAddr.toString() + "): " + vote.toString());

			} catch (Exception e) {
				plugin.getLogger().info(
						"Failed to send vote to " + server + "(" + serverIP
								+ ":" + serverPort + "): " + vote.toString()
								+ ", ignore this if server is offline");
				if (com.Ben12345rocks.AdvancedCore.Configs.Config.getInstance()
						.getDebugEnabled()) {
					e.printStackTrace();
				}
				ArrayList<Vote> votes = plugin.offlineBungee.get(server);
				if (votes == null) {
					votes = new ArrayList<Vote>();
				}
				if (!votes.contains(vote)) {
					votes.add(vote);
				}
				plugin.offlineBungee.put(server, votes);
			}
		}
	}

	/**
	 * Send vote.
	 *
	 * @param vote
	 *            the vote
	 */
	public void sendVote(Vote vote) {
		if (ConfigBungeeVoting.getInstance().getEnabled()) {
			for (String server : ConfigBungeeVoting.getInstance().getServers()) {

				try {
					sendBungeeVoteServer(server, vote);
				} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
					e.printStackTrace();
				}
			}
		}
	}
}