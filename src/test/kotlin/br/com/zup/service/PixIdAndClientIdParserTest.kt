package br.com.zup.service

import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.response.CreatePixKeyResponse
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.model.Bank
import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import br.com.zup.model.PixKey
import br.com.zup.repository.BankRepository
import br.com.zup.repository.PixKeyRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

@MicronautTest
class PixIdAndClientIdParserTest {

    @Mock
    private lateinit var repository: PixKeyRepository

    @Mock
    private lateinit var bcbClient: BcbClient

    @Mock
    private lateinit var bankRepository: BankRepository

    private lateinit var pixKey: PixKey
    private lateinit var pixKeyResponse: CreatePixKeyResponse
    private lateinit var owner: Owner
    private lateinit var bankAccount: BankAccount
    private lateinit var bank: Bank

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        pixKey = PixKey(
            "bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "CPF",
            "06628726061",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "NATURAL_PERSON",
            "Alberto Tavares",
            "06628726061",
            "0001",
            "212233",
            "60701190",
            "ITAÚ UNIBANCO S.A.",
            "CONTA_CORRENTE",
            "2021-15-02t12:33"
        )

        bank = Bank()
        bank.name = "ITAÚ UNIBANCO S.A."
        bank.participant = "60701190"

        owner = Owner("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "NATURAL_PERSON", "Alberto Tavares",
            "06628726061")

        bankAccount = BankAccount("0001", "212233", "CONTA_CORRENTE", "60701190")

        pixKeyResponse = CreatePixKeyResponse("bc3b1cdd-8b06-4bcd-abe3-11535b275134","CPF", "06628726061",
            bankAccount, owner, "2021-01-12T15:34")
    }

    @Test
    fun `test retrievePixKey passing valid pixId and clientId when pixKey is stored in db`() {
        Mockito.`when`(repository.findByPixIdAndOwnerId(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Optional.of(pixKey))
        Mockito.`when`(bcbClient.findById(Mockito.anyString())).thenReturn(Optional.of(pixKeyResponse))
        Mockito.`when`(bankRepository.findByParticipant(Mockito.anyString())).thenReturn(Optional.of(bank))

        val keyRequestByIdDto = KeyRequestByIdDto("bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        val pixIdAndClientIdParser = PixIdAndClientIdParser(repository, bcbClient, bankRepository)

        val result = pixIdAndClientIdParser.retrievePixKey(keyRequestByIdDto)

        Mockito.verify(repository).findByPixIdAndOwnerId("bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")
        Mockito.verify(bankRepository).findByParticipant("60701190")
        Assertions.assertEquals("06628726061", result?.pixKey)
        Assertions.assertEquals("bc3b1cdd-8b06-4bcd-abe3-11535b275134", result?.pixId)
        Assertions.assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", result?.clientId)
        Assertions.assertEquals("CPF", result?.keyType?.toString())
        Assertions.assertEquals("Alberto Tavares", result?.owner?.name)
    }

    @Test
    fun `test retrievePixKey passing valid pixId and clientId when pixKey is not stored in db`() {
        Mockito.`when`(repository.findByPixIdAndOwnerId(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Optional.empty())
        Mockito.`when`(bcbClient.findById(Mockito.anyString())).thenReturn(Optional.of(pixKeyResponse))
        Mockito.`when`(bankRepository.findByParticipant(Mockito.anyString())).thenReturn(Optional.of(bank))

        val keyRequestByIdDto = KeyRequestByIdDto("bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        val pixIdAndClientIdParser = PixIdAndClientIdParser(repository, bcbClient, bankRepository)

        val result = pixIdAndClientIdParser.retrievePixKey(keyRequestByIdDto)

        Mockito.verify(bcbClient).findById("bc3b1cdd-8b06-4bcd-abe3-11535b275134")
        Mockito.verify(bankRepository).findByParticipant("60701190")
        Assertions.assertEquals("06628726061", result?.pixKey)
        Assertions.assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", result?.clientId)
        Assertions.assertEquals("CPF", result?.keyType?.toString())
        Assertions.assertEquals("Alberto Tavares", result?.owner?.name)
    }

    @Test
    fun `test retrievePixKey passing invalid pixId and valid clientId should throw KeyNotFoundException`() {
        Mockito.`when`(repository.findByPixIdAndOwnerId(Mockito.anyString(), Mockito.anyString()))
            .thenReturn(Optional.empty())
        Mockito.`when`(bcbClient.findById(Mockito.anyString())).thenReturn(Optional.empty())
        Mockito.`when`(bankRepository.findByParticipant(Mockito.anyString())).thenReturn(Optional.of(bank))

        val keyRequestByIdDto = KeyRequestByIdDto("bc3b1cdd-8b06-4bcd-abe3-11535b275131",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        val pixIdAndClientIdParser = PixIdAndClientIdParser(repository, bcbClient, bankRepository)

        Assertions.assertThrows(KeyNotFoundException::class.java) {
            pixIdAndClientIdParser.retrievePixKey(keyRequestByIdDto)
        }
        Mockito.verify(repository).findByPixIdAndOwnerId("bc3b1cdd-8b06-4bcd-abe3-11535b275131",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")
        Mockito.verify(bcbClient).findById("bc3b1cdd-8b06-4bcd-abe3-11535b275131")
    }
}