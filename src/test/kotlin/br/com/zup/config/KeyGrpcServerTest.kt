package br.com.zup.config


import br.com.zup.*
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.ReceivedKeyRemoveRequestDto
import br.com.zup.dto.request.RemoveKeyRequestDto
import br.com.zup.dto.response.CreatePixKeyResponse
import br.com.zup.dto.response.RemoveKeyResponseDto
import br.com.zup.model.Bank
import br.com.zup.model.BankAccount
import br.com.zup.model.Owner
import br.com.zup.model.PixKey
import br.com.zup.repository.BankRepository
import br.com.zup.repository.PixKeyRepository
import br.com.zup.service.BcbClient
import br.com.zup.service.KeyService
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import java.util.*
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class KeyGrpcServerTest {

    @Inject
    private lateinit var grpcClient: KeyServiceGrpc.KeyServiceBlockingStub

    @Inject
    private lateinit var repository: PixKeyRepository

    @Inject
    private lateinit var bankRepository: BankRepository

    @Inject
    private lateinit var bcbClient: BcbClient

    @Inject
    private lateinit var keyService: KeyService

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
    private var pixKeyValue: String = ""

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
                .setInstitution(
                    Institution.newBuilder()
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

        removeRequestDto = ReceivedKeyRemoveRequestDto(
            KeyRemoveRequest.newBuilder()
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
            .setOwner(
                ResponseOwner.newBuilder()
                .setName(EXISTING_PIXKEY.ownerName)
                .setTaxIdNumber(EXISTING_PIXKEY.ownerTaxIdNumber)
                .build())
            .setAccount(br.com.zup.BankAccount.newBuilder()
                .setBranch(EXISTING_PIXKEY.bankBranch)
                .setAccountNumber(EXISTING_PIXKEY.bankAccountNumber)
                .setAccountType(EXISTING_PIXKEY.bankAccountType)
                .setInstitution(
                    Institution.newBuilder()
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

        if (pixKeyValue.isNotBlank()) bcbClient.delete(pixKeyValue, removeKeyRequestDto)
    }

    @Test
    fun `test create new PixKey when key not registered yet`() {

        val response = grpcClient.create(keyRequestRest)
        pixKeyValue = keyService.getPixKeyById(KeyRequestByIdDto(response.pixId, response.clientId))!!.pixKey

        with(response) {
            assertEquals(pixKey.ownerId, clientId)
            assertNotNull(pixId)
        }
    }

    @Test
    fun `test create new PixKey when key already registered should throw StatusRuntimeException`() {

        repository.save(EXISTING_PIXKEY)

        assertThrows(StatusRuntimeException::class.java) {grpcClient.create(keyRequestRest)}
    }

    @Test
    fun `test delete pixKey when key exists and belongs to the requesting user`() {

        val response = grpcClient.create(keyRequestRest)
        val keyToDelete = keyService.getPixKeyById(KeyRequestByIdDto(response.pixId, response.clientId))
        repository.save(EXISTING_PIXKEY)

        val result = grpcClient.delete(KeyRemoveRequest.newBuilder()
            .setPixId(EXISTING_PIXKEY.pixId)
            .setClientId(EXISTING_PIXKEY.ownerId)
            .build())

        assertEquals(keyToDelete?.pixKey, result.key)
        assertEquals(keyToDelete?.account?.institution?.participant, result.participant)
    }

    @Test
    fun `test delete pixKey when key exists but does not belong to the user`() {
        val response = grpcClient.create(keyRequestRest)
        pixKeyValue = keyService.getPixKeyById(KeyRequestByIdDto(response.pixId, response.clientId))!!.pixKey

        assertThrows(StatusRuntimeException::class.java) {grpcClient.delete(KeyRemoveRequest.newBuilder()
            .setPixId(EXISTING_PIXKEY.pixId)
            .setClientId(UUID.randomUUID().toString())
            .build())}
    }

    @Test
    fun `test delete pixKey when key does not exist`() {
        assertThrows(StatusRuntimeException::class.java) {grpcClient.delete(KeyRemoveRequest.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setClientId(UUID.randomUUID().toString())
            .build())}
    }

    @Test
    fun `test retrievePixKey by PixKey when pixKey exists`() {

        val createResponse = grpcClient.create(keyRequestRest)
        pixKeyValue = keyService.getPixKeyById(KeyRequestByIdDto(createResponse.pixId, createResponse.clientId))!!.pixKey

        val response = grpcClient.retrievePixKey(PixKeyRequest.newBuilder()
            .setPixKey(pixKeyValue)
            .setClientId("")
            .build())

        assertEquals(pixKeyValue, response.pixKey)
        assertEquals(keyRequestRest.owner.taxIdNumber, response.owner.taxIdNumber)
    }

    @Test
    fun `test retrievePixKey by pixKey when pixKey does not exist`() {

        assertThrows(StatusRuntimeException::class.java) {
            grpcClient.retrievePixKey(
                PixKeyRequest.newBuilder()
                    .setPixKey(UUID.randomUUID().toString())
                    .setClientId("")
                    .build()
            )
        }
    }

    @Test
    fun `test retrievePixKey by pixId and clientId when pixKey exist`() {

        val createResponse = grpcClient.create(keyRequestRest)
        pixKeyValue = keyService.getPixKeyById(KeyRequestByIdDto(createResponse.pixId, createResponse.clientId))!!.pixKey

        val response = grpcClient.retrievePixKey(PixKeyRequest.newBuilder()
            .setPixKey(createResponse.pixId)
            .setClientId(createResponse.clientId)
            .build())

        assertEquals(pixKeyValue, response.pixKey)
        assertEquals(keyRequestRest.owner.taxIdNumber, response.owner.taxIdNumber)
    }

    @Test
    fun `test retrievePixKey by pixId and clientId when pixId does not exist should throw StatusRuntimeException`() {

        assertThrows(StatusRuntimeException::class.java) { grpcClient.retrievePixKey(PixKeyRequest.newBuilder()
            .setPixKey(UUID.randomUUID().toString())
            .setClientId(keyRequestRest.owner.id)
            .build())}
    }

    @Test
    fun `test retrievePixKey by pixId and clientId when pixId exist but clientId does not exist should throw StatusRuntimeException`() {

        val createResponse = grpcClient.create(keyRequestRest)
        pixKeyValue = keyService.getPixKeyById(KeyRequestByIdDto(createResponse.pixId, createResponse.clientId))!!.pixKey

        assertThrows(StatusRuntimeException::class.java) { grpcClient.retrievePixKey(PixKeyRequest.newBuilder()
            .setPixKey(createResponse.pixId)
            .setClientId(UUID.randomUUID().toString())
            .build())}
    }

    @Test
    fun `test list when client exists`() {
        repository.save(EXISTING_PIXKEY)

        val response = grpcClient.list(KeyListRequest.newBuilder()
            .setClientId(EXISTING_PIXKEY.ownerId)
            .build())

        assertEquals(1, response.pixKeysList.size)
        assertEquals(EXISTING_PIXKEY.pixKey, response.pixKeysList[0].pixKey)
    }

    @Test
    fun `test list when clientId is blank should throw StatusRuntimeException`() {

        assertThrows(StatusRuntimeException::class.java) {grpcClient.list(KeyListRequest.newBuilder()
            .setClientId("")
            .build()) }
    }

    @Test
    fun `test list when clientId does not exist should return an empty list`() {
        repository.save(EXISTING_PIXKEY)

        val response = grpcClient.list(KeyListRequest.newBuilder()
            .setClientId(UUID.randomUUID().toString())
            .build())

        assertEquals(0, response.pixKeysList.size)
    }

    @Factory
    class Clients  {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                KeyServiceGrpc.KeyServiceBlockingStub {
            return KeyServiceGrpc.newBlockingStub(channel)
        }
    }
}