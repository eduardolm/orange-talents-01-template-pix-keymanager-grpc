package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.ReceivedKeyRemoveRequestDto
import br.com.zup.dto.request.RemoveKeyRequestDto
import br.com.zup.exception.KeyAlreadyRegisteredException
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.factory.RetrievePixKeyFactory
import br.com.zup.model.PixKey
import br.com.zup.repository.PixKeyRepository
import com.google.protobuf.Any
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import io.micronaut.validation.Validated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Validated
class KeyService(
    @Inject val bcbClient: BcbClient,
    @Inject val repository: PixKeyRepository,
    @Inject val parser: RetrievePixKeyFactory
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createPixKey(createPixKeyRequest: CreatePixKeyRequest, request: KeyRequestRest): PixKey {

        logger.info("Iniciando cadastro de chave Pix: $createPixKeyRequest")

        if (repository.existsByPixKey(createPixKeyRequest.key)) {
            logger.warn("Chave já cadastrada: ${createPixKeyRequest.key}")
            throw KeyAlreadyRegisteredException("Chave Pix ${createPixKeyRequest.key} já cadastrada")
        }

        val newKey = bcbClient.create(createPixKeyRequest).toModel()
        newKey.bankName = request.bankAccount.institution.name
        newKey.ownerId = request.owner.id
        repository.save(newKey)

        logger.info("Chave Pix gerada: ${newKey.pixKey}")
        return newKey
    }

    fun buildErrorResponse(
        responseObserver: StreamObserver<*>,
        codeNumber: Int,
        message: String,
        httpStatusCode: Int,
        httpMessage: String
    ) {
        val statusProto: Status = Status.newBuilder()
            .setCode(codeNumber)
            .setMessage(message)
            .addDetails(
                Any.pack(
                    ErrorDetails.newBuilder()
                        .setCode(httpStatusCode)
                        .setMessage(httpMessage)
                        .build()
                )
            )
            .build()

        responseObserver.onError(StatusProto.toStatusRuntimeException(statusProto))
    }

    fun buildKeyResponseRest(result: PixKey, createPixKeyRequest: CreatePixKeyRequest): KeyResponseRest? {
        return KeyResponseRest.newBuilder()
            .setClientId(createPixKeyRequest.owner.id)
            .setPixId(result.pixId)
            .setCreatedAt(result.createdAt).build()
    }

    fun deletePixKey(request: ReceivedKeyRemoveRequestDto): KeyRemoveResponse? {

        logger.info("Iniciando remoção de Chave Pix: ${request.pixId}")

        return repository.findByPixIdAndOwnerId(request.pixId, request.clientId)
            .map {
                repository.delete(it)
                return@map bcbClient.delete(it.pixKey, RemoveKeyRequestDto(it))
            }
            .map {
                logger.info("Chave ${request.pixId} removida com sucesso.")
                KeyRemoveResponse.newBuilder()
                    .setKey(it.key)
                    .setParticipant(it.participant)
                    .setDeletedAt(it.deletedAt)
                    .build()
            }
            .orElseThrow {
                logger.error("Chave ${request.pixId} não encontrada ou não pertence ao usuário.")
                throw KeyNotFoundException("Chave não encontrada ou não pertence ao usuário.")
            }
    }

    fun getPixKeyById(keyRequestById: KeyRequestByIdDto?): PixKeyResponse? {

        logger.info("Iniciando consulta à chave Pix...")

        return parser.createFromRequest(keyRequestById).retrievePixKey(keyRequestById)
    }

    fun findAllByClientId(clientId: String?): MutableList<KeyListResponse.PixKeyItem>? {

        clientId?.let {
            if (it.isBlank()) throw IllegalArgumentException("ClientId não pode ser nulo ou vazio. ") }

        return clientId?.let { idClient ->
            repository.findAllByOwnerId(idClient)?.stream()?.map { buildPixKeyItem(it) }?.collect(Collectors.toList())
        }
    }

    fun buildPixKeyItem(it: PixKey): KeyListResponse.PixKeyItem = KeyListResponse.PixKeyItem.newBuilder()
        .setPixId(it.pixId)
        .setKeyType(KeyType.valueOf(it.keyType))
        .setPixKey(it.pixKey)
        .setAccountType(
            AccountType.valueOf(
                when (it.bankAccountType) {
                    "CACC" -> "CONTA_CORRENTE"
                    "SVGS" -> "CONTA_POUPANCA"
                    else -> "UNKNOWN_ACCOUNT_TYPE"
                }
            )
        )
        .setCreatedAt(it.createdAt)
        .build()

    fun buildKeyListResponse(request: KeyListRequest?, result: List<KeyListResponse.PixKeyItem>?): KeyListResponse? {
        return KeyListResponse.newBuilder()
            .setClientId(request?.clientId)
            .addAllPixKeys(result)
            .build()
    }
}

