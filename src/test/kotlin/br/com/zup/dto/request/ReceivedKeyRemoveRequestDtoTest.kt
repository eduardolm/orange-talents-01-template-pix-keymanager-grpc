package br.com.zup.dto.request

import br.com.zup.KeyRemoveRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ReceivedKeyRemoveRequestDtoTest {

    @Test
    fun `test ReceivedKeyRemoveRequestDto constructor and getters`() {
        val keyRemoveRequest = KeyRemoveRequest.newBuilder()
            .setPixId("bc3b1cdd-8b06-4bcd-abe3-11535b275134")
            .setClientId("0d1bb194-3c52-4e67-8c35-a93c0af9284f")
            .build()

        val actual = ReceivedKeyRemoveRequestDto(keyRemoveRequest)

        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.clientId)
        assertEquals("bc3b1cdd-8b06-4bcd-abe3-11535b275134", actual.pixId)
    }
}