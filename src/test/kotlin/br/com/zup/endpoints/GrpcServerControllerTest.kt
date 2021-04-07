package br.com.zup.endpoints

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class GrpcServerControllerTest {

    @Inject
    private lateinit var grpcServer: GrpcEmbeddedServer

    @Test
    fun `test should stop the server`() {
        val controller = GrpcServerController(grpcServer)
        assertTrue(grpcServer.isRunning)
        assertTrue(grpcServer.isServer)
        val result = controller.stop()

        assertEquals(HttpStatus.OK, result.status)
        assertEquals("Is running? false", result.body())
        assertFalse(grpcServer.isRunning)
    }
}