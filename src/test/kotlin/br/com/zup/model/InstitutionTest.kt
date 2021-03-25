package br.com.zup.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InstitutionTest {

    @Test
    fun `test Institution constructor and getters`() {
        val actual = Institution("BANCO TESTE", "321654984")

        assertEquals("BANCO TESTE", actual.name)
        assertEquals("321654984", actual.ispb)
    }

    @Test
    fun `test Institution toString`() {
        val actual = Institution("BANCO TESTE", "321654984")

        assertEquals("Institution(name=BANCO TESTE, ispb=321654984)", actual.toString())
    }

}