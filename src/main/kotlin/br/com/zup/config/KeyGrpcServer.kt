package br.com.zup.config

import br.com.zup.KeyRequestRest
import br.com.zup.KeyResponseRest
import br.com.zup.KeyServiceGrpc
import br.com.zup.dto.request.CreatePixKeyRequest
import br.com.zup.dto.request.ReceivedKeyRequestDto
import br.com.zup.service.KeyService
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
            keyService.buildErrorResponse(responseObserver)
            return
        }
    }
}