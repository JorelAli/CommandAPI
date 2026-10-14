plugins {
	id("buildlogic.java-conventions")
	kotlin("jvm")
}

description = "CommandAPI - Bukkit Kotlin DSL tests shared between the platforms"

/*
   The Kotlin DSL tests in this module are shared between the Bukkit platforms, like the tests in
   commandapi-bukkit-test-tests. They are compiled and run by commandapi-paper-test-tests and
   commandapi-spigot-test-tests, which provide the test setup (TestBase, Main, CommandAPIVersionHandler, etc.).

   This module has no test setup of its own, so the tests aren't compiled here.
*/
tasks.named("compileTestKotlin") {
	enabled = false
}

tasks.named("test") {
	enabled = false
}
