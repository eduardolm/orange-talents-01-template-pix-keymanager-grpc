package br.com.zup.validators

import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.validators.constraints.Key
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton

@Singleton
class KeyValidator : ConstraintValidator<Key, CreatePixKeyRequest> {

    override fun isValid(
        value: CreatePixKeyRequest?,
        annotationMetadata: AnnotationValue<Key>,
        context: ConstraintValidatorContext
    ): Boolean {
        if (value == null) return true

        when(value.keyType) {
            "CPF" -> return value.key?.matches("([0-9]{3}[.]?[0-9]{3}[.]?[0-9]{3}-[0-9]{2})|([0-9]{11})".toRegex())!!
            "CNPJ" -> return value.key?.matches("([0-9]{2}[.]?[0-9]{3}[.]?[0-9]{3}[/]?[0-9]{4}[-]?[0-9]{2})".toRegex())!!
            "PHONE" -> return value.key?.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())!!
            "EMAIL" -> return value.key?.matches("(^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.+))".toRegex())!!
            "RANDOM" -> return value.key.isNullOrBlank()
        }
        return true
    }
}