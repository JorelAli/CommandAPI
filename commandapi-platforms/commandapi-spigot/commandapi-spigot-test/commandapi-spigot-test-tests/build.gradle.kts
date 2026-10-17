plugins {
	id("buildlogic.java-conventions")
	kotlin("jvm")
}

description = "CommandAPI - Spigot support tests"

repositories {
	maven {
		url = uri("https://jitpack.io")
		content {
			includeGroup("com.saicone.rtag")
		}
	}
}

/*
   Version-specific test profiles, the same as commandapi-bukkit-test-tests

   Select a profile with -PtestProfile=<profile>, for example:

     ./gradlew :commandapi-spigot-test-tests:test -PtestProfile=Minecraft_1_20_5_Mojang

   Available profiles are Minecraft_1_20_5, Minecraft_1_20_3, Minecraft_1_20_2 and
   Minecraft_1_20 (default). Append _Mojang to run against Mojang-mapped
   implementations instead of Spigot-mapped ones.
*/
val testProfile = providers.gradleProperty("testProfile").getOrElse("Minecraft_1_20")
val mojangMapped = testProfile.endsWith("_Mojang")
val mapping = if (mojangMapped) "mojang" else "spigot"

val (minecraftVersion, spigotServer) = when (testProfile.removeSuffix("_Mojang")) {
	"Minecraft_1_20_5" -> "1.20.5" to spigot.version.v1206
	"Minecraft_1_20_3" -> "1.20.3" to spigot.version.v1204
	"Minecraft_1_20_2" -> "1.20.2" to spigot.version.v1202
	"Minecraft_1_20" -> "1.20" to spigot.version.v1201
	else -> error("Unknown testProfile: $testProfile")
}

dependencies {
	// Dependencies of the shared tests
	testImplementation(libs.com.github.zafarkhaja.java.semver)
	testImplementation(libs.de.tr7zw.item.nbt.api)
	testImplementation(libs.com.saicone.rtag.rtag)
	testImplementation(libs.org.junit.jupiter.junit.jupiter.params)
	testImplementation(project(":commandapi-bukkit-plugin-common"))
	// Dependency of the shared Kotlin DSL tests
	testImplementation(project(":commandapi-kotlin-spigot"))

	testImplementation(libs.org.junit.jupiter.junit.jupiter.engine)
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation(libs.com.mojang.brigadier)
	testImplementation(libs.org.mockito.mockito.core)
	testImplementation(libs.org.apache.logging.log4j.log4j.api)

	// MockBukkit-v1.20 3.9.0 (from the Bukkit test implementations) targets 1.20.1. MockBukkit's paper-api provides
	//  the Bukkit API classes here, so newer versions need a matching MockBukkit. Otherwise, CraftBukkit refers to
	//  Sound, Material and Particle members that don't exist in the older API. 1.20.5 also made enchantments,
	//  potion effects, etc. registry-backed, which 3.9.0 can't handle.
	when (minecraftVersion) {
		// MockBukkit 3.9.0 works with paper-api 1.20.2, so just update the API
		"1.20.2" -> testImplementation("io.papermc.paper:paper-api") {
			version { strictly("1.20.2-R0.1-SNAPSHOT") }
		}
		// Last version for paper-api 1.20.4
		"1.20.3" -> testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.88.1")
		// paper-api 1.20.6
		"1.20.5" -> testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.93.2")
	}

	// Previously commandapi-bukkit-shade, now the Spigot platform
	testImplementation(project(":commandapi-spigot-core"))
	testImplementation(project(":commandapi-bukkit-core"))
	testImplementation(project(":commandapi-bukkit-nms-common", mapping))
	testImplementation(project(":commandapi-bukkit-$minecraftVersion", mapping))
	if (mojangMapped) {
		testImplementation(project(":commandapi-spigot-$minecraftVersion"))
	} else {
		testImplementation(project(":commandapi-spigot-$minecraftVersion", "remapped"))
	}

	testImplementation(project(":commandapi-bukkit-test-impl-$minecraftVersion", mapping))
	testImplementation(project(":commandapi-spigot-test-impl-$minecraftVersion", mapping))
	testImplementation(spigotServer) {
		if (mojangMapped) {
			artifact {
				classifier = "remapped-mojang"
			}
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	// Every test sets up a new mocked server and registries. Gradle's default 512MB isn't enough for that,
	//  while Maven's Surefire used the JVM's default (a quarter of the system's memory)
	maxHeapSize = "2g"
	systemProperty("profileId", testProfile)
	testLogging {
		exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
	}

	// The ByteBuddy version MockBukkit-v1.20 uses to load plugins doesn't support Java versions after 23
	javaLauncher = javaToolchains.launcherFor {
		languageVersion = JavaLanguageVersion.of(21)
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
	if (minecraftVersion == "1.20.5") {
		exclude(
			// Uses particle names that were renamed in 1.20.5
			"dev/jorel/commandapi/test/arguments/ArgumentParticleTests.java",
		)
	}
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
