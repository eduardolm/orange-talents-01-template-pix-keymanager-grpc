package br.com.zup.repository

import br.com.zup.model.PixKey
import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PixKeyRepository : JpaRepository<PixKey, String> {
    fun existsByPixKey(key: String): Boolean
}