package net.citizen428.klox

data class Token(val type: TokenType, val lexeme: String, val line: Int = 1, val literal: Any? = null)

