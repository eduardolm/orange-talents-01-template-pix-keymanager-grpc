package br.com.zup.exception.handlers

import io.grpc.Status

class DefaultExceptionHandler : ExceptionHandler<Exception> {

    override fun handle(exception: Exception): StatusWithDetails {
        val status = when (exception) {
            is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(exception.message)
            is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(exception.message)
            else -> Status.UNKNOWN
        }
        return StatusWithDetails(status.withCause(exception))
    }

    override fun supports(exception: Exception): Boolean {
        return true
    }
}