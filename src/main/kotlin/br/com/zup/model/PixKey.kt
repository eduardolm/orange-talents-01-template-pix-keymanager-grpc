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
    var ownerId: String?,
    val ownerType: String,
    val ownerName: String,
    val ownerTaxIdNumber: String,
    val bankBranch: String,
    val bankAccountNumber: String,
    val bankParticipant: String,
    var bankName: String?,
    val bankAccountType: String,
    val createdAt: String,
) {

}
