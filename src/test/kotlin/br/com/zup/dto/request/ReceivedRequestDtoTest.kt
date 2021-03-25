package br.com.zup.dto.request

import br.com.zup.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ReceivedRequestDtoTest {

    private lateinit var institution: Institution
    private lateinit var owner: Owner
    private lateinit var bankAccount: BankAccount

    @BeforeEach
    fun setup() {
        institution = Institution.newBuilder()
            .setName("ITAÚ UNIBANCO S.A.")
            .setParticipant("60701190")
            .build()

        owner = Owner.newBuilder()
            .setId("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .setName("Alberto Tavares")
            .setTaxIdNumber("06628726061")
            .build()

        bankAccount = BankAccount.newBuilder()
            .setBranch("0001")
            .setAccountNumber("212233")
            .setAccountType("CONTA_CORRENTE")
            .setInstitution(institution)
            .build()
    }

    @Test
    fun `test ReceivedRequestDto constructor and getters`() {

        val request = KeyRequestRest.newBuilder()
            .setKeyType(KeyType.valueOf("CPF"))
            .setKey(owner.taxIdNumber)
            .setBankAccount(
                BankAccount.newBuilder()
                    .setBranch(bankAccount.branch)
                    .setAccountNumber(bankAccount.accountNumber)
                    .setAccountType(bankAccount.accountType)
                    .setInstitution(
                        Institution.newBuilder()
                            .setName(institution.name)
                            .setParticipant(institution.participant)
                            .build()
                    )
                    .build()
            )
            .setOwner(
                Owner.newBuilder()
                    .setId(owner.id)
                    .setName(owner.name)
                    .setTaxIdNumber(owner.taxIdNumber)
                    .build()
            )
            .build()

        val actual = ReceivedKeyRequestDto(request).toModel()

        assertEquals("CPF", actual.keyType)
        assertEquals("06628726061", actual.key)
        assertEquals(bankAccount.branch, actual.bankAccount.branch)
        assertEquals(bankAccount.accountNumber, actual.bankAccount.accountNumber)
        assertEquals(bankAccount.accountType,
            if (actual.bankAccount.accountType == "CACC") "CONTA_CORRENTE" else "CONTA_POUPANCA")
        assertEquals(bankAccount.institution.participant, actual.bankAccount.participant)
        assertEquals(owner.id, actual.owner.id)
        assertEquals(owner.name, actual.owner.name)
        assertEquals(owner.taxIdNumber, actual.owner.taxIdNumber)
    }
}