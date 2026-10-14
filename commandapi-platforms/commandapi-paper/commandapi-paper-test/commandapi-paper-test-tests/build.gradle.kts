plugins {
	id("buildlogic.java-conventions")
	id("io.papermc.paperweight.userdev")
	kotlin("jvm")
}

description = "CommandAPI - Paper support tests"

repositories {
	maven {
		url = uri("https://jitpack.io")
		content {
			includeGroup("com.saicone.rtag")
		}
	}
}

dependencies {
	// Dependencies of the shared tests
	testImplementation(libs.com.github.zafarkhaja.java.semver)
	testImplementation(libs.de.tr7zw.item.nbt.api)
	testImplementation(libs.com.saicone.rtag.rtag)
	testImplementation(libs.org.junit.jupiter.junit.jupiter.params)
	testImplementation(project(":commandapi-bukkit-plugin-common"))
	// Dependency of the shared Kotlin DSL tests
	testImplementation(project(":commandapi-kotlin-paper"))

	// Adds the Paper server to the test classpath
	paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")

	// `mojang-mapped` must be before `core` so we resolve the correct version of `MojangMappedVersionHandler`
	testImplementation(project(":commandapi-paper-mojang-mapped"))
	testImplementation(project(":commandapi-paper-core"))
	testImplementation(project(":commandapi-paper-api"))
	testImplementation(project(":commandapi-paper-1.20.5", "include"))
	testImplementation(project(":commandapi-bukkit-nms-common", "mojang"))
	testImplementation(project(":commandapi-paper-test-impl-1.20.5", "include"))

	testImplementation(libs.org.junit.jupiter.junit.jupiter.engine)
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

interface InjectedArchiveOperations {
	@get:Inject
	val archives: ArchiveOperations
}

/**
 * Registers a task that copies the jar called [jarName] from the test runtime classpath without the given services
 */
fun registerServiceFilteredJar(taskName: String, jarName: String, services: List<String>) = tasks.register<Jar>(taskName) {
	description = "Copies $jarName without the services that conflict between MockBukkit and the Paper server"
	archiveFileName = jarName
	destinationDirectory = layout.buildDirectory.dir("serviceFilteredJars")

	val archives = objects.newInstance<InjectedArchiveOperations>().archives
	val jar = configurations.testRuntimeClasspath.get().filter { it.name == jarName }
	from(jar.elements.map { jars -> jars.map { archives.zipTree(it) } }) {
		exclude(services.map { "META-INF/services/$it" })
	}
}

// MockBukkit and the Paper server both provide implementations of these services, but Paper only accepts one
// provider for each. MockBukkit is the running server here, so we mostly keep its providers. The exception is
// RegistryAccess, because Paper's patched vanilla registries require Paper's implementation to bootstrap.
val filteredJars = listOf(
	registerServiceFilteredJar("filterPaperServerServices", "mappedServerJar.jar", listOf(
		"io.papermc.paper.ServerBuildInfo",
		"net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer\$Provider",
		"org.bukkit.plugin.PluginLoader",
	)),
	registerServiceFilteredJar("filterMockBukkitServices", "MockBukkit-v1.20-3.93.2.jar", listOf(
		"io.papermc.paper.registry.RegistryAccess",
	)),
)

tasks.named<Test>("test") {
	// Replace the filtered jars in place, so the classpath order stays the same
	val originalClasspath = classpath
	val filteredFiles = files(filteredJars)
	classpath = files(originalClasspath.elements.zip(filteredFiles.elements) { entries, filtered ->
		val byName = filtered.associateBy { it.asFile.name }
		entries.map { (byName[it.asFile.name] ?: it).asFile }
	})

	// Paper 1.20.6 and MockBukkit-v1.20 target Java 21. The ByteBuddy version MockBukkit uses
	// to load plugins doesn't support newer versions of Java
	javaLauncher = javaToolchains.launcherFor {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	// Every test sets up a new mocked server and registries. Gradle's default 512MB isn't enough for that,
	//  while Maven's Surefire used the JVM's default (a quarter of the system's memory)
	maxHeapSize = "2g"
	testLogging {
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}
}

/*
   The tests in commandapi-bukkit-test-tests are shared between the platforms. Its CommandAPIVersionHandler
   and Main are replaced by this module's, and tests that need another platform's implementation are excluded.
*/
val sharedTests = objects.sourceDirectorySet("sharedTests", "Tests shared between the Bukkit platforms").apply {
	srcDir(project(":commandapi-bukkit-test-tests").layout.projectDirectory.dir("src/test/java"))
	include("**/*.java")
	exclude(
		"dev/jorel/commandapi/CommandAPIVersionHandler.java",
		"dev/jorel/commandapi/test/Main.java",
		// Version-specific tests for versions before 1.20
		"dev/jorel/commandapi/test/arguments/ArgumentParticle_*Tests.java",
	)
	exclude(
		// Uses particle names that were renamed in 1.20.5
		"dev/jorel/commandapi/test/arguments/ArgumentParticleTests.java",
	)
}

sourceSets.test {
	java.source(sharedTests)
}

/*
   The Kotlin DSL tests in commandapi-bukkit-kotlin-test are shared between the platforms too
*/
kotlin {
	sourceSets.test {
		kotlin.srcDir(project(":commandapi-bukkit-kotlin-test").layout.projectDirectory.dir("src/test/kotlin"))
	}
	compilerOptions {
		// Same as the Java code (see buildlogic.java-conventions). jvmToolchain isn't used, because the
		//  test classpath has classes for newer Java versions.
		jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
	}
}
