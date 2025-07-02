package net.citizen428.klox

import net.citizen428.klox.TokenType.*

class Parser(val tokens: List<Token>) {
    class ParseError : RuntimeException()

    private var current = 0

    fun parse(): Result<Expr> = runCatching { expression() }

    private fun expression(): Expr = equality()

    private fun equality(): Expr {
        var expr = comparison()

        while (match(BANG_EQUAL, BANG_EQUAL))
            expr = Expr.Binary(left = expr, operator = previous(), right = comparison())

        return expr
    }

    private fun comparison(): Expr {
        var expr = term()

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL))
            expr = Expr.Binary(left = expr, operator = previous(), right = term())

        return expr
    }

    private fun term(): Expr {
        var expr = factor()

        while (match(MINUS, PLUS))
            expr = Expr.Binary(left = expr, operator = previous(), right = factor())

        return expr
    }

    private fun factor(): Expr {
        var expr = unary()

        while (match(SLASH, STAR))
            expr = Expr.Binary(left = expr, operator = previous(), right = unary())

        return expr
    }

    private fun unary(): Expr =
        if (match(BANG, MINUS)) Expr.Unary(operator = previous(), right = unary())
        else primary()

    private fun primary(): Expr {
        if (match(FALSE)) return Expr.Literal(false)
        if (match(TRUE)) return Expr.Literal(true)
        if (match(NIL)) return Expr.Literal(null)

        if (match(NUMBER, STRING)) return Expr.Literal(previous().literal)

        if (match(LEFT_PAREN)) {
            val expr = expression()
            consume(RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun match(vararg types: TokenType): Boolean {
        types.forEach {
            if (check(it)) {
                advance()
                return true
            }
        }

        return false
    }

    private fun check(type: TokenType) =
        if (isAtEnd()) false
        else peek().type == type

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean = peek().type == EOF

    private fun peek(): Token = tokens[current]

    private fun previous(): Token = tokens[current - 1]

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()

        throw error(peek(), message)
    }

    private fun error(token: Token, message: String): ParseError {
        Lox.error(token, message)
        return ParseError()
    }

    private fun synchronize() {
        advance()

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return

            when (peek().type) {
                CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> return
                else -> advance()
            }
        }
    }

    companion object {
        fun parseString(source: String): Result<Expr> =
            Scanner(source)
                .scanTokens()
                .let { tokens -> Parser(tokens) }
                .parse()
    }
}
