package br.com.zup.enums

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AccountTypeTest {

    @Test
    fun `test AccountType resturn values`() {
        val actual = AccountType.valueOf("CONTA_CORRENTE")
        val actual2 = AccountType.valueOf("CONTA_POUPANCA")

        assertEquals("CONTA_CORRENTE", actual.name)
        assertEquals("CACC", actual.toString())
        assertEquals("CONTA_POUPANCA", actual2.name)
        assertEquals("SVGS", actual2.toString())
    }
}