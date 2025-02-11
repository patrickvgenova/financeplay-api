package com.example.financeplayapi.commons.spring.context

import org.springframework.web.bind.annotation.RestController


@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiresPermission(val permission: String)