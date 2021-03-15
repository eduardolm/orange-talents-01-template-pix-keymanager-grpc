package br.com.zup.dto.response

import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import com.google.protobuf.Timestamp

data class CreatePixKeyResponse(
    val keyType: String,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: String
) {

}
