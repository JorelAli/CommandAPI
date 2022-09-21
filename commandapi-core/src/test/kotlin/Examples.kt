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
import dev.jorel.commandapi.executors.BlockCommandExecutor
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.EntityCommandExecutor
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
        for (chunk in player.world.loadedChunks) {
            for (blockState in chunk.tileEntities) {

                // The distance between the block and the player
                val distance = blockState.location.distance(player.location).toInt()

                // Check if the distance is within the specified range
                if (range.isInRange(distance)) {

                    // Check if the tile entity is a chest
                    if (blockState is Chest) {

                        // Check if the chest contains the item specified by the player
                        if (blockState.inventory.contains(itemStack.type)) {
                            locations.add(blockState.location)
                        }
                    }
                }

            }
        }

        // Output the locations of the chests, or whether no chests were found
        if (locations.isEmpty()) {
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

        if (target is ArmorStand) {
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


@Suppress("deprecation")
fun chatcomponentarguments() {
/* ANCHOR: chatcomponentarguments */
CommandAPICommand("makebook")
    .withArguments(PlayerArgument("player"))
    .withArguments(ChatComponentArgument("contents"))
    .executes(CommandExecutor { sender, args ->
        val player = args[0] as Player
        val arr = args[1] as Array<BaseComponent>
        
        //Create book
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.getItemMeta() as BookMeta 
        meta.setTitle("Custom Book")
        meta.setAuthor(player.name)
        meta.spigot().setPages(arr)
        item.setItemMeta(meta)
        
        //Give player the book
        player.inventory.addItem(item)
    })
    .register()
/* ANCHOR_END: chatcomponentarguments */
}

@Suppress("deprecation")
fun chatarguments() {
/* ANCHOR: chatarguments */
CommandAPICommand("pbroadcast")
    .withArguments(ChatArgument("message"))
    .executes(CommandExecutor { sender, args ->
        val message = args[0] as Array<BaseComponent>
    
        //Broadcast the message to everyone on the server
        Bukkit.server.spigot().broadcast(message)
    })
    .register()
/* ANCHOR_END: chatarguments */

/* ANCHOR: chatpreviewspigot */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").withPreview({ info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText = BaseComponent.toPlainText(info.parsedInput() as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    }))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a BaseComponent[] by converting to plain text then to BaseComponent[]
        val plainText = BaseComponent.toPlainText(args[0] as Array<BaseComponent>)
        Bukkit.spigot().broadcast(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText)))
    })
    .register()
/* ANCHOR_END: chatpreviewspigot */

/* ANCHOR: chatpreviewadventure */
CommandAPICommand("broadcast")
    .withArguments(AdventureChatArgument("message").withPreview({ info ->
        // Convert parsed Component to plain text
        val plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        return LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    }))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a Component by converting to plain text then to Component
        val plainText = PlainTextComponentSerializer.plainText().serialize(args[0] as Component)
        Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(plainText))
    })
    .register()
/* ANCHOR_END: chatpreviewadventure */

/* ANCHOR: chatpreviewspigotusepreview */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").usePreview(true).withPreview({ info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText = BaseComponent.toPlainText(info.parsedInput() as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        return TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    }))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Bukkit.spigot().broadcast(args[0] as Array<BaseComponent>)
    })
    .register()
/* ANCHOR_END: chatpreviewspigotusepreview */

/* ANCHOR: chatpreviewadventureusepreview */
CommandAPICommand("broadcast")
    .withArguments(AdventureChatArgument("message").usePreview(true).withPreview({ info ->
        // Convert parsed Component to plain text
        val plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        return LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    }))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Bukkit.broadcast(args[0] as Component)
    })
    .register()
/* ANCHOR_END: chatpreviewadventureusepreview */
}


fun argumentadventurechatcomponent() {
/* ANCHOR: ArgumentAdventureChatComponent */
CommandAPICommand("showbook")
    .withArguments(PlayerArgument("target"))
    .withArguments(TextArgument("title"))
    .withArguments(StringArgument("author"))
    .withArguments(AdventureChatComponentArgument("contents"))
    .executes(CommandExecutor { sender, args ->
        val target = args[0] as Player
        val title = args[1] as String
        val author = args[2] as String
        val content = args[3] as Component
        
        // Create a book and show it to the user (Requires Paper)
        val mybook = Book.book(Component.text(title), Component.text(author), content)
        target.openBook(mybook)
    })
    .register()
/* ANCHOR_END: ArgumentAdventureChatComponent */
}

fun argumentadventurechat() {
/* ANCHOR: ArgumentAdventureChat */
CommandAPICommand("pbroadcast")
    .withArguments(AdventureChatArgument("message"))
    .executes(CommandExecutor { sender, args ->
        val message = args[0] as Component
        
        // Broadcast the message to everyone with broadcast permissions.
        Bukkit.getServer().broadcast(message, Server.BROADCAST_CHANNEL_USERS)
        Bukkit.getServer().broadcast(message)
    })
    .register()
/* ANCHOR_END: ArgumentAdventureChat */
}

fun entityselectorarguments() {
/* ANCHOR: entityselectorarguments */
CommandAPICommand("remove")
    //Using a collective entity selector to select multiple entities
    .withArguments(EntitySelectorArgument<Collection<Entity>>("entities", EntitySelector.MANY_ENTITIES))
    .executes(CommandExecutor { sender, args ->
        //Parse the argument as a collection of entities (as stated above in the documentation)
        @Suppress("unchecked")
        val entities = args[0] as Collection<Entity>
        
        sender.sendMessage("Removed ${entities.size} entities")
        for (e in entities) {
            e.remove()
        }
    })
    .register()
/* ANCHOR_END: entityselectorarguments */
}

fun entitytypearguments() {
/* ANCHOR: entitytypearguments */
CommandAPICommand("spawnmob")
    .withArguments(EntityTypeArgument("entity"))
    .withArguments(IntegerArgument("amount", 1, 100)) //Prevent spawning too many entities
    .executesPlayer(PlayerCommandExecutor { player, args ->
        for (i in 0 until args[1] as Int) {
            player.world.spawnEntity(player.location, args[0] as EntityType)
        }
    })
    .register()
/* ANCHOR_END: entitytypearguments */
}

fun scoreholderargument() {
/* ANCHOR: scoreholderargument */
CommandAPICommand("reward")
    // We want multiple players, so we use ScoreHolderType.MULTIPLE in the constructor
    .withArguments(ScoreHolderArgument<Collection<String>>("players", ScoreHolderType.MULTIPLE))
    .executes(CommandExecutor { sender, args ->
        // Get player names by casting to Collection<String>
        @Suppress("unchecked")
        players = args[0] as Collection<String> 

        for (playerName in players) {
            Bukkit.getPlayer(playerName).inventory.addItem(ItemStack(Material.DIAMOND, 3))
        }
    })
    .register()
/* ANCHOR_END: scoreholderargument */
}

// fun scoreholderargument_2() {
// val args = arrayOf()
// @Suppress("unchecked")
// // This example isn't used because for some reason, mdbook doesn't render it properly
// /* ANCHOR: scoreholderargument_2 */
// Collection<String> entitiesAndPlayers = (Collection<String>) args[0]
// for (String str : entitiesAndPlayers) {
//     try {
//         UUID uuid = UUID.fromString(str)
//         // Is a UUID, so it must by an entity
//         Bukkit.getEntity(uuid)
//     } catch(IllegalArgumentException exception) {
//         // Not a UUID, so it must be a player name
//         Bukkit.getPlayer(str) 
//     }
// }
// /* ANCHOR_END: scoreholderargument_2 */
// }

fun scoreboardslotargument() {
/* ANCHOR: scoreboardslotargument */
CommandAPICommand("clearobjectives")
    .withArguments(ScoreboardSlotArgument("slot"))
    .executes(CommandExecutor { sender, args ->
        val scoreboard = Bukkit.scoreboardManager.mainScoreboard
        val slot = (args[0] as ScoreboardSlot).displaySlot
        scoreboard.clearSlot(slot)
    })
    .register()
/* ANCHOR_END: scoreboardslotargument */
}

fun objectiveargument() {
/* ANCHOR: objectiveargument */
CommandAPICommand("sidebar")
    .withArguments(ObjectiveArgument("objective"))
    .executes(CommandExecutor { sender, args ->
        // The ObjectArgument must be casted to a String
        val objectiveName = args[0] as String

        // An objective name can be turned into an Objective using getObjective(String)
        val objective = Bukkit.scoreboardManager.mainScoreboard.getObjective(objectiveName)

        // Set display slot
        objective.setDisplaySlot(DisplaySlot.SIDEBAR)
    })
    .register()
/* ANCHOR_END: objectiveargument */
}

fun objectivecriteriaarguments() {
/* ANCHOR: objectivecriteriaarguments */
CommandAPICommand("unregisterall")
    .withArguments(ObjectiveCriteriaArgument("objective criteria"))
    .executes(CommandExecutor { sender, args ->
        val objectiveCriteria = args[0] as String
        val objectives = Bukkit.scoreboardManager.mainScoreboard.getObjectivesByCriteria(objectiveCriteria)

        // Unregister the objectives
        for (objective in objectives) {
            objective.unregister()
        }
    })
    .register()
/* ANCHOR_END: objectivecriteriaarguments */
}

fun teamarguments() {
/* ANCHOR: teamarguments */
CommandAPICommand("togglepvp")
    .withArguments(TeamArgument("team"))
    .executes(CommandExecutor { sender, args ->
        // The TeamArgument must be casted to a String
        val teamName = args[0] as String
        
        // A team name can be turned into a Team using getTeam(String)
        val team = Bukkit.scoreboardManager.mainScoreboard.getTeam(teamName)
        
        // Toggle pvp
        team.setAllowFriendlyFire(team.allowFriendlyFire())
    })
    .register()
/* ANCHOR_END: teamarguments */
}

fun advancementarguments() {
/* ANCHOR: advancementarguments */
CommandAPICommand("award")
    .withArguments(PlayerArgument("player"))
    .withArguments(AdvancementArgument("advancement"))
    .executes(CommandExecutor { sender, args ->
        val target = args[0] as Player
        val advancement = args[1] as Advancement
        
        //Award all criteria for the advancement
        val progress = target.getAdvancementProgress(advancement)
        for (criteria in advancement.criteria) {
            progress.awardCriteria(criteria)
        }
    })
    .register()
/* ANCHOR_END: advancementarguments */
}

fun biomearguments() {
/* ANCHOR: biomearguments */
CommandAPICommand("setbiome")
    .withArguments(BiomeArgument("biome"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val biome = args[0] as Biome

        val chunk = player.location.chunk
        player.world.setBiome(chunk.x, player.location.blockY, chunk.z, biome)
    })
    .register()
/* ANCHOR_END: biomearguments */
}

fun blockstateargument() {
/* ANCHOR: blockstateargument */
CommandAPICommand("set")
    .withArguments(BlockStateArgument("block"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val blockdata = args[0] as BlockData
        val targetBlock = player.getTargetBlockExact(256)
        
        // Set the block, along with its data
        targetBlock.setType(blockdata.material)
        targetBlock.state.setBlockData(blockdata)
    })
    .register()
/* ANCHOR_END: blockstateargument */
}

fun enchantmentarguments() {
/* ANCHOR: enchantmentarguments */
CommandAPICommand("enchantitem")
    .withArguments(EnchantmentArgument("enchantment"))
    .withArguments(IntegerArgument("level", 1, 5))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val enchantment = args[0] as Enchantment
        val level = args[1] as Int

        // Add the enchantment
        player.inventory.itemInMainHand.addEnchantment(enchantment, level)
    })
    .register()
/* ANCHOR_END: enchantmentarguments */
}

fun environmentarguments() {
/* ANCHOR: environmentarguments */
CommandAPICommand("createworld")
    .withArguments(StringArgument("worldname"))
    .withArguments(EnvironmentArgument("type"))
    .executes(CommandExecutor { sender, args ->
        val worldName = args[0] as String
        val environment = args[1] as Environment

        // Create a new world with the specific world name and environment
        Bukkit.getServer().createWorld(WorldCreator(worldName).environment(environment))
        sender.sendMessage("World created!")
    })
    .register()
/* ANCHOR_END: environmentarguments */
}

fun itemstackarguments() {
/* ANCHOR: itemstackarguments */
CommandAPICommand("item")
    .withArguments(ItemStackArgument("itemstack"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.inventory.addItem(args[0] as ItemStack)
    })
    .register()
/* ANCHOR_END: itemstackarguments */
}

fun loottablearguments() {
/* ANCHOR: loottablearguments */
CommandAPICommand("giveloottable")
    .withArguments(LootTableArgument("loottable"))
    .withArguments(LocationArgument("location", LocationType.BLOCK_POSITION))
    .executes(CommandExecutor { sender, args ->
        val lootTable = args[0] as LootTable
        val location = args[1] as Location

        val state = location.getBlock().getState()

        // Check if the input block is a container (e.g. chest)
        if (state is Container && state is Lootable) {
            // Apply the loot table to the chest
            state.setLootTable(lootTable)
            state.update()
        }
    })
    .register()
/* ANCHOR_END: loottablearguments */
}

fun mathoperationarguments() {
/* ANCHOR: mathoperationarguments */
CommandAPICommand("changelevel")
    .withArguments(PlayerArgument("player"))
    .withArguments(MathOperationArgument("operation"))
    .withArguments(IntegerArgument("value"))
    .executes(CommandExecutor { sender, args ->
        val target = args[0] as Player
        val op = args[1] as MathOperation
        val value = args[2] as Int

        target.setLevel(op.apply(target.level, value))
    })
    .register()
/* ANCHOR_END: mathoperationarguments */
}

fun particlearguments() {
/* ANCHOR: particlearguments */
CommandAPICommand("showparticle")
    .withArguments(ParticleArgument("particle"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val particleData = args[0] as ParticleData<Any>
        player.world.spawnParticle(particleData.particle(), player.location, 1)
    })
    .register()
/* ANCHOR_END: particlearguments */

/* ANCHOR: particlearguments2 */
CommandAPICommand("showparticle")
    .withArguments(ParticleArgument("particle"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val particleData = args[0] as ParticleData<Any>
        player.world.spawnParticle(particleData.particle(), player.location, 1, particleData.data())
    })
    .register()
/* ANCHOR_END: particlearguments2 */
}

fun potioneffectarguments() {
/* ANCHOR: potioneffectarguments */
CommandAPICommand("potion")
    .withArguments(PlayerArgument("target"))
    .withArguments(PotionEffectArgument("potion"))
    .withArguments(TimeArgument("duration"))
    .withArguments(IntegerArgument("strength"))
    .executes(CommandExecutor { sender, args ->
        val target = args[0] as Player
        val potion = args[1] as PotionEffectType
        val duration = args[2] as Int
        val strength = args[3] as Int
        
        //Add the potion effect to the target player
        target.addPotionEffect(PotionEffect(potion, duration, strength))
    })
    .register()
/* ANCHOR_END: potioneffectarguments */
}

fun recipearguments() {
/* ANCHOR: recipearguments */
CommandAPICommand("giverecipe")
    .withArguments(RecipeArgument("recipe"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val recipe = args[0] as ComplexRecipe
        player.inventory.addItem(recipe.result)
    })
    .register()
/* ANCHOR_END: recipearguments */
}

fun recipearguments2() {
/* ANCHOR: recipearguments2 */
CommandAPICommand("unlockrecipe")
    .withArguments(PlayerArgument("player"))
    .withArguments(RecipeArgument("recipe"))
    .executes(CommandExecutor { sender, args ->
        val target = args[0] as Player
        val recipe = args[1] as ComplexRecipe

        target.discoverRecipe(recipe.key)
    })
    .register()
/* ANCHOR_END: recipearguments2 */
}

fun soundarguments() {
/* ANCHOR: soundarguments */
CommandAPICommand("sound")
    .withArguments(SoundArgument("sound"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.world.playSound(player.location, args[0] as Sound, 100.0f, 1.0f)
    })
    .register()
/* ANCHOR_END: soundarguments */
}


@Suppress("deprecation")
fun timearg() {
/* ANCHOR: timearguments */
CommandAPICommand("bigmsg")
    .withArguments(TimeArgument("duration"))
    .withArguments(GreedyStringArgument("message"))
    .executes(CommandExecutor { sender, args ->
        //Duration in ticks
        val duration = args[0] as Int
        val message = args[1] as String

        for (player in Bukkit.onlinePlayers) {
            //Display the message to all players, with the default fade in/out times (10 and 20).
            player.sendTitle(message, "", 10, duration, 20)
        }
    })
    .register()
/* ANCHOR_END: timearguments */
}

fun blockpredicatearguments() {
/* ANCHOR: blockpredicatearguments */
Array<Argument<Any>> arguments = arrayOf(
    IntegerArgument("radius"),
    BlockPredicateArgument("fromBlock"),
    BlockStateArgument("toBlock"),
)
/* ANCHOR_END: blockpredicatearguments */

/* ANCHOR: blockpredicatearguments2 */
CommandAPICommand("replace")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->

        // Parse the arguments
        val radius = args[0] as Int
        val predicate = args[1] as Predicate<Block>
        val blockData = args[2] as BlockData

        // Find a (solid) sphere of blocks around the player with a given radius
        val center = player.location // for (i in 1 until 11) { }
        for (x in -radius until radius + 1) {
            for (y in -radius until radius + 1) {
                for (z in -radius until radius + 1) {
                    if (Math.sqrt((x * x) + (y * y) + (z * z)) <= radius) {
                        val block = center.world.getBlockAt(x + center.blockX, y + center.blockY, z + center.blockZ)
                        
                        // If that block matches a block from the predicate, set it
                        if (predicate.test(block)) {
                            block.setType(blockData.material)
                            block.setBlockData(blockData)
                        }
                    }
                }
            }
        }
    })
    .register()
/* ANCHOR_END: blockpredicatearguments2 */
}

fun itemstackpredicatearguments() {
/* ANCHOR: itemstackpredicatearguments */
// Register our command
CommandAPICommand("rem")
    .withArguments(ItemStackPredicateArgument("items"))
    .executesPlayer(PlayerCommandExecutor { player, args ->

        // Get our predicate
        @Suppress("unchecked")
        predicate = args[0] as Predicate<ItemStack>

        for (item in player.getInventory()) {
            if (predicate.test(item)) {
                player.inventory.remove(item)
            }
        }
    })
    .register()
/* ANCHOR_END: itemstackpredicatearguments */
}

class NBTTest : JavaPlugin() {

/* ANCHOR: nbtcompoundargumentonload */
override fun onLoad() {
    CommandAPI.onLoad(CommandAPIConfig()
        .initializeNBTAPI(NBTContainer.javaClass, ::NBTContainer)
    )
}
/* ANCHOR_END: nbtcompoundargumentonload */
	
}

@Suppress("unused")
fun nbtcompoundarguments() {
/* ANCHOR: nbtcompoundarguments */
CommandAPICommand("award")
    .withArguments(NBTCompoundArgument<NBTContainer>("nbt"))
    .executes(CommandExecutor { sender, args ->
        val nbt = args[0] as NBTContainer
        
        //Do something with "nbt" here...
    })
    .register()
/* ANCHOR_END: nbtcompoundarguments */
}

@Suppress("unused")
fun literalarguments() {
/* ANCHOR: literalarguments */
CommandAPICommand("mycommand")
    .withArguments(LiteralArgument("hello"))
    .withArguments(TextArgument("text"))
    .executes(CommandExecutor { sender, args ->
        // This gives the variable "text" the contents of the TextArgument, and not the literal "hello"
        val text = args[0] as String
    })
    .register()
/* ANCHOR_END: literalarguments */
}

fun literalarguments2() {
/* ANCHOR: literalarguments2 */
//Create a map of gamemode names to their respective objects
val gamemodes = mapOf(
    "adventure" to GameMode.ADVENTURE,
    "creative" to GameMode.CREATIVE,
    "spectator" to GameMode.SPECTATOR,
    "survival" to GameMode.SURVIVAL
)

//Iterate over the map
for ((key, _) in gamemodes) {
    
    //Register the command as usual
    CommandAPICommand("changegamemode")
        .withArguments(LiteralArgument(key))
        .executesPlayer(PlayerCommandExecutor { player, args ->
            //Retrieve the object from the map via the key and NOT the args[]
            player.setGameMode(gamemodes.get(key))
        })
        .register()
}    
/* ANCHOR_END: literalarguments2 */
}

fun multiliteralarguments() {
/* ANCHOR: multiliteralarguments */
CommandAPICommand("gamemode")
    .withArguments(MultiLiteralArgument("adventure", "creative", "spectator", "survival"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        // The literal string that the player enters IS available in the args[]
        when (args[0] as String) {
            "adventure" -> player.setGameMode(GameMode.ADVENTURE)
            "creative" -> player.setGameMode(GameMode.CREATIVE)
            "spectator" -> player.setGameMode(GameMode.SPECTATOR)
            "survival" -> player.setGameMode(GameMode.SURVIVAL)
            else -> throw CommandAPI.fail("Invalid gamemode ${args[0]}")
        }
    }) 
    .register()
/* ANCHOR_END: multiliteralarguments */
}

fun customarguments() {
/* ANCHOR: customarguments */
CommandAPICommand("tpworld")
    .withArguments(worldArgument("world"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.teleport((args[0] as World).spawnLocation)
    })
    .register()
/* ANCHOR_END: customarguments */
}

/* ANCHOR: customarguments2 */
// Function that returns our custom argument
fun worldArgument(nodeName : String) : Argument<World> {
    
    // Construct our CustomArgument that takes in a String input and returns a World object
    return CustomArgument<World, String>(StringArgument(nodeName), { info ->
        // Parse the world from our input
        val world = Bukkit.getWorld(info.input())
    
        if (world == null) {
            throw CustomArgumentException(MessageBuilder("Unknown world: ").appendArgInput())
        } else {
            return world
        }
    }).replaceSuggestions(ArgumentSuggestions.strings { info -> 
        // List of world names on the server
        Bukkit.worlds.map { it.name }.toTypedArray()
    }
}
/* ANCHOR_END: customarguments2 */

fun functionarguments() {
/* ANCHOR: functionarguments */
CommandAPICommand("runfunc")
    .withArguments(FunctionArgument("function"))
    .executes(CommandExecutor { sender, args ->
        val functions = args[0] as Array<FunctionWrapper>
        for (function in functions) {
            function.run() // The command executor in this case is 'sender'
        }
    })
    .register()
/* ANCHOR_END: functionarguments */
}

fun functionarguments2() {
/* ANCHOR: functionarguments2 */
CommandAPICommand("runfunction")
    .withArguments(FunctionArgument("function"))
    .executes(CommandExecutor { sender, args ->
        val functions = args[0] as Array<FunctionWrapper>

        //Run all functions in our FunctionWrapper[]
        for (function in functions) {
            function.run()
        }
    })
    .register()
/* ANCHOR_END: functionarguments2 */
}

fun permissions() {
/* ANCHOR: permissions */
// Register the /god command with the permission node "command.god"
CommandAPICommand("god")
    .withPermission(CommandPermission.fromString("command.god"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setInvulnerable(true)
    })
    .register()
/* ANCHOR_END: permissions */

/* ANCHOR: permissions2 */
//Register the /god command with the permission node "command.god", without creating a CommandPermission
CommandAPICommand("god")
    .withPermission("command.god")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setInvulnerable(true)
    })
    .register()
/* ANCHOR_END: permissions2 */
}

fun permissions3_1() {
/* ANCHOR: permissions3_1 */
// Register /kill command normally. Since no permissions are applied, anyone can run this command
CommandAPICommand("kill")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setHealth(0)
    })
    .register()
/* ANCHOR_END: permissions3_1 */
}

fun permissions3_2() {
/* ANCHOR: permissions3_2 */
// Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
CommandAPICommand("kill")
    .withArguments(PlayerArgument("target").withPermission(CommandPermission.OP))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        (args[0] as Player).setHealth(0)
    })
    .register()
/* ANCHOR_END: permissions3_2 */
}

fun aliases() {
/* ANCHOR: aliases */
CommandAPICommand("getpos")
    // Declare your aliases
    .withAliases("getposition", "getloc", "getlocation", "whereami")

    // Declare your implementation
    .executesEntity(EntityCommandExecutor { entity, args ->
        val loc = entity.location
        entity.sendMessage("You are at ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
    })
    .executesCommandBlock(BlockCommandExecutor { block, args ->
        val loc = block.block.location
        block.sendMessage("You are at ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
    })

    // Register the command
    .register()
/* ANCHOR_END: aliases */
}

fun normalcommandexecutors() {
/* ANCHOR: normalcommandexecutors */
CommandAPICommand("suicide")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setHealth(0)
    })
    .register()
/* ANCHOR_END: normalcommandexecutors */
}

fun normalcommandexecutors() {
/* ANCHOR: normalcommandexecutors2 */
CommandAPICommand("suicide")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setHealth(0)
    })
    .executesEntity(EntityCommandExecutor { entity, args ->
        entity.world.createExplosion(entity.location, 4)
        entity.remove()
    })
    .register()
/* ANCHOR_END: normalcommandexecutors2 */
}

// TODO: Why do we have two normalcommandexecutors3 ?!?!?!
fun normalcommandexecutors3() {
/* ANCHOR: normalcommandexecutors3 */
CommandAPICommand("suicide")
    .executes(CommandExecutor { sender, args ->
        val entity = if (sender is ProxiedCommandSender) proxy.getCallee() else sender // TODO: as LivingEntity
        entity.setHealth(0)
    }, ExecutorType.PLAYER, ExecutorType.PROXY)
    .register()
/* ANCHOR_END: normalcommandexecutors3 */
}

@Suppress("deprecation")
fun normalcommandexecutors3_1() {
/* ANCHOR: normalcommandexecutors3_1 */
//Create our command
CommandAPICommand("broadcastmsg")
    .withArguments(GreedyStringArgument("message")) // The arguments
    .withAliases("broadcast", "broadcastmessage")       // Command aliases
    .withPermission(CommandPermission.OP)               // Required permissions
    .executes(CommandExecutor { sender, args ->
        String message = (String) args[0]
        Bukkit.getServer().broadcastMessage(message)
    })
    .register()
/* ANCHOR_END: normalcommandexecutors3_1 */
}

fun proxysender() {
/* ANCHOR: proxysender */
CommandAPICommand("killme")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setHealth(0)
    })
    .register()
/* ANCHOR_END: proxysender */
}

{
/* ANCHOR: proxysender2 */
CommandAPICommand("killme")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setHealth(0)
    })
    .executesProxy((proxy, args) -> {
        //Check if the callee (target) is an Entity and kill it
        if (proxy.getCallee() instanceof LivingEntity target) {
            target.setHealth(0)
        }
    })
    .register()
/* ANCHOR_END: proxysender2 */
}

{
/* ANCHOR: nativesender */
CommandAPICommand("break")
    .executesNative(CommandExecutor { sender, args ->
        Location location = sender.getLocation()
        if (location != null) {
            location.getBlock().breakNaturally()
        }
    })
    .register()
/* ANCHOR_END: nativesender */
}

{
/* ANCHOR: resultingcommandexecutor */
CommandAPICommand("randnum")
    .executes(CommandExecutor { sender, args ->
        return Random().nextInt()
    })
    .register()
/* ANCHOR_END: resultingcommandexecutor */
}

{
/* ANCHOR: resultingcommandexecutor2 */
//Register random number generator command from 1 to 99 (inclusive)
CommandAPICommand("randomnumber")
    .executes(CommandExecutor { sender, args ->
        return ThreadLocalRandom.current().nextInt(1, 100) //Returns random number from 1 <= x < 100
    })
    .register()
/* ANCHOR_END: resultingcommandexecutor2 */
}

@Suppress("deprecation")
fun resultingcommandexecutor3(){
/* ANCHOR: resultingcommandexecutor3 */
// Register reward giving system for a target player
CommandAPICommand("givereward")
    .withArguments(EntitySelectorArgument<Player>("target", EntitySelector.ONE_PLAYER))
    .executes(CommandExecutor { sender, args ->
        Player player = (Player) args[0]
        player.getInventory().addItem(ItemStack(Material.DIAMOND, 64))
        Bukkit.broadcastMessage(player.getName() + " won a rare 64 diamonds from a loot box!")
    })
    .register()
/* ANCHOR_END: resultingcommandexecutor3 */
}

{
/* ANCHOR: commandfailures */
// Array of fruit
String[] fruit = String[] {"banana", "apple", "orange"}

// Register the command
CommandAPICommand("getfruit")
    .withArguments(StringArgument("item").replaceSuggestions(ArgumentSuggestions.strings(fruit)))
    .executes(CommandExecutor { sender, args ->
        String inputFruit = (String) args[0]
        
        if (Arrays.stream(fruit).anyMatch(inputFruit::equals)) {
            // Do something with inputFruit
        } else {
            // The sender's input is not in the list of fruit
            throw CommandAPI.fail("That fruit doesn't exist!")
        }
    })
    .register()
/* ANCHOR_END: commandfailures */
}

{
/* ANCHOR: argumentsyntax1 */
CommandAPICommand("mycommand")
    .withArguments(StringArgument("arg0"))
    .withArguments(StringArgument("arg1"))
    .withArguments(StringArgument("arg2"))
    // And so on
/* ANCHOR_END: argumentsyntax1 */
    

/* ANCHOR: argumentsyntax2 */
CommandAPICommand("mycommand")
    .withArguments(StringArgument("arg0"), StringArgument("arg1"), StringArgument("arg2"))
    // And so on
/* ANCHOR_END: argumentsyntax2 */
    

/* ANCHOR: argumentsyntax3 */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(StringArgument("arg0"))
arguments.add(StringArgument("arg1"))
arguments.add(StringArgument("arg2"))

CommandAPICommand("mycommand")
    .withArguments(arguments)
    // And so on
/* ANCHOR_END: argumentsyntax3 */
    
}

{
/* ANCHOR: argumentkillcmd */
CommandAPICommand("kill")
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.setHealth(0)
    })
    .register()
/* ANCHOR_END: argumentkillcmd */

/* ANCHOR: argumentkillcmd2 */
// Register our second /kill <target> command
CommandAPICommand("kill")
    .withArguments(PlayerArgument("target"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        ((Player) args[0]).setHealth(0)
    })
    .register()
/* ANCHOR_END: argumentkillcmd2 */
}

@Suppress("unused")
public fun argumentCasting() {
/* ANCHOR: argumentcasting */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(StringArgument("arg0"))
arguments.add(PotionEffectArgument("arg1"))
arguments.add(LocationArgument("arg2"))

CommandAPICommand("cmd")
    .withArguments(arguments)
    .executes(CommandExecutor { sender, args ->
        String stringArg = (String) args[0]
        PotionEffectType potionArg = (PotionEffectType) args[1]
        Location locationArg = (Location) args[2]
    })
    .register()
/* ANCHOR_END: argumentcasting */
}

{
/* ANCHOR: requirements */
CommandAPICommand("repair")
    .withRequirement(sender -> ((Player) sender).getLevel() >= 30)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        
        //Repair the item back to full durability
        ItemStack is = player.getInventory().getItemInMainHand()
        ItemMeta itemMeta = is.getItemMeta()
        if (itemMeta instanceof Damageable) {
            ((Damageable) itemMeta).setDamage(0)
            is.setItemMeta(itemMeta)
        }
        
        // Subtract 30 levels
        player.setLevel(player.getLevel() - 30)
    })
    .register()
/* ANCHOR_END: requirements */
}

{
/* ANCHOR: requirementsmap */
Map<UUID, String> partyMembers = HashMap<>()
/* ANCHOR_END: requirementsmap */

/* ANCHOR: requirements2 */
List<Argument<?>> arguments = ArrayList<>()

// The "create" literal, with a requirement that a player must have a party
arguments.add(LiteralArgument("create")
    .withRequirement(sender -> !partyMembers.containsKey(((Player) sender).getUniqueId()))
)

arguments.add(StringArgument("partyName"))
/* ANCHOR_END: requirements2 */

/* ANCHOR: requirements3 */
CommandAPICommand("party")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        
        //Get the name of the party to create
        String partyName = (String) args[0]
        
        partyMembers.put(player.getUniqueId(), partyName)
    })
    .register()
/* ANCHOR_END: requirements3 */

/* ANCHOR: requirementstp */
/* ANCHOR: requirements4 */
arguments = ArrayList<>()
arguments.add(LiteralArgument("tp")
    .withRequirement(sender -> partyMembers.containsKey(((Player) sender).getUniqueId()))
)
/* ANCHOR_END: requirementstp */

arguments.add(PlayerArgument("player")
    .replaceSafeSuggestions(SafeSuggestions.suggest(info -> {
        
        //Store the list of party members to teleport to
        List<Player> playersToTeleportTo = ArrayList<>()
        
        String partyName = partyMembers.get(((Player) info.sender()).getUniqueId())
        
        //Find the party members
        for (UUID uuid : partyMembers.keySet()) {
            
            //Ignore yourself
            if (uuid.equals(((Player) info.sender()).getUniqueId())) {
                continue
            } else {
                //If the party member is in the same party as you
                if (partyMembers.get(uuid).equals(partyName)) {
                    Player target = Bukkit.getPlayer(uuid)
                    if (target.isOnline()) {
                        //Add them if they are online
                        playersToTeleportTo.add(target)
                    }
                }
            }
        }
        
        return playersToTeleportTo.toArray(Player[0])
    })))
/* ANCHOR_END: requirements4 */

/* ANCHOR: requirements5 */
CommandAPICommand("party")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Player target = (Player) args[0]
        player.teleport(target)
    })
    .register()
/* ANCHOR_END: requirements5 */

/* ANCHOR: updatingrequirements */
CommandAPICommand("party")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        
        //Get the name of the party to create
        String partyName = (String) args[0]
        
        partyMembers.put(player.getUniqueId(), partyName)
        
        CommandAPI.updateRequirements(player)
    })
    .register()
/* ANCHOR_END: updatingrequirements */
}

{
/* ANCHOR: multiplerequirements */
CommandAPICommand("someCommand")
    .withRequirement(sender -> ((Player) sender).getLevel() >= 30)
    .withRequirement(sender -> ((Player) sender).getInventory().contains(Material.DIAMOND_PICKAXE))
    .withRequirement(sender -> ((Player) sender).isInvulnerable())
    .executesPlayer(PlayerCommandExecutor { player, args ->
        //Code goes here
    })
    .register()
/* ANCHOR_END: multiplerequirements */
}

{
Map<UUID, String> partyMembers = HashMap<>()
/* ANCHOR: predicatetips */
Predicate<CommandSender> testIfPlayerHasParty = sender -> {
    return partyMembers.containsKey(((Player) sender).getUniqueId())
}
/* ANCHOR_END: predicatetips */

/* ANCHOR: predicatetips2 */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(LiteralArgument("create").withRequirement(testIfPlayerHasParty.negate()))
arguments.add(StringArgument("partyName"))
/* ANCHOR_END: predicatetips2 */

/* ANCHOR: predicatetips3 */
arguments = ArrayList<>()
arguments.add(LiteralArgument("tp").withRequirement(testIfPlayerHasParty))
/* ANCHOR_END: predicatetips3 */
}

{
/* ANCHOR: converter2 */
JavaPlugin essentials = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Essentials")

// /speed <speed>
Converter.convert(essentials, "speed", IntegerArgument("speed", 0, 10))

// /speed <target>
Converter.convert(essentials, "speed", PlayerArgument("target"))

// /speed <walk/fly> <speed>
Converter.convert(essentials, "speed", 
    MultiLiteralArgument("walk", "fly"), 
    IntegerArgument("speed", 0, 10)
)

// /speed <walk/fly> <speed> <target>
Converter.convert(essentials, "speed", 
    MultiLiteralArgument("walk", "fly"), 
    IntegerArgument("speed", 0, 10), 
    PlayerArgument("target")
)
/* ANCHOR_END: converter2 */
}

@Suppress({ "rawtypes", "unchecked" })
fun a(){
/* ANCHOR: brigadier */
/* ANCHOR: declareliteral */
//Register literal "randomchance"
LiteralCommandNode randomChance = Brigadier.fromLiteralArgument(LiteralArgument("randomchance")).build()
/* ANCHOR_END: declareliteral */

/* ANCHOR: declarearguments */
//Declare arguments like normal
Argument<Integer> numeratorArgument = IntegerArgument("numerator", 0)
Argument<Integer> denominatorArgument = IntegerArgument("denominator", 1)

List<Argument> arguments = ArrayList<>()
arguments.add(numeratorArgument)
arguments.add(denominatorArgument)
/* ANCHOR_END: declarearguments */

//Get brigadier argument objects
/* ANCHOR: declareargumentbuilders */
ArgumentBuilder numerator = Brigadier.fromArgument(numeratorArgument)
/* ANCHOR: declarefork */
ArgumentBuilder denominator = Brigadier.fromArgument(denominatorArgument)
/* ANCHOR_END: declareargumentbuilders */
    //Fork redirecting to "execute" and state our predicate
    .fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate(CommandExecutor { sender, args ->
        //Parse arguments like normal
        int num = (int) args[0]
        int denom = (int) args[1]
        
        //Return boolean with a num/denom chance
        return Math.ceil(Math.random() * (double) denom) <= (double) num
    }, arguments))
/* ANCHOR_END: declarefork */

/* ANCHOR: declarerandomchance */
//Add <numerator> <denominator> as a child of randomchance
randomChance.addChild(numerator.then(denominator).build())
/* ANCHOR_END: declarerandomchance */

/* ANCHOR: injectintoroot */
//Add (randomchance <numerator> <denominator>) as a child of (execute -> if)
Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance)
/* ANCHOR_END: injectintoroot */
/* ANCHOR_END: brigadier */
}

{

/* ANCHOR: subcommandspart */
CommandAPICommand groupAdd = CommandAPICommand("add")
    .withArguments(StringArgument("permission"))
    .withArguments(StringArgument("groupName"))
    .executes(CommandExecutor { sender, args ->
        //perm group add code
    })
/* ANCHOR_END: subcommandspart */
/* ANCHOR: subcommands */
CommandAPICommand groupRemove = CommandAPICommand("remove")
    .withArguments(StringArgument("permission"))
    .withArguments(StringArgument("groupName"))
    .executes(CommandExecutor { sender, args ->
        //perm group remove code
    })

CommandAPICommand group = CommandAPICommand("group")
    .withSubcommand(groupAdd)
    .withSubcommand(groupRemove)
/* ANCHOR_END: subcommands */
/* ANCHOR: subcommandsend */
CommandAPICommand("perm")
    .withSubcommand(group)
    .register()
/* ANCHOR_END: subcommandsend */
/* ANCHOR: subcommands1 */
CommandAPICommand("perm")
    .withSubcommand(CommandAPICommand("group")
        .withSubcommand(CommandAPICommand("add")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("groupName"))
            .executes(CommandExecutor { sender, args ->
                //perm group add code
            })
        )
        .withSubcommand(CommandAPICommand("remove")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("groupName"))
            .executes(CommandExecutor { sender, args ->
                //perm group remove code
            })
        )
    )
    .withSubcommand(CommandAPICommand("user")
        .withSubcommand(CommandAPICommand("add")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("userName"))
            .executes(CommandExecutor { sender, args ->
                //perm user add code
            })
        )
        .withSubcommand(CommandAPICommand("remove")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("userName"))
            .executes(CommandExecutor { sender, args ->
                //perm user remove code
            })
        )
    )
    .register()
/* ANCHOR_END: subcommands1 */
}

@Suppress("deprecation")
fun help() {
/* ANCHOR: help */
CommandAPICommand("mycmd")
    .withShortDescription("Says hi")
    .withFullDescription("Broadcasts hi to everyone on the server")
    .executes(CommandExecutor { sender, args ->
        Bukkit.broadcastMessage("Hi!")
    })
    .register()
/* ANCHOR_END: help */

/* ANCHOR: help2 */
CommandAPICommand("mycmd")
    .withHelp("Says hi", "Broadcasts hi to everyone on the server")
    .executes(CommandExecutor { sender, args ->
        Bukkit.broadcastMessage("Hi!")
    })
    .register()
/* ANCHOR_END: help2 */
}

{
    //NOTE: This example isn't used!
/* ANCHOR: anglearguments */
CommandAPICommand("yaw")
    .withArguments(AngleArgument("amount"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Location newLocation = player.getLocation()
        newLocation.setYaw((float) args[0])
        player.teleport(newLocation)
    })
    .register()
/* ANCHOR_END: anglearguments */
}

{
/* ANCHOR: listed */
CommandAPICommand("mycommand")
    .withArguments(PlayerArgument("player"))
    .withArguments(IntegerArgument("value").setListed(false))
    .withArguments(GreedyStringArgument("message"))
    .executes(CommandExecutor { sender, args ->
        // args == [player, message]
        Player player = (Player) args[0]
        String message = (String) args[1] //Note that this is args[1] and NOT args[2]
        player.sendMessage(message)
    })
    .register()
/* ANCHOR_END: listed */
}

{
/* ANCHOR: Tooltips1 */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(StringArgument("emote")
    .replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(info -> IStringTooltip[] {
            StringTooltip.ofString("wave", "Waves at a player"),
            StringTooltip.ofString("hug", "Gives a player a hug"),
            StringTooltip.ofString("glare", "Gives a player the death glare")
        }
    ))
)
arguments.add(PlayerArgument("target"))
/* ANCHOR_END: Tooltips1 */
/* ANCHOR: Tooltips2 */
CommandAPICommand("emote")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        String emote = (String) args[0]
        Player target = (Player) args[1]
        
        switch(emote) {
        case "wave":
            target.sendMessage(player.getName() + " waves at you!")
            break
        case "hug":
            target.sendMessage(player.getName() + " hugs you!")
            break
        case "glare":
            target.sendMessage(player.getName() + " gives you the death glare...")
            break
        }
    })
    .register()
/* ANCHOR_END: Tooltips2 */
}

{
/* ANCHOR: Tooltips4 */
CustomItem[] customItems = CustomItem[] {
    CustomItem(ItemStack(Material.DIAMOND_SWORD), "God sword", "A sword from the heavens"),
    CustomItem(ItemStack(Material.PUMPKIN_PIE), "Sweet pie", "Just like grandma used to make")
}
    
CommandAPICommand("giveitem")
    .withArguments(StringArgument("item").replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(customItems))) // We use customItems[] as the input for our suggestions with tooltips
    .executesPlayer(PlayerCommandExecutor { player, args ->
        String itemName = (String) args[0]
        
        //Give them the item
        for (CustomItem item : customItems) {
            if (item.getName().equals(itemName)) {
                player.getInventory().addItem(item.getItem())
                break
            }
        }
    })
    .register()
/* ANCHOR_END: Tooltips4 */
}

{
/* ANCHOR: SafeTooltips */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(LocationArgument("location")
    .replaceSafeSuggestions(SafeSuggestions.tooltips(info -> {
        // We know the sender is a player if we use .executesPlayer()
        Player player = (Player) info.sender()
        return Tooltip.arrayOf(
            Tooltip.ofString(player.getWorld().getSpawnLocation(), "World spawn"),
            Tooltip.ofString(player.getBedSpawnLocation(), "Your bed"),
            Tooltip.ofString(player.getTargetBlockExact(256).getLocation(), "Target block")
        )
    })))
/* ANCHOR_END: SafeTooltips */
/* ANCHOR: SafeTooltips2 */
CommandAPICommand("warp")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.teleport((Location) args[0])
    })
    .register()
/* ANCHOR_END: SafeTooltips2 */
}

{
/* ANCHOR: ArgumentSuggestionsPrevious */
// Declare our arguments as normal
List<Argument<?>> arguments = ArrayList<>()
arguments.add(IntegerArgument("radius"))

// Replace the suggestions for the PlayerArgument.
// info.sender() refers to the command sender that is running this command
// info.previousArgs() refers to the Object[] of previously declared arguments (in this case, the IntegerArgument radius)
arguments.add(PlayerArgument("target").replaceSuggestions(ArgumentSuggestions.strings(info -> {

    // Cast the first argument (radius, which is an IntegerArgument) to get its value
    int radius = (int) info.previousArgs()[0]
    
    // Get nearby entities within the provided radius
    Player player = (Player) info.sender()
    Collection<Entity> entities = player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)
    
    // Get player names within that radius
    return entities.stream()
        .filter(e -> e.getType() == EntityType.PLAYER)
        .map(Entity::getName)
        .toArray(String[]::new)
})))
arguments.add(GreedyStringArgument("message"))

// Declare our command as normal
CommandAPICommand("localmsg")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Player target = (Player) args[1]
        String message = (String) args[2]
        target.sendMessage(message)
    })
    .register()
/* ANCHOR_END: ArgumentSuggestionsPrevious */
}

{
/* ANCHOR: ArgumentSuggestions2_2 */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(PlayerArgument("friend").replaceSuggestions(ArgumentSuggestions.strings(info ->
    Friends.getFriends(info.sender())
)))

CommandAPICommand("friendtp")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Player target = (Player) args[0]
        player.teleport(target)
    })
    .register()
/* ANCHOR_END: ArgumentSuggestions2_2 */
}

{
Map<String, Location> warps = HashMap<>()
/* ANCHOR: ArgumentSuggestions1 */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(StringArgument("world").replaceSuggestions(ArgumentSuggestions.strings( 
    "northland", "eastland", "southland", "westland"
)))

CommandAPICommand("warp")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        String warp = (String) args[0]
        player.teleport(warps.get(warp)) // Look up the warp in a map, for example
    })
    .register()
/* ANCHOR_END: ArgumentSuggestions1 */
}

@Suppress("deprecation")
fun SafeRecipeArguments() {
/* ANCHOR: SafeRecipeArguments */
// Create our itemstack
ItemStack emeraldSword = ItemStack(Material.DIAMOND_SWORD)
ItemMeta meta = emeraldSword.getItemMeta()
meta.setDisplayName("Emerald Sword")
meta.setUnbreakable(true)
emeraldSword.setItemMeta(meta)

// Create and register our recipe
ShapedRecipe emeraldSwordRecipe = ShapedRecipe(NamespacedKey(this, "emerald_sword"), emeraldSword)
emeraldSwordRecipe.shape(
    "AEA", 
    "AEA", 
    "ABA"
)
emeraldSwordRecipe.setIngredient('A', Material.AIR)
emeraldSwordRecipe.setIngredient('E', Material.EMERALD)
emeraldSwordRecipe.setIngredient('B', Material.BLAZE_ROD)
getServer().addRecipe(emeraldSwordRecipe)

// Omitted, more itemstacks and recipes
/* ANCHOR_END: SafeRecipeArguments */

/* ANCHOR: SafeRecipeArguments_2 */
// Safely override with the recipe we've defined
List<Argument<?>> arguments = ArrayList<>()
arguments.add(RecipeArgument("recipe").replaceSafeSuggestions(SafeSuggestions.suggest(info -> 
    Recipe[] { emeraldSwordRecipe, /* Other recipes here */ }
)))

// Register our command
CommandAPICommand("giverecipe")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        Recipe recipe = (Recipe) args[0]
        player.getInventory().addItem(recipe.getResult())
    })
    .register()
/* ANCHOR_END: SafeRecipeArguments_2 */
}

{
/* ANCHOR: SafeMobSpawnArguments */
EntityType[] forbiddenMobs = EntityType[] {EntityType.ENDER_DRAGON, EntityType.WITHER}
List<EntityType> allowedMobs = ArrayList<>(Arrays.asList(EntityType.values()))
allowedMobs.removeAll(Arrays.asList(forbiddenMobs)) // Now contains everything except enderdragon and wither
/* ANCHOR_END: SafeMobSpawnArguments */

/* ANCHOR: SafeMobSpawnArguments_2 */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(EntityTypeArgument("mob").replaceSafeSuggestions(SafeSuggestions.suggest(
    info -> {
        if (info.sender().isOp()) {
            // All entity types
            return EntityType.values()
        } else {
            // Only allowedMobs
            return allowedMobs.toArray(EntityType[0])
        }
    })
))
/* ANCHOR_END: SafeMobSpawnArguments_2 */

/* ANCHOR: SafeMobSpawnArguments_3 */
CommandAPICommand("spawnmob")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        EntityType entityType = (EntityType) args[0]
        player.getWorld().spawnEntity(player.getLocation(), entityType)
    })
    .register()
/* ANCHOR_END: SafeMobSpawnArguments_3 */
}

{
/* ANCHOR: SafePotionArguments */
List<Argument<?>> arguments = ArrayList<>()
arguments.add(EntitySelectorArgument<Player>("target", EntitySelector.ONE_PLAYER))
arguments.add(PotionEffectArgument("potioneffect").replaceSafeSuggestions(SafeSuggestions.suggest(
    info -> {
        Player target = (Player) info.previousArgs()[0]
        
        // Convert PotionEffect[] into PotionEffectType[]
        return target.getActivePotionEffects().stream()
            .map(PotionEffect::getType)
            .toArray(PotionEffectType[]::new)
    })
))
/* ANCHOR_END: SafePotionArguments */

/* ANCHOR: SafePotionArguments_2 */
CommandAPICommand("removeeffect")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
    	Player target = (Player) args[0]
        PotionEffectType potionEffect = (PotionEffectType) args[1]
        target.removePotionEffect(potionEffect)
    })
    .register()
/* ANCHOR_END: SafePotionArguments_2 */
}

{
    // A really simple example showing how you can use the new suggestion system
	final String[] fruits = String[] { "Apple", "Apricot", "Artichoke", "Asparagus", "Atemoya", "Avocado",
			"Bamboo Shoots", "Banana", "Bean Sprouts", "Beans", "Beets", "Blackberries", "Blueberries", "Boniato",
			"Boysenberries", "Broccoflower", "Broccoli", "Cabbage", "Cactus Pear", "Cantaloupe", "Carambola", "Carrots",
			"Cauliflower", "Celery", "Chayote", "Cherimoya", "Cherries", "Coconuts", "Corn", "Cranberries", "Cucumber",
			"Dates", "Eggplant", "Endive", "Escarole", "Feijoa", "Fennel", "Figs", "Garlic", "Gooseberries",
			"Grapefruit", "Grapes", "Greens", "Guava", "Hominy", "Jicama", "Kale", "Kiwifruit", "Kohlrabi", "Kumquat",
			"Leeks", "Lemons", "Lettuce", "Lima Beans", "Limes", "Longan", "Loquat", "Lychee", "Madarins", "Malanga",
			"Mangos", "Mulberries", "Mushrooms", "Napa", "Nectarines", "Okra", "Onion", "Oranges", "Papayas", "Parsnip",
			"Peaches", "Pears", "Peas", "Peppers", "Persimmons", "Pineapple", "Plantains", "Plums", "Pomegranate",
			"Potatoes", "Prunes", "Pummelo", "Pumpkin", "Quince", "Radicchio", "Radishes", "Raisins", "Raspberries",
			"Rhubarb", "Rutabaga", "Shallots", "Spinach", "Sprouts", "Squash", "Strawberries", "Tangelo", "Tangerines",
			"Tomatillo", "Tomato", "Turnip", "Watercress", "Watermelon", "Yams", "Zucchini" }
    
    CommandAPICommand("concept")
        .withArguments(StringArgument("text"))
        .withArguments(StringArgument("input").replaceSuggestions(ArgumentSuggestions.strings(info -> {
            System.out.println(info.currentArg()) // partially typed argument
            System.out.println(info.currentInput()) // current input (includes the /)
            return Arrays.stream(fruits).filter(s -> s.toLowerCase().startsWith(info.currentArg().toLowerCase())).toArray(String[]::new)
        })))
        .withArguments(IntegerArgument("int"))
        .executes(CommandExecutor { sender, args ->
            //stuff
        })
        .register()
}

{
/* ANCHOR: CommandAPIConfigSilent */
CommandAPI.onLoad(CommandAPIConfig().silentLogs(true))
/* ANCHOR_END: CommandAPIConfigSilent */
}

{

JavaPlugin plugin = JavaPlugin() {}
/* ANCHOR: asyncreadfile */
CommandAPICommand("setconfig")
    .withArguments(StringArgument("key").replaceSuggestions(ArgumentSuggestions.stringsAsync(info -> {
        return CompletableFuture.supplyAsync(() -> {
            return plugin.getConfig().getKeys(false).toArray(String[0])
        })
    })))
    .withArguments(TextArgument("value"))
    .executes(CommandExecutor { sender, args ->
        String key = (String) args[0]
        String value = (String) args[1]
        plugin.getConfig().set(key, value)
    })
    .register()
/* ANCHOR_END: asyncreadfile */
	
}

@Suppress("unchecked")
fun listargument() {

/* ANCHOR: ListArgument_MultiGive */
CommandAPICommand("multigive")
    .withArguments(IntegerArgument("amount", 1, 64))
    .withArguments(ListArgumentBuilder<Material>("materials")
        .withList(List.of(Material.values()))
        .withMapper(material -> material.name().toLowerCase())
        .build()
    )
    .executesPlayer(PlayerCommandExecutor { player, args ->
        int amount = (int) args[0]
        List<Material> theList = (List<Material>) args[1]
        
        for (Material item : theList) {
            player.getInventory().addItem(ItemStack(item, amount))
        }
    })
    .register()
/* ANCHOR_END: ListArgument_MultiGive */
}

@Suppress({ "unchecked" })
fun brigadierargs() {

/* ANCHOR: BrigadierSuggestions1 */
ArgumentSuggestions commandSuggestions = (info, builder) -> {
    // The current argument, which is a full command
    String arg = info.currentArg()

    // Identify the position of the current argument
    int start
    if (arg.contains(" ")) {
        // Current argument contains spaces - it starts after the last space and after the start of this argument.
        start = builder.getStart() + arg.lastIndexOf(' ') + 1
    } else {
        // Input starts at the start of this argument
        start = builder.getStart()
    }
    
    // Parse command using brigadier
    ParseResults<?> parseResults = Brigadier.getCommandDispatcher()
        .parse(info.currentArg(), Brigadier.getBrigadierSourceFromCommandSender(info.sender()))
    
    // Intercept any parsing errors indicating an invalid command
    for (CommandSyntaxException exception : parseResults.getExceptions().values()) {
        // Raise the error, with the cursor offset to line up with the argument
        throw CommandSyntaxException(exception.getType(), exception.getRawMessage(), exception.getInput(), exception.getCursor() + start)
    }

    return Brigadier
        .getCommandDispatcher()
        .getCompletionSuggestions(parseResults)
        .thenApply((suggestionsObject) -> {
            // Brigadier's suggestions
            Suggestions suggestions = (Suggestions) suggestionsObject

            return Suggestions(
                // Offset the index range of the suggestions by the start of the current argument
                StringRange(start, start + suggestions.getRange().getLength()),
                // Copy the suggestions
                suggestions.getList()
            )
        })
}
/* ANCHOR_END: BrigadierSuggestions1 */

/* ANCHOR: BrigadierSuggestions2 */
CommandAPICommand("commandargument")
    .withArguments(GreedyStringArgument("command").replaceSuggestions(commandSuggestions))
    .executes(CommandExecutor { sender, args ->
        // Run the command using Bukkit.dispatchCommand()
        Bukkit.dispatchCommand(sender, (String) args[0])
    }).register()
/* ANCHOR_END: BrigadierSuggestions2 */
}

{
CommandTree("treeexample")
	//Set the aliases as you normally would 
	.withAliases("treealias")
	//Set an executor on the command itself
	.executes(CommandExecutor { sender, args ->
		sender.sendMessage("Root with no arguments")
	})
	//Create a new branch starting with a the literal 'integer'
	.then(LiteralArgument("integer")
		//Execute on the literal itself
		.executes(CommandExecutor { sender, args ->
			sender.sendMessage("Integer Branch with no arguments")
		})
		//Create a further branch starting with an integer argument, which executes a command
		.then(IntegerArgument("integer").executes(CommandExecutor { sender, args ->
			sender.sendMessage("Integer Branch with integer argument: " + args[0])
		})))
	.then(LiteralArgument("biome")
		.executes(CommandExecutor { sender, args ->
			sender.sendMessage("Biome Branch with no arguments")
		})
		.then(BiomeArgument("biome").executes(CommandExecutor { sender, args ->
			sender.sendMessage("Biome Branch with biome argument: " + args[0])
		})))
	.then(LiteralArgument("string")
		.executes(CommandExecutor { sender, args ->
			sender.sendMessage("String Branch with no arguments")
		})
		.then(StringArgument("string").executes(CommandExecutor { sender, args ->
			sender.sendMessage("String Branch with string argument: " + args[0])
		})))
	//Call register to finish as you normally would
	.register()

/* ANCHOR: CommandTree_sayhi1 */
CommandTree("sayhi")
    .executes(CommandExecutor { sender, args ->
        sender.sendMessage("Hi!")
    })
    .then(PlayerArgument("target")
        .executes(CommandExecutor { sender, args ->
            Player target = (Player) args[0]
            target.sendMessage("Hi")
        }))
    .register()
/* ANCHOR_END: CommandTree_sayhi1 */
}

@Suppress("deprecation")
public fun signedit(){

/* ANCHOR: CommandTree_signedit */
CommandTree("signedit")
    .then(LiteralArgument("set")
        .then(IntegerArgument("line_number", 1, 4)
            .then(GreedyStringArgument("text")
                .executesPlayer(PlayerCommandExecutor { player, args ->
                    // /signedit set <line_number> <text>
                    Sign sign = getTargetSign(player)
                    int line_number = (int) args[0]
                    String text = (String) args[1]
                    sign.setLine(line_number - 1, text)
                    sign.update(true)
                 }))))
    .then(LiteralArgument("clear")
        .then(IntegerArgument("line_number", 1, 4)
            .executesPlayer(PlayerCommandExecutor { player, args ->
                // /signedit clear <line_number>
                Sign sign = getTargetSign(player)
                int line_number = (int) args[0]
                sign.setLine(line_number - 1, "")
                sign.update(true)
            })))
    .then(LiteralArgument("copy")
        .then(IntegerArgument("line_number", 1, 4)
            .executesPlayer(PlayerCommandExecutor { player, args ->
                // /signedit copy <line_number>
                Sign sign = getTargetSign(player)
                int line_number = (int) args[0]
                player.setMetadata("copied_sign_text", FixedMetadataValue(this, sign.getLine(line_number - 1)))
            })))
    .then(LiteralArgument("paste")
        .then(IntegerArgument("line_number", 1, 4)
            .executesPlayer(PlayerCommandExecutor { player, args ->
                // /signedit copy <line_number>
                Sign sign = getTargetSign(player)
                int line_number = (int) args[0]
                sign.setLine(line_number - 1, player.getMetadata("copied_sign_text").get(0).asString())
                sign.update(true)
            })))
    .register()
/* ANCHOR_END: CommandTree_signedit */
}

public Sign getTargetSign(Player player) throws WrapperCommandSyntaxException {
	Block block = player.getTargetBlock(null, 256)
	if (block != null && block.getState() instanceof Sign sign) {
		return sign
	} else {
		throw CommandAPI.fail("You're not looking at a sign!")
	}
}


} // Examples class end ////////////////////////////////////////////////////////////////////

/* ANCHOR: ArgumentSuggestions2_1 */
class Friends {
    
    static Map<UUID, String[]> friends = HashMap<>()
    
    public static String[] getFriends(CommandSender sender) {
        if (sender instanceof Player player) {
            //Look up friends in a database or file
            return friends.get(player.getUniqueId())
        } else {
            return String[0]
        }
    }
}
/* ANCHOR_END: ArgumentSuggestions2_1 */


/* ANCHOR: Tooltips3 */
@Suppress("deprecation")
class CustomItem implements IStringTooltip {

    private ItemStack itemstack
    private String name
    
	public CustomItem(ItemStack itemstack, String name, String lore) {
        ItemMeta meta = itemstack.getItemMeta()
        meta.setDisplayName(name)
        meta.setLore(Arrays.asList(lore))
        itemstack.setItemMeta(meta)
        this.itemstack = itemstack
        this.name = name
    }
    
    public String getName() {
        return this.name
    }
    
    public ItemStack getItem() {
        return this.itemstack
    }
    
    @Override
    public String getSuggestion() {
        return this.itemstack.getItemMeta().getDisplayName()
    }

    @Override
    public Message getTooltip() {
        return Tooltip.messageFromString(this.itemstack.getItemMeta().getLore().get(0))
    }
    
}
/* ANCHOR_END: Tooltips3 */

/* ANCHOR: functionregistration */
class Main extends JavaPlugin {

    @Override
    public fun onLoad() {
        //Commands which will be used in Minecraft functions are registered here

        CommandAPICommand("killall")
            .executes(CommandExecutor { sender, args ->
                //Kills all enemies in all worlds
                Bukkit.getWorlds().forEach(w -> w.getLivingEntities().forEach(e -> e.setHealth(0)))
            })
            .register()
    }
    
    @Override
    public fun onEnable() {
        //Register all other commands here
    } 
}
/* ANCHOR_END: functionregistration */

/* ANCHOR: shading */
class MyPlugin extends JavaPlugin {

    @Override
    public fun onLoad() {
        CommandAPI.onLoad(CommandAPIConfig().verboseOutput(true)) //Load with verbose output
        
        CommandAPICommand("ping")
            .executes(CommandExecutor { sender, args ->
                sender.sendMessage("pong!")
            })
            .register()
    }
    
    @Override
    public fun onEnable() {
        CommandAPI.onEnable(this)
        
        //Register commands, listeners etc.
    }

}
/* ANCHOR_END: shading */

/* ANCHOR: converter */
class YourPlugin extends JavaPlugin {
    
    @Override
    public fun onEnable() {
        Converter.convert((JavaPlugin) Bukkit.getPluginManager().getPlugin("TargetPlugin"))
        //Other code goes here...
    }
    
}
/* ANCHOR_END: converter */

}