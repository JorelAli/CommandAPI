package dev.jorel.commandapi.nms;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R1.CraftServer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import net.minecraft.server.v1_16_R1.CommandDispatcher.ServerType;
import net.minecraft.server.v1_16_R1.CustomFunctionManager;
import net.minecraft.server.v1_16_R1.DataPackConfiguration;
import net.minecraft.server.v1_16_R1.DataPackResources;
import net.minecraft.server.v1_16_R1.DedicatedServer;
import net.minecraft.server.v1_16_R1.DedicatedServerSettings;
import net.minecraft.server.v1_16_R1.IReloadableResourceManager;
import net.minecraft.server.v1_16_R1.IResourcePack;
import net.minecraft.server.v1_16_R1.PackSource;
import net.minecraft.server.v1_16_R1.ResourcePackLoader;
import net.minecraft.server.v1_16_R1.ResourcePackRepository;
import net.minecraft.server.v1_16_R1.ResourcePackSource;
import net.minecraft.server.v1_16_R1.ResourcePackSourceFolder;
import net.minecraft.server.v1_16_R1.ResourcePackSourceVanilla;
import net.minecraft.server.v1_16_R1.SavedFile;
import net.minecraft.server.v1_16_R1.SystemUtils;
import net.minecraft.server.v1_16_R1.Unit;

public class CommandAPIRP {

	static Logger LOGGER = LogManager.getLogger();

	public static void setDataPack(DataPackConfiguration datapackconfiguration, DataPackResources datapackresources) {
		CraftServer server = (CraftServer) Bukkit.getServer();
		server.getHandle().getServer().datapackconfiguration = datapackconfiguration;
		server.getHandle().getServer().dataPackResources = datapackresources;
	}

	@SuppressWarnings("resource")
	public static void invoke() throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		DedicatedServer server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();

		{

			LOGGER.info("Using default (constructed) datapackconfiguration");
			//Construct our datapackconfiguration
//			File root = (File) server.options.valueOf("universe");
//			Convertable convertable = Convertable.a(root.toPath());
//			String mainWorldName = (String) Optional.ofNullable(server.options.valueOf("world"))
//					.orElse(server.propertyManager.getProperties().levelName);
//			ConversionSession convertable_conversionsession = convertable.c(mainWorldName, WorldDimension.OVERWORLD);
			DataPackConfiguration datapackconfiguration = server.convertable.e(); //convertable_conversionsession.e();
//			LOGGER.info("datapackconfiguration constructed!");
			
			LOGGER.info("Loading resourcepack repo");
			ResourcePackRepository<ResourcePackLoader> resourcepackrepository = new ResourcePackRepository<ResourcePackLoader>(
					ResourcePackLoader::new,
					new ResourcePackSource[] { 
							new ResourcePackSourceVanilla(), 
//							new ResourcePackSourceFolder(convertable_conversionsession.getWorldFolder(SavedFile.DATAPACKS).toFile(), PackSource.c) 
							new ResourcePackSourceFolder(server.convertable.getWorldFolder(SavedFile.DATAPACKS).toFile(), PackSource.b)
					});

//			ResourcePackRepository<ResourcePackLoader> resourcepackrepository = null;
//			DataPackConfiguration datapackconfiguration = null;
			boolean flag = false;

			DataPackConfiguration result;

			resourcepackrepository.a();
			if (flag) {
				resourcepackrepository.a(Collections.singleton("vanilla"));
				result = new DataPackConfiguration(ImmutableList.of("vanilla"), ImmutableList.of());
			} else {
				Set<String> set = Sets.newLinkedHashSet();
				Iterator iterator = datapackconfiguration.a().iterator();

				LOGGER.info("Adding resourcepacks to something");
				while (iterator.hasNext()) {
					String s = (String) iterator.next();
					if (resourcepackrepository.b(s)) {
						LOGGER.info("Added " + s);
						set.add(s);
					} else {
						LOGGER.warn("Missing data pack {}", s);
					}
				}
				LOGGER.info("Finished adding resourcepacks");
				
				iterator = resourcepackrepository.c().iterator();

				while (iterator.hasNext()) {
					ResourcePackLoader resourcepackloader = (ResourcePackLoader) iterator.next();
					
					String s1 = resourcepackloader.e();
					LOGGER.info("Loading resourcepack " + s1);
					if (!datapackconfiguration.b().contains(s1) && !set.contains(s1)) {
						LOGGER.info("Found new data pack {}, loading it automatically", s1);
						set.add(s1);
					}
				}

				if (set.isEmpty()) {
					LOGGER.info("No datapacks selected, forcing vanilla");
					set.add("vanilla");
				}

				resourcepackrepository.a(set);

				Collection<String> collection = resourcepackrepository.d();
				List<String> list = ImmutableList.copyOf(collection);
				List<String> list1 = resourcepackrepository.b().stream().filter((s) -> {
					return !collection.contains(s);
				}).collect(ImmutableList.toImmutableList());
				result = new DataPackConfiguration(list, list1);
				LOGGER.info("Constructed new dpc: " + list + ":" + list1);

				DedicatedServerSettings dedicatedserversettings = server.propertyManager;


				DataPackResources raw = server.dataPackResources;
				raw.commandDispatcher = server.getCommandDispatcher();
				Field i = DataPackResources.class.getDeclaredField("i");
				i.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(i, i.getModifiers() & ~Modifier.FINAL);
				
				Field f = CustomFunctionManager.class.getDeclaredField("f");
				f.setAccessible(true);
				
						//raw.a().f;
				
				i.set(raw, new CustomFunctionManager((int)f.get(raw.a()), raw.commandDispatcher.a()));

//				CompletableFuture completablefuture = DataPackResources.a(resourcepackrepository.f(),
//						ServerType.DEDICATED, dedicatedserversettings.getProperties().functionPermissionLevel,
//						SystemUtils.f(), Runnable::run);
				
				DataPackResources datapackResources = raw;//new DataPackResources(ServerType.DEDICATED, dedicatedserversettings.getProperties().functionPermissionLevel);
				Field b = DataPackResources.class.getDeclaredField("b");
				b.setAccessible(true);
				Field a = DataPackResources.class.getDeclaredField("a");
				a.setAccessible(true);
				LOGGER.info("a: " + (CompletableFuture<Unit>) a.get(null));
				CompletableFuture<Unit> unitCompletableFuture = ((IReloadableResourceManager)b.get(datapackResources)).a(SystemUtils.f(), Runnable::run, resourcepackrepository.f(), (CompletableFuture<Unit>) a.get(null));
				CompletableFuture<DataPackResources> completablefuture = unitCompletableFuture.whenComplete((Unit u, Throwable t) -> {
					if (t != null) {
						datapackResources.close();
					}

				}).thenApply((Unit u) -> {
					return datapackResources;
				});

				DataPackResources datapackresources;
				try {
					LOGGER.info("Running completablefuture");

					datapackresources = (DataPackResources) completablefuture.get();
					LOGGER.info("Finished completablefuture");

				} catch (Exception e) {
					LOGGER.warn(
							"Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode",
							e);
					resourcepackrepository.close();
					return;
				}
				datapackresources.i();

				setDataPack(datapackconfiguration, datapackresources);
			}
		}
	}
}
