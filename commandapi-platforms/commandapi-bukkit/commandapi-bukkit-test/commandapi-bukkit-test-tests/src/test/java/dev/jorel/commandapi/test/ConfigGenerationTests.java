package dev.jorel.commandapi.test;

import dev.jorel.commandapi.config.BukkitConfigurationAdapter;
import dev.jorel.commandapi.config.CommentedConfigOption;
import dev.jorel.commandapi.config.CommentedSection;
import dev.jorel.commandapi.config.ConfigGenerator;
import dev.jorel.commandapi.config.ConfigurationAdapter;
import dev.jorel.commandapi.config.DefaultBukkitConfig;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ConfigGenerationTests {

	private CommentedConfigOption<Boolean> silentLogs;
	private CommentedConfigOption<Boolean> verboseOutputs;
	private CommentedConfigOption<String> missingExecutorImplementation;

	private CommentedSection messages;

	private ConfigGenerator generator;
	private DefaultBukkitConfig bukkitConfig;
	private BukkitConfigurationAdapter adapter;

	@BeforeEach
	public void setup() {
		silentLogs = new CommentedConfigOption<>(new String[]{
			"Silent logs (default: false)",
			"If \"true\", turns off all logging from the CommandAPI, except for errors."
		}, false);
		verboseOutputs = new CommentedConfigOption<>(new String[]{
			"Verbose outputs (default: false)",
			"If \"true\", outputs command registration and unregistration logs in the console"
		}, false);
		missingExecutorImplementation = new CommentedConfigOption<>(new String[]{
			"Missing executor implementation (default: \"This command has no implementations for %s\")",
			"The message to display to senders when a command has no executor. Available",
			"parameters are:",
			"  %s - the executor class (lowercase)",
			"  %S - the executor class (normal case)"
		}, "This command has no implementations for %s");

		messages = new CommentedSection(new String[]{
			"Messages",
			"Controls messages that the CommandAPI displays to players"
		});

		Map<String, CommentedConfigOption<?>> options = new LinkedHashMap<>();
		options.put("silent-logs", silentLogs);
		options.put("verbose-outputs", verboseOutputs);
		options.put("messages.missing-executor-implementation", missingExecutorImplementation);

		Map<String, CommentedSection> sections = new LinkedHashMap<>();
		sections.put("messages", messages);

		ConfigurationAdapter<YamlConfiguration> adapter = new BukkitConfigurationAdapter(new YamlConfiguration());
		bukkitConfig = DefaultBukkitConfig.create(options, sections);
		generator = ConfigGenerator.createNew(bukkitConfig);
		this.adapter = (BukkitConfigurationAdapter) generator.generate(adapter);
	}

	@AfterEach
	public void reset() {
		this.silentLogs = null;
		this.verboseOutputs = null;
		this.missingExecutorImplementation = null;
		this.messages = null;
		this.generator = null;
		this.bukkitConfig = null;
		this.adapter = null;
	}

	@Test
	public void testDefaultConfigOptionGeneration() {
		validateConfigOptions(Set.of(
			"silent-logs", "verbose-outputs", "messages.missing-executor-implementation"
		), adapter);
	}

	@Test
	public void testDefaultConfigOptionCommentGeneration() {
		validateConfigOptionComments(Map.ofEntries(
			Map.entry("silent-logs", silentLogs.comment()),
			Map.entry("verbose-outputs", verboseOutputs.comment()),
			Map.entry("messages.missing-executor-implementation", missingExecutorImplementation.comment()),
			Map.entry("messages", messages.comment())
		), adapter);
	}

	@Test
	public void testConfigOptionAddition() {
		CommentedConfigOption<Boolean> createDispatcherJson = new CommentedConfigOption<>(new String[] {
			"Create dispatcher JSON (default: false)",
			"If \"true\", the CommandAPI creates a command_registration.json file showing the",
			"mapping of registered commands. This is designed to be used by developers -",
			"setting this to \"false\" will improve command registration performance."
		}, false);

		bukkitConfig.getAllOptions().put("create-dispatcher-json", createDispatcherJson);
		generator = ConfigGenerator.createNew(bukkitConfig);
		BukkitConfigurationAdapter updatedAdapter = (BukkitConfigurationAdapter) generator.generate(adapter);

		validateConfigOptions(Set.of(
			"silent-logs", "verbose-outputs", "messages.missing-executor-implementation", "create-dispatcher-json"
		), updatedAdapter);

		validateConfigOptionComments(Map.ofEntries(
			Map.entry("silent-logs", silentLogs.comment()),
			Map.entry("verbose-outputs", verboseOutputs.comment()),
			Map.entry("messages.missing-executor-implementation", missingExecutorImplementation.comment()),
			Map.entry("create-dispatcher-json", createDispatcherJson.comment()),
			Map.entry("messages", messages.comment())
		), updatedAdapter);
	}

	@Test
	public void testConfigOptionDeletion() {
		bukkitConfig.getAllOptions().remove("silent-logs");
		generator = ConfigGenerator.createNew(bukkitConfig);
		BukkitConfigurationAdapter updatedAdapter = (BukkitConfigurationAdapter) generator.generate(adapter);

		validateConfigOptionsAbsent(Set.of("silent-logs"), updatedAdapter);
	}

	@Test
	public void testConfigOptionCommentUpdate() {
		silentLogs = new CommentedConfigOption<>(new String[] {
			"Defines if silent logs should happen"
		}, false);

		bukkitConfig.getAllOptions().put("silent-logs", silentLogs);
		generator = ConfigGenerator.createNew(bukkitConfig);
		BukkitConfigurationAdapter updatedAdapter = (BukkitConfigurationAdapter) generator.generate(adapter);

		validateConfigOptionComments(Map.ofEntries(
			Map.entry("silent-logs", silentLogs.comment())
		), updatedAdapter);
	}

	// Test methods
	public void validateConfigOptions(Set<String> options, BukkitConfigurationAdapter adapter) {
		boolean containsAll;
		for (String option : options) {
			containsAll = adapter.contains(option);
			if (!containsAll) {
				throw new IllegalStateException("Config option '" + option + "' does not exist!");
			}
		}
	}

	public void validateConfigOptionComments(Map<String, String[]> comments, BukkitConfigurationAdapter adapter) {
		boolean correctComment;
		for (String option : comments.keySet()) {
			String[] generatedComment = adapter.getComment(option);
			correctComment = Arrays.equals(comments.get(option), generatedComment);
			if (!correctComment) {
				throw new IllegalStateException("Config option comment for option '" + option + "' does not exist or was incorrect!");
			}
		}
	}

	public void validateConfigOptionsAbsent(Set<String> options, BukkitConfigurationAdapter adapter) {
		boolean isAbsent;
		for (String option : options) {
			isAbsent = !adapter.contains(option);
			if (!isAbsent) {
				throw new IllegalStateException("Config option '" + option + "' does still exist!");
			}
		}
	}

}
