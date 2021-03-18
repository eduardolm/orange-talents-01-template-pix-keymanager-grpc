package br.com.zup.dto.request

import br.com.zup.model.PixKey

class RemoveKeyRequestDto(pixKey: PixKey) {

    val key: String = pixKey.pixKey
    val participant: String = pixKey.bankParticipant
}