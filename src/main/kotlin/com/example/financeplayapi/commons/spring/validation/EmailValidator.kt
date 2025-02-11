package com.example.financeplayapi.commons.spring.validation

import com.example.financeplayapi.commons.core.type.Email
import com.example.financeplayapi.commons.core.util.trimToNull
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class EmailValidator : ConstraintValidator<ValidEmail, Any> {
    private var message: String? = null

    override fun initialize(annotation: ValidEmail) {
        this.message = annotation.message
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        val text = if (value is String) {
            value
        } else if (value is Email) {
            value.toEmailString()
        } else {
            setError(value, context)
            return false
        }

        if (!Email.isValidEmail(text)) {
            setError(text, context)
            return false
        }
        return true
    }

    private fun setError(value: Any, context: ConstraintValidatorContext) {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(message?.trimToNull() ?: "Illegal Email: $value")
            .addConstraintViolation()
    }
}
