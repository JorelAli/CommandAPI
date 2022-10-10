package dev.jorel.commandapi.commandsenders;
import org.bukkit.entity.Entity;

import dev.jorel.commandapi.abstractions.AbstractEntity;

public class BukkitEntity extends AbstractEntity<Entity> {

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
