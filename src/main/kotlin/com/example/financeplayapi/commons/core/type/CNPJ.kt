package com.example.financeplayapi.commons.core.type

class CNPJ {

    private val cnpj: String

    constructor(cpf: String) {
        require(isValidCNPJ(cpf)) { "Illegal cpf format: $cpf" }
        this.cnpj = cpf.replace(nonNumbers, "").padStart(14, '0')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CNPJ

        return cnpj == other.cnpj
    }

    override fun hashCode(): Int {
        return cnpj.hashCode()
    }

    fun printPretty(): String {
        return "${this.cnpj[0]}${this.cnpj[1]}${this.cnpj[2]}.${this.cnpj[3]}${this.cnpj[4]}${this.cnpj[5]}.${this.cnpj[6]}${this.cnpj[7]}${this.cnpj[8]}-${this.cnpj[9]}${this.cnpj[10]}"
    }

    fun printNumbers(): String {
        return this.cnpj
    }

    override fun toString(): String {
        return printNumbers()
    }

    companion object {

        private val nonNumbers = "[^0-9]".toRegex()

        private val validFormats = listOf(
            """\d{1,14}""".toRegex(),
            """^\d{1,2}\.\d{1,3}\.\d{3}\/\d{4}-\d{2}$""".toRegex(),
            """^\d{1,3}\.\d{3}\/\d{4}-\d{2}$""".toRegex(),
            """^\d{3}\/\d{4}-\d{2}$""".toRegex(),
        )

        /**
         * Verifica se um cnpj é válido.
         * CNPJs podem ser informados tanto no formato numerico (14 digitos ou menos, considerando que os digitos faltantes sao zeros à esquerda) quanto em formato com máscara (12.345.678/0001-01).
         *
         * @param cnpj snpj no formato string
         * @return true para cnpj valido. false caso contrário
         */
        fun isValidCNPJ(cnpj: String): Boolean {
            // https://gist.github.com/clairtonluz/0e82a03e8b6c148608f1
            validFormats.find { cnpj.matches(it) } ?: return false
            // Remova caracteres não numéricos do CNPJ
            var cleanedCNPJ = cnpj.replace(nonNumbers, "").padStart(14, '0')

            // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
            // Verifique se todos os dígitos são iguais (caso contrário, não é válido)
            if (cleanedCNPJ.all { it == cleanedCNPJ[0] }) {
                return false
            }

            // Calculo do 1o. Digito Verificador
            var sm = 0
            var peso = 2
            var num: Int
            for (i in 11 downTo 0) {
                //  48 eh a posição de '0' na tabela ASCII
                num = (cleanedCNPJ[i].code - 48)
                sm += num * peso
                peso += 1
                if (peso == 10) peso = 2
            }
            var r: Int = sm % 11
            val dig13: Char = if (r == 0 || r == 1) '0' else (11 - r + 48).toChar()

            // Calculo do 2o. Digito Verificador
            sm = 0
            peso = 2
            for (i in 12 downTo 0) {
                num = (cleanedCNPJ[i].code - 48)
                sm += num * peso
                peso += 1
                if (peso == 10) peso = 2
            }
            r = sm % 11
            val dig14: Char = if (r == 0 || r == 1) '0' else (11 - r + 48).toChar()
            // Verifica se os dígitos calculados conferem com os dígitos informados.
            return (dig13 == cleanedCNPJ[12] && dig14 == cleanedCNPJ[13])
        }
    }
}
