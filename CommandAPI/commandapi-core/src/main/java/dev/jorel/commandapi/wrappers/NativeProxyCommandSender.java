package dev.jorel.commandapi.wrappers;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class NativeProxyCommandSender implements ProxiedCommandSender {

	private final CommandSender caller;
	private final CommandSender callee;
	private final Location location;
	private final World world;

	public NativeProxyCommandSender(CommandSender caller, CommandSender callee, Location location, World world) {
		this.caller = caller;
		this.callee = callee;
		this.location = location;
		this.world = world;
	}
	
	public World getWorld() {
		return this.world;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	@Override
	public CommandSender getCaller() {
		return this.caller;
	}

	@Override
	public CommandSender getCallee() {
		return this.callee;
	}

	@Override
	public void sendMessage(String message) {
		this.caller.sendMessage(message);
	}

	@Override
	public void sendMessage(String[] messages) {
		this.caller.sendMessage(messages);
	}

	@Override
	public Server getServer() {
		return this.callee.getServer();
	}

	@Override
	public String getName() {
		return this.callee.getName();
	}

	@Override
	public boolean isPermissionSet(String name) {
		return this.caller.isPermissionSet(name);
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
		return this.caller.isPermissionSet(perm);
	}

	@Override
	public boolean hasPermission(String name) {
		return this.caller.hasPermission(name);
	}

	@Override
	public boolean hasPermission(Permission perm) {
		return this.caller.hasPermission(perm);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return this.caller.addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return this.caller.addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return this.caller.addAttachment(plugin, name, value, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return this.caller.addAttachment(plugin, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		this.caller.removeAttachment(attachment);
	}

	@Override
	public void recalculatePermissions() {
		this.caller.recalculatePermissions();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return this.caller.getEffectivePermissions();
	}

	@Override
	public boolean isOp() {
		return this.caller.isOp();
	}

	@Override
	public void setOp(boolean value) {
		this.caller.setOp(value);
	}

	@Override
	public Spigot spigot() {
		return this.caller.spigot();
	}

}
