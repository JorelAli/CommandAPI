package dev.jorel.commandapi.nms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_14_R1.CraftSound;
import org.bukkit.craftbukkit.v1_14_R1.help.SimpleHelpMap;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;

import dev.jorel.commandapi.arguments.SuggestionProviders;
import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.SimpleFunctionWrapper;
import net.minecraft.server.v1_14_R1.Advancement;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.CompletionProviders;
import net.minecraft.server.v1_14_R1.CustomFunction;
import net.minecraft.server.v1_14_R1.CustomFunctionData;
import net.minecraft.server.v1_14_R1.EntitySelector;
import net.minecraft.server.v1_14_R1.IBlockData;
import net.minecraft.server.v1_14_R1.ICompletionProvider;
import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import net.minecraft.server.v1_14_R1.ParticleParamBlock;
import net.minecraft.server.v1_14_R1.ParticleParamItem;
import net.minecraft.server.v1_14_R1.ParticleParamRedstone;

/**
 * NMS implementation for Minecraft 1.14.3
 */
@NMSMeta(compatibleWith = "1.14.3")
@RequireField(in = CraftSound.class, name = "minecraftKey", ofType = String.class)
@RequireField(in = EntitySelector.class, name = "checkPermissions", ofType = boolean.class)
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
@RequireField(in = ParticleParamBlock.class, name = "c", ofType = IBlockData.class)
@RequireField(in = ParticleParamItem.class, name = "c", ofType = ItemStack.class)
@RequireField(in = ParticleParamRedstone.class, name = "f", ofType = float.class)
public class NMS_1_14_3 extends NMS_1_14 {

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.14.3" };
	}

	@Differs(from = "1.14", by = "MINECRAFT_SERVER.getAdvancementData().b() -> MINECRAFT_SERVER.getAdvancementData().a(). functionData.g() -> functionData.h()")
	@Override
	public SuggestionProvider<CommandListenerWrapper> getSuggestionProvider(SuggestionProviders provider) {
		return switch (provider) {
			case FUNCTION -> (context, builder) -> {
				CustomFunctionData functionData = MINECRAFT_SERVER.getFunctionData();
				ICompletionProvider.a(functionData.h().a(), builder, "#");
				return ICompletionProvider.a(functionData.c().keySet(), builder);
			};
			case RECIPES -> CompletionProviders.b;
			case SOUNDS -> CompletionProviders.c;
			case ADVANCEMENTS -> (cmdCtx, builder) -> {
				return ICompletionProvider
					.a(MINECRAFT_SERVER.getAdvancementData().a().stream().map(Advancement::getName), builder);
			};
			case LOOT_TABLES -> (cmdCtx, builder) -> {
				return ICompletionProvider.a(MINECRAFT_SERVER.getLootTableRegistry().a(), builder);
			};
			case ENTITIES -> CompletionProviders.d;
			default -> (context, builder) -> Suggestions.empty();
		};
	}

	@Differs(from = "1.14", by = "MINECRAFT_SERVER.getFunctionData().g() -> MINECRAFT_SERVER.getFunctionData().h()")
	@Override
	public SimpleFunctionWrapper[] getTag(NamespacedKey key) {
		List<CustomFunction> customFunctions = new ArrayList<>(
			MINECRAFT_SERVER.getFunctionData().h().b(new MinecraftKey(key.getNamespace(), key.getKey())).a());
		SimpleFunctionWrapper[] result = new SimpleFunctionWrapper[customFunctions.size()];
		for (int i = 0, size = customFunctions.size(); i < size; i++) {
			result[i] = convertFunction(customFunctions.get(i));
		}
		return result;
	}

	@Differs(from = "1.14", by = "MINECRAFT_SERVER.getFunctionData().g() -> MINECRAFT_SERVER.getFunctionData().h()")
	@Override
	public Set<NamespacedKey> getTags() {
		Set<NamespacedKey> functions = new HashSet<>();
		for (MinecraftKey key : MINECRAFT_SERVER.getFunctionData().h().a()) {
			functions.add(fromMinecraftKey(key));
		}
		return functions;
	}
}
