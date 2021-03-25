package br.com.zup.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OwnerTest {

    @Test
    fun `test Owner constructor and getters with null id`() {
        val actual = Owner(null, "NATURAL_PERSON", "Alberto Tavares", "32165498714")

        assertNull(actual.id)
        assertEquals("NATURAL_PERSON", actual.type)
        assertEquals("Alberto Tavares", actual.name)
        assertEquals("32165498714", actual.taxIdNumber)
    }

    @Test
    fun `test Owner constructor and getters with id not null`() {
        val actual = Owner("bc3b1cdd-8b06-4bcd-abe3-11535b275134", "NATURAL_PERSON", "Alberto Tavares",
            "32165498714")

        assertNotNull(actual.id)
        assertEquals("bc3b1cdd-8b06-4bcd-abe3-11535b275134", actual.id)
        assertEquals("NATURAL_PERSON", actual.type)
        assertEquals("Alberto Tavares", actual.name)
        assertEquals("32165498714", actual.taxIdNumber)
    }

    @Test
    fun `test Owner toString`() {
        val actual = Owner("bc3b1cdd-8b06-4bcd-abe3-11535b275134", "NATURAL_PERSON", "Alberto Tavares",
            "32165498714")

        assertEquals( "Owner(id=bc3b1cdd-8b06-4bcd-abe3-11535b275134, type=NATURAL_PERSON, " +
                "name=Alberto Tavares, taxIdNumber=32165498714)", actual.toString())
    }
}