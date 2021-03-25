package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BankAccountTest {

    @Test
    fun `test BankAccount constructor and getters`() {
        val actual = BankAccount("0001", "321354", "CONTA_CORRENTE", "32154964")

        assertEquals("0001", actual.branch)
        assertEquals("321354", actual.accountNumber)
        assertEquals("CONTA_CORRENTE", actual.accountType)
        assertEquals("32154964", actual.participant)
    }

    @Test
    fun `test BankAccount toString`() {
        val actual = BankAccount("0001", "321354", "CONTA_CORRENTE", "32154964")

        assertEquals("BankAccount(branch=0001, accountNumber=321354, accountType=CONTA_CORRENTE, participant=32154964)", actual.toString())
    }
}