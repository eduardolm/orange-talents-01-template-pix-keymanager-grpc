package br.com.zup.enums

enum class AccountType {
    CONTA_CORRENTE {
        override fun toString(): String {
            return "CACC"
        }
    },
    CONTA_POUPANCA {
        override fun toString(): String {
            return "SVGS"
        }
    }
}