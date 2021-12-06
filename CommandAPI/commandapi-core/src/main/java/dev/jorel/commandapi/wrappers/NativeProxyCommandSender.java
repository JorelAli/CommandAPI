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
package dev.jorel.commandapi.wrappers;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;

/**
 * A simple representation of Minecraft's CommandListenerWrapper, in the form of
 * Bukkit's ProxiedCommandSender
 */
public class NativeProxyCommandSender implements ProxiedCommandSender {

	private final CommandSender caller;
	private final CommandSender callee;
	private final Location location;
	private final World world;

	/**
	 * Constructs a NativeProxyCommandSender, which is basically Minecraft's CommandListenerWrapper
	 * @param caller the command sender that actually sent the command
	 * @param callee the command sender that will be executing the command
	 * @param location the proxied location that the command will be run at 
	 * @param world the proxied world that the command will be run in
	 */
	public NativeProxyCommandSender(CommandSender caller, CommandSender callee, Location location, World world) {
		this.caller = caller;
		this.callee = callee == null ? caller : callee;
		this.location = location;
		this.world = world;
	}
	
	/**
     * Adds a new empty PermissionAttachment to this object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @return The PermissionAttachment that was just created
     */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
		return this.caller.addAttachment(plugin);
	}
	
	/**
     * Temporarily adds a new empty PermissionAttachment to this
     * object
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @param ticks Amount of ticks to automatically remove this attachment
     *     after
     * @return The PermissionAttachment that was just created
     */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
		return this.caller.addAttachment(plugin, ticks);
	}
	
	/**
     * Adds a new PermissionAttachment with a single permission by
     * name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @param name Name of the permission to attach
     * @param value Value of the permission
     * @return The PermissionAttachment that was just created
     */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
		return this.caller.addAttachment(plugin, name, value);
	}

	/**
     * Temporarily adds a new PermissionAttachment with a single
     * permission by name and value
     *
     * @param plugin Plugin responsible for this attachment, may not be null
     *     or disabled
     * @param name Name of the permission to attach
     * @param value Value of the permission
     * @param ticks Amount of ticks to automatically remove this attachment
     *     after
     * @return The PermissionAttachment that was just created
     */
	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
		return this.caller.addAttachment(plugin, name, value, ticks);
	}

	/**
     * Returns the CommandSender which is being used to call the command
     *
     * @return the caller which the command is being run as
     */
	@Override
	public CommandSender getCallee() {
		return this.callee;
	}

	/**
     * Returns the CommandSender which triggered this proxied command
     *
     * @return the caller which triggered the command
     */
	@Override
	public CommandSender getCaller() {
		return this.caller;
	}

	/**
     * Gets a set containing all of the permissions currently in effect by
     * this object
     *
     * @return Set of currently effective permissions
     */
	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
		return this.caller.getEffectivePermissions();
	}

	/**
	 * Returns the location that this native command sender represents
	 * @return the location that this native command sender represents
	 */
	public Location getLocation() {
		return this.location;
	}

	/**
     * Gets the name of this command sender
     *
     * @return Name of the sender
     */
	@Override
	public String getName() {
		return this.callee.getName();
	}

	/**
     * Returns the server instance that this command is running on
     *
     * @return Server instance
     */
	@Override
	public Server getServer() {
		return this.callee.getServer();
	}

	/**
	 * Returns the world that this native command sender represents
	 * @return the world that this native command sender represents
	 */
	public World getWorld() {
		return this.world;
	}

	/**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned
     *
     * @param perm Permission to get
     * @return Value of the permission
     */
	@Override
	public boolean hasPermission(Permission perm) {
		return this.caller.hasPermission(perm);
	}

	/**
     * Gets the value of the specified permission, if set.
     * <p>
     * If a permission override is not set on this object, the default value
     * of the permission will be returned.
     *
     * @param name Name of the permission
     * @return Value of the permission
     */
	@Override
	public boolean hasPermission(String name) {
		return this.caller.hasPermission(name);
	}

	/**
     * Checks if this object is a server operator
     *
     * @return true if this is an operator, otherwise false
     */
	@Override
	public boolean isOp() {
		return this.caller.isOp();
	}

	/**
     * Checks if this object contains an override for the specified Permission
     *
     * @param perm Permission to check
     * @return true if the permission is set, otherwise false
     */
	@Override
	public boolean isPermissionSet(Permission perm) {
		return this.caller.isPermissionSet(perm);
	}

	/**
     * Checks if this object contains an override for the specified
     * permission, by fully qualified name
     *
     * @param name Name of the permission
     * @return true if the permission is set, otherwise false
     */
	@Override
	public boolean isPermissionSet(String name) {
		return this.caller.isPermissionSet(name);
	}

	/**
     * Recalculates the permissions for this object, if the attachments have
     * changed values.
     * <p>
     * This should very rarely need to be called from a plugin.
     */
	@Override
	public void recalculatePermissions() {
		this.caller.recalculatePermissions();
	}

	/**
     * Removes the given PermissionAttachment from this object
     *
     * @param attachment Attachment to remove
     * @throws IllegalArgumentException Thrown when the specified attachment
     *     isn't part of this object
     */
	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		this.caller.removeAttachment(attachment);
	}

	/**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     */
	@Override
	public void sendMessage(String message) {
		this.caller.sendMessage(message);
	}

	/**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     */
	@Override
	public void sendMessage(String... messages) {
		this.caller.sendMessage(messages);
	}

	/**
     * Sends this sender a message
     *
     * @param message Message to be displayed
     * @param sender The sender of this message
     */
	@Override
	public void sendMessage(UUID sender, String message) {
		this.caller.sendMessage(sender, message);
	}

	/**
     * Sends this sender multiple messages
     *
     * @param messages An array of messages to be displayed
     * @param sender The sender of this message
     */
	@Override
	public void sendMessage(UUID sender, String... messages) {
		this.caller.sendMessage(sender, messages);
	}
	
	/**
     * Sets the operator status of this object
     *
     * @param value New operator value
     */
	@Override
	public void setOp(boolean value) {
		this.caller.setOp(value);
	}

	/**
	 * Returns a Spigot instance of this object
	 * @return a Spigot instance of this object
	 */
	@Override
	public Spigot spigot() {
		return this.caller.spigot();
	}

	@Override
	public @NotNull Component name() {
		return this.caller.name();
	}

}
