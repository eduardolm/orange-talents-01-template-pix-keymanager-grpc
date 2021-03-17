package br.com.zup.config

import br.com.zup.*
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.ReceivedKeyRequestDto
import br.com.zup.dto.request.RemoveKeyRequestDto
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
            val result = keyService.createPixKey(createPixKeyRequest!!)

            val response = keyService.buildKeyResponseRest(result, createPixKeyRequest)
            responseObserver.onNext(response)
            responseObserver.onCompleted()

        } catch (e: Exception) {
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
        val keyRemoveRequest: RemoveKeyRequestDto? = request?.let { RemoveKeyRequestDto(it) }

        try {
            val result = keyService.deletePixKey(keyRemoveRequest!!)
            responseObserver?.onNext(result)
            responseObserver?.onCompleted()

        } catch (e: KeyNotFoundException) {

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
}