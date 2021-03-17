package br.com.zup.dto.response

import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.model.BankAccount
import br.com.zup.model.PixKey

data class CreatePixKeyResponse(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: ResponseOwner,
    val createdAt: String
) {

    fun toModel(createPixKeyRequest: CreatePixKeyRequest): PixKey {
        return PixKey(
            keyType = keyType,
            pixKey = key,
            ownerId = createPixKeyRequest.owner.id,
            bankBranch = bankAccount.branch,
            bankAccountNumber = bankAccount.accountNumber,
            bankName = bankAccount.participant,
            bankAccountType = bankAccount.accountType,
            bankAccountOwner = owner.name,
            createdAt = createdAt
        )
    }
}
