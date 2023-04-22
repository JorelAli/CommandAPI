package dev.jorel.commandapi.commandsenders;

import org.bukkit.entity.Entity;

public class BukkitEntity implements AbstractEntity<Entity>, BukkitCommandSender<Entity> {

	private final Entity entity;
	
	public BukkitEntity(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public boolean hasPermission(String permissionNode) {
		return this.entity.hasPermission(permissionNode);
	}
	
	@Override
	public boolean isOp() {
		return this.entity.isOp();
	}

	@Override
	public Entity getSource() {
		return this.entity;
	}
	
}
