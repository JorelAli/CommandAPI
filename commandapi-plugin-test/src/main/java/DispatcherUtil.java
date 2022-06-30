import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;

import net.minecraft.commands.arguments.ArgumentAnchor;
import net.minecraft.commands.arguments.ArgumentAngle;
import net.minecraft.commands.arguments.ArgumentChat;
import net.minecraft.commands.arguments.ArgumentChatComponent;
import net.minecraft.commands.arguments.ArgumentChatFormat;
import net.minecraft.commands.arguments.ArgumentCriterionValue;
import net.minecraft.commands.arguments.ArgumentDimension;
import net.minecraft.commands.arguments.ArgumentEnchantment;
import net.minecraft.commands.arguments.ArgumentEntity;
import net.minecraft.commands.arguments.ArgumentEntitySummon;
import net.minecraft.commands.arguments.ArgumentInventorySlot;
import net.minecraft.commands.arguments.ArgumentMathOperation;
import net.minecraft.commands.arguments.ArgumentMinecraftKeyRegistered;
import net.minecraft.commands.arguments.ArgumentMobEffect;
import net.minecraft.commands.arguments.ArgumentNBTBase;
import net.minecraft.commands.arguments.ArgumentNBTKey;
import net.minecraft.commands.arguments.ArgumentNBTTag;
import net.minecraft.commands.arguments.ArgumentParticle;
import net.minecraft.commands.arguments.ArgumentProfile;
import net.minecraft.commands.arguments.ArgumentScoreboardCriteria;
import net.minecraft.commands.arguments.ArgumentScoreboardObjective;
import net.minecraft.commands.arguments.ArgumentScoreboardSlot;
import net.minecraft.commands.arguments.ArgumentScoreboardTeam;
import net.minecraft.commands.arguments.ArgumentScoreholder;
import net.minecraft.commands.arguments.ArgumentTime;
import net.minecraft.commands.arguments.ArgumentUUID;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceOrTagLocationArgument;
import net.minecraft.commands.arguments.TemplateMirrorArgument;
import net.minecraft.commands.arguments.TemplateRotationArgument;
import net.minecraft.commands.arguments.blocks.ArgumentBlockPredicate;
import net.minecraft.commands.arguments.blocks.ArgumentTile;
import net.minecraft.commands.arguments.coordinates.ArgumentPosition;
import net.minecraft.commands.arguments.coordinates.ArgumentRotation;
import net.minecraft.commands.arguments.coordinates.ArgumentRotationAxis;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2;
import net.minecraft.commands.arguments.coordinates.ArgumentVec2I;
import net.minecraft.commands.arguments.coordinates.ArgumentVec3;
import net.minecraft.commands.arguments.item.ArgumentItemPredicate;
import net.minecraft.commands.arguments.item.ArgumentItemStack;
import net.minecraft.commands.arguments.item.ArgumentTag;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.ArgumentUtils;

/**
 * An implementation of {@link ArgumentUtils} which produces JSON from a command
 * dispatcher and its root node. We have to avoid accessing IRegistry because it
 * isn't mock-able and cannot be instantiated through normal means
 */
public class DispatcherUtil {

	static Map<Class<?>, String> argumentParsers = new HashMap<>();

	static {
		argumentParsers.put(BoolArgumentType.class, "brigadier:bool");
		argumentParsers.put(FloatArgumentType.class, "brigadier:float");
		argumentParsers.put(DoubleArgumentType.class, "brigadier:double");
		argumentParsers.put(IntegerArgumentType.class, "brigadier:integer");
		argumentParsers.put(LongArgumentType.class, "brigadier:long");
		argumentParsers.put(StringArgumentType.class, "brigadier:string");
		argumentParsers.put(ArgumentEntity.class, "entity");
		argumentParsers.put(ArgumentProfile.class, "game_profile");
		argumentParsers.put(ArgumentPosition.class, "block_pos");
		argumentParsers.put(ArgumentVec2I.class, "column_pos");
		argumentParsers.put(ArgumentVec3.class, "vec3");
		argumentParsers.put(ArgumentVec2.class, "vec2");
		argumentParsers.put(ArgumentTile.class, "block_state");
		argumentParsers.put(ArgumentBlockPredicate.class, "block_predicate");
		argumentParsers.put(ArgumentItemStack.class, "item_stack");
		argumentParsers.put(ArgumentItemPredicate.class, "item_predicate");
		argumentParsers.put(ArgumentChatFormat.class, "color");
		argumentParsers.put(ArgumentChatComponent.class, "component");
		argumentParsers.put(ArgumentChat.class, "message");
		argumentParsers.put(ArgumentNBTTag.class, "nbt_compound_tag");
		argumentParsers.put(ArgumentNBTBase.class, "nbt_tag");
		argumentParsers.put(ArgumentNBTKey.class, "nbt_path");
		argumentParsers.put(ArgumentScoreboardObjective.class, "objective");
		argumentParsers.put(ArgumentScoreboardCriteria.class, "objective_criteria");
		argumentParsers.put(ArgumentMathOperation.class, "operation");
		argumentParsers.put(ArgumentParticle.class, "particle");
		argumentParsers.put(ArgumentAngle.class, "angle");
		argumentParsers.put(ArgumentRotation.class, "rotation");
		argumentParsers.put(ArgumentScoreboardSlot.class, "scoreboard_slot");
		argumentParsers.put(ArgumentScoreholder.class, "score_holder");
		argumentParsers.put(ArgumentRotationAxis.class, "swizzle");
		argumentParsers.put(ArgumentScoreboardTeam.class, "team");
		argumentParsers.put(ArgumentInventorySlot.class, "item_slot");
		argumentParsers.put(ArgumentMinecraftKeyRegistered.class, "resource_location");
		argumentParsers.put(ArgumentMobEffect.class, "mob_effect");
		argumentParsers.put(ArgumentTag.class, "function");
		argumentParsers.put(ArgumentAnchor.class, "entity_anchor");
		argumentParsers.put(ArgumentCriterionValue.b.class, "int_range");
		argumentParsers.put(ArgumentCriterionValue.a.class, "float_range");
		argumentParsers.put(ArgumentEnchantment.class, "item_enchantment");
		argumentParsers.put(ArgumentEntitySummon.class, "entity_summon");
		argumentParsers.put(ArgumentDimension.class, "dimension");
		argumentParsers.put(ArgumentTime.class, "time");
		argumentParsers.put(ResourceOrTagLocationArgument.class, "resource_or_tag");
		argumentParsers.put(ResourceKeyArgument.class, "resource");
		argumentParsers.put(TemplateMirrorArgument.class, "template_mirror");
		argumentParsers.put(TemplateRotationArgument.class, "template_rotation");
		argumentParsers.put(ArgumentUUID.class, "uuid");
	}

	public static <S> JsonObject toJSON(CommandDispatcher<S> dispatcher, CommandNode<S> node) {
		JsonObject jsonObject = new JsonObject();

		// Unpack nodes
		if (node instanceof RootCommandNode) {
			jsonObject.addProperty("type", "root");
		} else if (node instanceof LiteralCommandNode) {
			jsonObject.addProperty("type", "literal");
		} else if (node instanceof ArgumentCommandNode) {
			ArgumentCommandNode<?, ?> argumentCommandNode = (ArgumentCommandNode<?, ?>) node;
			argToJSON(jsonObject, argumentCommandNode.getType());
		} else {
			jsonObject.addProperty("type", "unknown");
		}

		// Write children
		JsonObject children = new JsonObject();
		for (CommandNode<S> child : node.getChildren()) {
			children.add(child.getName(), (JsonElement) toJSON(dispatcher, child));
		}
		if (children.size() > 0) {
			jsonObject.add("children", (JsonElement) children);
		}

		// Write whether the command is executable
		if (node.getCommand() != null) {
			jsonObject.addProperty("executable", Boolean.valueOf(true));
		}
		if (node.getRedirect() != null) {
			Collection<String> redirectPaths = dispatcher.getPath(node.getRedirect());
			if (!redirectPaths.isEmpty()) {
				JsonArray redirects = new JsonArray();
				for (String redirectPath : redirectPaths) {
					redirects.add(redirectPath);
				}
				jsonObject.add("redirect", (JsonElement) redirects);
			}
		}
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	private static <T extends ArgumentType<?>> void argToJSON(JsonObject jsonObject, T argument) {
		ArgumentTypeInfo.a<T> argumentInfo = ArgumentTypeInfos.b(argument);
		jsonObject.addProperty("type", "argument");
		jsonObject.addProperty("parser", argumentParsers.get(argument.getClass()));
		
		// Properties
		JsonObject properties = new JsonObject();
		@SuppressWarnings("rawtypes")
		ArgumentTypeInfo argumentTypeInfo = argumentInfo.a();
		argumentTypeInfo.a(argumentInfo, properties);
		if (properties.size() > 0) {
			jsonObject.add("properties", (JsonElement) properties);
		}
	}

}
