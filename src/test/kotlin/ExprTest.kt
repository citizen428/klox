package net.citizen428.klox

import net.citizen428.klox.TokenType.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ExprTest : BaseTest() {
    @Test
    fun testPrint() {
        val expression = Expr.Binary(
            Expr.Unary(Token(MINUS, "-"), Expr.Literal(123)),
            Token(STAR, "*"),
            Expr.Grouping(Expr.Literal(45.67))
        )
        val expected = "(* (- 123) (group 45.67))"

        assertEquals(expression.print(), expected)
    }
}
