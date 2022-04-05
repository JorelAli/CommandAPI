/*******************************************************************************
 * Copyright 2018, 2021 Jorel Ali (Skepter) - MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *******************************************************************************/
package dev.jorel.commandapi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.RegexArgument;

public class CommandAPIMain extends JavaPlugin {
	
	@Override
	public void onLoad() {		
		//Config loading
		saveDefaultConfig();
		CommandAPI.config = new Config(getConfig());
		CommandAPI.dispatcherFile = new File(getDataFolder(), "command_registration.json");
		CommandAPI.logger = getLogger();
		
		//Check dependencies for CommandAPI
		CommandAPIHandler.getInstance().checkDependencies();
		
		//Convert all plugins to be converted
		for(Entry<JavaPlugin, String[]> pluginToConvert : CommandAPI.config.getPluginsToConvert()) {
			if(pluginToConvert.getValue().length == 0) {
				Converter.convert(pluginToConvert.getKey());
			} else {
				for(String command : pluginToConvert.getValue()) {
					new AdvancedConverter(pluginToConvert.getKey(), command).convert();
				}
			}
		}
		
		// Convert all arbitrary commands		
		for(String commandName : CommandAPI.config.getCommandsToConvert()) {
			new AdvancedConverter(commandName).convertCommand();
		}

		CommandAPIHandler.getInstance().NMS.registerRegexArgument();
	}
	
	@Override
	public void onEnable() {
		CommandAPI.onEnable(this);
		
		Argument materialArg = new RegexArgument("types", "[a-zA-Z0-9_]+(,[a-zA-Z0-9_]+)*").replaceSuggestions(ArgumentSuggestions.strings(info -> {
			// Make a modifiable list of all materials
			List<String> materialList = new ArrayList<>(Arrays.stream(Material.values()).map(Material::name).map(String::toLowerCase).toList());
			String[] currentMaterials = info.currentArg().split(",");
			
			if(info.currentArg().endsWith(",")) {
				// We're expecting a list of new materials to suggest. Suggest a new material
				// that is not currently in the list
				List<String> existingMaterials = Arrays.stream(currentMaterials).map(String::toLowerCase).toList();
				materialList.removeAll(existingMaterials);
				
				// Suggest <currentarg>,<material> for each material
				return materialList.stream().map(mat -> info.currentArg() + mat).toArray(String[]::new);
			} else {
				// We're expecting some auto-completion for the current material. Perform a search
				// of what we're currently typing
				
				// Remove the last argument and turn it into a string as the base for suggestions
				List<String> currentArgList = new ArrayList<>(Arrays.asList(currentMaterials));
				currentArgList.remove(currentArgList.size() - 1);
				String suggestionBase = currentArgList.isEmpty() ? "" : currentArgList.stream().collect(Collectors.joining(",")) + ",";
				
				return materialList.stream()
					.filter(mat -> mat.startsWith(currentMaterials[currentMaterials.length - 1].toLowerCase()))
					.map(mat -> suggestionBase + mat)
					.toArray(String[]::new);
			}
		}));
		
		new CommandAPICommand("materials")
			.withArguments(materialArg)
			.withArguments(new IntegerArgument("amount"))
			.executesPlayer((player, args) -> {
				String materials = (String) args[0];
				int amount = (int) args[1];
				for(String str : materials.split(",")) {
					player.getInventory().addItem(new ItemStack(Material.valueOf(str.toUpperCase()), amount));
				}
			})
			.register();
		
		// /materials dirt,acacia_boat,acacia_button,acacia_fence,acacia_fence_gate,acacia_leaves 200
	}
}
