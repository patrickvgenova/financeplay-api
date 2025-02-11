package com.example.financeplayapi.commons.core.type

class CPF {

    private val cpf: String

    constructor(cpf: String) {
        require(isValidCPF(cpf)) { "Illegal cpf format: $cpf" }
        this.cpf = cpf.replace(nonNumbers, "").padStart(11, '0')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CPF

        return cpf == other.cpf
    }

    override fun hashCode(): Int {
        return cpf.hashCode()
    }

    fun printPretty(): String {
        return "${this.cpf[0]}${this.cpf[1]}${this.cpf[2]}.${this.cpf[3]}${this.cpf[4]}${this.cpf[5]}.${this.cpf[6]}${this.cpf[7]}${this.cpf[8]}-${this.cpf[9]}${this.cpf[10]}"
    }

    fun printNumbers(): String {
        return this.cpf
    }

    fun print(pretty: Boolean = false): String {
        return if (pretty) printPretty() else printNumbers()
    }

    override fun toString(): String {
        return printNumbers()
    }

    companion object {

        private val nonNumbers = "[^0-9]".toRegex()

        private val validFormats = listOf(
            """\d{1,11}""".toRegex(),
            """^\d{1,3}\.\d{3}\.\d{3}-\d{2}$""".toRegex(),
            """^\d{1,3}\.\d{3}-\d{2}$""".toRegex(),
            """^\d{1,3}-\d{2}$""".toRegex(),
        )

        fun isValidCPF(cpf: String): Boolean {
            validFormats.find { cpf.matches(it) } ?: return false
//            if (true) return validFormats.any { cpf.matches(it) }
            // Remova caracteres não numéricos do CPF
            val cleanedCPF = cpf.replace(nonNumbers, "").padStart(11, '0')

            // Verifique se todos os dígitos são iguais (caso contrário, não é válido)
            if (cleanedCPF.all { it == cleanedCPF[0] }) {
                return false
            }

            // Calcula o primeiro dígito verificador
            val firstDigit = (0 until 9).sumOf { (cleanedCPF[it].code - 48) * (10 - it) } % 11
            val expectedFirstDigit = if (firstDigit < 2) 0 else 11 - firstDigit

            // Calcula o segundo dígito verificador
            val secondDigit = (0 until 10).sumOf { (cleanedCPF[it].code - 48) * (11 - it) } % 11
            val expectedSecondDigit = if (secondDigit < 2) 0 else 11 - secondDigit

            // Verifica se os dígitos verificadores calculados correspondem aos dígitos reais
            return (cleanedCPF[9].code - 48 == expectedFirstDigit) && (cleanedCPF[10].code - 48 == expectedSecondDigit)
        }
    }
}
