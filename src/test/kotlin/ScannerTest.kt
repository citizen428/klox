package net.citizen428.klox

import net.citizen428.klox.TokenType.*

import kotlin.test.Test
import kotlin.test.assertEquals

class ScannerTest : BaseTest() {
    @Test
    fun testScanToken() {
        val program = "fun test() { print 1 + 2 }"
        val scanner = Scanner(program)

        val expected = listOf(
            Token(type = FUN, lexeme = "fun"),
            Token(type = IDENTIFIER, lexeme = "test"),
            Token(type = LEFT_PAREN, lexeme = "("),
            Token(type = RIGHT_PAREN, lexeme = ")"),
            Token(type = LEFT_BRACE, lexeme = "{"),
            Token(type = PRINT, lexeme = "print"),
            Token(type = NUMBER, lexeme = "1", literal = 1.0),
            Token(type = PLUS, lexeme = "+"),
            Token(type = NUMBER, lexeme = "2", literal = 2.0),
            Token(type = RIGHT_BRACE, lexeme = "}"),
            Token(type = EOF, lexeme = ""),
        )

        assertEquals(scanner.scanTokens(), expected)
    }
}
