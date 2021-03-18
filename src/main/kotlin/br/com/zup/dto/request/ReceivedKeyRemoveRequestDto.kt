package br.com.zup.dto.request

import br.com.zup.KeyRemoveRequest

class ReceivedKeyRemoveRequestDto(keyRemoveRequest: KeyRemoveRequest) {
    val pixId: String = keyRemoveRequest.pixId
    val clientId: String = keyRemoveRequest.clientId
}