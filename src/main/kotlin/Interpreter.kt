package net.citizen428.klox

import net.citizen428.klox.TokenType.*

class Interpreter {
    fun interpret(expression: Expr?) {
        try {
            val value = expression?.interpret()
            println(stringify(value))
        } catch (error: RuntimeError) {
            Lox.runtimeError(error)
        }
    }

    private fun stringify(obj: Any?): String {
        if (obj == null) return "nil"

        if (obj is Double)
            return obj.toString().removeSuffix(".0")

        return obj.toString()
    }
}

// Interpreting expressions
fun Expr.interpret(): Any? {
    return when (this) {
        is Expr.Literal -> if (this.value is Int) this.value.toDouble() else this.value
        is Expr.Grouping -> this.expression.interpret()
        is Expr.Unary -> {
            val right = this.right.interpret()

            when (this.operator.type) {
                BANG -> !isTruthy(right)
                MINUS -> {
                    checkNumberOperands(this.operator, right)
                    -(right as Double)
                }

                else -> check(false)
            }
        }

        is Expr.Binary -> {
            val left = this.left.interpret()
            val right = this.right.interpret()

            when (this.operator.type) {
                PLUS -> {
                    when (left) {
                        is Double if right is Double -> left + right
                        is String if right is String -> "$left$right"
                        else -> throw RuntimeError(
                            "Both operands must be of same type (number or string)",
                            token = this.operator
                        )
                    }
                }

                MINUS -> {
                    checkNumberOperands(this.operator, left, right)
                    (left as Double) - (right as Double)
                }

                STAR -> {
                    checkNumberOperands(this.operator, left, right)
                    (left as Double) * (right as Double)
                }

                SLASH -> {
                    checkNumberOperands(this.operator, left, right)
                    if (right as Double == 0.0)
                        throw RuntimeError("Division by 0", token = this.operator)
                    (left as Double) / (right as Double)
                }

                GREATER -> {
                    checkNumberOperands(this.operator, left, right)
                    (left as Double) > (right as Double)
                }

                GREATER_EQUAL -> {
                    checkNumberOperands(this.operator, left, right)
                    (left as Double) >= (right as Double)
                }

                LESS -> {
                    checkNumberOperands(this.operator, left, right)
                    (left as Double) < (right as Double)
                }

                LESS_EQUAL -> {
                    checkNumberOperands(this.operator, left, right)
                    (left as Double) <= (right as Double)
                }

                BANG_EQUAL -> {
                    !isEqual(left, right)
                }

                EQUAL_EQUAL -> {
                    isEqual(left, right)
                }

                else -> check(false)

            }
        }
    }
}

private fun checkNumberOperands(operator: Token, vararg objects: Any?) {
    if (objects.all { it is Double }) return

    throw RuntimeError("Operands must be numbers", token = operator)
}

private fun isTruthy(obj: Any?): Boolean {
    if (obj == null) return false
    if (obj is Boolean) return obj
    return true
}

private fun isEqual(a: Any?, b: Any?): Boolean = a?.equals(b) ?: (b == null)

