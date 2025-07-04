package net.citizen428.klox

class RuntimeError(
    message: String? = null,
    cause: Throwable? = null,
    val token: Token? = null
) : Exception(message, cause)
