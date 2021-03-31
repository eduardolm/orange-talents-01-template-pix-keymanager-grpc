package br.com.zup.config

import br.com.zup.*
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.dto.request.ReceivedKeyRemoveRequestDto
import br.com.zup.dto.request.ReceivedKeyRequestDto
import br.com.zup.exception.KeyAlreadyRegisteredException
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.service.KeyService
import com.google.rpc.Code
import io.grpc.stub.StreamObserver
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyGrpcServer(@Inject val keyService: KeyService) : KeyServiceGrpc.KeyServiceImplBase() {

    private val logger: Logger = LoggerFactory.getLogger(KeyGrpcServer::class.java)

    override fun create(request: KeyRequestRest?, responseObserver: StreamObserver<KeyResponseRest>) {

        logger.info("Cadastro de Chave Pix...")
        val createPixKeyRequest: CreatePixKeyRequest? = request?.let { ReceivedKeyRequestDto(it).toModel() }

        try {
            val result = keyService.createPixKey(createPixKeyRequest!!, request)

            val response = keyService.buildKeyResponseRest(result, createPixKeyRequest)
            responseObserver.onNext(response)
            responseObserver.onCompleted()

        } catch (e: KeyAlreadyRegisteredException) {

            logger.error("Chave: ${request?.let {it.key}} já cadastrada.")

            keyService.buildErrorResponse(
                responseObserver,
                Code.ALREADY_EXISTS.number,
                "Chave já cadastrada.",
                422,
                "UNPROCESSABLE_ENTITY"
            )
            return
        }
    }

    override fun delete(request: KeyRemoveRequest?, responseObserver: StreamObserver<KeyRemoveResponse>?) {

        logger.info("Remoção de Chave Pix...")
        val receivedKeyRemoveKeyRequestDto: ReceivedKeyRemoveRequestDto? = request?.let {ReceivedKeyRemoveRequestDto(it)}

        try {
            val result = keyService.deletePixKey(receivedKeyRemoveKeyRequestDto!!)
            responseObserver?.onNext(result)
            responseObserver?.onCompleted()

        } catch (e: KeyNotFoundException) {

            logger.error("Chave: ${request?.let {it.pixId}} não encontrada")

            keyService.buildErrorResponse(
                responseObserver!!,
                Code.NOT_FOUND.number,
                "Chave não encontrada ou não pertence ao usuário.",
                400,
                "BAD_REQUEST"
            )
            return
        }
    }

    override fun retrievePixKey(request: PixKeyRequest?, responseObserver: StreamObserver<PixKeyResponse>) {

        with(request) {
            try {
                val result = keyService.getPixKeyById(this?.let { KeyRequestByIdDto(it.pixKey, it.clientId) })

                responseObserver.onNext(result)
                responseObserver.onCompleted()

            } catch (e: KeyNotFoundException) {

            logger.error("Chave: ${request?.pixKey} não encontrada")

            keyService.buildErrorResponse(
                responseObserver,
                Code.NOT_FOUND.number,
                "Chave não encontrada.",
                404,
                "NOT_FOUND"
            )
            return
        }
        }
    }

    override fun list(request: KeyListRequest?, responseObserver: StreamObserver<KeyListResponse>?) {

        with(request) {
            try {

                val result = keyService.findAllByClientId(this?.clientId)

                responseObserver?.onNext(keyService.buildKeyListResponse(request, result))
                responseObserver?.onCompleted()

            } catch (e: IllegalArgumentException) {

                logger.error("Cliente: ${request?.clientId} não encontrado.")

                if (responseObserver != null) {
                    keyService.buildErrorResponse(
                        responseObserver,
                        Code.UNKNOWN.number,
                        "Cliente não encontrado.",
                        404,
                        "NOT_FOUND"
                    )
                }
                return
            }
        }
    }
}