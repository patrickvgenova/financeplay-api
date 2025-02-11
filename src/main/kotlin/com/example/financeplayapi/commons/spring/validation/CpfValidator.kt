package com.example.financeplayapi.commons.spring.validation

import com.example.financeplayapi.commons.core.type.CPF
import com.example.financeplayapi.commons.core.util.trimToNull
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CpfValidator : ConstraintValidator<ValidCpf, Any> {
    private var message: String? = null

    override fun initialize(annotation: ValidCpf) {
        this.message = annotation.message
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val text = if (value is String) {
            value
        } else if (value is CPF) {
            value.printNumbers()
        } else {
            setError(value, context)
            return false
        }

        if (!CPF.isValidCPF(text)) {
            setError(text, context)
            return false
        }
        return true
    }

    private fun setError(value: Any, context: ConstraintValidatorContext) {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(message?.trimToNull() ?: "Illegal CPF: $value")
            .addConstraintViolation()
    }
}
