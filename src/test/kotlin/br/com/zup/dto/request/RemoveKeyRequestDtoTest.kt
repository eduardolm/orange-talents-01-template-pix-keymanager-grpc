package br.com.zup.dto.request

import br.com.zup.model.PixKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RemoveKeyRequestDtoTest {

    @Test
    fun `test RemoveKeyRequestDto constructor and getters`() {
        val request = PixKey(
            "bc3b1cdd-8b06-4bcd-abe3-11535b275134",
            "CPF",
            "06628726061",
            "0d1bb194-3c52-4e67-8c35-a93c0af9284f",
            "NATURAL_PERSON",
            "Alberto Tavares",
            "06628726061",
            "0001",
            "212233",
            "60701190",
            "ITAÚ UNIBANCO S.A.",
            "CONTA_CORRENTE",
            "2021-15-02t12:33"
        )

        val actual = RemoveKeyRequestDto(request)

        assertEquals(request.pixKey, actual.key)
        assertEquals(request.bankParticipant, actual.participant)
    }
}