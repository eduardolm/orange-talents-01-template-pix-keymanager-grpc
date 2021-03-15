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

    override fun create(request: KeyRequestRest?, responseObserver: StreamObserver<KeyResponseRest>?) {
        logger.info("Obtendo chave para o request: $request")

        println("\nRequest recebido por gRPC da API REST: ")
        println(request)

        val createPixKeyRequest: CreatePixKeyRequest? = request?.let { ReceivedKeyRequestDto(it).toModel() }
        println("\n Request a ser enviado para o BCB: ")
        println(createPixKeyRequest)

        val result = keyService.create(createPixKeyRequest!!)


        val response = KeyResponseRest.newBuilder()
            .setClientId(result.owner.taxIdNumber)
            .setPixId(result.key)
            .setCreatedAt(result.createdAt).build()

        logger.info("Chave gerada: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}