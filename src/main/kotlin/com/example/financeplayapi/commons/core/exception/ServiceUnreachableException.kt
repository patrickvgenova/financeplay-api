package com.example.financeplayapi.commons.core.exception

class ServiceUnreachableException : AbstractAppException {

    constructor() : super()
    constructor(vararg pairs: Pair<String, Any>) : super(*pairs)
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
    constructor(cause: Throwable?) : super(cause)
    constructor(message: String?, cause: Throwable?, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}
