package dev.jorel.commandapi.examples.kotlin

import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.Message
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.tree.LiteralCommandNode
import de.tr7zw.changeme.nbtapi.NBTContainer
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException
import dev.jorel.commandapi.executors.*
import dev.jorel.commandapi.wrappers.*
import dev.jorel.commandapi.wrappers.Rotation
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.World.Environment
import org.bukkit.advancement.Advancement
import org.bukkit.block.*
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandSender
import org.bukkit.command.ProxiedCommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.*
import org.bukkit.inventory.ComplexRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.inventory.meta.Damageable
import org.bukkit.loot.LootTable
import org.bukkit.loot.Lootable
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.util.EulerAngle
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate
import kotlin.random.Random

class Examples : JavaPlugin() {

fun commandregistration() {
/* ANCHOR: commandregistration */
// Create our command
CommandAPICommand("broadcastmsg")
    .withArguments(GreedyStringArgument("message")) // The arguments
    .withAliases("broadcast", "broadcastmessage")   // Command aliases
    .withPermission(CommandPermission.OP)           // Required permissions
    .executes(CommandExecutor { sender, args ->
        val message = args[0] as String
        Bukkit.getServer().broadcastMessage(message)
    })
    .register()
/* ANCHOR_END: commandregistration */
}

fun commandunregistration() {
/* ANCHOR: commandunregistration */
// Unregister the gamemode command from the server (by force)
CommandAPI.unregister("gamemode", true)

// Register our new /gamemode, with survival, creative, adventure and spectator
CommandAPICommand("gamemode")
    .withArguments(MultiLiteralArgument("survival", "creative", "adventure", "spectator"))
    .executes(CommandExecutor { sender, args ->
        // Implementation of our /gamemode command
    })
    .register()
/* ANCHOR_END: commandunregistration */
}

fun booleanargs() {
/* ANCHOR: booleanargs */
// Load keys from config file
val configKeys: Array<String> = getConfig().getKeys(true).toTypedArray()

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
    // We want to target blocks in particular, so use BLOCK_POSITION
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
    .withArguments(EntitySelectorArgument.OneEntity("target"))
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
    .executes(CommandExecutor { _, args ->
        val player = args[0] as Player
        val arr = args[1] as Array<BaseComponent>

        // Create book
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.getItemMeta() as BookMeta
        meta.setTitle("Custom Book")
        meta.setAuthor(player.name)
        meta.spigot().setPages(arr)
        item.setItemMeta(meta)

        // Give player the book
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
    .executes(CommandExecutor { _, args ->
        val message = args[0] as Array<BaseComponent>

        // Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(*message)
    })
    .register()
/* ANCHOR_END: chatarguments */

/* ANCHOR: chatpreviewspigot */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").withPreview( { info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText: String = BaseComponent.toPlainText(*info.parsedInput() as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    }))
    .executesPlayer(PlayerCommandExecutor { _, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a BaseComponent[] by converting to plain text then to BaseComponent[]
        val plainText: String = BaseComponent.toPlainText(*args[0] as Array<BaseComponent>)
        val baseComponents: Array<BaseComponent> = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
        Bukkit.spigot().broadcast(*baseComponents)
    })
    .register()
/* ANCHOR_END: chatpreviewspigot */

/* ANCHOR: chatpreviewadventure */
CommandAPICommand("broadcast")
    .withArguments(AdventureChatArgument("message").withPreview( { info ->
        // Convert parsed Component to plain text
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    }))
    .executesPlayer(PlayerCommandExecutor { _, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a Component by converting to plain text then to Component
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(args[0] as Component)
        Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(plainText))
    })
    .register()
/* ANCHOR_END: chatpreviewadventure */

/* ANCHOR: chatpreviewspigotusepreview */
CommandAPICommand("broadcast")
    .withArguments(ChatArgument("message").usePreview(true).withPreview( { info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText = BaseComponent.toPlainText(*info.parsedInput() as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    }))
    .executesPlayer(PlayerCommandExecutor { _, args ->
        Bukkit.spigot().broadcast(*args[0] as Array<BaseComponent>)
    })
    .register()
/* ANCHOR_END: chatpreviewspigotusepreview */

/* ANCHOR: chatpreviewadventureusepreview */
CommandAPICommand("broadcast")
    .withArguments(AdventureChatArgument("message").usePreview(true).withPreview( { info ->
        // Convert parsed Component to plain text
        val plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    }))
    .executesPlayer(PlayerCommandExecutor { _, args ->
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
    .executes(CommandExecutor { _, args ->
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
    .executes(CommandExecutor { _, args ->
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
    // Using a collective entity selector to select multiple entities
    .withArguments(EntitySelectorArgument.ManyEntities("entities"))
    .executes(CommandExecutor { sender, args ->
        // Parse the argument as a collection of entities (as stated above in the documentation)
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
    .withArguments(IntegerArgument("amount", 1, 100)) // Prevent spawning too many entities
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
    // We want multiple players, so we use the ScoreHolderArgument.Multiple constructor
    .withArguments(ScoreHolderArgument.Multiple("players"))
    .executes(CommandExecutor { _, args ->
        // Get player names by casting to Collection<String>
        val players = args[0] as Collection<String>

        for (playerName in players) {
            Bukkit.getPlayer(playerName)?.inventory!!.addItem(ItemStack(Material.DIAMOND, 3))
        }
    })
    .register()
/* ANCHOR_END: scoreholderargument */
}

// fun scoreholderargument_2() {
// val args = arrayOf()
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
    .executes(CommandExecutor { _, args ->
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
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
    .executes(CommandExecutor { _, args ->
        // The ObjectArgument must be casted to a String
        val objectiveName = args[0] as String

        // An objective name can be turned into an Objective using getObjective(String)
        val objective = Bukkit.getScoreboardManager().mainScoreboard.getObjective(objectiveName)

        // Set display slot
        objective?.setDisplaySlot(DisplaySlot.SIDEBAR)
    })
    .register()
/* ANCHOR_END: objectiveargument */
}

fun objectivecriteriaarguments() {
/* ANCHOR: objectivecriteriaarguments */
CommandAPICommand("unregisterall")
    .withArguments(ObjectiveCriteriaArgument("objective criteria"))
    .executes(CommandExecutor { _, args ->
        val objectiveCriteria = args[0] as String
        val objectives = Bukkit.getScoreboardManager().mainScoreboard.getObjectivesByCriteria(objectiveCriteria)

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
    .executes(CommandExecutor { _, args ->
        // The TeamArgument must be casted to a String
        val teamName = args[0] as String

        // A team name can be turned into a Team using getTeam(String)
        val team = Bukkit.getScoreboardManager().mainScoreboard.getTeam(teamName)!!

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
    .executes(CommandExecutor { _, args ->
        val target = args[0] as Player
        val advancement = args[1] as Advancement

        // Award all criteria for the advancement
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
        targetBlock?.setType(blockdata.material)
        targetBlock?.state?.setBlockData(blockdata)
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

fun worldarguments() {
/* ANCHOR: worldarguments */
CommandAPICommand("unloadworld")
    .withArguments(WorldArgument("world"))
    .executes(CommandExecutor { sender, args ->
        val world = args[0] as World

        // Unload the world (and save the world's chunks)
        Bukkit.getServer().unloadWorld(world, true)
    })
    .register()
/* ANCHOR_END: worldarguments */
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
    .executes(CommandExecutor { _, args ->
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
    .executes(CommandExecutor { _, args ->
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
    .executes(CommandExecutor { _, args ->
        val target = args[0] as Player
        val potion = args[1] as PotionEffectType
        val duration = args[2] as Int
        val strength = args[3] as Int

        // Add the potion effect to the target player
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
    .executes(CommandExecutor { _, args ->
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

/* ANCHOR: soundarguments2 */
CommandAPICommand("sound")
    .withArguments(SoundArgument.NamespacedKey("sound"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.world.playSound(player.location, (args[0] as NamespacedKey).asString(), 100.0f, 1.0f)
    })
    .register()
/* ANCHOR_END: soundarguments2 */
}

fun timearg() {
/* ANCHOR: timearguments */
CommandAPICommand("bigmsg")
    .withArguments(TimeArgument("duration"))
    .withArguments(GreedyStringArgument("message"))
    .executes(CommandExecutor { _, args ->
        // Duration in ticks
        val duration = args[0] as Int
        val message = args[1] as String

        for (player in Bukkit.getOnlinePlayers()) {
            // Display the message to all players, with the default fade in/out times (10 and 20).
            player.sendTitle(message, "", 10, duration, 20)
        }
    })
    .register()
/* ANCHOR_END: timearguments */
}

fun blockpredicatearguments() {
/* ANCHOR: blockpredicatearguments */
val arguments = arrayOf<Argument<*>>(
    IntegerArgument("radius"),
    BlockPredicateArgument("fromBlock"),
    BlockStateArgument("toBlock"),
)
/* ANCHOR_END: blockpredicatearguments */

/* ANCHOR: blockpredicatearguments2 */
CommandAPICommand("replace")
    .withArguments(*arguments)
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
                    if (Math.sqrt((x * x + y * y + z * z).toDouble()) <= radius) {
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
        val predicate = args[0] as Predicate<ItemStack>

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
        .initializeNBTAPI(NBTContainer::class.java, ::NBTContainer)
    )
}
/* ANCHOR_END: nbtcompoundargumentonload */

}

@Suppress("unused")
fun nbtcompoundarguments() {
/* ANCHOR: nbtcompoundarguments */
CommandAPICommand("award")
    .withArguments(NBTCompoundArgument<NBTContainer>("nbt"))
    .executes(CommandExecutor { _, args ->
        val nbt = args[0] as NBTContainer

        // Do something with "nbt" here...
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
    .executes(CommandExecutor { _, args ->
        // This gives the variable "text" the contents of the TextArgument, and not the literal "hello"
        val text = args[0] as String
    })
    .register()
/* ANCHOR_END: literalarguments */
}

fun literalarguments2() {
/* ANCHOR: literalarguments2 */
// Create a map of gamemode names to their respective objects
val gamemodes = mapOf(
    "adventure" to GameMode.ADVENTURE,
    "creative" to GameMode.CREATIVE,
    "spectator" to GameMode.SPECTATOR,
    "survival" to GameMode.SURVIVAL
)

// Iterate over the map
for ((key, _) in gamemodes) {

    // Register the command as usual
    CommandAPICommand("changegamemode")
        .withArguments(LiteralArgument(key))
        .executesPlayer(PlayerCommandExecutor { player, _ ->
            // Retrieve the object from the map via the key and NOT the args[]
            player.setGameMode(gamemodes[key]!!)
        })
        .register()
}
/* ANCHOR_END: literalarguments2 */
}

fun literalArguments3() {
/* ANCHOR: literalarguments3 */
CommandAPICommand("mycommand")
    .withArguments(LiteralArgument.of("hello"))
    .withArguments(TextArgument("text"))
    .executes(CommandExecutor { _, args ->
        val text = args[0] as String
    })
    .register()

CommandAPICommand("mycommand")
    .withArguments(LiteralArgument.literal("hello"))
    .withArguments(TextArgument("text"))
    .executes(CommandExecutor { _, args ->
        val text = args[0] as String
    })
    .register()
/* ANCHOR_END: literalarguments3 */
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
fun worldArgument(nodeName: String): Argument<World> {

    // Construct our CustomArgument that takes in a String input and returns a World object
    return CustomArgument<World, String>(StringArgument(nodeName), { info ->
        // Parse the world from our input
        val world = Bukkit.getWorld(info.input())

        if (world == null) {
            throw CustomArgumentException(MessageBuilder("Unknown world: ").appendArgInput())
        } else {
            world
        }
    }).replaceSuggestions(ArgumentSuggestions.strings { _ ->
        // List of world names on the server
        Bukkit.getWorlds().map{ it.name }.toTypedArray()
    })
}
/* ANCHOR_END: customarguments2 */

fun functionarguments() {
/* ANCHOR: functionarguments */
CommandAPICommand("runfunc")
    .withArguments(FunctionArgument("function"))
    .executes(CommandExecutor { _, args ->
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
    .executes(CommandExecutor { _, args ->
        val functions = args[0] as Array<FunctionWrapper>

        // Run all functions in our FunctionWrapper[]
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
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setInvulnerable(true)
    })
    .register()
/* ANCHOR_END: permissions */

/* ANCHOR: permissions2 */
// Register the /god command with the permission node "command.god", without creating a CommandPermission
CommandAPICommand("god")
    .withPermission("command.god")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setInvulnerable(true)
    })
    .register()
/* ANCHOR_END: permissions2 */
}

fun permissions3_1() {
/* ANCHOR: permissions3_1 */
// Register /kill command normally. Since no permissions are applied, anyone can run this command
CommandAPICommand("kill")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setHealth(0.0)
    })
    .register()
/* ANCHOR_END: permissions3_1 */
}

fun permissions3_2() {
/* ANCHOR: permissions3_2 */
// Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
CommandAPICommand("kill")
    .withArguments(PlayerArgument("target").withPermission(CommandPermission.OP))
    .executesPlayer(PlayerCommandExecutor { _, args ->
        (args[0] as Player).setHealth(0.0)
    })
    .register()
/* ANCHOR_END: permissions3_2 */
}

fun permissions4_1() {
/* ANCHOR: permissions4_1 */
// /economy - requires the permission "economy.self" to exectue
CommandAPICommand("economy")
    .withPermission("economy.self")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        // send the executor their own balance here.
	})
    .register()

// /economy <target> - requires the permission "economy.other" to execute
CommandAPICommand("economy")
    .withPermission("economy.other") // The important part of this example
    .withArguments(PlayerArgument("target"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args.get(0) as Player
        // send the executor the targets balance here.
    })
    .register()

// /economy give <target> <amount> - requires the permission "economy.admin.give" to execute
CommandAPICommand("economy")
    .withPermission("economy.admin.give") // The important part of this example
    .withArguments(PlayerArgument("target"))
    .withArguments(DoubleArgument("amount"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args.get(0) as Player
        val amount = args.get(1) as Double
        // update the targets balance here
    })
    .register()

// /economy reset <target> - requires the permission "economy.admin.reset" to execute
CommandAPICommand("economy")
    .withPermission("economy.admin.reset") // The important part of this example
    .withArguments(PlayerArgument("target"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args.get(0) as Player
        // reset the targets balance here
    })
    .register()
/* ANCHOR_END: permissions4_1 */
}

fun permissions4_2() {
/* ANCHOR: permissions4_2 */
// /economy - requires the permission "economy.self" to execute
CommandAPICommand("economy")
    .withRequirement { sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.other") ||
        sender.hasPermission("economy")
    }
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        // send the executor their own balance here
    })
    .register()

// /economy <target> - requires the permission "economy.other" to execute
CommandAPICommand("economy")
    .withRequirement { sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.other")
    }
    .withArguments(PlayerArgument("target"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args.get(0) as Player
        // send the executor the targets balance here
    })
    .register()

// /economy give <target> <amount> - requires the permission "economy.admin.give" to execute
CommandAPICommand("economy")
    .withRequirement { sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.admin.*") ||
        sender.hasPermission("economy.admin.give")
    }
    .withArguments(PlayerArgument("target"))
    .withArguments(DoubleArgument("amount"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args.get(0) as Player
        val amount = args.get(1) as Double
        // update the targets balance here
    })
    .register()

// /economy reset <target> - requires the permission "economy.admin.give" to execute
CommandAPICommand("economy")
    .withRequirement { sender ->
        sender.hasPermission("economy.*") ||
        sender.hasPermission("economy.admin.*") ||
        sender.hasPermission("economy.admin.reset")
    }
    .withArguments(PlayerArgument("target"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args.get(0) as Player
        // reset the targets balance here
    })
    .register()
/* ANCHOR_END: permissions4_2 */
}

fun aliases() {
/* ANCHOR: aliases */
CommandAPICommand("getpos")
    // Declare your aliases
    .withAliases("getposition", "getloc", "getlocation", "whereami")

    // Declare your implementation
    .executesEntity(EntityCommandExecutor { entity, _ ->
        val loc = entity.location
        entity.sendMessage("You are at ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
    })
    .executesCommandBlock(CommandBlockCommandExecutor { block, _ ->
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
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setHealth(0.0)
    })
    .register()
/* ANCHOR_END: normalcommandexecutors */
}

fun normalcommandexecutors2() {
/* ANCHOR: normalcommandexecutors2 */
CommandAPICommand("suicide")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setHealth(0.0)
    })
    .executesEntity(EntityCommandExecutor { entity, _ ->
        entity.world.createExplosion(entity.location, 4f)
        entity.remove()
    })
    .register()
/* ANCHOR_END: normalcommandexecutors2 */
}

fun normalcommandexecutors3() {
/* ANCHOR: normalcommandexecutors3 */
CommandAPICommand("suicide")
    .executes(CommandExecutor { sender, _ ->
        val entity = (if (sender is ProxiedCommandSender) sender.callee else sender) as LivingEntity
        entity.setHealth(0.0)
    }, ExecutorType.PLAYER, ExecutorType.PROXY)
    .register()
/* ANCHOR_END: normalcommandexecutors3 */
}

@Suppress("deprecation")
fun normalcommandexecutors3_1() {
/* ANCHOR: normalcommandexecutors3_1 */
// Create our command
CommandAPICommand("broadcastmsg")
    .withArguments(GreedyStringArgument("message")) // The arguments
    .withAliases("broadcast", "broadcastmessage")       // Command aliases
    .withPermission(CommandPermission.OP)               // Required permissions
    .executes(CommandExecutor { _, args ->
        val message = args[0] as String
        Bukkit.getServer().broadcastMessage(message)
    })
    .register()
/* ANCHOR_END: normalcommandexecutors3_1 */
}

fun proxysender() {
/* ANCHOR: proxysender */
CommandAPICommand("killme")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setHealth(0.0)
    })
    .register()
/* ANCHOR_END: proxysender */
}

fun proxysender2() {
/* ANCHOR: proxysender2 */
CommandAPICommand("killme")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setHealth(0.0)
    })
    .executesProxy(ProxyCommandExecutor { proxy, _ ->
        // Check if the callee (target) is an Entity and kill it
        if (proxy.callee is LivingEntity) {
            (proxy.callee as LivingEntity).setHealth(0.0)
        }
    })
    .register()
/* ANCHOR_END: proxysender2 */
}

fun nativesender() {
/* ANCHOR: nativesender */
CommandAPICommand("break")
    .executesNative(NativeCommandExecutor { sender, _ ->
        val location = sender.location
        location.block.breakNaturally()
    })
    .register()
/* ANCHOR_END: nativesender */
}

fun resultingcommandexecutor() {
/* ANCHOR: resultingcommandexecutor */

CommandAPICommand("randnum")
    .executes(ResultingCommandExecutor { _, _ ->
        Random.nextInt()
    })
    .register()
/* ANCHOR_END: resultingcommandexecutor */
}

fun resultingcommandexecutor2() {
/* ANCHOR: resultingcommandexecutor2 */
// Register random number generator command from 1 to 99 (inclusive)
CommandAPICommand("randomnumber")
    .executes(ResultingCommandExecutor { _, _ ->
        (1..100).random() // Returns random number from 1 <= x < 100
    })
    .register()
/* ANCHOR_END: resultingcommandexecutor2 */
}

@Suppress("deprecation")
fun resultingcommandexecutor3(){
/* ANCHOR: resultingcommandexecutor3 */
// Register reward giving system for a target player
CommandAPICommand("givereward")
    .withArguments(EntitySelectorArgument.OnePlayer("target"))
    .executes(CommandExecutor { _, args ->
        val player = args[0] as Player
        player.inventory.addItem(ItemStack(Material.DIAMOND, 64))
        Bukkit.broadcastMessage("${player.name} won a rare 64 diamonds from a loot box!")
    })
    .register()
/* ANCHOR_END: resultingcommandexecutor3 */
}

fun commandfailures() {
/* ANCHOR: commandfailures */
// List of fruit
val fruit = listOf<String>("banana", "apple", "orange")

// Register the command
CommandAPICommand("getfruit")
    .withArguments(StringArgument("item").replaceSuggestions(ArgumentSuggestions.strings(fruit)))
    .executes(CommandExecutor { _, args ->
        val inputFruit = args[0] as String

        if(fruit.any { it == inputFruit }) {
            // Do something with inputFruit
        } else {
            // The sender's input is not in the list of fruit
            throw CommandAPI.fail("That fruit doesn't exist!")
        }
    })
    .register()
/* ANCHOR_END: commandfailures */
}

fun argumentsyntax1() {
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
val arguments = listOf(
    StringArgument("arg0"),
    StringArgument("arg1"),
    StringArgument("arg2")
)

CommandAPICommand("mycommand")
    .withArguments(arguments)
    // And so on
/* ANCHOR_END: argumentsyntax3 */
}

fun argumentkillcmd() {
/* ANCHOR: argumentkillcmd */
CommandAPICommand("kill")
    .executesPlayer(PlayerCommandExecutor { player, _ ->
        player.setHealth(0.0)
    })
    .register()
/* ANCHOR_END: argumentkillcmd */

/* ANCHOR: argumentkillcmd2 */
// Register our second /kill <target> command
CommandAPICommand("kill")
    .withArguments(PlayerArgument("target"))
    .executesPlayer(PlayerCommandExecutor { _, args ->
        (args[0] as Player).setHealth(0.0)
    })
    .register()
/* ANCHOR_END: argumentkillcmd2 */
}

@Suppress("unused")
public fun argumentCasting() {
/* ANCHOR: argumentcasting */
val arguments = listOf(
    StringArgument("arg0"),
    PotionEffectArgument("arg1"),
    LocationArgument("arg2")
)

CommandAPICommand("cmd")
    .withArguments(arguments)
    .executes(CommandExecutor { _, args ->
        val stringArg = args[0] as String
        val potionArg = args[1] as PotionEffectType
        val locationArg = args[2] as Location
    })
    .register()
/* ANCHOR_END: argumentcasting */
}

fun requirements() {
/* ANCHOR: requirements */
CommandAPICommand("repair")
    .withRequirement { (it as Player).level >= 30 }
    .executesPlayer(PlayerCommandExecutor { player, _ ->

        // Repair the item back to full durability
        val item = player.inventory.itemInMainHand
        val itemMeta = item.itemMeta
        if (itemMeta is Damageable) {
            itemMeta.setDamage(0)
            item.setItemMeta(itemMeta)
        }

        // Subtract 30 levels
        player.setLevel(player.level - 30)
    })
    .register()
/* ANCHOR_END: requirements */
}

fun requirementsmap() {
/* ANCHOR: requirementsmap */
val partyMembers = mutableMapOf<UUID, String>()
/* ANCHOR_END: requirementsmap */

/* ANCHOR: requirements2 */
var arguments = mutableListOf<Argument<*>>()

// The "create" literal, with a requirement that a player must have a party
arguments.add(LiteralArgument("create")
    .withRequirement { !partyMembers.containsKey((it as Player).uniqueId) }
)

arguments.add(StringArgument("partyName"))
/* ANCHOR_END: requirements2 */

/* ANCHOR: requirements3 */
CommandAPICommand("party")
    .withArguments(*arguments.toTypedArray())
    .executesPlayer(PlayerCommandExecutor { player, args ->

        // Get the name of the party to create
        val partyName = args[0] as String

        partyMembers.put(player.uniqueId, partyName)
    })
    .register()
/* ANCHOR_END: requirements3 */

/* ANCHOR: requirementstp */
/* ANCHOR: requirements4 */
arguments = mutableListOf<Argument<*>>()
arguments.add(LiteralArgument("tp")
    .withRequirement({ partyMembers.containsKey((it as Player).uniqueId) })
)
/* ANCHOR_END: requirementstp */

arguments.add(PlayerArgument("player")
    .replaceSafeSuggestions(SafeSuggestions.suggest( { info ->

        // Store the list of party members to teleport to
        val playersToTeleportTo = mutableListOf<Player>()

        val partyName = partyMembers[(info.sender() as Player).uniqueId]

        // Find the party members
        for ((uuid, party) in partyMembers) {

            // Ignore yourself
            if (uuid == (info.sender() as Player).uniqueId) {
                continue
            } else {
                // If the party member is in the same party as you
                if (party == partyName) {
                    val target = Bukkit.getPlayer(uuid)!!
                    if (target.isOnline()) {
                        // Add them if they are online
                        playersToTeleportTo.add(target)
                    }
                }
            }
        }

        playersToTeleportTo.toTypedArray()
    })))
/* ANCHOR_END: requirements4 */

/* ANCHOR: requirements5 */
CommandAPICommand("party")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args[0] as Player
        player.teleport(target)
    })
    .register()
/* ANCHOR_END: requirements5 */

/* ANCHOR: updatingrequirements */
CommandAPICommand("party")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->

        // Get the name of the party to create
        val partyName = args[0] as String

        partyMembers.put(player.uniqueId, partyName)

        CommandAPI.updateRequirements(player)
    })
    .register()
/* ANCHOR_END: updatingrequirements */
}

fun multiplerequirements() {
/* ANCHOR: multiplerequirements */
CommandAPICommand("someCommand")
    .withRequirement({ (it as Player).level >= 30 })
    .withRequirement({ (it as Player).inventory.contains(Material.DIAMOND_PICKAXE) })
    .withRequirement({ (it as Player).isInvulnerable() })
    .executesPlayer(PlayerCommandExecutor { player, args ->
        // Code goes here
    })
    .register()
/* ANCHOR_END: multiplerequirements */
}

fun predicatetips() {
val partyMembers = mutableMapOf<UUID, String>()
/* ANCHOR: predicatetips */
val testIfPlayerHasParty = Predicate { sender: CommandSender ->
    partyMembers.containsKey((sender as Player).uniqueId)
}
/* ANCHOR_END: predicatetips */

/* ANCHOR: predicatetips2 */
var arguments = listOf<Argument<*>>(
    LiteralArgument("create").withRequirement(testIfPlayerHasParty.negate()),
    StringArgument("partyName")
)
/* ANCHOR_END: predicatetips2 */

@Suppress("unused")
/* ANCHOR: predicatetips3 */
arguments = listOf<Argument<*>>(LiteralArgument("tp").withRequirement(testIfPlayerHasParty))
/* ANCHOR_END: predicatetips3 */
}

fun converter2() {
/* ANCHOR: converter2 */
val essentials = Bukkit.getPluginManager().getPlugin("Essentials") as JavaPlugin

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

fun brigadier() {
/* ANCHOR: brigadier */
/* ANCHOR: declareliteral */
// Register literal "randomchance"
val randomChance: LiteralCommandNode<Any> = Brigadier.fromLiteralArgument(LiteralArgument("randomchance")).build()
/* ANCHOR_END: declareliteral */

/* ANCHOR: declarearguments */
// Declare arguments like normal
val numeratorArgument = IntegerArgument("numerator", 0)
val denominatorArgument = IntegerArgument("denominator", 1)

val arguments = listOf<Argument<*>>(numeratorArgument, denominatorArgument)
/* ANCHOR_END: declarearguments */

// Get brigadier argument objects
/* ANCHOR: declareargumentbuilders */
val numerator = Brigadier.fromArgument(numeratorArgument)
/* ANCHOR: declarefork */
val denominator = Brigadier.fromArgument(denominatorArgument)
/* ANCHOR_END: declareargumentbuilders */
    // Fork redirecting to "execute" and state our predicate
    .fork(Brigadier.getRootNode().getChild("execute"), Brigadier.fromPredicate( { _: CommandSender, args ->
        // Parse arguments like normal
        val num = (args[0] as Int).toDouble()
        val denom = (args[1] as Int).toDouble()

        // Return boolean with a num/denom chance
        Math.ceil(Math.random() * denom) <= num
    }, arguments))
/* ANCHOR_END: declarefork */

/* ANCHOR: declarerandomchance */
// Add <numerator> <denominator> as a child of randomchance
randomChance.addChild(numerator.then(denominator).build())
/* ANCHOR_END: declarerandomchance */

/* ANCHOR: injectintoroot */
// Add (randomchance <numerator> <denominator>) as a child of (execute -> if)
Brigadier.getRootNode().getChild("execute").getChild("if").addChild(randomChance)
/* ANCHOR_END: injectintoroot */
/* ANCHOR_END: brigadier */
}

@Suppress("UNUSED_PARAMETER")
fun subcommands() {

/* ANCHOR: subcommandspart */
val groupAdd = CommandAPICommand("add")
    .withArguments(StringArgument("permission"))
    .withArguments(StringArgument("groupName"))
    .executes(CommandExecutor { sender, args ->
        // perm group add code
    })
/* ANCHOR_END: subcommandspart */
/* ANCHOR: subcommands */
val groupRemove = CommandAPICommand("remove")
    .withArguments(StringArgument("permission"))
    .withArguments(StringArgument("groupName"))
    .executes(CommandExecutor { sender, args ->
        // perm group remove code
    })

val group = CommandAPICommand("group")
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
                // perm group add code
            })
        )
        .withSubcommand(CommandAPICommand("remove")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("groupName"))
            .executes(CommandExecutor { sender, args ->
                // perm group remove code
            })
        )
    )
    .withSubcommand(CommandAPICommand("user")
        .withSubcommand(CommandAPICommand("add")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("userName"))
            .executes(CommandExecutor { sender, args ->
                // perm user add code
            })
        )
        .withSubcommand(CommandAPICommand("remove")
            .withArguments(StringArgument("permission"))
            .withArguments(StringArgument("userName"))
            .executes(CommandExecutor { sender, args ->
                // perm user remove code
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
    .executes(CommandExecutor { _, _ ->
        Bukkit.broadcastMessage("Hi!")
    })
    .register()
/* ANCHOR_END: help */

/* ANCHOR: help2 */
CommandAPICommand("mycmd")
    .withHelp("Says hi", "Broadcasts hi to everyone on the server")
    .executes(CommandExecutor { _, _ ->
        Bukkit.broadcastMessage("Hi!")
    })
    .register()
/* ANCHOR_END: help2 */
}

fun anglearguments() {
    // TODO: This example isn't used!
/* ANCHOR: anglearguments */
CommandAPICommand("yaw")
    .withArguments(AngleArgument("amount"))
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val newLocation = player.location
        newLocation.setYaw(args[0] as Float)
        player.teleport(newLocation)
    })
    .register()
/* ANCHOR_END: anglearguments */
}

fun listed() {
/* ANCHOR: listed */
CommandAPICommand("mycommand")
    .withArguments(PlayerArgument("player"))
    .withArguments(IntegerArgument("value").setListed(false))
    .withArguments(GreedyStringArgument("message"))
    .executes(CommandExecutor { _, args ->
        // args == [player, message]
        val player = args[0] as Player
        val message = args[1] as String // Note that this is args[1] and NOT args[2]
        player.sendMessage(message)
    })
    .register()
/* ANCHOR_END: listed */
}

fun tooltips1() {
/* ANCHOR: Tooltips1 */
val arguments = mutableListOf<Argument<*>>()
arguments.add(StringArgument("emote")
    .replaceSuggestions(ArgumentSuggestions.stringsWithTooltips { info ->
        arrayOf<IStringTooltip>(
            StringTooltip.ofString("wave", "Waves at a player"),
            StringTooltip.ofString("hug", "Gives a player a hug"),
            StringTooltip.ofString("glare", "Gives a player the death glare")
        )
    })
)
arguments.add(PlayerArgument("target"))
/* ANCHOR_END: Tooltips1 */
/* ANCHOR: Tooltips2 */
CommandAPICommand("emote")
    .withArguments(*arguments.toTypedArray())
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val emote = args[0] as String
        val target = args[1] as Player

        when (emote) {
            "wave" -> target.sendMessage("${player.name} waves at you!")
            "hug" -> target.sendMessage("${player.name} hugs you!")
            "glare" -> target.sendMessage("${player.name} gives you the death glare...")
        }
    })
    .register()
/* ANCHOR_END: Tooltips2 */
}

fun tooltips4() {
/* ANCHOR: Tooltips4 */
val customItems = arrayOf<CustomItem>(
    CustomItem(ItemStack(Material.DIAMOND_SWORD), "God sword", "A sword from the heavens"),
    CustomItem(ItemStack(Material.PUMPKIN_PIE), "Sweet pie", "Just like grandma used to make")
)

CommandAPICommand("giveitem")
    .withArguments(StringArgument("item").replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(*customItems))) // We use customItems[] as the input for our suggestions with tooltips
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val itemName = args[0] as String

        // Give them the item
        for (item in customItems) {
            if (item.name == itemName) {
                player.inventory.addItem(item.item)
                break
            }
        }
    })
    .register()
/* ANCHOR_END: Tooltips4 */
}

fun safetooltips() {
/* ANCHOR: SafeTooltips */
val arguments = listOf<Argument<*>>(
    LocationArgument("location")
        .replaceSafeSuggestions(SafeSuggestions.tooltips( { info ->
            // We know the sender is a player if we use .executesPlayer()
            val player = info.sender() as Player
            Tooltip.arrayOf(
                Tooltip.ofString(player.world.spawnLocation, "World spawn"),
                Tooltip.ofString(player.bedSpawnLocation, "Your bed"),
                Tooltip.ofString(player.getTargetBlockExact(256)?.location, "Target block")
            )
        }))
)
/* ANCHOR_END: SafeTooltips */
/* ANCHOR: SafeTooltips2 */
CommandAPICommand("warp")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        player.teleport(args[0] as Location)
    })
    .register()
/* ANCHOR_END: SafeTooltips2 */
}

fun argumentsuggestionsprevious() {
/* ANCHOR: ArgumentSuggestionsPrevious */
// Declare our arguments as normal
val arguments = mutableListOf<Argument<*>>()
arguments.add(IntegerArgument("radius"))

// Replace the suggestions for the PlayerArgument.
// info.sender() refers to the command sender that is running this command
// info.previousArgs() refers to the Object[] of previously declared arguments (in this case, the IntegerArgument radius)
arguments.add(PlayerArgument("target").replaceSuggestions(ArgumentSuggestions.strings { info: SuggestionInfo<CommandSender> ->

    // Cast the first argument (radius, which is an IntegerArgument) to get its value
    val radius = (info.previousArgs()[0] as Int).toDouble()

    // Get nearby entities within the provided radius
    val player = info.sender() as Player
    val entities = player.world.getNearbyEntities(player.location, radius, radius, radius)

    // Get player names within that radius
    entities
        .filter { it.type == EntityType.PLAYER }
        .map { it.name }
        .toTypedArray()
}))
arguments.add(GreedyStringArgument("message"))

// Declare our command as normal
CommandAPICommand("localmsg")
    .withArguments(*arguments.toTypedArray())
    .executesPlayer(PlayerCommandExecutor { _, args ->
        val target = args[1] as Player
        val message = args[2] as String
        target.sendMessage(message)
    })
    .register()
/* ANCHOR_END: ArgumentSuggestionsPrevious */
}

fun argumentsuggestions2_2() {
/* ANCHOR: ArgumentSuggestions2_2 */
val arguments = listOf<Argument<*>>(
    PlayerArgument("friend").replaceSuggestions(ArgumentSuggestions.strings({ info ->
		Friends.getFriends(info.sender())
    }))
)

CommandAPICommand("friendtp")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val target = args[0] as Player
        player.teleport(target)
    })
    .register()
/* ANCHOR_END: ArgumentSuggestions2_2 */
}

fun argumentsuggestions1() {
val warps = mutableMapOf<String, Location>()
/* ANCHOR: ArgumentSuggestions1 */
val arguments = listOf<Argument<*>>(
    StringArgument("world").replaceSuggestions(ArgumentSuggestions.strings(
        "northland", "eastland", "southland", "westland"
    ))
)

CommandAPICommand("warp")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val warp = args[0] as String
        player.teleport(warps[warp]!!) // Look up the warp in a map, for example
    })
    .register()
/* ANCHOR_END: ArgumentSuggestions1 */
}

@Suppress("deprecation")
fun SafeRecipeArguments() {
/* ANCHOR: SafeRecipeArguments */
// Create our itemstack
val emeraldSword = ItemStack(Material.DIAMOND_SWORD)
val meta = emeraldSword.getItemMeta()
meta?.setDisplayName("Emerald Sword")
meta?.setUnbreakable(true)
emeraldSword.setItemMeta(meta)

// Create and register our recipe
val emeraldSwordRecipe = ShapedRecipe(NamespacedKey(this, "emerald_sword"), emeraldSword)
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
val arguments = listOf<Argument<*>>(
    RecipeArgument("recipe").replaceSafeSuggestions(SafeSuggestions.suggest {
        arrayOf(emeraldSwordRecipe, /* Other recipes here */)
    })
)

// Register our command
CommandAPICommand("giverecipe")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val recipe = args[0] as Recipe
        player.inventory.addItem(recipe.result)
    })
    .register()
/* ANCHOR_END: SafeRecipeArguments_2 */
}

fun safemobspawnarguments() {
/* ANCHOR: SafeMobSpawnArguments */
val forbiddenMobs = listOf<EntityType>(EntityType.ENDER_DRAGON, EntityType.WITHER)
val allowedMobs = EntityType.values().toMutableList()
allowedMobs.removeAll(forbiddenMobs) // Now contains everything except enderdragon and wither
/* ANCHOR_END: SafeMobSpawnArguments */

/* ANCHOR: SafeMobSpawnArguments_2 */
val arguments = listOf<Argument<*>>(
    EntityTypeArgument("mob").replaceSafeSuggestions(SafeSuggestions.suggest {
        info ->
            if (info.sender().isOp()) {
                // All entity types
                EntityType.values()
            } else {
                // Only allowedMobs
                allowedMobs.toTypedArray()
            }
        }
    )
)
/* ANCHOR_END: SafeMobSpawnArguments_2 */

/* ANCHOR: SafeMobSpawnArguments_3 */
CommandAPICommand("spawnmob")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val entityType = args[0] as EntityType
        player.world.spawnEntity(player.location, entityType)
    })
    .register()
/* ANCHOR_END: SafeMobSpawnArguments_3 */
}

fun safepotionarguments() {
/* ANCHOR: SafePotionArguments */
val arguments = mutableListOf<Argument<*>>()
arguments.add(EntitySelectorArgument.OnePlayer("target"))
arguments.add(PotionEffectArgument("potioneffect").replaceSafeSuggestions(SafeSuggestions.suggest {
    info ->
        val target = info.previousArgs()[0] as Player

        // Convert PotionEffect[] into PotionEffectType[]
        target.activePotionEffects.map{ it.type }.toTypedArray()
    })
)
/* ANCHOR_END: SafePotionArguments */

/* ANCHOR: SafePotionArguments_2 */
CommandAPICommand("removeeffect")
    .withArguments(arguments)
    .executesPlayer(PlayerCommandExecutor { _, args ->
        val target = args[0] as Player
        val potionEffect = args[1] as PotionEffectType
        target.removePotionEffect(potionEffect)
    })
    .register()
/* ANCHOR_END: SafePotionArguments_2 */
}

// TODO: Why does this example never get used????
fun fruits() {
    // A really simple example showing how you can use the new suggestion system
    val fruits = arrayOf<String>( "Apple", "Apricot", "Artichoke", "Asparagus", "Atemoya", "Avocado",
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
            "Tomatillo", "Tomato", "Turnip", "Watercress", "Watermelon", "Yams", "Zucchini" )

    CommandAPICommand("concept")
        .withArguments(StringArgument("text"))
        .withArguments(StringArgument("input").replaceSuggestions(ArgumentSuggestions.strings( { info ->
            println(info.currentArg()) // partially typed argument
            println(info.currentInput()) // current input (includes the /)

            fruits.filter{ it.lowercase().startsWith(info.currentArg().lowercase()) }.toTypedArray()
        })))
        .withArguments(IntegerArgument("int"))
        .executes(CommandExecutor { _, _ ->
            // stuff
        })
        .register()
}

fun commandapiconfigsilent() {
/* ANCHOR: CommandAPIConfigSilent */
CommandAPI.onLoad(CommandAPIConfig().silentLogs(true))
/* ANCHOR_END: CommandAPIConfigSilent */
}

fun asyncreadfile() {

val plugin: JavaPlugin = object: JavaPlugin() {}
/* ANCHOR: asyncreadfile */
CommandAPICommand("setconfig")
    .withArguments(StringArgument("key").replaceSuggestions(ArgumentSuggestions.stringsAsync({ _ ->
        CompletableFuture.supplyAsync { plugin.config.getKeys(false).toTypedArray() }
    })))
    .withArguments(TextArgument("value"))
    .executes(CommandExecutor { _, args ->
        val key = args[0] as String
        val value = args[1] as String
        plugin.config.set(key, value)
    })
    .register()
/* ANCHOR_END: asyncreadfile */
}

fun listargument() {

/* ANCHOR: ListArgument_MultiGive */
CommandAPICommand("multigive")
    .withArguments(IntegerArgument("amount", 1, 64))
    .withArguments(ListArgumentBuilder<Material>("materials")
        .withList(Material.values().toList())
        .withMapper { material -> material.name.lowercase() }
        .buildGreedy()
    )
    .executesPlayer(PlayerCommandExecutor { player, args ->
        val amount = args[0] as Int
        val theList = args[1] as List<Material>

        for (item in theList) {
            player.inventory.addItem(ItemStack(item, amount))
        }
    })
    .register()
/* ANCHOR_END: ListArgument_MultiGive */
}

fun brigadierargs() {

/* ANCHOR: BrigadierSuggestions1 */
val commandSuggestions: ArgumentSuggestions<CommandSender> = ArgumentSuggestions { info, builder ->
    // The current argument, which is a full command
    val arg: String = info.currentArg()

    // Identify the position of the current argument
    var start = if (arg.contains(" ")) {
        // Current argument contains spaces - it starts after the last space and after the start of this argument.
        builder.start + arg.lastIndexOf(' ') + 1
    } else {
        // Input starts at the start of this argument
        builder.start
    }

    // Parse command using brigadier
    val parseResults: ParseResults<*> = Brigadier.getCommandDispatcher()
        .parse(info.currentArg(), Brigadier.getBrigadierSourceFromCommandSender(info.sender))

    // Intercept any parsing errors indicating an invalid command
    for ((_, exception) in parseResults.exceptions) {
        // Raise the error, with the cursor offset to line up with the argument
        throw CommandSyntaxException(exception.type, exception.rawMessage, exception.input, exception.cursor + start)
    }

    val completableFutureSuggestions: CompletableFuture<Suggestions> =
        Brigadier.getCommandDispatcher().getCompletionSuggestions(parseResults) as CompletableFuture<Suggestions>

    completableFutureSuggestions.thenApply { suggestions: Suggestions ->
        Suggestions(
            // Offset the index range of the suggestions by the start of the current argument
            StringRange(start, start + suggestions.range.length),
            // Copy the suggestions
            suggestions.list
        )
    }
}
/* ANCHOR_END: BrigadierSuggestions1 */

/* ANCHOR: BrigadierSuggestions2 */
CommandAPICommand("commandargument")
    .withArguments(GreedyStringArgument("command").replaceSuggestions(commandSuggestions))
    .executes(CommandExecutor { sender, args ->
        // Run the command using Bukkit.dispatchCommand()
        Bukkit.dispatchCommand(sender, args[0] as String)
    }).register()
/* ANCHOR_END: BrigadierSuggestions2 */
}

fun brigadiersuggestionsemoji() {
/* ANCHOR: BrigadierSuggestions3 */
val emojis = mapOf(
    "☻" to "smile",
    "❤" to "heart",
    "🔥" to "fire",
    "★" to "star",
    "☠" to "death",
    "⚠" to "warning",
    "☀" to "sun",
    "☺" to "smile",
    "☹" to "frown",
    "✉" to "mail",
    "☂" to "umbrella",
    "✘" to "cross",
    "♪" to "music note (eighth)",
    "♬" to "music note (beamed sixteenth)",
    "♩" to "music note (quarter)",
    "♫" to "music note (beamed eighth)",
    "☄" to "comet",
    "✦" to "star",
    "🗡" to "sword",
    "🪓" to "axe",
    "🔱" to "trident",
    "🎣" to "fishing rod",
    "🏹" to "bow",
    "⛏" to "pickaxe",
    "🍖" to "food"
)

val messageArgument = GreedyStringArgument("message")
    .replaceSuggestions { info, builder ->
        // Only display suggestions at the very end character
        val newBuilder = builder.createOffset(builder.getStart() + info.currentArg().length);

        // Suggest all the emojis!
        emojis.forEach { (emoji, description) ->
            newBuilder.suggest(emoji, LiteralMessage(description));
        }

        newBuilder.buildFuture()
    }

CommandAPICommand("emoji")
    .withArguments(messageArgument)
    .executes(CommandExecutor { _, args ->
        Bukkit.broadcastMessage(args[0] as String);
    })
    .register()
/* ANCHOR_END: BrigadierSuggestions3 */
}

// TODO: This example isn't used in the documentation!
fun treeexample() {
CommandTree("treeexample")
    // Set the aliases as you normally would
    .withAliases("treealias")
    // Set an executor on the command itself
    .executes(CommandExecutor { sender, _ ->
        sender.sendMessage("Root with no arguments")
    })
    // Create a new branch starting with a the literal 'integer'
    .then(LiteralArgument("integer")
        // Execute on the literal itself
        .executes(CommandExecutor { sender, _ ->
            sender.sendMessage("Integer Branch with no arguments")
        })
        // Create a further branch starting with an integer argument, which executes a command
        .then(IntegerArgument("integer").executes(CommandExecutor { sender, args ->
            sender.sendMessage("Integer Branch with integer argument: ${args[0]}")
        })))
    .then(LiteralArgument("biome")
        .executes(CommandExecutor { sender, _ ->
            sender.sendMessage("Biome Branch with no arguments")
        })
        .then(BiomeArgument("biome").executes(CommandExecutor { sender, args ->
            sender.sendMessage("Biome Branch with biome argument: ${args[0]}")
        })))
    .then(LiteralArgument("string")
        .executes(CommandExecutor { sender, _ ->
            sender.sendMessage("String Branch with no arguments")
        })
        .then(StringArgument("string").executes(CommandExecutor { sender, args ->
            sender.sendMessage("String Branch with string argument: ${args[0]}")
        })))
    // Call register to finish as you normally would
    .register()

/* ANCHOR: CommandTree_sayhi1 */
CommandTree("sayhi")
    .executes(CommandExecutor { sender, _ ->
        sender.sendMessage("Hi!")
    })
    .then(PlayerArgument("target")
        .executes(CommandExecutor { _, args ->
            val target = args[0] as Player
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
                    val sign: Sign = getTargetSign(player)
                    val line_number = args[0] as Int
                    val text = args[1] as String
                    sign.setLine(line_number - 1, text)
                    sign.update(true)
                 }))))
    .then(LiteralArgument("clear")
        .then(IntegerArgument("line_number", 1, 4)
            .executesPlayer(PlayerCommandExecutor { player, args ->
                // /signedit clear <line_number>
                val sign: Sign = getTargetSign(player)
                val line_number = args[0] as Int
                sign.setLine(line_number - 1, "")
                sign.update(true)
            })))
    .then(LiteralArgument("copy")
        .then(IntegerArgument("line_number", 1, 4)
            .executesPlayer(PlayerCommandExecutor { player, args ->
                // /signedit copy <line_number>
                val sign: Sign = getTargetSign(player)
                val line_number = args[0] as Int
                player.setMetadata("copied_sign_text", FixedMetadataValue(this, sign.getLine(line_number - 1)))
            })))
    .then(LiteralArgument("paste")
        .then(IntegerArgument("line_number", 1, 4)
            .executesPlayer(PlayerCommandExecutor { player, args ->
                // /signedit copy <line_number>
                val sign: Sign = getTargetSign(player)
                val line_number = args[0] as Int
                sign.setLine(line_number - 1, player.getMetadata("copied_sign_text")[0].asString())
                sign.update(true)
            })))
    .register()
/* ANCHOR_END: CommandTree_signedit */
}

@Throws(WrapperCommandSyntaxException::class)
fun getTargetSign(player: Player): Sign {
    val block: Block? = player.getTargetBlock(null, 256)
    if (block != null && block.state is Sign) {
        return block.state as Sign
    } else {
        throw CommandAPI.failWithString("You're not looking at a sign!")
    }
}

fun sudoCommandArgument() {
/* ANCHOR: command_argument_sudo */
CommandAPICommand("sudo")
    .withArguments(PlayerArgument("target"))
    .withArguments(CommandArgument("command"))
    .executes(CommandExecutor { _, args ->
        val target = args[0] as Player
        val command = args[1] as CommandResult

        command.execute(target)
    })
    .register()
/* ANCHOR_END: command_argument_sudo */
}

fun giveCommandArgument() {

/* ANCHOR: command_argument_branch_give */
SuggestionsBranch.suggest<CommandSender>(
    ArgumentSuggestions.strings("give"),
    ArgumentSuggestions.strings { _ -> Bukkit.getOnlinePlayers().map{ it.name }.toTypedArray() }
).branch(
    SuggestionsBranch.suggest(
        ArgumentSuggestions.strings("diamond", "minecraft:diamond"),
        ArgumentSuggestions.empty()
    ),
    SuggestionsBranch.suggest(
        ArgumentSuggestions.strings("dirt", "minecraft:dirt"),
        null,
        ArgumentSuggestions.empty()
    )
)
/* ANCHOR_END: command_argument_branch_give */

/* ANCHOR: command_argument_branch_tp */
SuggestionsBranch.suggest<CommandSender>(
    ArgumentSuggestions.strings("tp"),
    ArgumentSuggestions.strings { _ -> Bukkit.getOnlinePlayers().map{ it.name }.toTypedArray() },
    ArgumentSuggestions.strings { _ -> Bukkit.getOnlinePlayers().map{ it.name }.toTypedArray() }
)
/* ANCHOR_END: command_argument_branch_tp */


/* ANCHOR: command_argument_branch */
CommandArgument("command")
    .branchSuggestions(
        SuggestionsBranch.suggest<CommandSender>(
            ArgumentSuggestions.strings("give"),
            ArgumentSuggestions.strings { _ -> Bukkit.getOnlinePlayers().map{ it.name }.toTypedArray() }
        ).branch(
            SuggestionsBranch.suggest(
                ArgumentSuggestions.strings("diamond", "minecraft:diamond"),
                ArgumentSuggestions.empty()
            ),
            SuggestionsBranch.suggest(
                ArgumentSuggestions.strings("dirt", "minecraft:dirt"),
                null,
                ArgumentSuggestions.empty()
            )
        ),
        SuggestionsBranch.suggest(
            ArgumentSuggestions.strings("tp"),
            ArgumentSuggestions.strings { _ -> Bukkit.getOnlinePlayers().map{ it.name }.toTypedArray() },
            ArgumentSuggestions.strings { _ -> Bukkit.getOnlinePlayers().map{ it.name }.toTypedArray() }
        )
    )
/* ANCHOR_END: command_argument_branch */
}


} // Examples class end // /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// /// ///

/* ANCHOR: ArgumentSuggestions2_1 */
class Friends {

    companion object {

        val friends = mutableMapOf<UUID, Array<String>>()

        fun getFriends(sender: CommandSender): Array<String> {
            if (sender is Player) {
                // Look up friends in a database or file
                return friends[sender.uniqueId]!!
            } else {
                return arrayOf<String>()
            }
        }

    }

}
/* ANCHOR_END: ArgumentSuggestions2_1 */


@Suppress("deprecation")
/* ANCHOR: Tooltips3 */
class CustomItem(val item: ItemStack, val name: String, lore: String): IStringTooltip {

    init {
        val meta = item.itemMeta
        meta.setDisplayName(name)
        meta.setLore(listOf(lore))
        item.setItemMeta(meta)
    }

    override fun getSuggestion(): String = this.item.itemMeta.displayName

    override fun getTooltip(): Message = Tooltip.messageFromString(this.item.itemMeta.lore?.get(0) ?: "")

}
/* ANCHOR_END: Tooltips3 */

/* ANCHOR: functionregistration */
class Main : JavaPlugin() {

    override fun onLoad() {
        // Commands which will be used in Minecraft functions are registered here

        CommandAPICommand("killall")
            .executes(CommandExecutor { _, _ ->
                // Kills all enemies in all worlds
                Bukkit.getWorlds().forEach { it.livingEntities.forEach { it.setHealth(0.0) } }
            })
            .register()
    }

    override fun onEnable() {
        // Register all other commands here
    }

}
/* ANCHOR_END: functionregistration */

/* ANCHOR: shading */
class MyPlugin : JavaPlugin() {

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIConfig().verboseOutput(true)) // Load with verbose output

        CommandAPICommand("ping")
            .executes(CommandExecutor { sender, _ ->
                sender.sendMessage("pong!")
            })
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable(this)

        // Register commands, listeners etc.
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

}
/* ANCHOR_END: shading */

/* ANCHOR: converter */
class YourPlugin : JavaPlugin() {

    override fun onEnable() {
        Converter.convert(Bukkit.getPluginManager().getPlugin("TargetPlugin") as JavaPlugin)
        // Other code goes here...
    }

}
/* ANCHOR_END: converter */