package br.com.zup.dto.response

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RemoveKeyResponseDtoTest {

    @Test
    fun `test RemoveKeyResponseDto constructor and getters`() {
        val actual = RemoveKeyResponseDto("06628726061", "60701190", "2021-01-12T15:34")

        assertEquals("06628726061", actual.key)
        assertEquals("60701190", actual.participant)
        assertEquals("2021-01-12T15:34", actual.deletedAt)
    }

    @Test
    fun `test RemoveKeyResponseDto toString`() {
        val actual = RemoveKeyResponseDto("06628726061", "60701190", "2021-01-12T15:34")

        assertEquals("RemoveKeyResponseDto(key=06628726061, participant=60701190, deletedAt=2021-01-12T15:34)",
            actual.toString())
    }
}