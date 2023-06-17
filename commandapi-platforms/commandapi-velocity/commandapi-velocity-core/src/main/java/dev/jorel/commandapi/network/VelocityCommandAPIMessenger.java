package dev.jorel.commandapi.network;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.ChannelMessageSink;
import com.velocitypowered.api.proxy.messages.ChannelMessageSource;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

// Based on https://gist.github.com/Xernium/95c9262c5f70b8791557861bbc09be1b
/**
 * A {@link CommandAPIMessenger} for sending and receiving messages on Velocity.
 * <p>
 * Velocity may receive messages from a Client ({@link Player}) or a Server ({@link ServerConnection}). The common
 * interface is {@link ChannelMessageSource} (coming into Velocity) and {@link ChannelMessageSink} (going out of
 * Velocity). Packets may be handled differently depending on their direction, so check which of these two sent
 * the packet before continuing.
 */
public class VelocityCommandAPIMessenger extends CommandAPIMessenger<ChannelMessageSource, ChannelMessageSink> {
	private final ChannelIdentifier channel;
	private final Object plugin;
	private final ProxyServer proxy;

	/**
	 * Creates a new {@link VelocityCommandAPIMessenger}.
	 *
	 * @param channelName The name of the channel to communicate on.
	 * @param plugin      The plugin object (annotated by {@link Plugin}) sending and receiving messages.
	 * @param proxy       The {@link ProxyServer} the plugin is running on.
	 */
	public VelocityCommandAPIMessenger(String channelName, Object plugin, ProxyServer proxy) {
		super(new VelocityPacketHandler());
		this.channel = MinecraftChannelIdentifier.from(channelName);
		this.plugin = plugin;
		this.proxy = proxy;

		// Register the channel so the PluginMessageEvent is fired
		this.proxy.getChannelRegistrar().register(this.channel);
		// Register the event handler to catch the PluginMessageEvent
		this.proxy.getEventManager().register(this.plugin, this);
	}

	@Override
	public void close() {
		// Unregister the channel and this listener
		this.proxy.getChannelRegistrar().unregister(this.channel);
		this.proxy.getEventManager().unregisterListener(this.plugin, this);
	}

	@Subscribe
	public void onPluginMessageEvent(PluginMessageEvent event) {
		// A plugin message was sent to Velocity, check if it is for us
		if (!event.getIdentifier().equals(this.channel)) return;

		// We will handle this message, set the result so that it is not forwarded
		event.setResult(PluginMessageEvent.ForwardResult.handled());

		// Handle the message
		messageReceived(event.getSource(), event.getData());
	}

	@Override
	public void sendRawBytes(ChannelMessageSink target, byte[] bytes) {
		target.sendPluginMessage(this.channel, bytes);
	}
}
