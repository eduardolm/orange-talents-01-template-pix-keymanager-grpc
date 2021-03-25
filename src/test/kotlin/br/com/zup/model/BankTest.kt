package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BankTest {

    @Test
    fun `test Bank constructor and getters`() {
        val actual = Bank()
        actual.name = "ITAÚ UNIBANCO S.A."
        actual.participant = "321321654"

        assertEquals("ITAÚ UNIBANCO S.A.", actual.name)
        assertEquals("321321654", actual.participant)
    }
}