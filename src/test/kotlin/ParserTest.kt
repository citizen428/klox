package net.citizen428.klox

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ParserTest : BaseTest() {
    @Test
    fun testSuccessfulParse() {
        val expected = "(+ 1.0 (/ 1.0 2.0))"
        val actual = Parser.parseString("1 + 1 / 2")
        assertEquals(expected, actual?.print())
    }

    @Test
    fun testSuccessfulParseWithGrouping() {
        val expected = "(/ (group (+ 1.0 1.0)) 2.0)"
        val actual = Parser.parseString("(1 + 1) / 2")
        assertEquals(expected, actual?.print())
    }

    @Test
    fun testParseError() {
        assertNull(Parser.parseString("1 +"))
    }
}

// AST printing for eaasier test assertions
fun Expr.print(): String {
    fun parenthesize(name: String, vararg exprs: Expr): String =
        buildString {
            append("(").append(name)
            for (expr in exprs) {
                append(" ")
                append(expr.print())
            }
            append(")")
        }


    return when (this) {
        is Expr.Binary -> parenthesize(this.operator.lexeme, this.left, this.right)
        is Expr.Grouping -> parenthesize("group", this.expression)
        is Expr.Literal -> this.value.toString()
        is Expr.Unary -> parenthesize(this.operator.lexeme, this.right)
    }
}


