package net.citizen428.klox

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.fail

class ParserTest : BaseTest() {
    @Test
    fun testSuccessfulParse() {
        val expected = "(+ 1.0 (/ 1.0 2.0))"
        Parser.parseString("1 + 1 / 2")
            .onSuccess { actual -> assertEquals(expected, actual.print()) }
            .onFailure { e -> fail("Parsing failed.", e) }
    }

    @Test
    fun testSuccessfulParseWithGrouping() {
        val expected = "(/ (group (+ 1.0 1.0)) 2.0)"
        Parser.parseString("(1 + 1) / 2")
            .onSuccess { actual -> assertEquals(expected, actual.print()) }
            .onFailure { e -> fail("Parsing failed.", e) }
    }

    @Test
    fun testParseError() {
        assertFailsWith<Parser.ParseError> {
            Parser.parseString("1 +").getOrThrow()
        }
    }
}
