import java.util.ArrayList
import java.util.Arrays
import java.util.HashMap
import java.util.Random
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadLocalRandom
import java.util.function.Predicate

import com.mojang.brigadier.Message
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Chunk
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Server
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.World.Environment
import org.bukkit.WorldCreator
import org.bukkit.advancement.Advancement
import org.bukkit.advancement.AdvancementProgress
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.Chest
import org.bukkit.block.Container
import org.bukkit.block.Sign
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ComplexRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.loot.LootTable
import org.bukkit.loot.Lootable
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import org.bukkit.util.EulerAngle

import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.tree.LiteralCommandNode

import de.tr7zw.changeme.nbtapi.NBTContainer
import dev.jorel.commandapi.Brigadier
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandAPIConfig
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.Converter
import dev.jorel.commandapi.IStringTooltip
import dev.jorel.commandapi.StringTooltip
import dev.jorel.commandapi.SuggestionInfo
import dev.jorel.commandapi.Tooltip
import dev.jorel.commandapi.arguments.AdvancementArgument
import dev.jorel.commandapi.arguments.AdventureChatArgument
import dev.jorel.commandapi.arguments.AdventureChatComponentArgument
import dev.jorel.commandapi.arguments.AngleArgument
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.BiomeArgument
import dev.jorel.commandapi.arguments.BlockPredicateArgument
import dev.jorel.commandapi.arguments.BlockStateArgument
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.arguments.ChatArgument
import dev.jorel.commandapi.arguments.ChatColorArgument
import dev.jorel.commandapi.arguments.ChatComponentArgument
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.EnchantmentArgument
import dev.jorel.commandapi.arguments.EntitySelector
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.EntityTypeArgument
import dev.jorel.commandapi.arguments.EnvironmentArgument
import dev.jorel.commandapi.arguments.FunctionArgument
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.IntegerRangeArgument
import dev.jorel.commandapi.arguments.ItemStackArgument
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument
import dev.jorel.commandapi.arguments.ListArgumentBuilder
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.arguments.LocationArgument
import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.arguments.LootTableArgument
import dev.jorel.commandapi.arguments.MathOperationArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.NBTCompoundArgument
import dev.jorel.commandapi.arguments.ObjectiveArgument
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument
import dev.jorel.commandapi.arguments.ParticleArgument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.PotionEffectArgument
import dev.jorel.commandapi.arguments.RecipeArgument
import dev.jorel.commandapi.arguments.RotationArgument
import dev.jorel.commandapi.arguments.SafeSuggestions
import dev.jorel.commandapi.arguments.ScoreHolderArgument
import dev.jorel.commandapi.arguments.ScoreHolderArgument.ScoreHolderType
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument
import dev.jorel.commandapi.arguments.SoundArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.arguments.TeamArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.arguments.TimeArgument
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ExecutorType
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.wrappers.FunctionWrapper
import dev.jorel.commandapi.wrappers.IntegerRange
import dev.jorel.commandapi.wrappers.MathOperation
import dev.jorel.commandapi.wrappers.ParticleData
import dev.jorel.commandapi.wrappers.Rotation
import dev.jorel.commandapi.wrappers.ScoreboardSlot
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent

class Examples : JavaPlugin() {

fun commandunregistration() {
/* ANCHOR: commandunregistration */
//Unregister the gamemode command from the server (by force)
CommandAPI.unregister("gamemode", true)

// Register our new /gamemode, with survival, creative, adventure and spectator
CommandAPICommand("gamemode")
    .withArguments(MultiLiteralArgument("survival", "creative", "adventure", "spectator"))
    .executes(CommandExecutor { sender, args ->
        //Implementation of our /gamemode command
    })
    .register()
/* ANCHOR_END: commandunregistration */
}

fun booleanargs() {
/* ANCHOR: booleanargs */
// Load keys from config file
val configKeys : Array<String> = getConfig().getKeys(true).toTypedArray()

// Register our command
CommandAPICommand("editconfig")
    .withArguments(TextArgument("config-key").replaceSuggestions(ArgumentSuggestions.strings { _ -> configKeys }))
    .withArguments(BooleanArgument("value"))
    .executes(CommandExecutor { _, args ->
        // Update the config with the boolean argument
        getConfig().set(args[0] as String, args[1] as Boolean)
    })
    .register()
/* ANCHOR_END: booleanargs */
}

fun rangedarguments() {
/* ANCHOR: rangedarguments */
CommandAPICommand("searchrange")
    .withArguments(IntegerRangeArgument("range")) // Range argument
    .withArguments(ItemStackArgument("item"))     // The item to search for
    .executesPlayer(PlayerCommandExecutor { player, args ->
        // Retrieve the range from the arguments
        val range = args[0] as IntegerRange
        val itemStack = args[1] as ItemStack

        // Store the locations of chests with certain items
        val locations = mutableListOf<Location>()

        // Iterate through all chunks, and then all tile entities within each chunk
        for(chunk in player.world.loadedChunks) {
            for(blockState in chunk.tileEntities) {

                // The distance between the block and the player
                val distance = blockState.location.distance(player.location).toInt()

                // Check if the distance is within the specified range
                if(range.isInRange(distance)) {

                    // Check if the tile entity is a chest
                    if(blockState is Chest) {

                        // Check if the chest contains the item specified by the player
                        if(blockState.inventory.contains(itemStack.type)) {
                            locations.add(blockState.location)
                        }
                    }
                }

            }
        }

        // Output the locations of the chests, or whether no chests were found
        if(locations.isEmpty()) {
            player.sendMessage("No chests were found")
        } else {
            player.sendMessage("Found ${locations.size} chests:")
            locations.forEach({
                player.sendMessage("  Found at: ${it.getX()}, ${it.getY()}, ${it.getZ()}")
            })
        }
    })
    .register()
/* ANCHOR_END: rangedarguments */
}

fun greedystringarguments() {
/* ANCHOR: greedystringarguments */
CommandAPICommand("message")
    .withArguments(PlayerArgument("target"))
    .withArguments(GreedyStringArgument("message"))
    .executes(CommandExecutor { _, args ->
        (args[0] as Player).sendMessage(args[1] as String)
    })
    .register()
/* ANCHOR_END: greedystringarguments */
}

fun locationarguments() {
/* ANCHOR: locationarguments */
CommandAPICommand("break")
    //We want to target blocks in particular, so use BLOCK_POSITION
    .withArguments(LocationArgument("block", LocationType.BLOCK_POSITION))
    .executesPlayer(PlayerCommandExecutor { _, args -> 
        (args[0] as Location).block.setType(Material.AIR)
    })
    .register()
/* ANCHOR_END: locationarguments */
}

fun rotationarguments() {
/* ANCHOR: rotationarguments */
CommandAPICommand("rotate")
    .withArguments(RotationArgument("rotation"))
    .withArguments(EntitySelectorArgument<Entity>("target", EntitySelector.ONE_ENTITY))
    .executes(CommandExecutor { _, args ->
        val rotation = args[0] as Rotation
        val target = args[1] as Entity

        if(target is ArmorStand) {
            target.setHeadPose(EulerAngle(Math.toRadians(rotation.pitch.toDouble()), Math.toRadians(rotation.yaw.toDouble() - 90), 0.0))
        }
    })
    .register()
/* ANCHOR_END: rotationarguments */
}

@Suppress("DEPRECATION")
fun chatcolorarguments(){
/* ANCHOR: chatcolorarguments */
CommandAPICommand("namecolor")
    .withArguments(ChatColorArgument("chatcolor"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val color = args[0] as ChatColor
        player.setDisplayName("$color${player.getName()}")
    })
    .register()
/* ANCHOR_END: chatcolorarguments */
}

}