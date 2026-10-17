plugins {
	id("buildlogic.java-conventions")
}

description = "CommandAPI - Bukkit support test library implementation"

dependencies {
	compileOnly(libs.com.mojang.brigadier)
	compileOnly(spigot.version.common)
	compileOnly(paper.version.bukkit.common)

	// Previously commandapi-bukkit-shade
	api(project(":commandapi-bukkit-core"))
}
