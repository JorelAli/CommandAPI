import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.mockito.Mockito;

import com.google.common.io.Files;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.brigadier.ArgumentSerializerString;

public class CustomNMS extends BlankNMS {

	public CustomNMS() {
		try {
			initializeArgumentsInArgumentTypeInfos();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.19" };
	}

	CommandDispatcher<CommandListenerWrapper> dispatcher;

	@Override
	public CommandDispatcher<CommandListenerWrapper> getBrigadierDispatcher() {
		if (this.dispatcher == null) {
			this.dispatcher = new CommandDispatcher<>();
		}
		return this.dispatcher;
	}

	@Override
	public CommandListenerWrapper getCLWFromCommandSender(CommandSender sender) {
		CommandListenerWrapper clw = Mockito.mock(CommandListenerWrapper.class);
		Mockito.when(clw.getBukkitSender()).thenReturn(sender);
		return clw;
	}

	@Override
	public CommandSender getCommandSenderFromCSS(CommandListenerWrapper clw) {
		try {
			return clw.getBukkitSender();
		} catch (UnsupportedOperationException e) {
			return null;
		}
	}

	@Override
	public CommandSender getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean forceNative) {
		CommandListenerWrapper css = cmdCtx.getSource();
		return css.getBukkitSender();
	}

	@Override
	public void createDispatcherFile(File file, CommandDispatcher<CommandListenerWrapper> dispatcher)
			throws IOException {
		Files.asCharSink(file, StandardCharsets.UTF_8).write(new GsonBuilder().setPrettyPrinting().create()
				.toJson(DispatcherUtil.toJSON(dispatcher, dispatcher.getRoot())));
	}

	
	private void initializeArgumentsInArgumentTypeInfos() throws ReflectiveOperationException {
		Field f = ArgumentTypeInfos.class.getDeclaredField("a");
		f.setAccessible(true);
		@SuppressWarnings("unchecked")
		Map<Class<?>, ArgumentTypeInfo<?, ?>> map = (Map<Class<?>, ArgumentTypeInfo<?, ?>>) f.get(null);
		map.put(StringArgumentType.class, new ArgumentSerializerString());
	}

}
