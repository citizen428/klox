package net.citizen428.klox

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.io.ByteArrayOutputStream
import java.io.PrintStream

// Used to supress all error output from Lox.error during test runs.
open class BaseTest {
    companion object {
        private val originalErr = System.err

        @BeforeAll
        @JvmStatic
        fun setUpAll() {
            System.setErr(PrintStream(ByteArrayOutputStream()))
        }

        @AfterAll
        @JvmStatic
        fun tearDownAll() {
            System.setErr(originalErr)
        }
    }
}
