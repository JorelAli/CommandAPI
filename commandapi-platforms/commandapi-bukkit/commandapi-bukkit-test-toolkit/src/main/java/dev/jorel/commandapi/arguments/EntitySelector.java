package dev.jorel.commandapi.arguments;

import dev.jorel.commandapi.MockCommandSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Predicate;

public record EntitySelector(
	int maxResults, boolean includesEntities, Order order, boolean selfSelector, String playerName,
	Predicate<Entity> entityCheck, UUID entityUUID, EntityType type
) {
	// Sorting orders
	@FunctionalInterface
	public interface Order {
		void sort(Location location, List<? extends Entity> entities);
	}

	public static final Order ORDER_ARBITRARY = (location, entities) -> {
	};

	public static final Order ORDER_NEAREST = (location, entities) ->
		entities.sort(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(location)));

	public static final Order ORDER_FURTHEST = (location, entities) ->
		entities.sort(Comparator.comparingDouble(entity -> -entity.getLocation().distanceSquared(location)));

	public static final Order ORDER_RANDOM = (location, entities) -> Collections.shuffle(entities);

	// Retrieve entities
	public List<? extends Entity> findEntities(MockCommandSource source) {
		if (!includesEntities) {
			return findPlayers(source);
		} else if (playerName != null) {
			Player player = Bukkit.getPlayer(playerName);
			return player == null ? List.of() : List.of(player);
		} else if (entityUUID != null) {
			for (World world : Bukkit.getServer().getWorlds()) {
				// Note that Paper does have the method `World#getEntity(UUID)`, which would be more accurate
				//  to the vanilla implementation, but MockBukkit doesn't implement that method, so this works too.
				for (Entity entity : world.getEntities()) {
					if (entity.getUniqueId().equals(entityUUID)) {
						return List.of(entity);
					}
				}
			}
			return List.of();
		} else if (selfSelector) {
			Entity entity = source.entity();
			return (entity != null && entityCheck.test(entity)) ? List.of(entity) : List.of();
		} else {
			List<Entity> entities = new ArrayList<>();
			for (World world : Bukkit.getServer().getWorlds()) {
				for (Entity entity : world.getEntities()) {
					if (
						(type == null || entity.getType().equals(type))
							&& entityCheck.test(entity)
					) {
						entities.add(entity);
					}
				}
			}
			return sortAndLimit(source.location(), entities);
		}
	}

	public List<Player> findPlayers(MockCommandSource source) {
		if (playerName != null) {
			Player player = Bukkit.getPlayer(playerName);
			return player == null ? List.of() : List.of(player);
		} else if (entityUUID != null) {
			Player player = Bukkit.getPlayer(entityUUID);
			return player == null ? List.of() : List.of(player);
		} else if (selfSelector) {
			Entity entity = source.entity();
			return (entity instanceof Player player && entityCheck.test(entity)) ? List.of(player) : List.of();
		} else {
			List<Player> players = new ArrayList<>();
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (entityCheck.test(player)) {
					players.add(player);
				}
			}
			return sortAndLimit(source.location(), players);
		}
	}

	private <T extends Entity> List<T> sortAndLimit(Location originLocation, List<T> entities) {
		if (entities.size() > 1) {
			order.sort(originLocation, entities);
		}

		return entities.subList(0, Math.min(maxResults, entities.size()));
	}
}
