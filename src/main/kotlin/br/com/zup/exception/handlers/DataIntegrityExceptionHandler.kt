package br.com.zup.exception.handlers

import io.grpc.Status
import io.micronaut.context.MessageSource
import io.micronaut.context.MessageSource.MessageContext
import org.hibernate.exception.ConstraintViolationException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataIntegrityExceptionHandler(@Inject var messageSource: MessageSource) : ExceptionHandler<ConstraintViolationException> {

    override fun handle(exception: ConstraintViolationException): StatusWithDetails {

        val constraintName = exception.constraintName
        if (constraintName.isNullOrBlank()) {
            return internalServerError(exception)
        }

        val message = messageSource.getMessage("data.integrity.error.$constraintName", MessageContext.DEFAULT)
        return message
            .map { alreadyExistsError(it, exception) }
            .orElse(internalServerError(exception))
    }

    override fun supports(exception: Exception): Boolean {
        return exception is ConstraintViolationException
    }

    private fun alreadyExistsError(message: String?, e: ConstraintViolationException) =
        StatusWithDetails(Status.ALREADY_EXISTS
            .withDescription(message)
            .withCause(e))

    private fun internalServerError(e: ConstraintViolationException) =
        StatusWithDetails(Status.INTERNAL
            .withDescription("Unexpected internal server error")
            .withCause(e))
}