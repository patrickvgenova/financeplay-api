package com.example.financeplayapi.commons.core.exception


abstract class AbstractAppException : RuntimeException {

    val data = mutableMapOf<String, Any?>()

    constructor() : this(null, null, true, true)
    constructor(vararg pairs: Pair<String, Any>) : this(null, null, false, true, pairs.toList())
    constructor(message: String?) : this(message, null)
    constructor(message: String?, cause: Throwable?) : this(message, cause, true, true)
    constructor(cause: Throwable?) : this(null, cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : this(
        message,
        cause,
        enableSuppression,
        writableStackTrace,
        listOf()
    )

    private constructor(
        message: String?,
        cause: Throwable?,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
        pairs: List<Pair<String, Any>>
    ) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace,
    ) {
        data["_message"] = message
        pairs.forEach { data[it.first] = it.second }
    }

    fun withData(vararg pairs: Pair<String, Any>): AbstractAppException {
        pairs.forEach { data[it.first] = it.second }
        return this
    }
}
