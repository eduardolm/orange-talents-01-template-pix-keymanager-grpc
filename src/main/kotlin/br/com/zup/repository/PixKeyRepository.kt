package br.com.zup.repository

import br.com.zup.model.PixKey
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface PixKeyRepository : JpaRepository<PixKey, String> {
    fun existsByPixKey(key: String): Boolean
    fun findByPixIdAndOwnerId(pixId: String, ownerId: String): Optional<PixKey>
    fun findByPixKey(key: String): Optional<PixKey>
}