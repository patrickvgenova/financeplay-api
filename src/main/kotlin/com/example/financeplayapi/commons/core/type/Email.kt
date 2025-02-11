package com.example.financeplayapi.commons.core.type

import com.example.financeplayapi.commons.core.util.emailPatern


class Email {

    private val email: String

    constructor(email: String) {
        require(isValidEmail(email)) { "Illegal email format: $email" }
        this.email = email
    }

    fun toEmailString(): String {
        return this.email
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Email

        return email == other.email
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }

    companion object {
        fun isValidEmail(str: String): Boolean {
            return str.matches(emailPatern)
        }
    }
}
