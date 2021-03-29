package br.com.zup.factory

import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.repository.BankRepository
import br.com.zup.repository.PixKeyRepository
import br.com.zup.service.BcbClient
import br.com.zup.service.PixIdAndClientIdParser
import br.com.zup.service.PixKeyParser
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Validated
class RetrievePixKeyFactory(
    @Inject val repository: PixKeyRepository,
    @Inject val bcbClient: BcbClient,
    @Inject val bankRepository: BankRepository
) :
    KeyParserFactory {

     override fun createFromRequest(keyRequestById: KeyRequestByIdDto?): KeyParser {
         return when (if (keyRequestById?.clientId.isNullOrEmpty() || keyRequestById?.clientId == "0") "PixKey" else "PixId") {
             "PixKey" -> PixKeyParser(repository, bcbClient, bankRepository)
             "PixId" -> PixIdAndClientIdParser(repository, bcbClient, bankRepository)
             else -> throw Exception("Tipo de consulta desconhecido.")
         }
    }
}