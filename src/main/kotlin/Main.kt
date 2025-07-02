package net.citizen428.klox

import java.io.File
import kotlin.system.exitProcess


object Lox {
    var hadError = false

    @JvmStatic
    fun main(args: Array<String>) {
        when (args.size) {
            0 -> runPrompt()
            1 -> runFile(args[0])
            else -> {
                println("Usage: jlox [script]")
                exitProcess(64)
            }
        }
    }

    fun error(line: Int, message: String) = report(line, message = message)

    fun error(token: Token, message: String) {
        val where = if (token.type == TokenType.EOF) " at end" else " at '${token.lexeme}'"
        report(token.line, where, message)
    }

    private fun runFile(path: String) {
        run(File(path).readText())
        if (hadError) exitProcess(65)
    }

    private fun runPrompt() {
        while (true) {
            print("> ")
            val line = readlnOrNull()
            if (line == null) break
            run(line)
            hadError = false
        }
    }

    private fun run(source: String) {
        val ast = Parser.parseString(source)
        if (hadError) return

        ast
            .onSuccess { println(it.print()) }
            .onFailure { println(it) }
    }

    private fun report(line: Int, where: String = "", message: String = "") {
        System.err.println("[line ${line}] Error${where}: $message")
        hadError = true
    }
}
