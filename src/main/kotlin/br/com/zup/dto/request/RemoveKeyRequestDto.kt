package br.com.zup.dto.request

import br.com.zup.KeyRemoveRequest

class RemoveKeyRequestDto(keyRemoveRequest: KeyRemoveRequest) {
    val key: String = keyRemoveRequest.key
    val participant: String = keyRemoveRequest.participant
}