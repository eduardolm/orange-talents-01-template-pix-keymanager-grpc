package br.com.zup.dto.request

import br.com.zup.PixKeyRequest

class KeyRequestByIdDto(pixKeyRequest: PixKeyRequest) {
    val pixKey: String = pixKeyRequest.pixKey
    val pixId: String = pixKeyRequest.pixIdAndClientId.pixId
    val clientId: String = pixKeyRequest.pixIdAndClientId.clientId

}
