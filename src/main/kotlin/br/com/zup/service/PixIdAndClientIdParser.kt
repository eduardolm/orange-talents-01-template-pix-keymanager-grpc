package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.factory.KeyParser
import br.com.zup.model.PixKey
import br.com.zup.repository.BankRepository
import br.com.zup.repository.PixKeyRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject

class PixIdAndClientIdParser(
    @Inject val repository: PixKeyRepository,
    @Inject val bcbClient: BcbClient,
    @Inject val bankRepository: BankRepository
) : KeyParser{

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun retrievePixKey(request: KeyRequestByIdDto?): PixKeyResponse? {
        request?.let { retrieveKey ->

            return retrieveKey.clientId?.let { clientId ->
                repository.findByPixIdAndOwnerId(retrieveKey.id, clientId)
                    .map {
                        logger.info("Chave Pix: ${it.pixId}")
                        return@map buildRetrieveKeyByIdAndClientId(it)
                    }
                    .orElseGet {
                        bcbClient.findById(retrieveKey.id).map {
                            it.toModel()
                        }
                            .map {
                                logger.info("Chave Pix: ${it.pixId}")
                                return@map buildRetrieveKeyByIdAndClientId(it)
                            }
                            .orElseThrow {

                                logger.error("Chave Pix: ${request.id} não encontrada.")
                                throw KeyNotFoundException("Chave Pix: ${request.id} não encontrada.")
                            }
                    }
            }
        }
        return null
    }

    fun buildRetrieveKeyByIdAndClientId(pixKey: PixKey): PixKeyResponse? {

        val bankInfo = bankRepository.findByParticipant(pixKey.bankParticipant)

        return pixKey.let {
            PixKeyResponse.newBuilder()
                .setPixId(it.pixId)
                .setClientId(it.ownerId)
                .setKeyType(KeyType.valueOf(it.keyType))
                .setPixKey(it.pixKey)
                .setAccount(
                    BankAccount.newBuilder()
                        .setInstitution(Institution.newBuilder()
                            .setName(bankInfo.get().name)
                            .setParticipant(bankInfo.get().participant)
                            .build())
                        .setBranch(it.bankBranch)
                        .setAccountNumber(it.bankAccountNumber)
                        .setAccountType(if (it.bankAccountType == "CACC") "CONTA_CORRENTE"
                        else "CONTA_POUPANCA")
                        .build())
                .setOwner(
                    ResponseOwner.newBuilder()
                        .setName(it.ownerName)
                        .setTaxIdNumber(it.ownerTaxIdNumber)
                        .build())
                .setCreatedAt(it.createdAt)
                .build()
        }
    }
}
