package br.com.zup.exception.handlers

import br.com.zup.exception.KeyAlreadyRegisteredException
import io.grpc.Status

class KeyAlreadyRegisteredExceptionHandler : ExceptionHandler<KeyAlreadyRegisteredException> {

    override fun handle(exception: KeyAlreadyRegisteredException): StatusWithDetails {
        return StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription(exception.message)
            .withCause(exception))
    }

    override fun supports(exception: Exception): Boolean {
        return exception is KeyAlreadyRegisteredException
    }
}