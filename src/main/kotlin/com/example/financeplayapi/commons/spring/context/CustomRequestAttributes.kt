package com.example.financeplayapi.commons.spring.context

import org.springframework.web.context.request.RequestAttributes
import java.util.concurrent.atomic.AtomicLong

/**
 * Contexto do request iniciado programaticamente.
 */
internal class CustomRequestAttributes : RequestAttributes {
    private val map: MutableMap<String, Bean> = HashMap()
    private val sessionMutex = Any()
    private val sessionId = idCounter.getAndAdd(1)
    override fun getAttribute(name: String, scope: Int): Any? {
        return if (map.containsKey(name)) map[name]!!.value else null
    }

    override fun getAttributeNames(scope: Int): Array<String> {
        return map.keys.toTypedArray<String>()
    }

    override fun getSessionId(): String {
        return sessionId.toString() + ""
    }

    override fun getSessionMutex(): Any {
        return sessionMutex
    }

    override fun registerDestructionCallback(name: String, callback: Runnable, scope: Int) {
        if (map.containsKey(name)) {
            map[name]!!.callback = callback
        }
    }

    override fun removeAttribute(name: String, scope: Int) {
        val b = map.remove(name)?.callback
        if (b != null) {
            try {
                b.run()
            } catch (e: Exception) {
            }
        }
    }

    override fun resolveReference(key: String): Any? {
        return if (map.containsKey(key)) map[key]!!.value else null
    }

    override fun setAttribute(name: String, value: Any, scope: Int) {
        map[name] = Bean(value)
    }

    /**
     * Classe que armazena um valor registrado no contexto de request.
     */
    private class Bean
    /**
     * Construtor.
     *
     * @param value valor armazenado
     */(val value: Any) {
        var callback: Runnable? = null
    }

    companion object {
        private val idCounter = AtomicLong()
    }
}
