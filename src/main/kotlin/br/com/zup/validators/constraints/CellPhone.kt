package br.com.zup.validators.constraints

import javax.validation.Constraint

@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [])
annotation class CellPhone(
    val message: String = "Telefone inválido."
)
