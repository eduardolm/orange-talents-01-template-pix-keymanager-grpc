package br.com.zup.factory

import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.repository.BankRepository
import br.com.zup.repository.PixKeyRepository
import br.com.zup.service.BcbClient
import br.com.zup.service.PixIdAndClientIdParser
import br.com.zup.service.PixKeyParser
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class RetrievePixFactoryTest {

    @Inject
    private lateinit var repository: PixKeyRepository

    @Inject
    private lateinit var bcbClient: BcbClient

    @Inject
    private lateinit var bankRepository: BankRepository

    @Test
    fun `test createFromRequest when passing PixKey`() {
        val request = KeyRequestByIdDto("06628726061", "")
        val factory = RetrievePixKeyFactory(repository, bcbClient, bankRepository)
        val result = factory.createFromRequest(request)

        assertEquals(PixKeyParser::class.simpleName, result::class.simpleName)
    }

    @Test
    fun `test createFromRequest when passing PixId and ClientId`() {
        val request = KeyRequestByIdDto("bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")
        val factory = RetrievePixKeyFactory(repository, bcbClient, bankRepository)
        val result = factory.createFromRequest(request)

        assertEquals(PixIdAndClientIdParser::class.simpleName, result::class.simpleName)
    }
}