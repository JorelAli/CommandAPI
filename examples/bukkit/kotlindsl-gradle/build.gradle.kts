plugins {
	kotlin("jvm") version "1.8.20"
}

kotlin {
	jvmToolchain(17)
}

version = "0.0.1-SNAPSHOT"

repositories {
	// Use Maven Central for resolving dependencies.
	mavenCentral()
	// This adds the Paper Maven repository to the build
	maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
	implementation(kotlin("stdlib-jdk8"))

	// This adds the Spigot API artifact to the build
	implementation("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")

	// The CommandAPI dependency used for Bukkit and it's forks
	implementation("dev.jorel:commandapi-paper-core:9.6.2-SNAPSHOT")
	// Due to all functions available in the kotlindsl being inlined, we only need this dependency at compile-time
	compileOnly("dev.jorel:commandapi-kotlin-paper:9.6.2-SNAPSHOT")
}