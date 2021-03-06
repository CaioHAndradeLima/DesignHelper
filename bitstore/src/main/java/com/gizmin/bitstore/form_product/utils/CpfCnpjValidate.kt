package com.gizmin.bitstore.form_product.utils

object CpfCnpjValidate {

    private val pesoCPF = intArrayOf(11, 10, 9, 8, 7, 6, 5, 4, 3, 2)
    private val pesoCNPJ = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

    fun isValid(cpfCnpj: String): Boolean {
        return isValidCPF(cpfCnpj) || isValidCNPJ(cpfCnpj)
    }

    private fun calcularDigito(str: String, peso: IntArray): Int {
        var soma = 0
        var indice = str.length - 1
        var digito: Int
        while (indice >= 0) {
            digito = Integer.parseInt(str.substring(indice, indice + 1))
            soma += digito * peso[peso.size - str.length + indice]
            indice--
        }
        soma = 11 - soma % 11
        return if (soma > 9) 0 else soma
    }

    private fun padLeft(text: String, character: Char): String {
        return String.format("%11s", text).replace(' ', character)
    }

    fun isValidCPF(cpf: String?): Boolean {
        var cpf = cpf
        cpf = cpf!!.trim { it <= ' ' }.replace(".", "").replace("-", "")
        if (cpf == null || cpf.length != 11 || !cpf.matches("[0-9]+".toRegex())) return false

        for (j in 0..9)
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)) == cpf)
                return false

        val digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF)
        val digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF)
        return cpf == cpf.substring(0, 9) + digito1.toString() + digito2.toString()
    }

    private fun isValidCNPJ(cnpj: String?): Boolean {
        var cnpj = cnpj
        cnpj = cnpj!!.trim { it <= ' ' }.replace(".", "").replace("-", "")
        if (cnpj == null || cnpj.length != 14 || !cnpj.matches("[0-9]+".toRegex())) return false

        val digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ)
        val digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ)
        return cnpj == cnpj.substring(0, 12) + digito1.toString() + digito2.toString()
    }

}
