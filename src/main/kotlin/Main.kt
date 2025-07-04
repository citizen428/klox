package net.citizen428.klox

import java.io.File
import kotlin.system.exitProcess

enum class ExitCodes(val value: Int) {
    ArgumentError(64),
    ParseError(65),
    RuntimeError(70)
}

object Lox {
    private val interpreter = Interpreter()
    var hadError = false
    var hadRuntimeError = false

    @JvmStatic
    fun main(args: Array<String>) {
        when (args.size) {
            0 -> runPrompt()
            1 -> runFile(args[0])
            else -> {
                println("Usage: jlox [script]")
                exitProcess(ExitCodes.ArgumentError.value)
            }
        }
    }

    fun error(line: Int, message: String) = report(line, message = message)

    fun error(token: Token, message: String) {
        val where = if (token.type == TokenType.EOF) " at end" else " at '${token.lexeme}'"
        report(token.line, where, message)
    }

    fun runtimeError(error: RuntimeError) {
        System.err.println("${error.message}\n[line ${error.token?.line}]\n")
        hadRuntimeError = true
    }

    private fun runFile(path: String) {
        run(File(path).readText())
        if (hadError) exitProcess(ExitCodes.ParseError.value)
        if (hadRuntimeError) exitProcess(ExitCodes.RuntimeError.value)
    }

    private fun runPrompt() {
        while (true) {
            print("> ")
            val line = readlnOrNull()
            if (line == null) break
            if (line.isEmpty()) continue

            run(line)
            hadError = false
            hadRuntimeError = false
        }
    }

    private fun run(source: String) {
        val ast = Parser.parseString(source)
        if (hadError) return

        interpreter.interpret(ast)
    }

    private fun report(line: Int, where: String = "", message: String = "") {
        System.err.println("[line ${line}] Error${where}: $message")
        hadError = true
    }
}
