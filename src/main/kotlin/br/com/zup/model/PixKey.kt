package br.com.zup.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class PixKey(
    @Id
    var pixId: String?,
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

    override fun toString(): String {
        return "PixKey(pixId=$pixId, keyType='$keyType', pixKey='$pixKey', ownerId=$ownerId, ownerType='$ownerType', " +
                "ownerName='$ownerName', ownerTaxIdNumber='$ownerTaxIdNumber', bankBranch='$bankBranch', " +
                "bankAccountNumber='$bankAccountNumber', bankParticipant='$bankParticipant', bankName=$bankName, " +
                "bankAccountType='$bankAccountType', createdAt='$createdAt')"
    }
}
