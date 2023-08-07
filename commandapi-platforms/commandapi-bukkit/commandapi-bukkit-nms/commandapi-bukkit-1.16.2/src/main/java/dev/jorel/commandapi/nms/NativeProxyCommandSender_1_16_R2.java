package dev.jorel.commandapi.nms;

import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.minecraft.server.v1_16_R2.CommandListenerWrapper;
import net.minecraft.server.v1_16_R2.Vec2F;
import net.minecraft.server.v1_16_R2.Vec3D;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R2.command.ProxiedNativeCommandSender;

public class NativeProxyCommandSender_1_16_R2 extends ProxiedNativeCommandSender implements NativeProxyCommandSender {
	private final World world;
	private final Location location;

	public NativeProxyCommandSender_1_16_R2(CommandListenerWrapper clw, CommandSender caller, CommandSender callee) {
		super(clw, caller, callee);

		Vec3D pos = clw.getPosition();
		Vec2F rot = clw.i();
		this.world = CommandAPIBukkit.get().getWorldForCSS(clw);
		this.location = new Location(this.world, pos.getX(), pos.getY(), pos.getZ(), rot.j, rot.i);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public World getWorld() {
		return world;
	}
}
