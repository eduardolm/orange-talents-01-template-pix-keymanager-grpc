package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.ReceivedKeyRemoveRequestDto
import br.com.zup.dto.request.RemoveKeyRequestDto
import br.com.zup.dto.response.CreatePixKeyResponse
import br.com.zup.dto.response.RemoveKeyResponseDto
import br.com.zup.exception.KeyAlreadyRegisteredException
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.factory.RetrievePixKeyFactory
import br.com.zup.model.Bank
import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import br.com.zup.model.PixKey
import br.com.zup.repository.BankRepository
import br.com.zup.repository.PixKeyRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.verification.VerificationModeFactory.times
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
class KeyServiceTest {

    @Mock
    private lateinit var parser: RetrievePixKeyFactory

    @Inject
    private lateinit var repository: PixKeyRepository

    @Inject
    private lateinit var bankRepository: BankRepository

    @Mock
    private lateinit var bcbClient: BcbClient

    private lateinit var pixKey: PixKey
    private lateinit var keyRequestRest: KeyRequestRest
    private lateinit var pixKeyRequest: CreatePixKeyRequest
    private lateinit var createPixKeyResponse: CreatePixKeyResponse
    private lateinit var removeRequestDto: ReceivedKeyRemoveRequestDto
    private lateinit var keyRemoveResponse: KeyRemoveResponse
    private lateinit var removeResponseDto: RemoveKeyResponseDto
    private lateinit var removeKeyRequestDto: RemoveKeyRequestDto
    private lateinit var owner: Owner
    private lateinit var bankAccount: BankAccount
    private lateinit var bank: Bank
    private lateinit var EXISTING_PIXKEY: PixKey
    private lateinit var pixKeyResponse: PixKeyResponse

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
            "CACC",
            "2021-15-02t12:33"
        )

        bank = Bank()
        bank.name = "ITAÚ UNIBANCO S.A."
        bank.participant = "60701190"
        bankRepository.save(bank)

        owner = Owner("0d1bb194-3c52-4e67-8c35-a93c0af9284f", "NATURAL_PERSON", "Alberto Tavares",
            "06628726061")

        bankAccount = BankAccount("0001", "212233", "CONTA_CORRENTE", "60701190")

        keyRequestRest = KeyRequestRest.newBuilder()
            .setKeyType(KeyType.valueOf(pixKey.keyType))
            .setKey(pixKey.pixKey)
            .setBankAccount(br.com.zup.BankAccount.newBuilder()
                .setBranch(bankAccount.branch)
                .setAccountNumber(bankAccount.accountNumber)
                .setAccountType(bankAccount.accountType)
                .setInstitution(Institution.newBuilder()
                    .setName(bank.name)
                    .setParticipant(bank.participant)
                    .build())
                .build())
            .setOwner(br.com.zup.Owner.newBuilder()
                .setId(owner.id)
                .setName(owner.name)
                .setTaxIdNumber(owner.taxIdNumber)
                .build())
            .build()

        pixKeyRequest = CreatePixKeyRequest("CPF", "06628726061", bankAccount, owner)

        createPixKeyResponse = CreatePixKeyResponse("bc3b1cdd-8b06-4bcd-abe3-11535b275134","CPF",
            "06628726061", bankAccount, owner, "2021-01-12T15:34")

        removeRequestDto = ReceivedKeyRemoveRequestDto(KeyRemoveRequest.newBuilder()
            .setPixId("bc3b1cdd-8b06-4bcd-abe3-11535b275134")
            .setClientId("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .build())

        removeResponseDto = RemoveKeyResponseDto("06628726061", "60701190", "2021-03-26T:16:23")


        keyRemoveResponse = KeyRemoveResponse.newBuilder()
            .setKey(removeResponseDto.key)
            .setParticipant(removeResponseDto.participant)
            .setDeletedAt(removeResponseDto.deletedAt)
            .build()

        EXISTING_PIXKEY = pixKey

        removeKeyRequestDto = RemoveKeyRequestDto(EXISTING_PIXKEY)

        pixKeyResponse = PixKeyResponse.newBuilder()
            .setPixId(EXISTING_PIXKEY.pixId)
            .setClientId(EXISTING_PIXKEY.ownerId)
            .setKeyType(KeyType.valueOf(EXISTING_PIXKEY.keyType))
            .setPixKey(EXISTING_PIXKEY.pixKey)
            .setOwner(ResponseOwner.newBuilder()
                .setName(EXISTING_PIXKEY.ownerName)
                .setTaxIdNumber(EXISTING_PIXKEY.ownerTaxIdNumber)
                .build())
            .setAccount(br.com.zup.BankAccount.newBuilder()
                .setBranch(EXISTING_PIXKEY.bankBranch)
                .setAccountNumber(EXISTING_PIXKEY.bankAccountNumber)
                .setAccountType(EXISTING_PIXKEY.bankAccountType)
                .setInstitution(Institution.newBuilder()
                    .setName(EXISTING_PIXKEY.bankName)
                    .setParticipant(EXISTING_PIXKEY.bankParticipant)
                    .build())
                .build())
            .setCreatedAt(EXISTING_PIXKEY.createdAt)
            .build()
    }

    @AfterEach
    fun cleanUp() {
        repository.deleteAll()
        bankRepository.deleteAll()
    }

    @Test
    fun `test createPixKey when key does not exist`() {
        Mockito.`when`(bcbClient.create(pixKeyRequest)).thenReturn(createPixKeyResponse)

        val keyService = KeyService(bcbClient, repository, parser)

        val result = keyService.createPixKey(pixKeyRequest, keyRequestRest)

        Mockito.verify(bcbClient).create(pixKeyRequest)
        assertEquals("06628726061", result.pixKey)
    }

    @Test
    fun `test createPixKey when key exists should throw KeyAlreadyRegisteredException`() {
        repository.save(EXISTING_PIXKEY)
        val keyService = KeyService(bcbClient, repository, parser)

        assertThrows(KeyAlreadyRegisteredException::class.java) {
            keyService.createPixKey(pixKeyRequest, keyRequestRest)
        }
    }

    @Test
    fun `test deletePixKey with invalid pixKey and ClientId`() {
        repository.save(EXISTING_PIXKEY)
        val removeKey = RemoveKeyRequestDto(EXISTING_PIXKEY)
        Mockito.`when`(bcbClient.delete(EXISTING_PIXKEY.pixKey, removeKey)).thenReturn(removeResponseDto)

        val keyService = KeyService(bcbClient, repository, parser)

        assertThrows(KeyNotFoundException::class.java) {keyService.deletePixKey(removeRequestDto)}
    }

    @Test
    fun `test getPixKeyById when pixKey exists in db`() {
        repository.save(EXISTING_PIXKEY)
        Mockito.`when`(bcbClient.findById(EXISTING_PIXKEY.pixKey)).thenReturn(Optional.of(createPixKeyResponse))
        Mockito.`when`(parser.createFromRequest(KeyRequestByIdDto(EXISTING_PIXKEY.pixKey, null)))
            .thenReturn(PixKeyParser(repository, bcbClient, bankRepository))

        val keyService = KeyService(bcbClient, repository, parser)
        val result = keyService.getPixKeyById(KeyRequestByIdDto(EXISTING_PIXKEY.pixKey, null))

        Mockito.verify(bcbClient, times(0)).findById(EXISTING_PIXKEY.pixKey)
        assertEquals("06628726061", result?.pixKey)
    }

    @Test
    fun `test getPixKeyById when pixKey does not exist in db but exists in BCB`() {
        Mockito.`when`(bcbClient.findById(Mockito.anyString())).thenReturn(Optional.of(createPixKeyResponse))
        Mockito.`when`(parser.createFromRequest(KeyRequestByIdDto(EXISTING_PIXKEY.pixKey, null)))
            .thenReturn(PixKeyParser(repository, bcbClient, bankRepository))

        val keyService = KeyService(bcbClient, repository, parser)
        val result = keyService.getPixKeyById(KeyRequestByIdDto(EXISTING_PIXKEY.pixKey, null))

        Mockito.verify(bcbClient, times(1)).findById(EXISTING_PIXKEY.pixKey)
        assertEquals("06628726061", result?.pixKey)
    }

    @Test
    fun `test findAllByClientId with valid clientId`() {
        repository.save(EXISTING_PIXKEY)

        val keyService = KeyService(bcbClient, repository, parser)
        val result = keyService.findAllByClientId(EXISTING_PIXKEY.ownerId)

        assertEquals(1, result?.size)
        assertEquals("06628726061", result?.get(0)?.pixKey)
    }

    @Test
    fun `test findAllByClientId with blank clientId`() {
        repository.save(EXISTING_PIXKEY)

        val keyService = KeyService(bcbClient, repository, parser)

        assertThrows(IllegalArgumentException::class.java) { keyService.findAllByClientId("")}
    }
}