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
package dev.jorel.commandapi.nms;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.mojang.brigadier.CommandDispatcher;
import dev.jorel.commandapi.CommandAPIBukkit;
import dev.jorel.commandapi.CommandAPIHandler;
import dev.jorel.commandapi.preprocessor.RequireField;
import net.minecraft.commands.CommandSourceStack;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Recipe;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.preprocessor.NMSMeta;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;

/**
 * NMS implementation for Minecraft 1.17
 */
@NMSMeta(compatibleWith = { "1.17" })
@RequireField(in = ServerFunctionLibrary.class, name = "dispatcher", ofType = CommandDispatcher.class)
public class NMS_1_17 extends NMS_1_17_Common {

	private static final Field serverFunctionLibraryDispatcher;

	// Compute all var handles all in one go so we don't do this during main server
	// runtime
	static {
		// For some reason, MethodHandles fails for this field, but Field works okay
		serverFunctionLibraryDispatcher = CommandAPIHandler.getField(ServerFunctionLibrary.class, "i", "dispatcher");
	}

	@Override
	public String[] compatibleVersions() {
		return new String[] { "1.17" };
	}

	@Override
	public void reloadDataPacks() {
		CommandAPI.logNormal("Reloading datapacks...");

		// Get previously declared recipes to be re-registered later
		Iterator<Recipe> recipes = Bukkit.recipeIterator();

		// Update the commandDispatcher with the current server's commandDispatcher
		ServerResources serverResources = this.<MinecraftServer>getMinecraftServer().resources;
		serverResources.commands = this.<MinecraftServer>getMinecraftServer().getCommands();

		// Update the ServerFunctionLibrary's command dispatcher with the new one
		try {
			serverFunctionLibraryDispatcher.set(serverResources.getFunctionLibrary(),
				CommandAPIBukkit.<CommandSourceStack>get().getBrigadierDispatcher());
		} catch (IllegalAccessException ignored) {
			// Shouldn't happen, CommandAPIHandler#getField makes it accessible
		}

		// Construct the new CompletableFuture that now uses our updated serverResources
		CompletableFuture<?> unitCompletableFuture = ((ReloadableResourceManager) serverResources.getResourceManager())
			.reload(Util.backgroundExecutor(), Runnable::run,
				this.<MinecraftServer>getMinecraftServer().getPackRepository().openAllSelected(),
				CompletableFuture.completedFuture(null));
		CompletableFuture<ServerResources> completablefuture = unitCompletableFuture
			.whenComplete((Object u, Throwable t) -> {
				if (t != null) {
					serverResources.close();
				}
			}).thenApply((Object u) -> serverResources);

		// Run the completableFuture and bind tags
		try {
			completablefuture.get().updateGlobals();

			// Register recipes again because reloading datapacks
			// removes all non-vanilla recipes
			CommandAPIBukkit.get().registerBukkitRecipesSafely(recipes);

			CommandAPI.logNormal("Finished reloading datapacks");
		} catch (InterruptedException e) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			CommandAPI.logError(
				"Failed to load datapacks, can't proceed with normal server load procedure. Try fixing your datapacks?\n"
					+ stringWriter.toString());
		
			// (╯°□°)╯︵ ┻━┻
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);

			CommandAPI.logError(
				"Failed to load datapacks, can't proceed with normal server load procedure. Try fixing your datapacks?\n"
					+ stringWriter.toString());
		}
	}
}
