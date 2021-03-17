package br.com.zup.service

import br.com.zup.ErrorDetails
import br.com.zup.KeyResponseRest
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.exception.KeyAlreadyRegisteredException
import br.com.zup.model.PixKey
import br.com.zup.repository.PixKeyRepository
import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
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

    fun buildErrorResponse(responseObserver: StreamObserver<KeyResponseRest>) {
        val statusProto: Status = Status.newBuilder()
            .setCode(Code.ALREADY_EXISTS.number)
            .setMessage("Chave já cadastrada.")
            .addDetails(
                Any.pack(
                    ErrorDetails.newBuilder()
                        .setCode(422)
                        .setMessage("UNPROCESSABLE_ENTITY")
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
}