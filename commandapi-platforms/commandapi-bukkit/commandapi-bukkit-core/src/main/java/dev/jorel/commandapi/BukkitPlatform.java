package dev.jorel.commandapi;

import java.util.List;
import java.util.function.Function;

import org.bukkit.NamespacedKey;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.abstractions.AbstractPlatform;
import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.nms.NMS;

// BukkitPlatform is an AbstractPlatform, but also needs all of the methods from
// NMS, so it implements NMS. Our implementation of BukkitPlatform is now derived
// using the version handler (and thus, deferred to our NMS-specific implementations)
public abstract class BukkitPlatform<Source> extends AbstractPlatform<Source> implements NMS<Source> {
	
	// Blah blah access instance directly and all of that stuff
	private static BukkitPlatform<?> instance;
	
	public static BukkitPlatform<?> get() {
		return instance;
	}

	@Override
	public AbstractCommandSender<? extends Source> getSenderForCommand(CommandContext<Source> cmdCtx, boolean forceNative) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractCommandSender<? extends Source> getCommandSenderFromCommandSource(Source cs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerPermission(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SuggestionProvider<Source> getSuggestionProvider(SuggestionProviders suggestionProvider) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postCommandRegistration(LiteralCommandNode<Source> resultantNode, List<LiteralCommandNode<Source>> aliasNodes) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LiteralCommandNode<Source> registerCommandNode(LiteralArgumentBuilder<Source> node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerHelp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unregister(String commandName) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * TODO: There's probably a much better place to put this, but I don't
	 * really fancy subclassing SafeOverrideableArgument for Bukkit specifically,
	 * so I'll dump it here and hope nobody cares because the CommandAPI doesn't
	 * really have a centralized "utils" class or anything
	 * 
	 * Composes a <code>S</code> to a <code>NamespacedKey</code> mapping function to
	 * convert <code>S</code> to a <code>String</code>
	 * 
	 * @param mapper the mapping function from <code>S</code> to
	 *               <code>NamespacedKey</code>
	 * @return a composed function that converts <code>S</code> to
	 *         <code>String</code>
	 */
	public static <S> Function<S, String> fromKey(Function<S, NamespacedKey> mapper) {
		return mapper.andThen(NamespacedKey::toString);
	}

}
