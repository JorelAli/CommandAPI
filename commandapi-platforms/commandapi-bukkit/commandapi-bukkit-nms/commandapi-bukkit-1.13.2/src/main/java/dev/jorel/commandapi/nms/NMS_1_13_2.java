package dev.jorel.commandapi.nms;

import java.util.Map;

import dev.jorel.commandapi.abstractions.AbstractCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitCommandSender;
import dev.jorel.commandapi.commandsenders.BukkitNativeProxyCommandSender;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.CraftSound;
import org.bukkit.craftbukkit.v1_13_R2.command.VanillaCommandWrapper;
import org.bukkit.craftbukkit.v1_13_R2.help.SimpleHelpMap;

import com.mojang.brigadier.context.CommandContext;

import dev.jorel.commandapi.preprocessor.Differs;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import dev.jorel.commandapi.preprocessor.RequireField;
import dev.jorel.commandapi.wrappers.FloatRange;
import dev.jorel.commandapi.wrappers.IntegerRange;
import dev.jorel.commandapi.wrappers.NativeProxyCommandSender;
import net.minecraft.server.v1_13_R2.ArgumentCriterionValue;
import net.minecraft.server.v1_13_R2.CommandListenerWrapper;
import net.minecraft.server.v1_13_R2.CriterionConditionValue;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntitySelector;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.LootTableRegistry;
import net.minecraft.server.v1_13_R2.ParticleParamBlock;
import net.minecraft.server.v1_13_R2.ParticleParamItem;
import net.minecraft.server.v1_13_R2.ParticleParamRedstone;
import net.minecraft.server.v1_13_R2.Vec2F;
import net.minecraft.server.v1_13_R2.Vec3D;

/**
 * NMS implementation for Minecraft 1.13.2
 */
@NMSMeta(compatibleWith = "1.13.2")
@RequireField(in = CraftSound.class, name = "minecraftKey", ofType = String.class)
@RequireField(in = EntitySelector.class, name = "m", ofType = boolean.class)
@RequireField(in = LootTableRegistry.class, name = "e", ofType = Map.class)
@RequireField(in = SimpleHelpMap.class, name = "helpTopics", ofType = Map.class)
@RequireField(in = ParticleParamBlock.class, name = "c", ofType = IBlockData.class)
@RequireField(in = ParticleParamItem.class, name = "c", ofType = ItemStack.class)
@RequireField(in = ParticleParamRedstone.class, name = "f", ofType = float.class)
public class NMS_1_13_2 extends NMS_1_13_1 {

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.13.2" };
	}

	@Differs(from = "1.13.1", by = "using VanillaCommandWrapper.getListener")
	@Override
	public CommandListenerWrapper getBrigadierSourceFromCommandSender(AbstractCommandSender<? extends CommandSender> senderWrapper) {
		return VanillaCommandWrapper.getListener(senderWrapper.getSource());
	}

	@Differs(from = "1.13.1", by = "rename CriterionConditionValue.c -> CriterionConditionValue.FloatRange")
	@Override
	public FloatRange getFloatRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.FloatRange range = cmdCtx.getArgument(key,
				CriterionConditionValue.FloatRange.class);
		float low = range.a() == null ? -Float.MAX_VALUE : range.a();
		float high = range.b() == null ? Float.MAX_VALUE : range.b();
		return new FloatRange(low, high);
	}

	@Differs(from = "1.13.1", by = "rename CriterionConditionValue.d -> CriterionConditionValue.IntegerRange")
	@Override
	public IntegerRange getIntRange(CommandContext<CommandListenerWrapper> cmdCtx, String key) {
		CriterionConditionValue.IntegerRange range = ArgumentCriterionValue.b.a(cmdCtx, key);
		int low = range.a() == null ? Integer.MIN_VALUE : range.a();
		int high = range.b() == null ? Integer.MAX_VALUE : range.b();
		return new IntegerRange(low, high);
	}

	@Differs(from = "1.13.1", by = "clw.f() -> clw.getEntity()")
	@Override
	public BukkitCommandSender<? extends CommandSender> getSenderForCommand(CommandContext<CommandListenerWrapper> cmdCtx, boolean isNative) {
		CommandListenerWrapper clw = cmdCtx.getSource();

		CommandSender sender = clw.getBukkitSender();
		Vec3D pos = clw.getPosition();
		Vec2F rot = clw.i();
		World world = getWorldForCSS(clw);
		Location location = new Location(world, pos.x, pos.y, pos.z, rot.j, rot.i);

		Entity proxyEntity = clw.getEntity();
		CommandSender proxy = proxyEntity == null ? null : proxyEntity.getBukkitEntity();
		if (isNative || (proxy != null && !sender.equals(proxy))) {
			return new BukkitNativeProxyCommandSender(new NativeProxyCommandSender(sender, proxy, location, world));
		} else {
			return wrapCommandSender(sender);
		}
	}
}
