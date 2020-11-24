package dev.jorel.commandapi.nms;

import dev.jorel.commandapi.CommandAPIHandler;
import net.minecraft.server.v1_16_R3.CommandListenerWrapper;

class NewCLW extends CommandListenerWrapper {

	int flag = 0;

	public NewCLW(CommandListenerWrapper clww) throws IllegalArgumentException, IllegalAccessException {
		super(clww.base, clww.getPosition(), clww.i(), clww.getWorld(),
				(int) CommandAPIHandler.getInstance().getField(CommandListenerWrapper.class, "f").get(clww),
				clww.getName(), clww.getScoreboardDisplayName(), clww.getServer(), clww.getEntity(),
				(boolean) CommandAPIHandler.getInstance().getField(CommandListenerWrapper.class, "j").get(clww), null,
				clww.k());

		flag++;
	}
}
//ResultConsumer<CommandListenerWrapper> lResultConsumer<CommandListenerWrapper> l