package br.com.zup.service.health

import io.grpc.health.v1.HealthCheckRequest
import io.grpc.health.v1.HealthCheckResponse
import io.grpc.health.v1.HealthGrpc
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class HealthCheckService : HealthGrpc.HealthImplBase() {

    override fun check(request: HealthCheckRequest?, responseObserver: StreamObserver<HealthCheckResponse>?) {

        responseObserver?.onNext(HealthCheckResponse.newBuilder()
            .setStatus(HealthCheckResponse.ServingStatus.SERVING)
            .build())
        responseObserver?.onCompleted()
    }

    override fun watch(request: HealthCheckRequest?, responseObserver: StreamObserver<HealthCheckResponse>?) {
        responseObserver?.onNext(HealthCheckResponse.newBuilder()
            .setStatus(HealthCheckResponse.ServingStatus.SERVING)
            .build())
    }
}