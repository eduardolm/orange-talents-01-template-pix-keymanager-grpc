package br.com.zup.dto.request

import br.com.zup.model.BankAccount
import br.com.zup.model.Owner

data class CreatePixKeyRequest(val keyType: String, val key: String, val bankAccount: BankAccount, val owner: Owner) {
}
