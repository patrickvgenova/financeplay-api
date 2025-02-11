package com.example.financeplayapi.commons.spring.context

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import java.util.function.Supplier

/**
 * Acesso programatico ao ciclo de vida Spring.
 */
@Component
object SpringContext : ApplicationListener<ContextRefreshedEvent> {

    private var applicationContext: ApplicationContext? = null

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        applicationContext = event.applicationContext
    }

    /**
     * Executa uma função dentro de um novo contexto de request.
     *
     * @param function  função a ser executada
     * @param <R>       tipo do retorno da função
     *
     * @return retorno da função
    </R> */
    fun <R> runWithNewRequestContext(function: Supplier<R>): R {
        val oldContext = RequestContextHolder.getRequestAttributes()
        check(oldContext == null) { "Request context already set" }
        val requestScope: RequestAttributes = CustomRequestAttributes()
        RequestContextHolder.setRequestAttributes(requestScope)
        return try {
            function.get()
        } finally {
            RequestContextHolder.setRequestAttributes(null)
        }
    }

    /**
     * Busca por um bean a partir da classe.
     *
     * @param clazz classe
     * @param <T>   tipo do objeto retornado
     *
     * @return bean
    </T> */
    fun <T> getBean(clazz: Class<T>): T {
        return applicationContext!!.getBean(clazz)
    }
}
