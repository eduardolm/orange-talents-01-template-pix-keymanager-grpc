package br.com.zup.dto.request

import br.com.zup.KeyRequestRest
import br.com.zup.KeyType
import br.com.zup.enums.AccountType
import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import java.util.*

class ReceivedKeyRequestDto(requestRest: KeyRequestRest) {

    private val keyType: String = requestRest.keyType.toString()
    val key: String = if (this.keyType == KeyType.RANDOM.toString()) UUID.randomUUID().toString() else requestRest.key

    private val participant: String = requestRest.bankAccount.institution.participant
    private val branch: String = requestRest.bankAccount.branch
    private val accountNumber: String = requestRest.bankAccount.accountNumber
    private val accountType: String = AccountType.valueOf(requestRest.bankAccount.accountType).toString()

    val type: String = if(requestRest.owner.taxIdNumber.length == 11) "NATURAL_PERSON" else "LEGAL_PERSON"
    val name: String = requestRest.owner.name
    private val taxIdNumber: String = requestRest.owner.taxIdNumber
    private val id: String = requestRest.owner.id

    fun toModel(): CreatePixKeyRequest {
        return CreatePixKeyRequest(
            keyType,
            key,
            BankAccount(
                branch,
                accountNumber,
                accountType,
                participant
            ),
            Owner(
                id,
                type,
                name,
                taxIdNumber
            )
        )
    }
}