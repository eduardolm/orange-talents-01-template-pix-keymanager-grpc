package br.com.zup.dto.request

import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.validation.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class CreatePixKeyRequestTest {

    @Inject
    lateinit var validator: Validator

    private lateinit var bankAccount: BankAccount
    private lateinit var owner: Owner

    @BeforeEach
    fun setup() {
        bankAccount = BankAccount("0001", "212233", "CONTA_CORRENTE",
            "60701190")
        owner = Owner("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "NATURAL_PERSON", "Alberto Tavares",
            "06628726061")
    }

    @Test
    fun `test CreatePixKeyRequest constructor with all valid args`() {

        val actual = CreatePixKeyRequest("CPF", "06628726061", bankAccount, owner)

        assertEquals("0001", actual.bankAccount.branch)
        assertEquals("212233", actual.bankAccount.accountNumber)
        assertEquals("CONTA_CORRENTE", actual.bankAccount.accountType)
        assertEquals("60701190", actual.bankAccount.participant)
        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.owner.id)
        assertEquals("NATURAL_PERSON", actual.owner.type)
        assertEquals("Alberto Tavares", actual.owner.name)
        assertEquals("06628726061", actual.owner.taxIdNumber)
    }

    @Test
    fun `test CreatePixKeyRequest with blank key type`() {

        val actual = CreatePixKeyRequest("", "06628726061", bankAccount, owner)

        val exception = validator.validate(actual)

        assertEquals(1, exception.size)
    }

    @Test
    fun `test CreatePixKeyRequest with blank key`() {

        val actual = CreatePixKeyRequest("CPF", "", bankAccount, owner)

        val exception = validator.validate(actual)

        assertEquals(2, exception.size)
    }

    @Test
    fun `test CreatePixKeyRequest with key length larger than 77 characters`() {

        val actual = CreatePixKeyRequest("CPF",
            "1236547891236547894563214789546312457896541236548797854654654654645", bankAccount, owner)

        val exception = validator.validate(actual)

        assertEquals(1, exception.size)
    }

    @Test
    fun `test CreatePixKeyRequestDto toString`() {
        val actual = CreatePixKeyRequest("", "06628726061", bankAccount, owner)

        assertEquals("CreatePixKeyRequest(keyType=, key=06628726061, bankAccount=BankAccount(branch=0001, " +
                "accountNumber=212233, accountType=CONTA_CORRENTE, participant=60701190), " +
                "owner=Owner(id=0d1bb194-3c52-4e67-8c35-a93c0af9284f, type=NATURAL_PERSON, name=Alberto Tavares, " +
                "taxIdNumber=06628726061))", actual.toString())
    }

}