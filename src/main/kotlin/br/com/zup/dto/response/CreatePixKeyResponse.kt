package br.com.zup.dto.response

import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import br.com.zup.model.PixKey
import java.util.*

data class CreatePixKeyResponse(
    val pixId: String = UUID.randomUUID().toString(),
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: String
) {

    fun toModel(): PixKey {
        return PixKey(
            pixId = pixId,
            keyType = keyType,
            pixKey = key,
            ownerId = owner.id,
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
