package net.citizen428.klox

sealed class Expr {
    class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr()
    class Grouping(val expression: Expr) : Expr()
    class Literal(val value: Any?) : Expr()
    class Unary(val operator: Token, val right: Expr) : Expr()
}

// AST printing
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
