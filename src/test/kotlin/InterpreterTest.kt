package net.citizen428.klox

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test
import kotlin.test.assertEquals

class InterpreterTest : BaseTest() {
    @Test
    fun testAddStrings() {
        val expr = Parser.parseString("\"foo\" + \"bar\"")
        val actual = expr?.interpret()
        assertEquals("foobar", actual)
    }

    @Test
    fun testAddNumbers() {
        val expr = Parser.parseString("1 + 2")
        val actual = expr?.interpret()
        assertEquals(3.0, actual)
    }

    @Test
    fun testArithmeticExpression() {
        val expr = Parser.parseString("(1 + 6 / 2) * 2 - 1")
        val actual = expr?.interpret()
        assertEquals(7.0, actual)
    }

    @Test
    fun testArithmeticExpressionPrintsCorrectOutpu() {
        val expr = Parser.parseString("(1 + 6 / 2) * 2 - 1")
        val outputStreamCaptor = ByteArrayOutputStream()

        System.setOut(PrintStream(outputStreamCaptor))
        Interpreter().interpret(expr)
        System.setOut(System.out)

        assertEquals("7\n", outputStreamCaptor.toString())
    }
}
