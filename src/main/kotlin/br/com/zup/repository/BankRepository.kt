package br.com.zup.repository

import br.com.zup.model.Bank
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface BankRepository : JpaRepository<Bank, Long> {
    fun findByParticipant(participant: String): Optional<Bank>
}