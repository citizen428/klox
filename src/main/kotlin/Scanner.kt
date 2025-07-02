package net.citizen428.klox

import net.citizen428.klox.TokenType.*

const val NULL_CHAR = Char.MIN_VALUE

class Scanner(private val source: String) {
    private val tokens = mutableListOf<Token>()

    private var start = 0
    private var current = 0
    private var line = 1

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(EOF, "", line))
        return tokens
    }

    private fun isAtEnd(): Boolean = current >= source.length

    private fun scanToken() {
        val c = advance()
        when (c) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            ',' -> addToken(COMMA)
            '.' -> addToken(DOT)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '!' -> addToken(if (match('=')) BANG_EQUAL else BANG)
            '=' -> addToken(if (match('=')) EQUAL_EQUAL else EQUAL)
            '<' -> addToken(if (match('=')) LESS_EQUAL else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            '/' -> {
                if (match('/')) {
                    // Comments go until the end of the line
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(SLASH)
                }
            }

            // Ignore whitespace
            ' ', '\r', '\t' -> Unit

            '\n' -> line++

            '"' -> string()

            in '0'..'9' -> number()

            else if c.isLetter() -> identifier()

            else -> Lox.error(line, "Unexpected character: $c")
        }
    }

    private fun advance() = source[current++]

    private fun addToken(type: TokenType, literal: Any? = null) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, line, literal))
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }

    private fun peek(): Char {
        if (isAtEnd()) return NULL_CHAR
        return source[current]
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.")
            return
        }

        // Consume the closing "
        advance()

        // Trim the surrounding quotes
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }

    private fun number() {
        while (peek().isDigit()) advance()

        // Look for a fractional part
        if (peek() == '.' && peekNext().isDigit()) {
            // Consume the "."
            advance()
        }

        while (peek().isDigit()) advance()

        addToken(NUMBER, source.substring(start, current).toDoubleOrNull())
    }

    private fun peekNext(): Char {
        if (current + 1 >= source.length) return NULL_CHAR
        return source[current + 1]
    }

    private fun identifier() {
        while (peek().isLetterOrDigit()) advance()
        val text = source.substring(start, current)
        val type = keywords.getOrElse(text) { IDENTIFIER }

        addToken(type)
    }

    companion object {
        private val keywords = mapOf<String, TokenType>(
            "and" to AND,
            "class" to CLASS,
            "else" to ELSE,
            "false" to FALSE,
            "for" to FOR,
            "fun" to FUN,
            "if" to IF,
            "nil" to NIL,
            "or" to OR,
            "print" to PRINT,
            "return" to RETURN,
            "super" to SUPER,
            "this" to THIS,
            "true" to TRUE,
            "var" to VAR,
            "while" to WHILE,
        )

    }
}
