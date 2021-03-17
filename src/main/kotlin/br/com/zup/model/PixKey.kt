package br.com.zup.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PixKey(
    @Id
    val pixId: String = UUID.randomUUID().toString(),
    val keyType: String,
    val pixKey: String,
    val ownerId: String,
    val bankBranch: String,
    val bankAccountNumber: String,
    val bankParticipant: String,
    val bankAccountType: String,
    val bankAccountOwner: String,
    val createdAt: String,
) {

}
