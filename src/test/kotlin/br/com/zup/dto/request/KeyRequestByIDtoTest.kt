package br.com.zup.dto.request

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class KeyRequestByIDtoTest {

    @Test
    fun `test KeyRequestByIdDto constructor and getters`() {
        val actual = KeyRequestByIdDto("bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        assertEquals("bc3b1cdd-8b06-4bcd-abe3-11535b275134", actual.id)
        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", actual.clientId)
    }

    @Test
    fun `test KeyRequestByIdDto toString`() {
        val actual = KeyRequestByIdDto("bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f")

        assertEquals("KeyRequestByIdDto(id=bc3b1cdd-8b06-4bcd-abe3-11535b275134, " +
                "clientId=0d1bb194-3c52-4e67-8c35-a93c0af9284f)", actual.toString())
    }


}