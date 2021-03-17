package br.com.zup.dto.response

data class RemoveKeyResponseDto(
    val key: String,
    val participant: String,
    val deletedAt: String
) {
}