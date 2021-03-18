package br.com.zup.service

import br.com.zup.ErrorDetails
import br.com.zup.KeyRemoveResponse
import br.com.zup.KeyResponseRest
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.ReceivedKeyRemoveRequestDto
import br.com.zup.dto.request.RemoveKeyRequestDto
import br.com.zup.exception.KeyAlreadyRegisteredException
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.model.PixKey
import br.com.zup.repository.PixKeyRepository
import com.google.protobuf.Any
import com.google.rpc.Status
import io.grpc.Context.key
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyService(@Inject val bcbClient: BcbClient, @Inject val repository: PixKeyRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun createPixKey(createPixKeyRequest: CreatePixKeyRequest): PixKey {

        logger.info("Iniciando cadastro de chave Pix: $createPixKeyRequest")

        if (repository.existsByPixKey(createPixKeyRequest.key)) {
            logger.warn("Chave já cadastrada: ${createPixKeyRequest.key}")
            throw KeyAlreadyRegisteredException("Chave Pix ${createPixKeyRequest.key} já cadastrada")
        }

        val newKey = bcbClient.create(createPixKeyRequest).toModel(createPixKeyRequest)
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
            .setPixId(result.pixKey)
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
                KeyRemoveResponse.newBuilder()
                    .setKey(it.key)
                    .setParticipant(it.participant)
                    .setDeletedAt(it.deletedAt)
                    .build()
            }
            .orElseThrow { throw KeyNotFoundException("Chave não encontrada ou não pertence ao usuário.") }
//        val result = KeyRemoveResponse.newBuilder()
//                    .setKey(teste.get().key)
//                    .setParticipant(teste.get().participant)
//                    .setDeletedAt(teste.get().deletedAt)
//                    .build()
//        return result
    }
}

