package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PixKeyTest {

    @Test
    fun `test PixKey constructor and getters`() {
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

        assertEquals("bc3b1cdd-8b06-4bcd-abe3-11535b275134", request.pixId)
        assertEquals("CPF", request.keyType)
        assertEquals("06628726061", request.pixKey)
        assertEquals("0d1bb194-3c52-4e67-8c35-a93c0af9284f", request.ownerId)
        assertEquals("NATURAL_PERSON", request.ownerType)
        assertEquals("Alberto Tavares", request.ownerName)
        assertEquals("06628726061", request.ownerTaxIdNumber)
        assertEquals("0001", request.bankBranch)
        assertEquals("212233", request.bankAccountNumber)
        assertEquals("60701190", request.bankParticipant)
        assertEquals("ITAÚ UNIBANCO S.A.", request.bankName)
        assertEquals("CONTA_CORRENTE", request.bankAccountType)
        assertEquals("2021-15-02t12:33", request.createdAt)
    }
}