package br.com.zup.dto.request

import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import br.com.zup.validators.constraints.Key
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
@Key
data class CreatePixKeyRequest(
    @field:NotBlank
    val keyType: String,
    @field:NotBlank
    @field:Size(max = 77)
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
) {
}
