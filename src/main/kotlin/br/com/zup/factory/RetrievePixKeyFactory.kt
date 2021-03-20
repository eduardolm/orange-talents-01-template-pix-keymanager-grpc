package br.com.zup.factory

import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.repository.PixKeyRepository
import br.com.zup.service.BcbClient
import br.com.zup.service.PixIdAndClientIdParser
import br.com.zup.service.PixKeyParser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrievePixKeyFactory(@Inject val repository: PixKeyRepository, @Inject val bcbClient: BcbClient) :
    KeyParserFactory {

     override fun createFromRequest(keyRequestById: KeyRequestByIdDto?): KeyParser {
         return when (if (keyRequestById?.clientId.isNullOrEmpty()) "PixKey" else "PixId") {
             "PixKey" -> PixKeyParser(repository, bcbClient)
             "PixId" -> PixIdAndClientIdParser(repository, bcbClient)
             else -> throw Exception("Tipo de consulta desconhecido.")
         }
    }
}