package br.com.zup.dto.request

import br.com.zup.KeyRequestRest
import br.com.zup.enums.AccountType
import br.com.zup.model.BankAccount
import br.com.zup.model.Owner

class ReceivedKeyRequestDto(requestRest: KeyRequestRest) {

    private val keyType: String = requestRest.keyType.toString()
    val key: String = requestRest.key

    private val participant: String = requestRest.bankAccount.participant
    private val branch: String = requestRest.bankAccount.branch
    private val accountNumber: String = requestRest.bankAccount.accountNumber
    private val accountType: String = AccountType.valueOf(requestRest.bankAccount.accountType).toString()

    val type: String = if(requestRest.owner.taxIdNumber.length == 11) "NATURAL_PERSON" else "LEGAL_PERSON"
    val name: String = requestRest.owner.name
    private val taxIdNumber: String = requestRest.owner.taxIdNumber

    fun toModel(): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            keyType,
            key,
            BankAccount(
                participant,
                branch,
                accountNumber,
                accountType
            ),
            Owner(
                type,
                name,
                taxIdNumber
            )
        )
    }
}