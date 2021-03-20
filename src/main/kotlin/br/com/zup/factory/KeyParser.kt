package br.com.zup.factory

import br.com.zup.PixKeyResponse
import br.com.zup.dto.request.KeyRequestByIdDto
import javax.inject.Singleton

@Singleton
interface KeyParser {
    fun retrievePixKey(request: KeyRequestByIdDto?): PixKeyResponse?
}