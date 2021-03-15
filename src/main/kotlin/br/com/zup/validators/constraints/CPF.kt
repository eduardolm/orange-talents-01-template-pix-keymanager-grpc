package br.com.zup.validators.constraints

import javax.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class CPF(
    val message: String = "CPF inválido."
)
