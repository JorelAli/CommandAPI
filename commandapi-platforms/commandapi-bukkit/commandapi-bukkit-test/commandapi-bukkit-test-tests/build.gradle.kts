plugins {
	id("buildlogic.java-conventions")
}

description = "CommandAPI - Bukkit tests shared between the platforms"

/*
   The tests in this module are shared between the Bukkit platforms. They are compiled and run by
   commandapi-paper-test-tests and commandapi-spigot-test-tests, which provide the platform's
   CommandAPIVersionHandler and Main, and exclude the tests that need another platform's implementation.

   This module's own CommandAPIVersionHandler and Main are no longer used, so the tests aren't compiled here.
*/
tasks.named("compileTestJava") {
	enabled = false
}

tasks.named("test") {
	enabled = false
}
