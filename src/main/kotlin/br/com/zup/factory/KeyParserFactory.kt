package br.com.zup.factory

import br.com.zup.dto.request.KeyRequestByIdDto
import javax.inject.Singleton

@Singleton
interface KeyParserFactory {
    fun createFromRequest(keyRequestById: KeyRequestByIdDto?): KeyParser
}