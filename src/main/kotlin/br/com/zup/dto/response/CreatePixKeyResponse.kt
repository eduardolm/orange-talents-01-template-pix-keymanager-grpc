package br.com.zup.dto.response

import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import br.com.zup.model.PixKey

data class CreatePixKeyResponse(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: String
) {

    fun toModel(): PixKey {
        return PixKey(
            keyType = keyType,
            pixKey = key,
            ownerId = null,
            ownerType = owner.type,
            ownerName = owner.name,
            ownerTaxIdNumber = owner.taxIdNumber,
            bankBranch = bankAccount.branch,
            bankAccountNumber = bankAccount.accountNumber,
            bankParticipant = bankAccount.participant,
            bankName = null,
            bankAccountType = bankAccount.accountType,
            createdAt = createdAt
        )
    }
}
