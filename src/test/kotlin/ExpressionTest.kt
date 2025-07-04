package net.citizen428.klox

import net.citizen428.klox.TokenType.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpressionTest : BaseTest() {
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

    @Test
    fun testInterpretLiteral() {
        val literals = listOf(
            Expr.Literal(23),
            Expr.Literal(1.23),
            Expr.Literal("test"),
            Expr.Literal(true),
            Expr.Literal(false)
        )
        val expected = listOf(23.0, 1.23, "test", true, false)

        assertEquals(expected, literals.map { it.interpret() })
    }

    @Test
    fun testInterpretGrouping() {
        val expr = Expr.Grouping(Expr.Literal("inside grouping"))
        assertEquals("inside grouping", expr.interpret())
    }

    @Test
    fun testInterpretNegation() {
        val bang = Token(BANG, "!", 1)
        val negations = listOf(
            Expr.Unary(bang, Expr.Literal(true)),
            Expr.Unary(bang, Expr.Literal(false)),
            Expr.Unary(bang, Expr.Literal(null)),
            Expr.Unary(bang, Expr.Literal("test"))
        )
        val expected = listOf(false, true, true, false)
        assertEquals(expected, negations.map { it.interpret() })
    }

    @Test
    fun testInterpretUnaryMinus() {
        val expr = Expr.Unary(
            Token(MINUS, "-"),
            Expr.Literal(42)
        )
        assertEquals(-42.0, expr.interpret())
    }

    @Test
    fun testInterpretBinaryPlusForNumbers() {
        val expr = Expr.Binary(
            Expr.Literal(1),
            operator = Token(PLUS, "+"),
            right = Expr.Literal(1)
        )
        assertEquals(2.0, expr.interpret())
    }

    @Test
    fun testInterpretBinaryPlusForStrings() {
        val expr = Expr.Binary(
            Expr.Literal("hello"),
            operator = Token(PLUS, "+"),
            right = Expr.Literal("world")
        )
        assertEquals("helloworld", expr.interpret())
    }

    @Test
    fun testInterpretBinaryMinus() {
        val expr = Expr.Binary(
            Expr.Literal(5),
            operator = Token(MINUS, "-"),
            right = Expr.Literal(3)
        )
        assertEquals(2.0, expr.interpret())
    }

    @Test
    fun testInterpretBinaryStar() {
        val expr = Expr.Binary(
            Expr.Literal(4),
            operator = Token(STAR, "*"),
            right = Expr.Literal(3)
        )
        assertEquals(12.0, expr.interpret())
    }

    @Test
    fun testInterpretBinarySlash() {
        val expr = Expr.Binary(
            Expr.Literal(10),
            operator = Token(SLASH, "/"),
            right = Expr.Literal(2)
        )
        assertEquals(5.0, expr.interpret())
    }

    @Test
    fun testInterpretBinaryGreater() {
        val expr = Expr.Binary(
            Expr.Literal(5),
            operator = Token(GREATER, ">"),
            right = Expr.Literal(3)
        )
        assertEquals(true, expr.interpret())
    }

    @Test
    fun testInterpretBinaryGreaterEqual() {
        val expr = Expr.Binary(
            Expr.Literal(5),
            operator = Token(GREATER_EQUAL, ">="),
            right = Expr.Literal(5)
        )
        assertEquals(true, expr.interpret())
    }

    @Test
    fun testInterpretBinaryLess() {
        val expr = Expr.Binary(
            Expr.Literal(3),
            operator = Token(LESS, "<"),
            right = Expr.Literal(5)
        )
        assertEquals(true, expr.interpret())
    }

    @Test
    fun testInterpretBinaryLessEqual() {
        val expr = Expr.Binary(
            Expr.Literal(3),
            operator = Token(LESS_EQUAL, "<="),
            right = Expr.Literal(3)
        )
        assertEquals(true, expr.interpret())
    }

    @Test
    fun testInterpretBinaryBangEqual() {
        val expr = Expr.Binary(
            Expr.Literal(3),
            operator = Token(BANG_EQUAL, "!="),
            right = Expr.Literal(5)
        )
        assertEquals(true, expr.interpret())
    }

    @Test
    fun testInterpretBinaryEqualEqual() {
        val expr = Expr.Binary(
            Expr.Literal(3),
            operator = Token(EQUAL_EQUAL, "=="),
            right = Expr.Literal(3)
        )
        assertEquals(true, expr.interpret())
    }
}
