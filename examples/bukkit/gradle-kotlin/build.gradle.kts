plugins {
	java
}

java {
	toolchain {
		// Make the compiler against Java 16
		languageVersion.set(JavaLanguageVersion.of(16))
	}
}

version = "0.0.1-SNAPSHOT"

repositories {
	// Use Maven Central for resolving dependencies.
	mavenCentral()
	// This adds the Spigot Maven repository to the build
	maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
	// CodeMC repository for the NBT API
	maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
	// This adds the Spigot API artifact to the build
	implementation("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")

	// The CommandAPI dependency used for Bukkit and it's forks
	implementation("dev.jorel:commandapi-bukkit-plugin:9.0.0-SNAPSHOT")
}