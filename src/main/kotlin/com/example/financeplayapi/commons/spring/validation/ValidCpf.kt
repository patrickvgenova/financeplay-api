package com.example.financeplayapi.commons.spring.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Retention(value = AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CpfValidator::class])
annotation class ValidCpf(
    val message: String = "",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
//    val propName: String = "",
//    val value: String = ""
)
