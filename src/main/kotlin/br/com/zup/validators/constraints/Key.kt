package br.com.zup.validators.constraints

import br.com.zup.validators.KeyValidator
import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.VALUE_PARAMETER
)
@ReportAsSingleViolation
@MustBeDocumented
@Constraint(validatedBy = [KeyValidator::class])
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Key(
    val message: String = "Chave inválida.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
