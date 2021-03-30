package br.com.zup.exception.handlers

import br.com.zup.exception.KeyNotFoundException
import io.grpc.Status

class KeyNotFoundExceptionHandler : ExceptionHandler<KeyNotFoundException> {

    override fun handle(exception: KeyNotFoundException): StatusWithDetails {
        return StatusWithDetails(Status.NOT_FOUND
            .withDescription(exception.message)
            .withCause(exception))
    }

    override fun supports(exception: Exception): Boolean {
        return exception is KeyNotFoundException
    }


}