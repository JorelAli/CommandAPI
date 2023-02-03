import com.mojang.brigadier.LiteralMessage
import com.mojang.brigadier.ParseResults
import com.mojang.brigadier.context.StringRange
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.suggestion.Suggestions
import de.tr7zw.changeme.nbtapi.NBTContainer
import dev.jorel.commandapi.*
import dev.jorel.commandapi.arguments.*
import dev.jorel.commandapi.arguments.CustomArgument.CustomArgumentException
import dev.jorel.commandapi.arguments.CustomArgument.MessageBuilder
import dev.jorel.commandapi.arguments.LiteralArgument.of
import dev.jorel.commandapi.examples.kotlin.CustomItem
import dev.jorel.commandapi.examples.kotlin.Friends
import dev.jorel.commandapi.exceptions.WrapperCommandSyntaxException
import dev.jorel.commandapi.executors.*
import dev.jorel.commandapi.kotlindsl.*
import dev.jorel.commandapi.wrappers.*
import dev.jorel.commandapi.wrappers.Rotation
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.*
import org.bukkit.advancement.Advancement
import org.bukkit.block.*
import org.bukkit.block.data.BlockData
import org.bukkit.command.CommandSender
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
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team
import org.bukkit.util.EulerAngle
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate
import kotlin.random.Random

class ExamplesKotlinDSL : JavaPlugin() {
fun sendMessageToCommand() {
/* ANCHOR: dslSendMessageToCommand */
commandTree("sendmessageto") {
    playerArgument("player") { // Defines a new PlayerArgument("player")
        greedyStringArgument("msg") { // Defines a new GreedyStringArgument("msg")
            anyExecutor { _, args -> // Command can be executed by anyone and anything (such as entities, the console, etc.)
                val player: Player = args[0] as Player
                val message: String = args[1] as String
                player.sendMessage(message)
            }
        }
    }
}
/* ANCHOR_END: dslSendMessageToCommand */

/* ANCHOR: dslSendMessageToCommand2 */
commandAPICommand("sendmessageto") {
    playerArgument("player") // Defines a new PlayerArgument("player")
    greedyStringArgument("msg") // Defines a new GreedyStringArgument("msg")
    anyExecutor { _, args -> // Command can be executed by anyone and anything (such as entities, the console, etc.)
        val player: Player = args[0] as Player
        val message: String = args[1] as String
        player.sendMessage(message)
    }
}
/* ANCHOR_END: dslSendMessageToCommand2 */

/* ANCHOR: dslSendMessageToCommandRequirement */
commandTree("sendMessageTo") {
    playerArgument("player") {
        greedyStringArgument("msg") {
            playerExecutor { _, args ->
                val player: Player = args[0] as Player
                val message: String = args[1] as String
                player.sendMessage(message)
            }
        }
    }
    requirement(of("broadcast"), { sender: CommandSender -> sender.isOp }) { // Define a new LiteralArgument("broadcast") that requires the CommandSender to be a player who is a server operator
        greedyStringArgument("msg") {
            playerExecutor { _, args ->
                val message: String = args[0] as String
                Bukkit.broadcastMessage(message)
            }
        }
    }
}
/* ANCHOR_END: dslSendMessageToCommandRequirement */

/* ANCHOR: dslSendMessageToCommandRequirement2 */
commandAPICommand("sendMessageTo") {
    playerArgument("player")
    greedyStringArgument("msg")
    playerExecutor { _, args ->
        val player: Player = args[0] as Player
        val message: String = args[1] as String
        player.sendMessage(message)
    }
}

commandAPICommand("sendMessageTo") {
    requirement(of("broadcast"), { sender: CommandSender -> sender.isOp }) // Define a new LiteralArgument("broadcast") that requires the CommandSender to be a player who is a server operator
    greedyStringArgument("msg")
    playerExecutor { _, args ->
        val message: String = args[0] as String
        Bukkit.broadcastMessage(message)
    }
}
/* ANCHOR_END: dslSendMessageToCommandRequirement2 */

/* ANCHOR: dslCommandRequirements */
commandTree("commandRequirement", {sender: CommandSender -> sender.isOp}) {
    playerExecutor { player, _ ->
        player.sendMessage("This command can only be executed by players who are server operators.")
    }
}
/* ANCHOR_END: dslCommandRequirements */

/* ANCHOR: dslCommandRequirements2 */
commandAPICommand("commandRequirement", {sender: CommandSender -> sender.isOp}) {
    playerExecutor { player, _ ->
        player.sendMessage("This command can only be executed by players who are server operators.")
    }
}
/* ANCHOR_END: dslCommandRequirements2 */
}

fun moreExamples() {
/* ANCHOR: optionalArgument */
commandTree("optionalArgument") {
    literalArgument("give") {
        itemStackArgument("item") {
            playerExecutor { player, args -> // This will let you execute "/optionalArgument give minecraft:stick"
                val itemStack: ItemStack = args[0] as ItemStack
                player.inventory.addItem(itemStack)
            }
            integerArgument("amount") {
                playerExecutor { player, args -> // This will let you execute "/optionalArgument give minecraft:stick 5"
                    val itemStack: ItemStack = args[0] as ItemStack
                    val amount: Int = args[1] as Int
                    itemStack.amount = amount
                    player.inventory.addItem(itemStack)
                }
            }
        }
    }
}
/* ANCHOR_END: optionalArgument */

/* ANCHOR: optionalArgument2 */
commandAPICommand("optionalArgument") {
    literalArgument("give")
    itemStackArgument("item")
    integerArgument("amount", optional = true) // This sets the argument as optional, technically, the "optional =" is not necessary
    playerExecutor { player, args ->
        // This command will let you execute:
        // "/optionalArgument give minecraft:stick"
        // "/optionalArgument give minecraft:stick 5"
        val itemStack: ItemStack = args[0] as ItemStack
        val amount: Int = args.getOrDefault("amount", 1) as Int
        itemStack.amount = amount
        player.inventory.addItem(itemStack)
    }
}
/* ANCHOR_END: optionalArgument2 */

/* ANCHOR: replaceSuggestions */
commandTree("replaceSuggestions") {
    argument(StringArgument("strings").replaceSuggestions(ArgumentSuggestions.strings("one", "two", "three"))) { // Implement an argument that has suggestions
        playerExecutor { player, args ->
            player.sendMessage("You chose option ${args[0] as String}!")
        }
    }
}
/* ANCHOR_END: replaceSuggestions */

/* ANCHOR: replaceSuggestions2 */
commandAPICommand("replaceSuggestions") {
    argument(StringArgument("strings").replaceSuggestions(ArgumentSuggestions.strings("one", "two", "three"))) // Implement an argument that has suggestions
    playerExecutor { player, args ->
        player.sendMessage("You chose option ${args[0] as String}!")
    }
}
/* ANCHOR_END: replaceSuggestions2 */
}

fun commandregistration() {
/* ANCHOR: commandregistration */
// Create our command
commandAPICommand("broadcastmsg") {
    withAliases("broadcast", "broadcastmessage") // Command aliases
    withPermission(CommandPermission.OP)                  // Required permissions
    greedyStringArgument("message")            // The arguments
    anyExecutor { sender, args ->
        val message = args[0] as String
        Bukkit.getServer().broadcastMessage(message)
    }
}
/* ANCHOR_END: commandregistration */
}

fun commandunregistration() {
/* ANCHOR: commandunregistration */
// Unregister the gamemode command from the server (by force)
CommandAPI.unregister("gamemode", true)

// Register our new /gamemode, with survival, creative, adventure and spectator
commandAPICommand("gamemode") {
    multiLiteralArgument("survival", "creative", "adventure", "spectator")
    anyExecutor { sender, args ->
        // Implementation of our /gamemode command
    }
}
/* ANCHOR_END: commandunregistration */
}

class ExamplesKotlinDSL : JavaPlugin() {
fun booleanargs() {
/* ANCHOR: booleanargs */
// Load keys from config file
val configKeys: Array<String> = getConfig().getKeys(true).toTypedArray()

// Register our command
commandAPICommand("editconfig") {
    argument(TextArgument("config-key").replaceSuggestions(ArgumentSuggestions.strings { configKeys }))
    booleanArgument("value")
    anyExecutor { _, args ->
        // Update the config with the boolean argument
        getConfig().set(args[0] as String, args[1] as Boolean)
    }
}
/* ANCHOR_END: booleanargs */
}
}

fun rangedarguments() {
/* ANCHOR: rangedarguments */
commandAPICommand("searchrange") {
    integerRangeArgument("range") // Range argument
    itemStackArgument("item") // The item to search for
    playerExecutor { player, args ->
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
            locations.forEach {
                player.sendMessage("  Found at: ${it.x}, ${it.y}, ${it.z}")
            }
        }
    }
}
/* ANCHOR_END: rangedarguments */
}

fun greedystringarguments() {
/* ANCHOR: greedystringarguments */
commandAPICommand("message") {
    playerArgument("target")
    greedyStringArgument("message")
    anyExecutor { _, args ->
        (args[0] as Player).sendMessage(args[1] as String)
    }
}
/* ANCHOR_END: greedystringarguments */
}

fun locationarguments() {
/* ANCHOR: locationarguments */
commandAPICommand("break") {
    // We want to target blocks in particular, so use BLOCK_POSITION
    locationArgument("block", LocationType.BLOCK_POSITION)
    playerExecutor { _, args ->
        (args[0] as Location).block.type = Material.AIR
    }
}
/* ANCHOR_END: locationarguments */
}

fun rotationarguments() {
/* ANCHOR: rotationarguments */
commandAPICommand("rotate") {
    rotationArgument("rotation")
    entitySelectorArgumentOneEntity("target")
    anyExecutor { _, args ->
        val rotation = args[0] as Rotation
        val target = args[1] as Entity

        if (target is ArmorStand) {
            target.headPose = EulerAngle(Math.toRadians(rotation.pitch.toDouble()), Math.toRadians(rotation.yaw.toDouble() - 90), 0.0)
        }
    }
}
/* ANCHOR_END: rotationarguments */
}

@Suppress("DEPRECATION")
fun chatcolorarguments() {
/* ANCHOR: chatcolorarguments */
commandAPICommand("namecolor") {
    chatColorArgument("chatcolor")
    playerExecutor { player, args ->
        val color = args[0] as ChatColor
        player.setDisplayName("$color${player.name}")
    }
}
/* ANCHOR_END: chatcolorarguments */
}


@Suppress("deprecation")
fun chatcomponentarguments() {
/* ANCHOR: chatcomponentarguments */
commandAPICommand("makebook") {
    playerArgument("player")
    chatComponentArgument("contents")
    anyExecutor { _, args ->
        val player = args[0] as Player
        val array = args[1] as Array<BaseComponent>

        // Create book
        val item = ItemStack(Material.WRITTEN_BOOK)
        val meta = item.itemMeta as BookMeta
        meta.title = "Custom Book"
        meta.author = player.name
        meta.spigot().setPages(array)
        item.itemMeta = meta

        // Give player the book
        player.inventory.addItem(item)
    }
}
/* ANCHOR_END: chatcomponentarguments */
}

@Suppress("deprecation")
fun chatarguments() {
/* ANCHOR: chatarguments */
commandAPICommand("pbroadcast") {
    chatArgument("message")
    anyExecutor { _, args ->
        val message = args[0] as Array<BaseComponent>

        // Broadcast the message to everyone on the server
        Bukkit.getServer().spigot().broadcast(*message)
    }
}
/* ANCHOR_END: chatarguments */

/* ANCHOR: chatpreviewspigot */
commandAPICommand("broadcast") {
    argument(ChatArgument("message").withPreview { info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText: String = BaseComponent.toPlainText(*info.parsedInput as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    })
    playerExecutor { _, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a BaseComponent[] by converting to plain text then to BaseComponent[]
        val plainText: String = BaseComponent.toPlainText(*args[0] as Array<BaseComponent>)
        val baseComponents: Array<BaseComponent> = TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
        Bukkit.spigot().broadcast(*baseComponents)
    }
}
/* ANCHOR_END: chatpreviewspigot */

/* ANCHOR: chatpreviewadventure */
commandAPICommand("broadcast") {
    argument(AdventureChatArgument("message").withPreview { info ->
        // Convert parsed Component to plain text
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(info.parsedInput as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    })
    playerExecutor { _, args ->
        // The user still entered legacy text. We need to properly convert this
        // to a Component by converting to plain text then to Component
        val plainText: String = PlainTextComponentSerializer.plainText().serialize(args[0] as Component)
        Bukkit.broadcast(LegacyComponentSerializer.legacyAmpersand().deserialize(plainText))
    }
}
/* ANCHOR_END: chatpreviewadventure */

/* ANCHOR: chatpreviewspigotusepreview */
commandAPICommand("broadcast") {
    argument(ChatArgument("message").usePreview(true).withPreview { info ->
        // Convert parsed BaseComponent[] to plain text
        val plainText = BaseComponent.toPlainText(*info.parsedInput() as Array<BaseComponent>)

        // Translate the & in plain text and generate a new BaseComponent[]
        TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', plainText))
    })
    playerExecutor { _, args ->
        Bukkit.spigot().broadcast(*args[0] as Array<BaseComponent>)
    }
}
/* ANCHOR_END: chatpreviewspigotusepreview */

/* ANCHOR: chatpreviewadventureusepreview */
commandAPICommand("broadcast") {
    argument(AdventureChatArgument("message").usePreview(true).withPreview { info ->
        // Convert parsed Component to plain text
        val plainText = PlainTextComponentSerializer.plainText().serialize(info.parsedInput() as Component)

        // Translate the & in plain text and generate a new Component
        LegacyComponentSerializer.legacyAmpersand().deserialize(plainText)
    })
    playerExecutor { _, args ->
        Bukkit.broadcast(args[0] as Component)
    }
}
/* ANCHOR_END: chatpreviewadventureusepreview */
}


fun argumentadventurechatcomponent() {
/* ANCHOR: ArgumentAdventureChatComponent */
commandAPICommand("showbook") {
    playerArgument("target")
    textArgument("title")
    stringArgument("author")
    adventureChatComponentArgument("contents")
    anyExecutor { _, args ->
        val target = args[0] as Player
        val title = args[1] as String
        val author = args[2] as String
        val content = args[3] as Component

        // Create a book and show it to the user (Requires Paper)
        val mybook = Book.book(Component.text(title), Component.text(author), content)
        target.openBook(mybook)
    }
}
/* ANCHOR_END: ArgumentAdventureChatComponent */
}

fun argumentadventurechat() {
/* ANCHOR: ArgumentAdventureChat */
commandAPICommand("pbroadcast") {
    adventureChatArgument("message")
    anyExecutor { _, args ->
        val message = args[0] as Component

        // Broadcast the message to everyone with broadcast permissions.
        Bukkit.getServer().broadcast(message, Server.BROADCAST_CHANNEL_USERS)
        Bukkit.getServer().broadcast(message)
    }
}
/* ANCHOR_END: ArgumentAdventureChat */
}

fun entityselectorarguments() {
/* ANCHOR: entityselectorarguments */
commandAPICommand("remove") {
    // Using a collective entity selector to select multiple entities
    entitySelectorArgumentManyEntities("entities")
    anyExecutor { sender, args ->
        // Parse the argument as a collection of entities (as stated above in the documentation)
        val entities = args[0] as Collection<Entity>

        sender.sendMessage("Removed ${entities.size} entities")
        for (e in entities) {
            e.remove()
        }
    }
}
/* ANCHOR_END: entityselectorarguments */
}

fun entitytypearguments() {
/* ANCHOR: entitytypearguments */
commandAPICommand("spawnmob") {
    entityTypeArgument("entity")
    integerArgument("amount", 1, 100) // Prevent spawning too many entities
    playerExecutor { player, args ->
        for (i in 0 until args[1] as Int) {
            player.world.spawnEntity(player.location, args[0] as EntityType)
        }
    }
}
/* ANCHOR_END: entitytypearguments */
}

fun scoreholderargument() {
/* ANCHOR: scoreholderargument */
commandAPICommand("reward") {
    // We want multiple players, so we use the scoreHolderArgumentMultiple method
    scoreHolderArgumentMultiple("player")
    anyExecutor { _, args ->
        // Get player names by casting to Collection<String>
        val players = args[0] as Collection<String>

        for (playerName in players) {
            Bukkit.getPlayer(playerName)?.inventory!!.addItem(ItemStack(Material.DIAMOND, 3))
        }
    }
}
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
commandAPICommand("clearobjectives") {
    scoreboardSlotArgument("slot")
    anyExecutor { _, args ->
        val scoreboard = Bukkit.getScoreboardManager().mainScoreboard
        val slot = (args[0] as ScoreboardSlot).displaySlot
        scoreboard.clearSlot(slot)
    }
}
/* ANCHOR_END: scoreboardslotargument */
}

fun objectiveargument() {
/* ANCHOR: objectiveargument */
commandAPICommand("sidebar") {
    objectiveArgument("objective")
    anyExecutor { _, args ->
        val objective = args[0] as Objective

        // Set display slot
        objective?.displaySlot = DisplaySlot.SIDEBAR
    }
}
/* ANCHOR_END: objectiveargument */
}

fun objectivecriteriaarguments() {
/* ANCHOR: objectivecriteriaarguments */
commandAPICommand("unregisterall") {
    objectiveCriteriaArgument("objective criteria")
    anyExecutor { _, args ->
        val objectiveCriteria = args[0] as String
        val objectives = Bukkit.getScoreboardManager().mainScoreboard.getObjectivesByCriteria(objectiveCriteria)

        // Unregister the objectives
        for (objective in objectives) {
            objective.unregister()
        }
    }
}
/* ANCHOR_END: objectivecriteriaarguments */
}

fun teamarguments() {
/* ANCHOR: teamarguments */
commandAPICommand("togglepvp") {
    teamArgument("team")
    anyExecutor { _, args ->
        val team = args[0] as Team

        // Toggle pvp
        team.setAllowFriendlyFire(team.allowFriendlyFire())
    }
}
/* ANCHOR_END: teamarguments */
}

fun advancementarguments() {
/* ANCHOR: advancementarguments */
commandAPICommand("award") {
    playerArgument("player")
    advancementArgument("advancement")
    anyExecutor { _, args ->
        val target = args[0] as Player
        val advancement = args[1] as Advancement

        // Award all criteria for the advancement
        val progress = target.getAdvancementProgress(advancement)
        for (criteria in advancement.criteria) {
            progress.awardCriteria(criteria)
        }
    }
}
/* ANCHOR_END: advancementarguments */
}

fun biomearguments() {
/* ANCHOR: biomearguments */
commandAPICommand("setbiome") {
    biomeArgument("biome")
    playerExecutor { player, args ->
        val biome = args[0] as Biome

        val chunk = player.location.chunk
        player.world.setBiome(chunk.x, player.location.blockY, chunk.z, biome)
    }
}
/* ANCHOR_END: biomearguments */
}

fun blockstateargument() {
/* ANCHOR: blockstateargument */
commandAPICommand("set") {
    blockStateArgument("block")
    playerExecutor { player, args ->
        val blockdata = args[0] as BlockData
        val targetBlock = player.getTargetBlockExact(256)

        // Set the block, along with its data
        targetBlock?.type = blockdata.material
        targetBlock?.state?.blockData = blockdata
    }
}
/* ANCHOR_END: blockstateargument */
}

fun enchantmentarguments() {
/* ANCHOR: enchantmentarguments */
commandAPICommand("enchantitem") {
    enchantmentArgument("enchantment")
    integerArgument("level", 1, 5)
    playerExecutor { player, args ->
        val enchantment = args[0] as Enchantment
        val level = args[1] as Int

        // Add the enchantment
        player.inventory.itemInMainHand.addEnchantment(enchantment, level)
    }
}
/* ANCHOR_END: enchantmentarguments */
}

fun worldarguments() {
/* ANCHOR: worldarguments */
commandAPICommand("unloadworld") {
    worldArgument("world")
    anyExecutor { sender, args ->
        val world = args[0] as World

        // Unload the world (and save the world's chunks)
        Bukkit.getServer().unloadWorld(world, true)
    }
}
/* ANCHOR_END: worldarguments */
}

fun itemstackarguments() {
/* ANCHOR: itemstackarguments */
commandAPICommand("item") {
    itemStackArgument("itemstack")
    playerExecutor { player, args ->
        player.inventory.addItem(args[0] as ItemStack)
    }
}
/* ANCHOR_END: itemstackarguments */
}

fun loottablearguments() {
/* ANCHOR: loottablearguments */
commandAPICommand("giveloottable") {
    lootTableArgument("loottable")
    locationArgument("location", LocationType.BLOCK_POSITION)
    anyExecutor { _, args ->
        val lootTable = args[0] as LootTable
        val location = args[1] as Location

        val state = location.block.state

        // Check if the input block is a container (e.g. chest)
        if (state is Container && state is Lootable) {
            // Apply the loot table to the chest
            state.lootTable = lootTable
            state.update()
        }
    }
}
/* ANCHOR_END: loottablearguments */
}

fun mathoperationarguments() {
/* ANCHOR: mathoperationarguments */
commandAPICommand("changelevel") {
    playerArgument("player")
    mathOperationArgument("operation")
    integerArgument("value")
    anyExecutor { _, args ->
        val target = args[0] as Player
        val op = args[1] as MathOperation
        val value = args[2] as Int

        target.level = op.apply(target.level, value)
    }
}
/* ANCHOR_END: mathoperationarguments */
}

fun particlearguments() {
/* ANCHOR: particlearguments */
commandAPICommand("showparticle") {
    particleArgument("particle")
    playerExecutor { player, args ->
        val particleData = args[0] as ParticleData<Any>
        player.world.spawnParticle(particleData.particle(), player.location, 1)
    }
}
/* ANCHOR_END: particlearguments */

/* ANCHOR: particlearguments2 */
commandAPICommand("showparticle") {
    particleArgument("particle")
    playerExecutor { player, args ->
        val particleData = args[0] as ParticleData<Any>
        player.world.spawnParticle(particleData.particle(), player.location, 1, particleData.data())
    }
}
/* ANCHOR_END: particlearguments2 */
}

fun potioneffectarguments() {
/* ANCHOR: potioneffectarguments */
commandAPICommand("potion") {
    playerArgument("target")
    potionEffectArgument("potion")
    timeArgument("duration")
    integerArgument("strength")
    anyExecutor { _, args ->
        val target = args[0] as Player
        val potion = args[1] as PotionEffectType
        val duration = args[2] as Int
        val strength = args[3] as Int

        // Add the potion effect to the target player
        target.addPotionEffect(PotionEffect(potion, duration, strength))
    }
}
/* ANCHOR_END: potioneffectarguments */
}

fun recipearguments() {
/* ANCHOR: recipearguments */
commandAPICommand("giverecipe") {
    recipeArgument("recipe")
    playerExecutor { player, args ->
        val recipe = args[0] as ComplexRecipe
        player.inventory.addItem(recipe.result)
    }
}
/* ANCHOR_END: recipearguments */
}

fun recipearguments2() {
/* ANCHOR: recipearguments2 */
commandAPICommand("unlockrecipe") {
    playerArgument("player")
    recipeArgument("recipe")
    anyExecutor { _, args ->
        val target = args[0] as Player
        val recipe = args[1] as ComplexRecipe

        target.discoverRecipe(recipe.key)
    }
}
/* ANCHOR_END: recipearguments2 */
}

fun soundarguments() {
/* ANCHOR: soundarguments */
commandAPICommand("sound") {
    soundArgument("sound")
    playerExecutor { player, args ->
        player.world.playSound(player.location, args[0] as Sound, 100.0f, 1.0f)
    }
}
/* ANCHOR_END: soundarguments */

/* ANCHOR: soundarguments2 */
commandAPICommand("sound") {
    soundArgument("sound", true)
    playerExecutor { player, args ->
        player.world.playSound(player.location, (args[0] as NamespacedKey).asString(), 100.0f, 1.0f)
    }
}
/* ANCHOR_END: soundarguments2 */
}

fun timearg() {
/* ANCHOR: timearguments */
commandAPICommand("bigmsg") {
    timeArgument("duration")
    greedyStringArgument("message")
    anyExecutor { _, args ->
        // Duration in ticks
        val duration = args[0] as Int
        val message = args[1] as String

        for (player in Bukkit.getOnlinePlayers()) {
            // Display the message to all players, with the default fade in/out times (10 and 20).
            player.sendTitle(message, "", 10, duration, 20)
        }
    }
}
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
commandAPICommand("replace") {
    arguments(*arguments)
    playerExecutor { player, args ->
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
                            block.type = blockData.material
                            block.blockData = blockData
                        }
                    }
                }
            }
        }
    }
}
/* ANCHOR_END: blockpredicatearguments2 */
}

fun itemstackpredicatearguments() {
/* ANCHOR: itemstackpredicatearguments */
// Register our command
commandAPICommand("rem") {
    itemStackPredicateArgument("items")
    playerExecutor { player, args ->
        // Get our predicate
        val predicate = args[0] as Predicate<ItemStack>

        for (item in player.inventory) {
            if (predicate.test(item)) {
                player.inventory.remove(item)
            }
        }
    }
}
/* ANCHOR_END: itemstackpredicatearguments */
}

class NBTTest : JavaPlugin() {

    /* ANCHOR: nbtcompoundargumentonload */
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this)
            .initializeNBTAPI(NBTContainer::class.java, ::NBTContainer)
        )
    }
/* ANCHOR_END: nbtcompoundargumentonload */

}

@Suppress("unused")
fun nbtcompoundarguments() {
/* ANCHOR: nbtcompoundarguments */
commandAPICommand("award") {
    nbtCompoundArgument<NBTContainer>("nbt")
    anyExecutor { _, args ->
        val nbt = args[0] as NBTContainer

        // Do something with "nbt" here...
    }
}
/* ANCHOR_END: nbtcompoundarguments */
}

@Suppress("unused")
fun literalarguments() {
/* ANCHOR: literalarguments */
commandAPICommand("mycommand") {
    literalArgument("hello")
    textArgument("text")
    anyExecutor { _, args ->
        // This gives the variable "text" the contents of the TextArgument, and not the literal "hello"
        val text = args[0] as String
    }
}
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
    commandAPICommand("changegamemode") {
        literalArgument(key)
        playerExecutor { player, args ->
            // Retrieve the object from the map via the key and NOT the args[]
            player.gameMode = gamemodes[key]!!
        }
    }

}
/* ANCHOR_END: literalarguments2 */
}

fun literalArguments3() {
/* ANCHOR: literalarguments3 */
commandAPICommand("mycommand") {
    argument(LiteralArgument.of("hello"))
    textArgument("text")
    anyExecutor { _, args ->
        val text = args[0] as String
    }
}

commandAPICommand("mycommand") {
    argument(LiteralArgument.literal("hello"))
    textArgument("text")
    anyExecutor { _, args ->
        val text = args[0] as String
    }
}
/* ANCHOR_END: literalarguments3 */
}

fun multiliteralarguments() {
/* ANCHOR: multiliteralarguments */
commandAPICommand("gamemode") {
    multiLiteralArgument("adventure", "creative", "spectator", "survival")
    playerExecutor { player, args ->
        // The literal string that the player enters IS available in the args[]
        when (args[0] as String) {
            "adventure" -> player.gameMode = GameMode.ADVENTURE
            "creative" -> player.gameMode = GameMode.CREATIVE
            "spectator" -> player.gameMode = GameMode.SPECTATOR
            "survival" -> player.gameMode = GameMode.SURVIVAL
        }
    }
}
/* ANCHOR_END: multiliteralarguments */
}

fun customarguments() {
/* ANCHOR: customarguments */
commandAPICommand("tpworld") {
    worldArgument("world") // This method is actually also built into the Kotlin DSL
    playerExecutor { player, args ->
        player.teleport((args[0] as World).spawnLocation)
    }
}
/* ANCHOR_END: customarguments */
}

/* ANCHOR: customarguments2 */
// Function that returns our custom argument
fun worldArgument(nodeName: String): Argument<World> {

    // Construct our CustomArgument that takes in a String input and returns a World object
    return CustomArgument<World, String>(StringArgument(nodeName)) { info ->
        // Parse the world from our input
        val world = Bukkit.getWorld(info.input())

        if (world == null) {
            throw CustomArgumentException(MessageBuilder("Unknown world: ").appendArgInput())
        } else {
            world
        }
    }.replaceSuggestions(ArgumentSuggestions.strings { _ ->
        // List of world names on the server
        Bukkit.getWorlds().map{ it.name }.toTypedArray()
    })
}
/* ANCHOR_END: customarguments2 */

fun functionarguments() {
/* ANCHOR: functionarguments */
commandAPICommand("runfunc") {
    functionArgument("function")
    anyExecutor { _, args ->
        val functions = args[0] as Array<FunctionWrapper>
        for (function in functions) {
            function.run() // The command executor in this case is 'sender'
        }
    }
}
/* ANCHOR_END: functionarguments */
}

fun functionarguments2() {
/* ANCHOR: functionarguments2 */
commandAPICommand("runfunction") {
    functionArgument("function")
    anyExecutor { _, args ->
        val functions = args[0] as Array<FunctionWrapper>

        // Run all functions in our FunctionWrapper[]
        for (function in functions) {
            function.run()
        }
    }
}
/* ANCHOR_END: functionarguments2 */
}

fun permissions() {
/* ANCHOR: permissions */
// Register the /god command with the permission node "command.god"
commandAPICommand("god") {
    withPermission(CommandPermission.fromString("command.god"))
    playerExecutor { player, _ ->
        player.isInvulnerable = true
    }
}
/* ANCHOR_END: permissions */

/* ANCHOR: permissions2 */
// Register the /god command with the permission node "command.god", without creating a CommandPermission
commandAPICommand("god") {
    withPermission("command.god")
    playerExecutor { player, _ ->
        player.isInvulnerable = true
    }
}
/* ANCHOR_END: permissions2 */
}

fun permissions3_1() {
/* ANCHOR: permissions3_1 */
// Register /kill command normally. Since no permissions are applied, anyone can run this command
commandAPICommand("kill") {
    playerExecutor { player, _ ->
        player.health = 0.0
    }
}
/* ANCHOR_END: permissions3_1 */
}

fun permissions3_2() {
/* ANCHOR: permissions3_2 */
// Adds the OP permission to the "target" argument. The sender requires OP to execute /kill <target>
commandAPICommand("kill") {
    argument(PlayerArgument("target").withPermission(CommandPermission.OP))
    playerExecutor { _, args ->
        (args[0] as Player).health = 0.0
    }
}
/* ANCHOR_END: permissions3_2 */
}

fun permissions4() {
/* ANCHOR: permissions4 */
// /economy - requires the permission "economy.self" to execute
commandAPICommand("economy") {
    withPermission("economy.self")
    playerExecutor { player, _ ->
        // send the executor their own balance here.
    }
}

// /economy <target> - requires the permission "economy.other" to execute
commandAPICommand("economy") {
    withPermission("economy.other") // The important part of this example
    playerArgument("target")
    playerExecutor { player, args ->
        val target = args[0] as Player
        // send the executor the targets balance here.
    }
}

// /economy give <target> <amount> - requires the permission "economy.admin.give" to execute
commandAPICommand("economy") {
    withPermission("economy.admin.give") // The important part of this example
    playerArgument("target")
    doubleArgument("amount")
    playerExecutor { player, args ->
        val target = args[0] as Player
        val amount = args[1] as Double
        // update the targets balance here
    }
}

// /economy reset <target> - requires the permission "economy.admin.reset" to execute
commandAPICommand("economy") {
    withPermission("economy.admin.reset") // The important part of this example
    playerArgument("target")
    playerExecutor { player, args ->
        val target = args[0] as Player
        // reset the targets balance here
    }
}
/* ANCHOR_END: permissions4 */
}

fun aliases() {
/* ANCHOR: aliases */
commandAPICommand("getpos") {
    // Declare your aliases
    withAliases("getposition", "getloc", "getlocation", "whereami")

    // Declare your implementation
    entityExecutor { entity, _ ->
        val loc = entity.location
        entity.sendMessage("You are at ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
    }
    commandBlockExecutor { block, _ ->
        val loc = block.block.location
        block.sendMessage("You are at ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
    }
}
/* ANCHOR_END: aliases */
}

fun normalcommandexecutors() {
/* ANCHOR: normalcommandexecutors */
commandAPICommand("suicide") {
    playerExecutor { player, _ ->
        player.health = 0.0
    }
}
/* ANCHOR_END: normalcommandexecutors */
}

fun normalcommandexecutors2() {
/* ANCHOR: normalcommandexecutors2 */
commandAPICommand("suicide") {
    playerExecutor { player, _ ->
        player.health = 0.0
    }
    entityExecutor { entity, _ ->
        entity.world.createExplosion(entity.location, 4f)
        entity.remove()
    }
}
/* ANCHOR_END: normalcommandexecutors2 */
}

fun normalcommandexecutors3() {
/* ANCHOR: normalcommandexecutors3 */
commandAPICommand("suicide") {
    anyExecutor { sender, _ ->
        // ExecutorTypes are not supported in the Kotlin DSL
    }
}
/* ANCHOR_END: normalcommandexecutors3 */
}

@Suppress("deprecation")
fun normalcommandexecutors3_1() {
/* ANCHOR: normalcommandexecutors3_1 */
// Create our command
commandAPICommand("broadcastmsg") {
    withAliases("broadcast")              // Command aliases
    withPermission(CommandPermission.OP)  // Required permissions
    greedyStringArgument("message")       // The arguments
    anyExecutor { _, args ->
        val message = args[0] as String
        Bukkit.getServer().broadcastMessage(message)
    }
}
/* ANCHOR_END: normalcommandexecutors3_1 */
}

fun proxysender() {
/* ANCHOR: proxysender */
commandAPICommand("killme") {
    playerExecutor { player, _ ->
        player.health = 0.0
    }
}
/* ANCHOR_END: proxysender */
}

fun proxysender2() {
/* ANCHOR: proxysender2 */
commandAPICommand("killme") {
    playerExecutor { player, _ ->
        player.health = 0.0
    }
    proxyExecutor { proxy, _ ->
        // Check if the callee (target) is an Entity and kill it
        if (proxy.callee is LivingEntity) {
            (proxy.callee as LivingEntity).health = 0.0
        }
    }
}
/* ANCHOR_END: proxysender2 */
}

fun nativesender() {
/* ANCHOR: nativesender */
commandAPICommand("break") {
    nativeExecutor { sender, _ ->
        val location = sender.location
        location.block.breakNaturally()
    }
}
/* ANCHOR_END: nativesender */
}

fun resultingcommandexecutor() {
/* ANCHOR: resultingcommandexecutor */
commandAPICommand("randnum") {
    anyResultingExecutor { _, _ ->
        Random.nextInt()
    }
}
/* ANCHOR_END: resultingcommandexecutor */
}

fun resultingcommandexecutor2() {
/* ANCHOR: resultingcommandexecutor2 */
// Register random number generator command from 1 to 99 (inclusive)
commandAPICommand("randomnumber") {
    anyResultingExecutor { _, _ ->
        (1..100).random()
    }
}
/* ANCHOR_END: resultingcommandexecutor2 */
}

@Suppress("deprecation")
fun resultingcommandexecutor3(){
/* ANCHOR: resultingcommandexecutor3 */
// Register reward giving system for a target player
commandAPICommand("givereward") {
    entitySelectorArgumentOnePlayer("target")
    anyExecutor { _, args ->
        val player = args[0] as Player
        player.inventory.addItem(ItemStack(Material.DIAMOND, 64))
        Bukkit.broadcastMessage("${player.name} won a rare 64 diamonds from a loot box!")
    }
}
/* ANCHOR_END: resultingcommandexecutor3 */
}

fun commandfailures() {
/* ANCHOR: commandfailures */
// List of fruit
val fruit = listOf<String>("banana", "apple", "orange")

// Register the command
commandAPICommand("getfruit") {
    argument(StringArgument("item").replaceSuggestions(ArgumentSuggestions.strings(fruit)))
    anyExecutor { _, args ->
        val inputFruit = args[0] as String

        if (fruit.any { it == inputFruit }) {
            // Do something with inputFruit
        } else {
            // The sender's input is not in the list of fruit
            throw CommandAPI.failWithString("That fruit doesn't exist!")
        }
    }
}
/* ANCHOR_END: commandfailures */
}

fun argumentsyntax1() {
/* ANCHOR: argumentsyntax1 */
commandAPICommand("mycommand") {
    stringArgument("arg0")
    stringArgument("arg1")
    stringArgument("arg2")
    // And so on
}
/* ANCHOR_END: argumentsyntax1 */


/* ANCHOR: argumentsyntax2 */
commandAPICommand("mycommand") {
    arguments(StringArgument("arg0"), StringArgument("arg1"), StringArgument("arg2"))
    // And so on
}
/* ANCHOR_END: argumentsyntax2 */


/* ANCHOR: argumentsyntax3 */
val arguments = listOf(
    StringArgument("arg0"),
    StringArgument("arg1"),
    StringArgument("arg2")
)

commandAPICommand("mycommand") {
    arguments(*arguments.toTypedArray())
    // And so on
}
/* ANCHOR_END: argumentsyntax3 */
}

fun argumentsayhicmd() {
/* ANCHOR: argumentsayhicmd */
commandAPICommand("sayhi") {
    playerArgument("target", optional = true)
    playerExecutor { player, args ->
        val target: Player? = args["target"] as Player?
        if (target != null) {
            target.sendMessage("Hi!")
        } else {
            player.sendMessage("Hi!")
        }
    }
}
/* ANCHOR_END: argumentsayhicmd */

/* ANCHOR: argumentsayhicmd2 */
commandAPICommand("sayhi") {
    playerArgument("target", optional = true)
    playerExecutor { player, args ->
        val target: Player = args.getOrDefault("target", player) as Player
        target.sendMessage("Hi!")
    }
}
/* ANCHOR_END: argumentsayhicmd2 */
}

fun ratecommand() {
/* ANCHOR: argumentrate */
commandAPICommand("rate") {
    argument(StringArgument("topic").setOptional(true).combineWith(IntegerArgument("rating", 0, 10)))
    playerArgument("target", optional = true)
    anyExecutor { sender, args ->
        val topic: String? = args["topic"] as String?
        if (topic == null) {
            sender.sendMessage(
                "Usage: /rate <topic> <rating> <player>(optional)",
                "Select a topic to rate, then give a rating between 0 and 10",
                "You can optionally add a player at the end to give the rating to"
            )
            return@anyExecutor
        }

        // We know this is not null because rating is required if topic is given
        val rating = args["rating"] as Int

        // The target player is optional, so give it a default here
        val target: CommandSender = args.getOrDefault("target", sender) as CommandSender

        target.sendMessage("Your $topic was rated: $rating/10")
    }
}
/* ANCHOR_END: argumentrate */
}

@Suppress("unused")
fun argumentCasting() {
/* ANCHOR: argumentcasting */
val arguments = listOf(
    StringArgument("arg0"),
    PotionEffectArgument("arg1"),
    LocationArgument("arg2")
)

commandAPICommand("cmd") {
    arguments(*arguments.toTypedArray())
    anyExecutor { _, args ->
        val stringArg = args[0] as String
        val potionArg = args[1] as PotionEffectType
        val locationArg = args[2] as Location
    }
}
/* ANCHOR_END: argumentcasting */
}

fun requirements() {
/* ANCHOR: requirements */
commandAPICommand("repair", {sender: CommandSender -> (sender as Player).level >= 30}) {
    playerExecutor { player, _ ->
        // Repair the item back to full durability
        val item = player.inventory.itemInMainHand
        val itemMeta = item.itemMeta
        if (itemMeta is Damageable) {
            itemMeta.damage = 0
            item.itemMeta = itemMeta
        }

        // Subtract 30 levels
        player.level = player.level - 30
    }
}
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
commandAPICommand("party") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        // Get the name of the party to create
        val partyName = args[0] as String

        partyMembers[player.uniqueId] = partyName
    }
}
/* ANCHOR_END: requirements3 */

/* ANCHOR: requirementstp */
/* ANCHOR: requirements4 */
arguments = mutableListOf<Argument<*>>()
arguments.add(LiteralArgument("tp")
    .withRequirement { partyMembers.containsKey((it as Player).uniqueId) }
)
/* ANCHOR_END: requirementstp */

arguments.add(PlayerArgument("player")
    .replaceSafeSuggestions(SafeSuggestions.suggest { info ->

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
                    if (target.isOnline) {
                        // Add them if they are online
                        playersToTeleportTo.add(target)
                    }
                }
            }
        }

        playersToTeleportTo.toTypedArray()
    }))
/* ANCHOR_END: requirements4 */

/* ANCHOR: requirements5 */
commandAPICommand("party") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        val target = args[0] as Player
        player.teleport(target)
    }
}
/* ANCHOR_END: requirements5 */

/* ANCHOR: updatingrequirements */
commandAPICommand("party") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        // Get the name of the party to create
        val partyName = args[0] as String

        partyMembers[player.uniqueId] = partyName

        CommandAPI.updateRequirements(player)
    }
}
/* ANCHOR_END: updatingrequirements */
}

fun multiplerequirements() {
/* ANCHOR: multiplerequirements */
commandAPICommand("someCommand") {
    withRequirement { (it as Player).level >= 30 }
    withRequirement { (it as Player).inventory.contains(Material.DIAMOND_PICKAXE) }
    withRequirement { (it as Player).isInvulnerable }
    playerExecutor { player, args ->
        // Code goes here
    }
}
/* ANCHOR_END: multiplerequirements */
}

@Suppress("UNUSED_PARAMETER")
fun subcommands() {

/* ANCHOR: subcommandspart */
val groupAdd = subcommand("add") {
    stringArgument("permission")
    stringArgument("groupName")
    anyExecutor { sender, args ->
        // perm group add code
    }
}
/* ANCHOR_END: subcommandspart */
/* ANCHOR: subcommands */
val groupRemove = subcommand("remove") {
    stringArgument("permission")
    stringArgument("groupName")
    anyExecutor { sender, args ->
        // perm group remove code
    }
}

val group = subcommand("group") {
    subcommand(groupAdd)
    subcommand(groupRemove)
}
/* ANCHOR_END: subcommands */
/* ANCHOR: subcommandsend */
commandAPICommand("perm") {
    subcommand(group)
}
/* ANCHOR_END: subcommandsend */
/* ANCHOR: subcommands1 */
commandAPICommand("perm") {
    subcommand("group") {
        subcommand("add") {
            stringArgument("permission")
            stringArgument("groupName")
            anyExecutor { sender, args ->
                // perm group add code
            }
        }
        subcommand("remove") {
            stringArgument("permission")
            stringArgument("groupName")
            anyExecutor { sender, args ->
                // perm group remove code
            }
        }
    }
    subcommand("user") {
        subcommand("add") {
            stringArgument("permission")
            stringArgument("userName")
            anyExecutor { sender, args ->
                // perm user add code
            }
        }
        subcommand("remove") {
            stringArgument("permission")
            stringArgument("userName")
            anyExecutor { sender, args ->
                // perm user add code
            }
        }
    }
}
/* ANCHOR_END: subcommands1 */
}

@Suppress("deprecation")
fun help() {
/* ANCHOR: help */
commandAPICommand("mycmd") {
    withShortDescription("Says hi")
    withFullDescription("Broadcasts ho to everyone on the server")
    anyExecutor { _, _ ->
        Bukkit.broadcastMessage("Hi!")
    }
}
/* ANCHOR_END: help */

/* ANCHOR: help2 */
commandAPICommand("mycmd") {
    withHelp("Says hi", "Broadcasts hi to everyone on the server")
    anyExecutor { _, _ ->
        Bukkit.broadcastMessage("Hi!")
    }
}

/* ANCHOR_END: help2 */
}

fun anglearguments() {
// TODO: This example isn't used!
/* ANCHOR: anglearguments */
commandAPICommand("yaw") {
    angleArgument("amount")
    playerExecutor { player, args ->
        val newLocation = player.location
        newLocation.yaw = args[0] as Float
        player.teleport(newLocation)
    }
}
/* ANCHOR_END: anglearguments */
}

fun listed() {
/* ANCHOR: listed */
commandAPICommand("mycommand") {
    playerArgument("player")
    argument(IntegerArgument("value").setListed(false))
    greedyStringArgument("message")
    anyExecutor { _, args ->
        // args == [player, message]
        val player = args[0] as Player
        val message = args[1] as String // Note that this is args[1] and NOT args[2]
        player.sendMessage(message)
    }
}
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
commandAPICommand("emote") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        val emote = args[0] as String
        val target = args[1] as Player

        when (emote) {
            "wave" -> target.sendMessage("${player.name} waves at you!")
            "hug" -> target.sendMessage("${player.name} hugs you!")
            "glare" -> target.sendMessage("${player.name} gives you the death glare...")
        }
    }
}
/* ANCHOR_END: Tooltips2 */
}

fun tooltips4() {
/* ANCHOR: Tooltips4 */
val customItems = arrayOf<CustomItem>(
    CustomItem(ItemStack(Material.DIAMOND_SWORD), "God sword", "A sword from the heavens"),
    CustomItem(ItemStack(Material.PUMPKIN_PIE), "Sweet pie", "Just like grandma used to make")
)

commandAPICommand("giveitem") {
    argument(StringArgument("item").replaceSuggestions(ArgumentSuggestions.stringsWithTooltips(*customItems))) // We use customItems[] as the input for our suggestions with tooltips
    playerExecutor { player, args ->
        val itemName = args[0] as String

        // Give them the item
        for (item in customItems) {
            if (item.name == itemName) {
                player.inventory.addItem(item.item)
                break
            }
        }
    }
}
/* ANCHOR_END: Tooltips4 */
}

fun safetooltips() {
/* ANCHOR: SafeTooltips */
val arguments = listOf<Argument<*>>(
    LocationArgument("location")
        .replaceSafeSuggestions(SafeSuggestions.tooltips { info ->
            // We know the sender is a player if we use .executesPlayer()
            val player = info.sender() as Player
            BukkitTooltip.arrayOf(
                BukkitTooltip.ofString(player.world.spawnLocation, "World spawn"),
                BukkitTooltip.ofString(player.bedSpawnLocation, "Your bed"),
                BukkitTooltip.ofString(player.getTargetBlockExact(256)?.location, "Target block")
            )
        })
)
/* ANCHOR_END: SafeTooltips */
/* ANCHOR: SafeTooltips2 */
commandAPICommand("warp") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        player.teleport(args[0] as Location)
    }
}
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
commandAPICommand("localmsg") {
    arguments(*arguments.toTypedArray())
    playerExecutor { _, args ->
        val target = args[1] as Player
        val message = args[2] as String
        target.sendMessage(message)
    }
}
/* ANCHOR_END: ArgumentSuggestionsPrevious */
}

fun argumentsuggestions2_2() {
/* ANCHOR: ArgumentSuggestions2_2 */
val arguments = listOf<Argument<*>>(
    PlayerArgument("friend").replaceSuggestions(ArgumentSuggestions.strings { info ->
        Friends.getFriends(info.sender())
    })
)

commandAPICommand("friendtp") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        val target = args[0] as Player
        player.teleport(target)
    }
}
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

commandAPICommand("warp") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        val warp = args[0] as String
        player.teleport(warps[warp]!!) // Look up the warp in a map, for example
    }
}
/* ANCHOR_END: ArgumentSuggestions1 */
}

@Suppress("deprecation")
fun SafeRecipeArguments() {
/* ANCHOR: SafeRecipeArguments */
// Create our itemstack
val emeraldSword = ItemStack(Material.DIAMOND_SWORD)
val meta = emeraldSword.itemMeta
meta?.setDisplayName("Emerald Sword")
meta?.isUnbreakable = true
emeraldSword.itemMeta = meta

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
server.addRecipe(emeraldSwordRecipe)

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
commandAPICommand("giverecipe") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        val recipe = args[0] as Recipe
        player.inventory.addItem(recipe.result)
    }
}
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
    EntityTypeArgument("mob").replaceSafeSuggestions(SafeSuggestions.suggest { info ->
        if (info.sender().isOp) {
            // All entity types
            EntityType.values()
        } else {
            // Only allowedMobs
            allowedMobs.toTypedArray()
        }
    })
)
/* ANCHOR_END: SafeMobSpawnArguments_2 */

/* ANCHOR: SafeMobSpawnArguments_3 */
commandAPICommand("spawnmob") {
    arguments(*arguments.toTypedArray())
    playerExecutor { player, args ->
        val entityType = args[0] as EntityType
        player.world.spawnEntity(player.location, entityType)
    }
}
/* ANCHOR_END: SafeMobSpawnArguments_3 */
}

fun safepotionarguments() {
/* ANCHOR: SafePotionArguments */
val arguments = mutableListOf<Argument<*>>()
arguments.add(EntitySelectorArgument.OnePlayer("target"))
arguments.add(PotionEffectArgument("potioneffect").replaceSafeSuggestions(SafeSuggestions.suggest { info ->
    val target = info.previousArgs()[0] as Player

    // Convert PotionEffect[] into PotionEffectType[]
    target.activePotionEffects.map{ it.type }.toTypedArray()
}))
/* ANCHOR_END: SafePotionArguments */

/* ANCHOR: SafePotionArguments_2 */
commandAPICommand("removeeffect") {
    arguments(*arguments.toTypedArray())
    playerExecutor { _, args ->
        val target = args[0] as Player
        val potionEffect = args[1] as PotionEffectType
        target.removePotionEffect(potionEffect)
    }
}
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

commandAPICommand("concept") {
    stringArgument("text")
    argument(StringArgument("input").replaceSuggestions(ArgumentSuggestions.strings { info ->
        println(info.currentArg)
        println(info.currentInput)

        fruits.filter { it.lowercase().startsWith(info.currentArg().lowercase()) }.toTypedArray()
    }))
    integerArgument("int")
    anyExecutor { _, _ ->
        // stuff
    }
}
}

fun commandapiconfigsilent() {
val plugin: JavaPlugin = this

/* ANCHOR: CommandAPIConfigSilent */
CommandAPI.onLoad(CommandAPIBukkitConfig(plugin).silentLogs(true))
/* ANCHOR_END: CommandAPIConfigSilent */
}

fun asyncreadfile() {

val plugin: JavaPlugin = object: JavaPlugin() {}
/* ANCHOR: asyncreadfile */
commandAPICommand("setconfig") {
    argument(StringArgument("key").replaceSuggestions(ArgumentSuggestions.stringsAsync {
        CompletableFuture.supplyAsync { plugin.config.getKeys(false).toTypedArray() }
    }))
    textArgument("value")
    anyExecutor { _, args ->
        val key = args[0] as String
        val value = args[1] as String
        plugin.config.set(key, value)
    }
}
/* ANCHOR_END: asyncreadfile */
}

fun listargument() {

/* ANCHOR: ListArgument_MultiGive */
commandAPICommand("multigive") {
    integerArgument("amount", 1, 64)
    argument(ListArgumentBuilder<Material>("materials")
        .withList(Material.values().toList())
        .withMapper { material -> material.name.lowercase() }
        .buildGreedy()
    )
    playerExecutor { player, args ->
        val amount = args[0] as Int
        val theList = args[1] as List<Material>

        for (item in theList) {
            player.inventory.addItem(ItemStack(item, amount))
        }
    }
}
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
commandAPICommand("commandargument") {
    argument(GreedyStringArgument("command").replaceSuggestions(commandSuggestions))
    anyExecutor { sender, args ->
        // Run the command using Bukkit.dispatchCommand()
        Bukkit.dispatchCommand(sender, args[0] as String)
    }
}
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
        val newBuilder = builder.createOffset(builder.start + info.currentArg().length);

        // Suggest all the emojis!
        emojis.forEach { (emoji, description) ->
            newBuilder.suggest(emoji, LiteralMessage(description));
        }

        newBuilder.buildFuture()
    }

commandAPICommand("emoji") {
    argument(messageArgument)
    anyExecutor { _, args ->
        Bukkit.broadcastMessage(args[0] as String)
    }
}
/* ANCHOR_END: BrigadierSuggestions3 */
}

// TODO: This example isn't used in the documentation!
fun treeexample() {
commandTree("teeexample") {
    // Set the aliases as you normally would
    withAliases("treealias")
    // Set an executor on the command itself
    anyExecutor { sender, _ ->
        sender.sendMessage("Root with no arguments")
    }
    // Create a new branch starting with a the literal 'integer'
    literalArgument("integer") {
        // Execute on the literal itself
        anyExecutor { sender, _ ->
            sender.sendMessage("Integer Branch with no arguments")
        }
        // Create a further branch starting with an integer argument, which executes a command
        integerArgument("integer") {
            anyExecutor { sender, args ->
                sender.sendMessage("Integer Branch with integer argument: ${args[0]}")
            }
        }
    }
    literalArgument("biome") {
        anyExecutor { sender, _ ->
            sender.sendMessage("Biome Branch with no arguments")
        }
        biomeArgument("biome") {
            anyExecutor { sender, args ->
                sender.sendMessage("Biome Branch with biome argument: ${args[0]}")
            }
        }
    }
    literalArgument("string") {
        anyExecutor { sender, _ ->
            sender.sendMessage("String Branch with no arguments")
        }
        stringArgument("string") {
            anyExecutor { sender, args ->
                sender.sendMessage("String Branch with string argument: ${args[0]}")
            }
        }
    }
}

/* ANCHOR: CommandTree_sayhi1 */
commandTree("sayhi") {
    anyExecutor { sender, _ ->
        sender.sendMessage("Hi!")
    }
    playerArgument("target") {
        anyExecutor { _, args ->
            val target = args[0] as Player
            target.sendMessage("Hi")
        }
    }
}
/* ANCHOR_END: CommandTree_sayhi1 */
}

@Suppress("deprecation")
fun signedit() {
val plugin = this
/* ANCHOR: CommandTree_signedit */
commandTree("signedit") {
    literalArgument("set") {
        integerArgument("line_number", 1, 4) {
            greedyStringArgument("text") {
                playerExecutor { player, args ->
                    // /signedit set <line_number> <text>
                    val sign: Sign = getTargetSign(player)
                    val line_number = args[0] as Int
                    val text = args[1] as String
                    sign.setLine(line_number - 1, text)
                    sign.update(true)
                }
            }
        }
    }
    literalArgument("clear") {
        integerArgument("line_number", 1, 4) {
            playerExecutor { player, args ->
                // /signedit clear <line_number>
                val sign: Sign = getTargetSign(player)
                val line_number = args[0] as Int
                sign.setLine(line_number - 1, "")
                sign.update(true)
            }
        }
    }
    literalArgument("copy") {
        integerArgument("line_number", 1, 4) {
            playerExecutor { player, args ->
                // /signedit copy <line_number>
                val sign: Sign = getTargetSign(player)
                val line_number = args[0] as Int
                player.setMetadata("copied_sign_text", FixedMetadataValue(plugin, sign.getLine(line_number - 1)))
            }
        }
    }
    literalArgument("paste") {
        integerArgument("line_number", 1, 4) {
            playerExecutor { player, args ->
                // /signedit copy <line_number>
                val sign: Sign = getTargetSign(player)
                val line_number = args[0] as Int
                sign.setLine(line_number - 1, player.getMetadata("copied_sign_text")[0].asString())
                sign.update(true)
            }
        }
    }
}
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
commandAPICommand("sudo") {
    playerArgument("target")
    commandArgument("command")
    anyExecutor { _, args ->
        val target = args[0] as Player
        val command = args[1] as CommandResult

        command.execute(target)
    }
}
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
}

/* ANCHOR: functionregistration */
class MainKotlinDSL : JavaPlugin() {

    override fun onLoad() {
        // Commands which will be used in Minecraft functions are registered here

        commandAPICommand("killall") {
            anyExecutor { _, _ ->
                // Kills all enemies in all worlds
                Bukkit.getWorlds().forEach { world -> world.livingEntities.forEach { entity -> entity.health = 0.0 } }
            }
        }

    }

    override fun onEnable() {
        // Register all other commands here
    }

}
/* ANCHOR_END: functionregistration */

/* ANCHOR: shading */
class MyPluginKotlinDSL : JavaPlugin() {

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true)) // Load with verbose output

        commandAPICommand("ping") {
            anyExecutor { sender, _ ->
                sender.sendMessage("pong!")
            }
        }
    }

    override fun onEnable() {
        CommandAPI.onEnable()

        // Register commands, listeners etc.
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

}
/* ANCHOR_END: shading */