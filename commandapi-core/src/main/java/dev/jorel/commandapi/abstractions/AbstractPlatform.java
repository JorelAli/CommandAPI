package dev.jorel.commandapi.abstractions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.jorel.commandapi.arguments.SuggestionProviders;

// TODO: Not entirely sure if this needs to be parameterized here or not
public abstract class AbstractPlatform<CommandSource> {
	// TODO: Add methods that need platform-specific implementations
	// All methods in bukkit NMS will probably also need to be here
	
	// ^ I don't think all bukkit NMS methods will have to be in here.
	// Almost all Bukkit NMS methods should be implemented in Bukkit's
	// AbstractPlatform implementation. The only things in here are going
	// to be supppppppppper low-level stuff

	public abstract AbstractCommandSender getSenderForCommand(CommandContext<CommandSource> cmdCtx, boolean forceNative);

	// Converts a command source into its source. For Bukkit, this 
	// is implemented in NMS. TODO: For Velocity, I have no idea what
	// a command source is or consists of
	public abstract AbstractCommandSender getCommandSenderFromCommandSource(CommandSource css);

	// Registers a permission. Bukkit's permission system requires permissions to be "registered"
	// before they can be used.
	public abstract void registerPermission(String string);

	// Some commands have existing suggestion providers. TODO: We can PROBABLY avoid this by
	// implementing the relevant NMS SuggestionProviders implementation on the platform-specific
	// argument, but I CBA to think about that now so I'll dump it here
	public abstract SuggestionProvider<CommandSource> getSuggestionProvider(SuggestionProviders suggestionProvider);
}
