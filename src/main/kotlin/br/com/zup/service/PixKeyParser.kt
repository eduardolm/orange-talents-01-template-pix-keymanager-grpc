package br.com.zup.service

import br.com.zup.*
import br.com.zup.dto.request.KeyRequestByIdDto
import br.com.zup.exception.KeyNotFoundException
import br.com.zup.factory.KeyParser
import br.com.zup.model.PixKey
import br.com.zup.repository.PixKeyRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PixKeyParser(@Inject val repository: PixKeyRepository, @Inject val bcbClient: BcbClient) : KeyParser {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun retrievePixKey(keyRequestById: KeyRequestByIdDto?): PixKeyResponse? {
        keyRequestById?.let { retrieveKey ->

            return repository.findByPixKey(retrieveKey.pixKey)
                .map {
                    logger.info("Chave Pix: ${it.pixKey}")
                    return@map buildRetrieveKeyByPixKey(it)
                }
                .orElseGet {
                    bcbClient.findById(retrieveKey.pixKey).map {
                        it.toModel()
                    }
                        .map {
                            logger.info("Chave Pix: ${it.pixKey}")
                            return@map buildRetrieveKeyByPixKey(it)
                        }
                        .orElseThrow {

                            logger.error("Chave Pix: ${keyRequestById.pixKey} não encontrada.")
                            throw KeyNotFoundException("Chave Pix: ${keyRequestById.pixKey} não encontrada.")
                        }
                }
        }
        return null
    }

    fun buildRetrieveKeyByPixKey(pixKey: PixKey): PixKeyResponse? {
        return pixKey.let {
            PixKeyResponse.newBuilder()
                .setPixId("0")
                .setClientId("0")
                .setKeyType(KeyType.valueOf(it.keyType))
                .setPixKey(it.pixKey)
                .setAccount(
                    ResponseAccount.newBuilder()
                    .setBankParticipant(it.bankParticipant)
                    .setBankBranch(it.bankBranch)
                    .setBankAccountNumber(it.bankAccountNumber)
                    .setBankAccountType(AccountType.valueOf(if (it.bankAccountType == "CACC") "CONTA_CORRENTE"
                    else "CONTA_POUPANCA"))
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