/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.io.File;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatPreviewEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.utils.MinecraftVersion;
import dev.jorel.commandapi.arguments.AdventureChatArgument;
import dev.jorel.commandapi.arguments.ChatArgument;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommandAPIMain extends JavaPlugin {

	@Override
	public void onLoad() {
		// Configure the NBT API - we're not allowing tracking at all, according
		// to the CommandAPI's design principles. The CommandAPI isn't used very
		// much, so this tiny proportion of servers makes very little impact to
		// the NBT API's stats.
		MinecraftVersion.disableBStats();
		MinecraftVersion.disableUpdateCheck();

		// Config loading
		saveDefaultConfig();
		CommandAPI.config = new InternalConfig(getConfig(), NBTContainer.class, NBTContainer::new, new File(getDataFolder(), "command_registration.json"));
		CommandAPI.logger = getLogger();

		// Check dependencies for CommandAPI
		CommandAPIHandler.getInstance().checkDependencies();

		// Convert all plugins to be converted
		for (Entry<JavaPlugin, String[]> pluginToConvert : CommandAPI.config.getPluginsToConvert()) {
			if (pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for (String command : pluginToConvert.getValue()) {
					new AdvancedConverter(pluginToConvert.getKey(), command).convert();
				}
			}
		}

		// Convert all arbitrary commands
		for (String commandName : CommandAPI.config.getCommandsToConvert()) {
			new AdvancedConverter(commandName).convertCommand();
		}
	}

	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);

		new CommandAPICommand("test")
			.withArguments(new AdventureChatArgument("testarg")
				.withPreview(previewInfo -> MiniMessage.miniMessage().deserialize(previewInfo.input())))
			.executes((commandSender, objects) -> {
				commandSender.sendMessage("test");
			})
			.register();

		new CommandAPICommand("test1")
			.withArguments(new AdventureChatArgument("testarg")
				.withPreview(previewInfo -> MiniMessage.miniMessage().deserialize(previewInfo.input())))
			.executes((commandSender, objects) -> {
				System.out.println("test1");
			})
			.register();

		new CommandAPICommand("test2")
			.withArguments(new AdventureChatArgument("testarg"))
			.executes((commandSender, objects) -> {
				commandSender.sendMessage("test2");
			})
			.register();

		new CommandAPICommand("testa")
			.withArguments(new AdventureChatArgument("testarg")
				.usePreview(true).withPreview(previewInfo -> MiniMessage.miniMessage().deserialize(previewInfo.input())))
			.executes((commandSender, objects) -> {
				commandSender.sendMessage("test");
			})
			.register();

		new CommandAPICommand("test1a")
			.withArguments(new AdventureChatArgument("testarg")
				.usePreview(true).withPreview(previewInfo -> MiniMessage.miniMessage().deserialize(previewInfo.input())))
			.executes((commandSender, objects) -> {
				System.out.println("test1");
			})
			.register();

		new CommandAPICommand("broadcast")
			.withArguments(new ChatArgument("message").usePreview(true).withPreview(info -> {
				// Convert parsed BaseComponent[] to plain text
				String plainText = BaseComponent.toPlainText((BaseComponent[]) info.parsedInput());

				// Translate the & in plain text and generate a new BaseComponent[]
				return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText));
			}))
			.executesPlayer((player, args) -> {
				Bukkit.spigot().broadcast((BaseComponent[]) args[0]);
			})
			.register();
	}
}
