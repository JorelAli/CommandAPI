package dev.jorel.commandapi.ktannotations

import dev.jorel.commandapi.ktannotations.tests.*
import dev.jorel.commandapi.ktannotations.tests.Testable
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import java.io.File
import kotlin.test.assertTrue

internal val kspDir =
	"target/generated-sources/ksp-java/${Testable::class.java.packageName.replace('.', '/')}"

internal class AnnotationsTest {
	private val tests = listOf<() -> Testable>(
		::TeleportCommand,
		::WarpCommand
	)

	@TestFactory
	fun runDynamicTests(): Collection<DynamicTest> {
		return buildList {
			for (testSupplier in tests) {
				val test = testSupplier()
				val testName = test.javaClass.simpleName
				add(dynamicTest(testName) {
					val expectedOutput = test.expectedOutput.trim(' ', '\n')
					val actualOutput = File(kspDir, "$testName\$Command.java").readText().trim(' ', '\n')
					assertTrue(actualOutput.contains(expectedOutput))
				})
			}
		}
	}
}