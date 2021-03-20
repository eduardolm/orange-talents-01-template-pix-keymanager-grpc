package br.com.zup.model

data class BankAccount(
    val branch: String,
    val accountNumber: String,
    val accountType: String,
    val participant: String
) {}
