package br.com.zup.dto.response

import br.com.zup.Institution
import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreatePixKeyResponseTest {

    private lateinit var institution: Institution
    private lateinit var owner: Owner
    private lateinit var bankAccount: BankAccount

    @BeforeEach
    fun setup() {
        institution = Institution.newBuilder()
            .setName("ITAÚ UNIBANCO S.A.")
            .setParticipant("60701190")
            .build()

        owner = Owner("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "NATURAL_PERSON", "Alberto Tavares",
            "06628726061")

        bankAccount = BankAccount("0001", "212233", "CONTA_CORRENTE", "60701190")
    }

    @Test
    fun `test CreatePixKeyResponseDto constructor and getters`() {
        val actual = CreatePixKeyResponse("bc3b1cdd-8b06-4bcd-abe3-11535b275134","CPF", "06628726061", bankAccount, owner,
            "2021-01-12T15:34")

        assertEquals("CPF", actual.keyType)
        assertEquals("06628726061", actual.key)
        assertEquals("0001", actual.bankAccount.branch)
        assertEquals("212233", actual.bankAccount.accountNumber)
        assertEquals("CONTA_CORRENTE", actual.bankAccount.accountType)
        assertEquals("60701190", actual.bankAccount.participant)
        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.owner.id)
        assertEquals("NATURAL_PERSON", actual.owner.type)
        assertEquals("Alberto Tavares", actual.owner.name)
        assertEquals("06628726061", actual.owner.taxIdNumber)
        assertEquals("2021-01-12T15:34", actual.createdAt)
    }

    @Test
    fun `test CreatePixKeyResponseDto toModel`() {
        val pixResponse = CreatePixKeyResponse("bc3b1cdd-8b06-4bcd-abe3-11535b275134","CPF", "06628726061", bankAccount, owner,
            "2021-01-12T15:34")

        val actual = pixResponse.toModel()

        assertEquals("CPF", actual.keyType)
        assertEquals("06628726061", actual.pixKey)
        assertEquals("0001", actual.bankBranch)
        assertEquals("212233", actual.bankAccountNumber)
        assertEquals("CONTA_CORRENTE", actual.bankAccountType)
        assertEquals("60701190", actual.bankParticipant)
        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.ownerId)
        assertEquals("NATURAL_PERSON", actual.ownerType)
        assertEquals("Alberto Tavares", actual.ownerName)
        assertEquals("06628726061", actual.ownerTaxIdNumber)
        assertEquals("2021-01-12T15:34", actual.createdAt)
    }
}