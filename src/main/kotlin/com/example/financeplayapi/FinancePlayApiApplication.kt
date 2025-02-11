package com.example.financeplayapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

//@ComponentScan(basePackages = ["com.example.financeplayapi"])
@SpringBootApplication
class FinancePlayApiApplication

fun main(args: Array<String>) {
    runApplication<FinancePlayApiApplication>(*args)
}
