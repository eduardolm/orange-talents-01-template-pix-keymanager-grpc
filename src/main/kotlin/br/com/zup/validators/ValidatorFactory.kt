package br.com.zup.validators

import br.com.zup.validators.constraints.CellPhone
import br.com.zup.validators.constraints.CPF
import io.micronaut.context.annotation.Factory
import io.micronaut.validation.validator.constraints.ConstraintValidator
import javax.inject.Singleton

@Factory
class ValidatorFactory {

    @Singleton
    fun cpfPatternValidator() : ConstraintValidator<CPF, CharSequence> {
        return ConstraintValidator {value, annotation, context ->
            value == null || value.toString().matches("^[0-9]{11}\$".toRegex())
        }
    }

    @Singleton
    fun cellPhoneValidator() : ConstraintValidator<CellPhone, CharSequence> {
        return ConstraintValidator {value, annotation, context ->
            value == null || value.toString().matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    }
}